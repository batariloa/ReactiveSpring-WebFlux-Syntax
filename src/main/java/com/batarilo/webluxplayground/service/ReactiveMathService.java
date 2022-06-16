package com.batarilo.webluxplayground.service;

import com.batarilo.webluxplayground.dto.MultiplyRequestDto;
import com.batarilo.webluxplayground.dto.Response;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReactiveMathService {

    public Mono<Response> findSquare(int input){
        return Mono.fromSupplier(()-> input*input)
                .map(Response::new);
    }

    public Flux<Response> multiplicationTable(int input){
        return Flux.range(1,10)
                .doOnNext(i->SleepUtil.sleepSeconds(1))
                .doOnNext(i-> System.out.println("Reactive math service on value: "+i))
                .map(i -> new Response(i * input));
    }

    public Mono<Response> simpleMultiply(Mono<MultiplyRequestDto> dtoMono){
        return dtoMono
                .map(dto-> dto.getFirst()*dto.getSecond())
                .map(Response::new);
    }

    public Mono<Response> addNumbers(Mono<MultiplyRequestDto> dtoMono){
        return dtoMono
                .map(dto -> dto.getFirst() + dto.getSecond())
                .map(Response::new);
    }
}
