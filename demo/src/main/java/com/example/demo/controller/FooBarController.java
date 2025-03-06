package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Foo bar")
@RestController
@RequestMapping("/book-service")
public class FooBarController {
    
    private Logger logger = LoggerFactory.getLogger(FooBarController.class);

    @GetMapping("/foo-bar")
    @Operation(summary = "Foo-bar")
    //@Retry(name = "foo-bar", fallbackMethod = "fallbackMethod")
    //CircuitBreaker(name = "default", fallbackMethod = "fallbackMethod")
    //@RateLimiter(name = "default") // determina a quantidade de chamadas que pode receber 
    @Bulkhead(name = "default")
    public String fooBar(){
        logger.info("Request to foo-bar is recived");
      //  var response = new RestTemplate().getForEntity("http://localhost:8080/foo-bar", String.class);
        return "foo-bar";
    }

    // poder ter o mesmo metodo para um tipo diferente de exceção
    // metodo q é lançado quando acontece um erro
    public String fallbackMethod(Exception e){
        return "Fallback foo bar!!!!";
    }
}
