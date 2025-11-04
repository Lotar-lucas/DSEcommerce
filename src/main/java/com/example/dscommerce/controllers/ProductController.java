package com.example.dscommerce.controllers;

import com.example.dscommerce.Entities.Product;
import com.example.dscommerce.dto.ProductDTO;
import com.example.dscommerce.repositories.ProductRepository;
import com.example.dscommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping(value = "/{id}")
  public ProductDTO findById(@PathVariable Long id) {
    ProductDTO dto = productService.findById(id);
    return dto;
  }

}
