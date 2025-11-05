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
    Product entity = new Product();
    copyDtoToEntity(productDTO, entity);
    entity = productRepository.save(entity);
    return new ProductDTO(entity);
  };

  @Transactional
  public ProductDTO update(Long id, ProductDTO productDTO) {

    Product entity = productRepository.getReferenceById(id);
    copyDtoToEntity(productDTO, entity);
    entity = productRepository.save(entity);
    return new ProductDTO(entity);
  }

  private void copyDtoToEntity(ProductDTO productDTO, Product entity) {

    entity.setName(productDTO.getName());
    entity.setDescription(productDTO.getDescription());
    entity.setPrice(productDTO.getPrice());
    entity.setImgUrl(productDTO.getImgUrl());
  }
}
