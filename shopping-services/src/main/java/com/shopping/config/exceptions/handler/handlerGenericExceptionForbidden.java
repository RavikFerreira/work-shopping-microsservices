package com.shopping.config.exceptions.handler;

import com.shopping.config.exceptions.StandardError;
import com.shopping.config.exceptions.exception.GenericException;
import com.shopping.config.exceptions.exception.GenericExceptionForbidden;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Singleton
@Produces
public class handlerGenericExceptionForbidden implements ExceptionHandler<GenericExceptionForbidden, HttpResponse<StandardError>> {

    @Override
    public HttpResponse<StandardError> handle(HttpRequest request, GenericExceptionForbidden ex) {
        String error = "Não Autorizado! ";
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError err = new StandardError(status, error, ex.getMessage(), request.getPath());
        return HttpResponse.status(status).body(err);
    }
}
