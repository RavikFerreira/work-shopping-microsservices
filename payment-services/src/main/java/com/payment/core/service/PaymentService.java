package com.payment.core.service;

import com.payment.core.repository.PaymentRepository;
import com.payment.core.dto.Event;
import com.payment.core.dto.History;
import com.payment.core.enums.EPaymentStatus;
import com.payment.core.kafka.Producer;
import com.payment.core.models.Payment;
import com.payment.core.utils.JsonUtil;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static com.payment.core.enums.EStatus.FAIL;
import static com.payment.core.enums.EStatus.ROLLBACK_PENDING;
import static com.payment.core.enums.EStatus.SUCCESS;

@Singleton
@AllArgsConstructor
public class PaymentService {
    private static final Logger LOG = LoggerFactory.getLogger(PaymentService.class);


    private static final String CURRENT_SOURCE = "PAYMENT_SERVICE";
    private static final Double REDUCE_SUM_VALUE = 0.0;
    private static final Double MIN_AMOUNT_VALUE = 0.1;

    @Inject
    private JsonUtil jsonUtil;
    @Inject
    private Producer producer;
    @Inject
    private PaymentRepository paymentRepository;

    public void realizedPayment(Event event){
        try{
            checkCurrentValidation(event);
            createPendingPayment(event);
            Payment payment = findByShoppingIdAndTransactionId(event);
            validateAmount(payment.getTotalAmount());
            changePaymentToSuccess(payment);
            handleSuccess(event);
        }catch (Exception ex) {
            LOG.error("Error trying to make payment: " , ex);
            handleFailCurrentNotExecuted(event, ex.getMessage());
        }
        producer.sendEvent(jsonUtil.toJson(event));
    }

    public void checkCurrentValidation(Event event){
        if(paymentRepository.existsByShoppingIdAndTransactionId(event.getPayload().getIdShopping(), event.getTransactionId())){
            throw new RuntimeException("There's another transactionId for this validation.");
        }
    }

    public void createPendingPayment(Event event){
        double totalAmount = calculateAmount(event);
        Payment payment = new Payment();
        payment.setShoppingId(event.getPayload().getId());
        payment.setTransactionId(event.getTransactionId());
        payment.setTotalAmount(totalAmount);
        save(payment);
        setEventAmountItems(event, payment);
    }
    private double calculateAmount(Event event){
        return event.getPayload().getOrder().getProducts().stream()
                .map(product -> product.getQuantity() * product.getPrice())
                .reduce(REDUCE_SUM_VALUE, Double::sum);
    }

    private void setEventAmountItems(Event event, Payment payment){
        event.getPayload().setAccount(payment.getTotalAmount());
    }

    private void validateAmount(double amount){
        if(amount < MIN_AMOUNT_VALUE){
            throw new RuntimeException("The minimum amount available is ".concat(MIN_AMOUNT_VALUE.toString()));
        }
    }

    private void changePaymentToSuccess(Payment payment){
        payment.setStatus(EPaymentStatus.SUCCESS);
        save(payment);
    }

    private void handleSuccess(Event event){
        event.setStatus(SUCCESS);
        event.setSource(CURRENT_SOURCE);
        addHistory(event, "Payment realized successfully");
    }

    private void addHistory(Event event, String message){
        History history = new History();
        history.setSource(event.getSource());
        history.setStatus(event.getStatus());
        history.setMessage(message);
        history.setCreatedAt(LocalDateTime.now());
        event.addToHistory(history);
    }

    private void handleFailCurrentNotExecuted(Event event, String message){
        event.setStatus(ROLLBACK_PENDING);
        event.setSource(CURRENT_SOURCE);
        addHistory(event, "Fail to realized payment: ".concat(message));
    }

    public void realizedRefund(Event event){
        event.setStatus(FAIL);
        event.setSource(CURRENT_SOURCE);
        try{
            changePaymentStatusToRefund(event);
            addHistory(event, "Rollback executed for payment! ");

        }catch (Exception ex){
            addHistory(event, "Rollback not executed for payment! ".concat(ex.getMessage()));
        }
        producer.sendEvent(jsonUtil.toJson(event));

    }
    private void changePaymentStatusToRefund(Event event){
        Payment payment = findByShoppingIdAndTransactionId(event);
        payment.setStatus(EPaymentStatus.REFUND);
        setEventAmountItems(event, payment);
        save(payment);
    }

    private Payment findByShoppingIdAndTransactionId(Event event){
        return paymentRepository.findByShoppingIdAndTransactionId(event.getPayload().getId(), event.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Payment not found by ShoppingId and TransactionId. "));
    }

    public void save(Payment payment){
        paymentRepository.update(payment);

    }
}
