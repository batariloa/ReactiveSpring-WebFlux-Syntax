package com.batarilo.webluxplayground.config;

import com.batarilo.webluxplayground.dto.InputFailedValidationResponse;
import com.batarilo.webluxplayground.exception.InputValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;
import java.util.function.Predicate;

@Configuration
public class RouterConfig {
    @Autowired
    private RequestHandler requestHandler;

    @Bean
    public RouterFunction<ServerResponse> highLevelRouter(){
        return RouterFunctions.route()
                .path("router", this::serverResponseRouterFunction)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> serverResponseRouterFunction(){
        return RouterFunctions.route()
                .GET("square/{input}", RequestPredicates.path("*/1?").or(RequestPredicates.path("*/20")), requestHandler::squareHandler )
                .GET("square/{input}", request -> ServerResponse.badRequest().bodyValue("Only 10-20 allowed.") )

                .POST("calc", RequestPredicates.headers(headers -> "+".equals(headers.asHttpHeaders().toSingleValueMap().get("op"))
                ), requestHandler::addHandler)
                .POST("calc", request -> ServerResponse.badRequest().bodyValue("Not a valid operation."))

                .GET("table/{input}", requestHandler::tableHandler)
                .GET("square-validation/{input}", requestHandler::squareHandlerWithValidation)
                .onError(InputValidationException.class, inputValidationExceptionHandler())
                .POST("multiply", requestHandler::multiplyHandler)
                .build();

    }

    private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> inputValidationExceptionHandler(){
        return (err,req) -> {
            InputValidationException ex = (InputValidationException) err;
            InputFailedValidationResponse response = new InputFailedValidationResponse();
            response.setInput(ex.getInput());
            response.setMessage(ex.getMessage());
            response.setErrorCode(ex.getErrorCode());

            return ServerResponse.badRequest().bodyValue(response);
        };
    }
}
