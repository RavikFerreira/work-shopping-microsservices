package com.productvalidationservice.core.service;

import com.productvalidationservice.core.dto.Event;
import com.productvalidationservice.core.dto.History;
import com.productvalidationservice.core.dto.Product;
import com.productvalidationservice.core.enums.EStatus;
import com.productvalidationservice.core.kafka.Producer;
import com.productvalidationservice.core.models.Validation;
import com.productvalidationservice.core.repository.ValidationRepository;
import com.productvalidationservice.core.utils.JsonUtil;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Singleton
public class ProductValidationService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductValidationService.class);
    private static final String CURRENT_SOURCE = "PRODUCT_VALIDATION_SERVICE";

    @Inject
    private JsonUtil jsonUtil;
    @Inject
    private Producer producer;
    @Inject
    private ValidationRepository validationRepository;

    public void validateExistingProducts(Event event) {
        try {
            checkCurrentValidation(event);
            createValidation(event, true);
            handleSuccess(event);
        } catch (Exception ex) {
            LOG.error("Error trying to validate product: ", ex);
            handleFailCurrentNotExecuted(event, ex.getMessage());
        }
        producer.sendEvent(jsonUtil.toJson(event));
    }

    private void validateProductsInformed(Event event) {
        if (event.getPayload() == null || event.getPayload().getOrder().getProducts().isEmpty()) {
            throw new RuntimeException("Product list is empty!");
        }
        if (event.getPayload().getId().isEmpty() || event.getTransactionId().isEmpty()) {
            throw new RuntimeException("OrderID and TransactionID must be informed!");
        }
    }

    private void checkCurrentValidation(Event event) {
        validateProductsInformed(event);
        if (validationRepository.existsByOrderIdAndTransactionId(
                event.getShoppingId(), event.getTransactionId())) {
            throw new RuntimeException("There's another transactionId for this validation.");
        }
        //            validateExistingProduct(product.getIdProduct());
        event.getPayload().getOrder().getProducts().forEach(this::validateProductInformed);
    }

    private void validateProductInformed(Product product) {
        if (product.getIdProduct().isEmpty()) {
            throw new RuntimeException("Product must be informed!");
        }
    }

//    private void validateExistingProduct(String productId) {
//        List<Product> products =
//        if (!validationRepository.existsByProductId(productId)) {
//            throw new RuntimeException("Product does not exists in database!");
//        }
//    }

    private void createValidation(Event event, boolean success) {
        Validation validation = new Validation();
        validation.setOrderId(event.getPayload().getId());
        validation.setTransactionId(event.getTransactionId());
        validation.setSuccess(success);
        validationRepository.save(validation);
    }

    private void handleSuccess(Event event) {
        event.setStatus(EStatus.SUCCESS);
        event.setSource(CURRENT_SOURCE);
        addHistory(event, "Products are validated successfully!");
    }

    private void addHistory(Event event, String message) {
        History history = new History();
        history.setSource(event.getSource());
        history.setStatus(event.getStatus());
        history.setMessage(message);
        history.setCreatedAt(LocalDateTime.now());
        event.addToHistory(history);
    }

    private void handleFailCurrentNotExecuted(Event event, String message) {
        event.setStatus(EStatus.ROLLBACK_PENDING);
        event.setSource(CURRENT_SOURCE);
        addHistory(event, "Fail to validate products: ".concat(message));
    }

    public void rollbackEvent(Event event) {
        changeValidationToFail(event);
        event.setStatus(EStatus.FAIL);
        event.setSource(CURRENT_SOURCE);
        addHistory(event, "Rollback executed on product validation!");
        producer.sendEvent(jsonUtil.toJson(event));
    }

    private void changeValidationToFail(Event event) {
        validationRepository
                .findByOrderIdAndTransactionId(event.getShoppingId(), event.getTransactionId())
                .ifPresentOrElse(validation -> {
                            validation.setSuccess(false);
                            validationRepository.save(validation);
                        },
                        () -> createValidation(event, false));
    }
}
