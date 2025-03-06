package com.totos.productsAPI.controllers;

import com.totos.productsAPI.services.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductsService productService;

    @GetMapping("/test")
    public String testEndpoint() {
        return "Endpoint working";
    }
}
