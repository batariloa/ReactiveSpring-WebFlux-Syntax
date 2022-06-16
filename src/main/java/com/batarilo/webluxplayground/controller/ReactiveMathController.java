package com.batarilo.webluxplayground.controller;

import com.batarilo.webluxplayground.dto.MultiplyRequestDto;
import com.batarilo.webluxplayground.dto.Response;
import com.batarilo.webluxplayground.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("reactive-math")
public class ReactiveMathController {

    @Autowired
    private ReactiveMathService ms;

    @GetMapping("square/{input}")
    private Mono<Response> handleSquareInput(@PathVariable int input){
        return ms.findSquare(input);
    }

    @GetMapping("table/{input}")
    private Flux<Response> handleTableInput(@PathVariable int input){
        return ms.multiplicationTable(input);
    }

    @GetMapping(value = "table/{input}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    private Flux<Response> handleStreamTableInput(@PathVariable int input){
        return ms.multiplicationTable(input);
    }

    @PostMapping(value = "multiply")
    private Mono<Response> multiply(@RequestBody Mono<MultiplyRequestDto> input){
        return ms.simpleMultiply(input);
    }

}
