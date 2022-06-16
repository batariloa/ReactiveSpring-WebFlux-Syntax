package com.batarilo.webluxplayground.controller;

import com.batarilo.webluxplayground.dto.Response;
import com.batarilo.webluxplayground.exception.InputValidationException;
import com.batarilo.webluxplayground.service.MathService;
import com.batarilo.webluxplayground.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("reactive-math")
public class ReactiveMathValidationController {


    @Autowired
    private ReactiveMathService ms;

    @GetMapping("square/{input}/throw")
    public Mono<Response> findSquare(@PathVariable int input){
        if(input<10 || input>20)
        throw new InputValidationException(input);

        return ms.findSquare(input);
    }
    @GetMapping("square/{input}/mono-error")
    public Mono<Response> monoErrore(@PathVariable int input){

        return Mono.just(input)
                .handle(((integer, sink) ->
                {
                    if(integer>=10 && integer<=20)
                        sink.next(integer);
                    else
                        sink.error(new InputValidationException(input));
                }))
                .cast(Integer.class)
                .flatMap(i -> ms.findSquare(i));
    }

    @GetMapping("square/{input}/bad")
    public Mono<ResponseEntity<Response>> badRequestSquare(@PathVariable int input){

        return Mono.just(input)
                .filter(i-> i>=10 && i<=20)
                .flatMap(i -> ms.findSquare(i))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
