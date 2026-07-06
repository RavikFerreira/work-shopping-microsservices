package com.shopping.core.controller;

import com.shopping.core.dto.UserEvent;
import com.shopping.config.exceptions.CannotCreateAShoppingWithTheSameId;
import com.shopping.config.exceptions.CannotDeleteABusyShopping;
import com.shopping.config.exceptions.ProductResourceNotFoundException;
import com.shopping.config.exceptions.ShoppingResourceNotFoundException;
import com.shopping.core.models.Shopping;
import com.shopping.core.service.ShoppingService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

import java.util.List;

@Controller("shopping/")
public class ShoppingController {
    @Inject
    private ShoppingService shoppingService;

    @Get("list")
    @Secured({"ADMIN"})
    public HttpResponse<List<Shopping>> listOrders(){
        return HttpResponse.ok(shoppingService.list());
    }


//    public HttpResponse<Shopping> addShoppingCart(@Body UserEvent user) throws ShoppingResourceNotFoundException, CannotCreateAShoppingWithTheSameId {
//        Shopping addShoppingCart = shoppingService.addShoppingCart(user);
//        return HttpResponse.created(addShoppingCart);
//    }

//    @Patch("orders/{idShopping}")
//    public HttpResponse<Shopping> addOrder(@PathVariable String idShopping){
//        Shopping addOrder = shoppingService.addOrder(idShopping);
//        return HttpResponse.ok(addOrder);
//    }
    @Patch("addProductInOrder/{idShopping}/{idProduct}")
    @Secured({"ADMIN", "USER"})
    public HttpResponse<Shopping> addProductInOrder(@Body Shopping idShopping, @PathVariable String idProduct) throws ProductResourceNotFoundException {
        Shopping product = shoppingService.addProductInOrder(idShopping,idProduct);
        return HttpResponse.created(product);
    }

    @Get("search/{idShopping}")
    @Secured({"ADMIN"})
    public HttpResponse<Shopping> search(@PathVariable String idShopping){
        Shopping shoppingList = shoppingService.search(idShopping);
        return HttpResponse.ok(shoppingList);
    }

    @Delete("delete/{idShopping}")
    @Secured({"ADMIN", "USER"})
    public HttpResponse<Shopping> delete(@PathVariable String idShopping) throws CannotDeleteABusyShopping {
        shoppingService.delete(idShopping);
        return HttpResponse.ok();
    }

    @Get("payment/{idShopping}")
    @Secured({"ADMIN", "USER"})
    public HttpResponse<Shopping> realizedPayment(@PathVariable String idShopping){
        shoppingService.realizedPayment(idShopping);
        return HttpResponse.ok();
    }
    @Get("finallyOrder/{idShopping}")
    @Secured({"ADMIN", "USER"})
    public HttpResponse<Shopping> finalizedOrder(@PathVariable String idShopping){
        shoppingService.finalizedOrder(idShopping);
        return HttpResponse.ok();
    }
}
