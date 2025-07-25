package com.shopping.core.service;


import com.shopping.config.exceptions.*;
import com.shopping.core.kafka.Producer;
import com.shopping.core.models.Event;
import com.shopping.core.models.Order;
import com.shopping.core.models.Product;
import com.shopping.core.models.Shopping;
import com.shopping.core.repository.EventRepository;
import com.shopping.core.repository.ProductRepository;
import com.shopping.core.repository.ShoppingRepository;
import com.shopping.core.utils.JsonUtil;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Singleton
public class ShoppingService {

    private static final String TRANSACTION_ID_PATTERN = "%s_%s";

    @Inject
    private ShoppingRepository shoppingRepository;
    @Inject
    private EventRepository eventRepository;
    @Inject
    private Producer producer;
    @Inject
    private JsonUtil jsonUtil;
    @Inject
    private EventService eventService;
    @Inject
    private ProductRepository productRepository;

    public List<Shopping> list(){
        List<Shopping> shoppingList = shoppingRepository.findAll();

        for(Shopping shopping : shoppingList){
            double account = 0.0;
            if(shopping.getOrder() != null){
                List<Product> products = shopping.getOrder().getProducts();
                if(products != null) {
                    for(Product product : products) {
                        account += product.getPrice() * product.getQuantity();
                    }
                    shopping.setAccount(account);
                }
            }
        }
        return shoppingList;
    }

    public Shopping addShoppingCart(Shopping shopping){
        Optional<Shopping> shoppingExists = shoppingRepository.findByIdShopping(shopping.getIdShopping());
        if(shoppingExists.isPresent()) {
            throw new CannotCreateAShoppingWithTheSameId("Cannot create a shopping with the same id: " + shopping.getIdShopping());
        }
        shopping.setOrder(shopping.getOrder());

        shoppingRepository.save(shopping);

        return shopping;
    }
    public Shopping addOrder(String idProduct){
        Shopping shoppingList = shoppingRepository.findByIdShopping(idProduct).orElseThrow(() -> new CannotCreateAShoppingWithTheSameId("Cannot create a shopping with the same id: " + idProduct));
        if(shoppingList.getOrder() == null){
            Order order = new Order();
            shoppingList.setOrder(order);
            order.setIdOrder(shoppingList.getIdShopping());
            shoppingRepository.update(shoppingList);
        }
        return shoppingList;
    }
    public Shopping addProductInOrder(String idShopping, String idProduct) {
        Shopping shoppingList = shoppingRepository.findByIdShopping(idShopping).orElseThrow(() -> new ShoppingResourceNotFoundException("Shopping resource not found!"));
        Product productExists = productRepository.findByIdProduct(idProduct).orElseThrow(() -> new ProductResourceNotFoundException("Product resource not found!"));
        if(!Objects.equals(productExists.getIdProduct(), idProduct)){
            throw new ProductResourceNotFoundException("Product resource not found!");
        }

        boolean orderNotExists = false;
        List<Product> products = shoppingList.getOrder().getProducts();
        if(products == null){
            products = new ArrayList<>();
            shoppingList.getOrder().setProducts(products);
            }
        for(Product product : products){
            if(product.getIdProduct().equals(productExists.getIdProduct())){
                product.setQuantity(product.getQuantity() + 1);
                orderNotExists = true;
                break;
            }
        }
        if (!orderNotExists) {
            Product productToAdd = new Product();
            productToAdd.setIdProduct(productExists.getIdProduct());
            productToAdd.setName(productExists.getName());
            productToAdd.setPrice(productExists.getPrice());
            productToAdd.setQuantity(1);
            products.add(productToAdd);
        }
        shoppingList.setAccount(shoppingList.getOrder().getProducts()
                .stream()
                .map( product -> product.getPrice() * product.getQuantity())
                .reduce(0.0, Double::sum));

        shoppingRepository.update(shoppingList);
        return shoppingList;
    }

    private Event createPayload(Shopping shopping){
        Event event = new Event();
        event.setShoppingId(shopping.getIdShopping());
        event.setTransactionId(String.format(TRANSACTION_ID_PATTERN, Instant.now().getEpochSecond(), UUID.randomUUID()));
        event.setPayload(shopping);
        event.setCreatedAt(LocalDateTime.now());
        eventService.save(event);
        return event;
    }

    public Shopping search(String shopping) {
        Shopping shoppingList = shoppingRepository.findByIdShopping(shopping).orElseThrow(() -> new ShoppingResourceNotFoundException("Shopping resource not found!"));
        if(shoppingList.getOrder() != null) {
            List<Product> products = shoppingList.getOrder().getProducts();
            double account = 0.0;
            for (Product product : products) {
                account += product.getPrice() * product.getQuantity();
            }
            shoppingList.setAccount(account);
        }

        return shoppingList;
    }

    public Shopping delete(String idShopping) throws CannotDeleteABusyShopping {
        Shopping shoppingList = shoppingRepository.findByIdShopping(idShopping).orElseThrow(()-> new ShoppingResourceNotFoundException("Shopping resource not found!"));
        if(shoppingList != null){
            shoppingRepository.delete(shoppingList);
        }
        return shoppingList;
    }
    public Shopping realizedPayment(String idShopping){
        Shopping shoppingList = shoppingRepository.findByIdShopping(idShopping).orElseThrow(() -> new ShoppingResourceNotFoundException("Shopping resource not found! "));
        producer.sendEvent(jsonUtil.toJson(createPayload(shoppingList)));
        return shoppingList;
    }

    public Shopping finalizedOrder(String shoppingId){
        Event event = eventRepository.findTop1ByShoppingIdOrderByCreatedAtDesc(shoppingId).orElseThrow(() -> new ShoppingResourceNotFoundException("Shopping resource not found!"));
        Shopping shoppingList = shoppingRepository.findByIdShopping(event.getShoppingId()).orElseThrow(() -> new ShoppingResourceNotFoundException("Shopping resource not found! "));
        if(event.getSource() == null  && event.getStatus() ==  null){
            throw new PaymentNotRealizedException("Payment not realized!");
        }
        if(!event.getSource().equals("ORCHESTRATOR")  && !event.getStatus().equals("SUCCESS")) {
            throw new PaymentNotRealizedException("Payment not realized!");
        }
        else {
            shoppingList.setOrder(null);
            shoppingRepository.update(shoppingList);
        }
        return shoppingList;
    }
}
