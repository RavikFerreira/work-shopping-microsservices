package com.autorization.config.exceptions.handler;

import com.autorization.config.exceptions.StandardError;
import com.autorization.config.exceptions.exception.InvalidEmailOrPasswordExceptions;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Singleton
@Produces
public class handlerInvalidEmailOrPasswordExceptions implements ExceptionHandler<InvalidEmailOrPasswordExceptions, HttpResponse<StandardError>> {

    @Override
    public HttpResponse<StandardError> handle(HttpRequest request, InvalidEmailOrPasswordExceptions ex) {
        String error = "Email ou senha inválidos!";
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError err = new StandardError(status ,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(status).body(err);
    }
}
