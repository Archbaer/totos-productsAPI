package com.totos.productsAPI.services;

import com.totos.productsAPI.models.Products;
import com.totos.productsAPI.repositories.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsService {

    @Autowired
    private ProductsRepository productsRepository;

    public List<Products> getAllProducts() {
        return productsRepository.findAll();
    }

    public Products getProductById(Long id) {
        return productsRepository.findById(id).orElseThrow(() -> new RuntimeException("Product Not Found."));
    }

    public Products createProduct(Products product) {
        return productsRepository.save(product);
    }

    public Products updateProduct(Long id, Products product) {
        Products existing = getProductById(id);
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStockQuantity(product.getStockQuantity());
        return productsRepository.save(existing);
    }

    public void deleteProduct(Long id) {
        productsRepository.deleteById(id);
    }

}
