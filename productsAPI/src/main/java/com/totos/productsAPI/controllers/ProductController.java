package com.totos.productsAPI.controllers;

import com.totos.productsAPI.models.Products;
import com.totos.productsAPI.services.ProductsService;
import jakarta.validation.Valid;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductsService productService;

    @GetMapping("/test")
    public String testEndpoint() {
        return "You are at test endpoint!";
    }


    @GetMapping
    public List<Products> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('API')")
    public Products getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('API')")
    public Products createProduct(@Valid @RequestBody Products product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
     @PreAuthorize("hasRole('ADMIN') or hasRole('API')")
    public Products updateProduct(@PathVariable Long id, @Valid @RequestBody Products product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
     @PreAuthorize("hasRole('ADMIN') or hasRole('API')")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

}
