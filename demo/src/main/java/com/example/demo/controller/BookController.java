package com.example.demo.controller;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.clients.CambioProxy;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Book endpoint") // Spring Doc 
@RestController
@RequestMapping("/book-service")
public class BookController {
    
    public Environment environment;

    public BookRepository bookRepository;

    public CambioProxy cambioProxy;

    public BookController(Environment environment, BookRepository bookRepository,
            CambioProxy cambioProxy) {
        this.environment = environment;
        this.bookRepository = bookRepository;
        this.cambioProxy = cambioProxy;
    }

    @Operation(summary = "find a specific book by your ID") // breve descrição do que o metodo faz
    @GetMapping(value = "/{id}/{currency}")
    public ResponseEntity<Book> findBook(
            @PathVariable("id") long id,
            @PathVariable("currency") String currency
            ){
        
        var book = bookRepository.findById(id).get();
        if(book == null) {
            return ResponseEntity.notFound().build();
        }
        
        var cambio = cambioProxy.getCambio(book.getPrice(), "USD", currency);
        var port = environment.getProperty("local.server.port");
       
        book.setEnvioromnent(port + " FEIGN");

        book.setPrice(cambio.getBody().getConvertedValue());
        
        return ResponseEntity.ok(book);
        }

}
