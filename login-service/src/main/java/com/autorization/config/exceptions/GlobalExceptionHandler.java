package com.autorization.config.exceptions;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Singleton;


@Produces
@Singleton
public class GlobalExceptionHandler {

    @Error(exception = InvalidEmailOrPasswordExceptions.class)
    public HttpResponse<StandardError> handleInvalidEmailOrPasswordExceptions(InvalidEmailOrPasswordExceptions ex, HttpRequest<?> request) {
        String error = "Email ou senha inválidos!";
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError err = new StandardError(status ,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(status).body(err);
    }

    @Error(exception = InvalidUpdateTokenExceptions.class)
    public HttpResponse<StandardError> handleInvalidUpdateTokenExceptions(InvalidUpdateTokenExceptions ex, HttpRequest<?> request) {
        String error = "Token de atualização inválido!";
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError err = new StandardError(status ,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(status).body(err);
    }

    @Error(global = true)
    public HttpResponse<StandardError> handleGenericException(Throwable ex, HttpRequest<?> request) {
        String error = "Ocorreu um erro inesperado!";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(status, error, ex.getMessage(), request.getPath());
        return HttpResponse.status(status).body(err);
    }
}
