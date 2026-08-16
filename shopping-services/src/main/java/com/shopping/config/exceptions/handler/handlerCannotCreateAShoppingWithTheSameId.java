package com.shopping.config.exceptions.handler;

import com.shopping.config.exceptions.StandardError;
import com.shopping.config.exceptions.exception.CannotCreateAShoppingWithTheSameId;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Singleton
@Produces
public class handlerCannotCreateAShoppingWithTheSameId implements ExceptionHandler<CannotCreateAShoppingWithTheSameId, HttpResponse<StandardError>> {

    @Override
    public HttpResponse<StandardError> handle(HttpRequest request, CannotCreateAShoppingWithTheSameId ex) {
        String error = "Não é possível criar um carrinho com o mesmo id! ";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status ,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(HttpStatus.NOT_FOUND).body(err);
    }
}
