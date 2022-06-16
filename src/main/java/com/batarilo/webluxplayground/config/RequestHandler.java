package com.batarilo.webluxplayground.config;

import com.batarilo.webluxplayground.dto.InputFailedValidationResponse;
import com.batarilo.webluxplayground.dto.MultiplyRequestDto;
import com.batarilo.webluxplayground.dto.Response;
import com.batarilo.webluxplayground.exception.InputValidationException;
import com.batarilo.webluxplayground.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RequestHandler {

    @Autowired
    private ReactiveMathService ms;

    public Mono<ServerResponse> squareHandler(ServerRequest sr){

        int input = Integer.parseInt(sr.pathVariable("input"));
        Mono<Response> responseMono = ms.findSquare(input);
        return ServerResponse.ok().body(responseMono, Response.class);
    }

    public Mono<ServerResponse> squareHandlerWithValidation(ServerRequest sr){

        int input = Integer.parseInt(sr.pathVariable("input"));
        if(input<10 || input>=10)
            return Mono.error(new InputValidationException(input));

        Mono<Response> responseMono = ms.findSquare(input);
        return ServerResponse.ok().body(responseMono, Response.class);
    }

    public Mono<ServerResponse> tableHandler(ServerRequest sr){

        int input = Integer.parseInt(sr.pathVariable("input"));
        Flux<Response> responseFlux = ms.multiplicationTable(input);
        return ServerResponse
                .ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(responseFlux, Response.class);
    }

    public Mono<ServerResponse> multiplyHandler(ServerRequest sr){
        Mono<MultiplyRequestDto> dtoMono = sr.bodyToMono(MultiplyRequestDto.class);
        Mono<Response> responseMono = ms.simpleMultiply(dtoMono);

        return ServerResponse.ok()
                .body(responseMono, Response.class);
    }

    public Mono<ServerResponse> addHandler(ServerRequest sr){

        Mono<MultiplyRequestDto> input = sr.bodyToMono(MultiplyRequestDto.class);
        return ServerResponse.ok()
                .body(ms.addNumbers(input), Response.class);
    }
}
