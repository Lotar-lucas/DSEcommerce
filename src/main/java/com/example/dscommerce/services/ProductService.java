package com.example.dscommerce.services;

import com.example.dscommerce.entities.Product;
import com.example.dscommerce.dto.ProductDTO;
import com.example.dscommerce.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

  @Transactional(readOnly = true)
  public Page<ProductDTO> findAll(Pageable pageable) {
    Page<Product> result = productRepository.findAll(pageable);
    return  result.map(ProductDTO::new);
  };

  @Transactional
  public ProductDTO insert(ProductDTO productDTO) {
    Product product = new Product();

    product.setName(productDTO.getName());
    product.setDescription(productDTO.getDescription());
    product.setPrice(productDTO.getPrice());
    product.setImgUrl(productDTO.getImgUrl());

    product = productRepository.save(product);
    return new ProductDTO(product);
  };
}
