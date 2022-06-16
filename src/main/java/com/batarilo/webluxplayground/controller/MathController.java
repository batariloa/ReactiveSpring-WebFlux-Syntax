package com.batarilo.webluxplayground.controller;

import com.batarilo.webluxplayground.dto.Response;
import com.batarilo.webluxplayground.service.MathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("math")
public class MathController {

    @Autowired
    MathService ms;

    @GetMapping("square/{input}")
    private Response handleSquareInput(@PathVariable int input){
        return ms.findSquare(input);
    }

    @GetMapping("table/{input}")
    private List<Response> handleTableInput(@PathVariable int input){
        return ms.multiplicationTable(input);
    }



}
