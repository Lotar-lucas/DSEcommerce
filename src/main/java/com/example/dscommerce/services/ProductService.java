package com.example.dscommerce.services;

import com.example.dscommerce.Entities.Product;
import com.example.dscommerce.dto.ProductDTO;
import com.example.dscommerce.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Transactional(readOnly = true)
  public ProductDTO findById(Long id){
    Optional<Product> result = productRepository.findById(id);
    Product product = result.get();
    return new ProductDTO(product);
  };
}
