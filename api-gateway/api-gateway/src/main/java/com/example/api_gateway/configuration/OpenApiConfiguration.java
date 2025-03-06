package com.example.api_gateway.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import reactor.core.publisher.Flux;

@Configuration
public class OpenApiConfiguration {
    
    @Bean
    @Lazy(false)
    public List<GroupedOpenApi> apis(RouteDefinitionLocator locator){
       
       Flux<RouteDefinition> definitions = locator.getRouteDefinitions();   
       definitions.filter(
                    routeDefinition -> routeDefinition.getId()
                        .matches(".*-service"))
                            .subscribe(routeDefinition -> {
                                String name = routeDefinition.getId();
                                GroupedOpenApi.builder()
                                .pathsToMatch("/" + name + "/**")
                                .group(name)
                                .build();                    
             });
        return new ArrayList<>(); 
    }
}
