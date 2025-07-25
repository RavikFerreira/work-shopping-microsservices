package com.inventory.core.service;


import com.inventory.core.dto.*;
import com.inventory.core.kafka.Producer;
import com.inventory.core.models.Inventory;
import com.inventory.core.repository.InventoryRepository;
import com.inventory.core.utils.JsonUtil;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static com.inventory.core.enums.EStatus.*;

@Singleton
@AllArgsConstructor
public class InventoryService {
    private static final Logger LOG = LoggerFactory.getLogger(InventoryService.class);

    private static final String CURRENT_SOURCE = "INVENTORY_SERVICE";

    @Inject
    private JsonUtil jsonUtil;
    @Inject
    private Producer producer;
    @Inject
    private InventoryRepository inventoryRepository;

    public void updateInventory(Event event){
        try{
            checkCurrentValidation(event);
            updateInventory(event.getPayload().getOrder());
            handleSuccess(event);
        }catch (Exception ex) {
            LOG.error("Error trying to update inventory: " , ex);
            handleFailCurrentNotExecuted(event, ex.getMessage());
        }
        producer.sendEvent(jsonUtil.toJson(event));
    }

    private void checkCurrentValidation(Event event){
        if(inventoryRepository.existsByShoppingIdAndTransactionId(event.getPayload().getIdShopping(), event.getTransactionId())){
            throw new RuntimeException("There's another transactionId for this validation.");
        }
    }

    private Inventory findInventoryByIdProduct(String idProduct){
        return inventoryRepository.findByIdProduct(idProduct).orElseThrow(() -> new RuntimeException("Inventory not found informed product"));
    }

    private void updateInventory(Order order){
        order
                .getProducts()
                .forEach(product -> {
                    Inventory inventory = findInventoryByIdProduct(product.getIdProduct());
                    checkInventory(inventory.getAvailable(), product.getQuantity());
                    inventory.setNewQuantity(inventory.getAvailable() - product.getQuantity());
                    inventory.setAvailable(inventory.getAvailable());
                    inventoryRepository.update(inventory);
                });

    }
    private void checkInventory(int available, int orderQuantity){
        if(orderQuantity > available){
            throw new RuntimeException("Product is out of stock");
        }

    }

    private void handleSuccess(Event event){
        event.setStatus(SUCCESS);
        event.setSource(CURRENT_SOURCE);
        addHistory(event, "Inventory updated successfully");
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
        addHistory(event, "Fail to update inventory: ".concat(message));
    }

    public void rollbackInventory(Event event){
        event.setStatus(FAIL);
        event.setSource(CURRENT_SOURCE);
        try{
            returnInventoryToPreviousValues(event);
            addHistory(event, "Rollback executed for inventory! ");

        }catch (Exception ex){
            addHistory(event, "Rollback not executed for inventory! ".concat(ex.getMessage()));
        }
        producer.sendEvent(jsonUtil.toJson(event));
    }

    private void returnInventoryToPreviousValues(Event event){
        inventoryRepository
                .findByShoppingIdAndTransactionId(event.getPayload().getIdShopping(), event.getTransactionId())
                .forEach(inventory -> {
                    Inventory inventory1 = new Inventory();
                    inventory.setAvailable(inventory.getOldQuantity());
                    inventoryRepository.save(inventory1);
                    LOG.info("Restored inventory for order {} from {} to {}",
                            event.getPayload().getIdShopping(), inventory.getNewQuantity(), inventory.getAvailable());
                });
    }

    public void createInventory(EventProduct event) {
        try{
            Product product = event.getPayload();
            Inventory orderInventory = createInventory(event, product);
            inventoryRepository.update(orderInventory);
            handleSuccess(event);
        } catch (Exception ex){
            LOG.error("Error trying to add to inventory: " , ex);
            handleFailCurrentNotExecuted(event, ex.getMessage());
        }
        producer.sendEventProduct(jsonUtil.toJson(event));
    }

    private Inventory createInventory(EventProduct event, Product product){
        Inventory inventory = new Inventory();
        inventory.setIdProduct(event.getPayload().getIdProduct());
        inventory.setAvailable(event.getPayload().getQuantity());
        inventory.setOldQuantity(inventory.getAvailable());
        inventoryRepository.save(inventory);
        return inventory;
    }

    public void finishFailInventory(EventProduct event){
        event.setStatus(FAIL);
        event.setSource(CURRENT_SOURCE);
        try{
            addHistory(event, "Failed execution for inventory! ");

        }catch (Exception ex){
            addHistory(event, "Failed not execution for inventory! ".concat(ex.getMessage()));
        }
        producer.sendEvent(jsonUtil.toJson(event));
    }

    private void addHistory(EventProduct event, String message) {
        History history = new History();
        history.setSource(event.getSource());
        history.setStatus(event.getStatus());
        history.setMessage(message);
        history.setCreatedAt(LocalDateTime.now());
        event.addToHistory(history);
    }

    private void handleSuccess(EventProduct event) {
        event.setStatus(SUCCESS);
        event.setSource(CURRENT_SOURCE);
        addHistory(event, "Inventory created successfully");
    }

    private void handleFailCurrentNotExecuted(EventProduct event, String message){
        event.setStatus(ROLLBACK_PENDING);
        event.setSource(CURRENT_SOURCE);
        addHistory(event, "Fail to create inventory: ".concat(message));
    }
}
