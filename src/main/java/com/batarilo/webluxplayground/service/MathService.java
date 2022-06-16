package com.batarilo.webluxplayground.service;

import com.batarilo.webluxplayground.dto.Response;
import org.springframework.stereotype.Service;


import java.util.stream.Collectors;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class MathService {
    public Response findSquare(int number){
        return new Response(number * number);
    }

    public List<Response> multiplicationTable(int input){
        return IntStream.rangeClosed(1,10)
                .peek(i -> SleepUtil.sleepSeconds(1))
                .peek(i-> System.out.println("Math service processing: "+i))
                .mapToObj(i -> new Response(i*input))
                .collect(Collectors.toList());
    }
}
