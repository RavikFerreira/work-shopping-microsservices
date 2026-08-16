package com.shopping.config.exceptions.handler;

import com.shopping.config.exceptions.StandardError;
import com.shopping.config.exceptions.exception.PaymentNotRealizedException;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

import java.io.Serial;

@Singleton
@Produces
public class handlerPaymentNotRealizedException implements ExceptionHandler<PaymentNotRealizedException, HttpResponse<StandardError>> {

    @Override
    public HttpResponse<StandardError> handle(HttpRequest request, PaymentNotRealizedException ex) {
        String error = "Pagamento não realizado!";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status, error, ex.getMessage(), request.getPath());
        return HttpResponse.status(status).body(err);
    }
}