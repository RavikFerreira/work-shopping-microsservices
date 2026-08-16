package com.shopping.config.exceptions.handler;

import com.shopping.config.exceptions.StandardError;
import com.shopping.config.exceptions.exception.CannotDeleteABusyShopping;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Singleton
@Produces
public class handlerCannotDeleteABusyShopping implements ExceptionHandler<CannotDeleteABusyShopping, HttpResponse<StandardError>> {

    @Override
    public HttpResponse<StandardError> handle(HttpRequest request, CannotDeleteABusyShopping ex) {
        String error = "Não é possível deletar um carrinho ocupado! ";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status ,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(status).body(err);
    }
}

