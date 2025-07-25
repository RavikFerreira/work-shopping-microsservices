package com.shopping.config.exceptions;

import com.shopping.core.models.Shopping;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Singleton;


@Produces
@Singleton
public class GlobalExceptionHandler {

    @Error(exception = ShoppingResourceNotFoundException.class)
    public HttpResponse<StandardError> handleShoppingResourceNotFoundException(ShoppingResourceNotFoundException ex, HttpRequest request) {
        String error = "Não existe mesa com esse id!";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status ,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(status).body(err);
    }
    @Error(exception = ProductResourceNotFoundException.class)
    public HttpResponse<StandardError> handleOrderResourceNotFoundException(ProductResourceNotFoundException ex, HttpRequest request) {
        String error = "Não existe pedido com esse id!";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status ,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(status).body(err);
    }
    @Error(exception = CannotDeleteABusyShopping.class)
    public HttpResponse<StandardError> handleCannotDeleteABusyShopping(CannotDeleteABusyShopping ex, HttpRequest request) {
        String error = "Não é possível deletar um carrinho ocupado! ";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status ,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(status).body(err);
    }
    @Error(exception = UnableToDeleteAnOrderFromAShopping.class)
    public HttpResponse<StandardError> handleUnableToDeleteAnOrderFromAShopping(UnableToDeleteAnOrderFromAShopping ex, HttpRequest request) {
        String error = "Não é possível excluir um pedido do carrinho! ";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status ,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(HttpStatus.NOT_FOUND).body(err);
    }
    @Error(exception = UnableToEditAnOrderFromAShopping.class)
    public HttpResponse<StandardError> handleUnableToEditAnOrderFromAShopping(UnableToEditAnOrderFromAShopping ex, HttpRequest request) {
        String error = "Não é possível editar um pedido de um carrinho! ";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status ,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(HttpStatus.NOT_FOUND).body(err);
    }
    @Error(exception = CannotCreateAShoppingWithTheSameId.class)
    public HttpResponse<StandardError> handleCannotCreateAShoppingWithTheSameId(CannotCreateAShoppingWithTheSameId ex, HttpRequest request) {
        String error = "Não é possível criar um carrinho com o mesmo id! ";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status ,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(HttpStatus.NOT_FOUND).body(err);
    }
    @Error(exception = ItIsNotPossibleToAddAProductToTheMenuWithTheSameId.class)
    public HttpResponse<StandardError> handleItIsNotPossibleToAddAProductToTheMenuWithTheSameId(ItIsNotPossibleToAddAProductToTheMenuWithTheSameId ex, HttpRequest request) {
        String error = "Não é possível adicionar ao menu um produto com o mesmo identificador! ";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(HttpStatus.NOT_FOUND).body(err);
    }

    @Error(exception = ItIsNotAllowedToAddOrdersWithTheSameId.class)
    public HttpResponse<StandardError> handleItIsNotAllowedToAddOrdersWithTheSameId (ItIsNotAllowedToAddOrdersWithTheSameId  ex, HttpRequest request) {
        String error = "Não é permitido adicionar pedidos com o mesmo identificador! ";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(HttpStatus.NOT_FOUND).body(err);
    }

    @Error(global = true)
    public HttpResponse<StandardError> handleGenericException(Throwable ex, HttpRequest<?> request) {
        String error = "Ocorreu um erro inesperado!";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(status, error, ex.getMessage(), request.getPath());
        return HttpResponse.status(status).body(err);
    }
    @Error(exception = PaymentNotRealizedException.class)
    public HttpResponse<StandardError> handlePaymentNotRealizedException(PaymentNotRealizedException ex, HttpRequest request) {
        String error = "Pagamento não realizado!";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status, error, ex.getMessage(), request.getPath());
        return HttpResponse.status(status).body(err);
    }

}
