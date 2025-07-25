package com.shopping.core.controller;

import com.shopping.config.exceptions.ItIsNotPossibleToAddAProductToTheMenuWithTheSameId;
import com.shopping.config.exceptions.UnableToEditAnOrderFromAShopping;
import com.shopping.core.models.Product;
import com.shopping.core.models.Shopping;
import com.shopping.core.service.ProductService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.util.List;

@Controller("product/")
public class ProductController {

    @Inject
    private ProductService productService;

    @Get("list")
    public HttpResponse<List<Product>> listProducts(){
        return HttpResponse.ok(productService.productList());
    }

    @Post("addProduct")
    public HttpResponse<Product> addProduct(@Body Product product) throws ItIsNotPossibleToAddAProductToTheMenuWithTheSameId {
        Product products = productService.addProduct(product);
        return HttpResponse.created(products);
    }
    @Get("searchProduct/{idProduct}")
    public HttpResponse<Product> search(@PathVariable String idProduct){
        Product product = productService.searchProduct(idProduct);
        return HttpResponse.ok(product);
    }

    @Patch("updateOrderInProduct/{idProduct}")
    public HttpResponse<Product> updateOrderInProduct(@PathVariable String idProduct, @Body Product product) throws UnableToEditAnOrderFromAShopping {
        Product products = productService.updateOrderInProduct(idProduct, product);
        return HttpResponse.ok(products);
    }

    @Delete("delete/{idProduct}")
    public HttpResponse<Product> delete(@PathVariable String idProduct){
        productService.deleteProduct(idProduct);
        return HttpResponse.ok();
    }
}
