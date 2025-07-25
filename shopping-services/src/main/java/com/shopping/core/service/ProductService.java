package com.shopping.core.service;


import com.shopping.config.exceptions.ProductResourceNotFoundException;
import com.shopping.core.kafka.Producer;
import com.shopping.core.models.Event;
import com.shopping.core.models.EventProduct;
import com.shopping.core.models.Product;
import com.shopping.core.models.Shopping;
import com.shopping.core.repository.ProductRepository;
import com.shopping.core.utils.JsonUtil;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Singleton
@AllArgsConstructor
public class ProductService {

    @Inject
    private ProductRepository productRepository;
    @Inject
    private Producer producer;
    @Inject
    private JsonUtil jsonUtil;
    @Inject
    private EventService eventService;


    public List<Product> productList(){
        return productRepository.findAll();
    }

    public Product addProduct(Product product){
        Optional<Product> orders = productRepository.findByIdProduct(product.getIdProduct());

        if (orders.isPresent()) {
            Product existingProduct = orders.get();
            existingProduct.setQuantity(existingProduct.getQuantity() + product.getQuantity());
            productRepository.update(existingProduct);
        }

        if(orders.isEmpty()){
            product.setIdProduct(product.getIdProduct());
            product.setQuantity(product.getQuantity());
            productRepository.save(product);
        }

        producer.sendEventProduct(jsonUtil.toJson(createPayload(product)));
        return product;
    }

    private EventProduct createPayload(Product product){
        EventProduct event = new EventProduct();
        event.setId(product.getId());
        event.setPayload(product);
        event.setCreatedAt(LocalDateTime.now());
        eventService.save(event);
        return event;
    }

    public Product searchProduct(String idProduct){
        Product product = productRepository.findByIdProduct(idProduct).orElseThrow(() -> new ProductResourceNotFoundException(idProduct));
        return product;
    }

    public Product updateOrderInProduct(String idProduct, Product product) {
        searchProduct(idProduct);
        product.setIdProduct(product.getIdProduct());
        product.setName(product.getName());
        product.setPrice(product.getPrice());
        productRepository.save(product);
        return product;
    }

    public Product deleteProduct(String idProduct) {
        Product product = productRepository.findByIdProduct(idProduct).orElseThrow(() -> new ProductResourceNotFoundException(idProduct));
        productRepository.delete(product);
        return product;
    }

}
