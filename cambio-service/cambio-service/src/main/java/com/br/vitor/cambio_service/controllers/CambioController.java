package com.br.vitor.cambio_service.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.vitor.cambio_service.model.Cambio;
import com.br.vitor.cambio_service.repository.CambioRepository;

@RestController
@RequestMapping("/cambio-service")
public class CambioController {

    private Environment environment;

    private CambioRepository repository;

    public CambioController(Environment environment, CambioRepository repository) {
        this.environment = environment;
        this.repository = repository;
    }

    @GetMapping("/{amount}/{from}/{to}")
    public ResponseEntity<Cambio> getCambio(
                    @PathVariable("amount") BigDecimal amount,
                    @PathVariable("from") String from,
                    @PathVariable("to") String to) {
    
        var cambio = repository.findByFromAndTo(from, to);
        if (cambio == null) throw new RuntimeException("Currency Unsupported");
        var port = environment.getProperty("Local.server.port");
        cambio.setEnvironment(port);
        BigDecimal conversionFactor = cambio.getConversionFactor();
        BigDecimal convertedValue = conversionFactor.multiply(amount);
        cambio.setConvertedValue(convertedValue.setScale(2, RoundingMode.CEILING)); // arredondar para duas casas decimais                
        
            return ResponseEntity.ok(cambio);
    }
}
