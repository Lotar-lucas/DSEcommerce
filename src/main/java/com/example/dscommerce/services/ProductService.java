package com.example.dscommerce.services;

import com.example.dscommerce.dto.CategoryDTO;
import com.example.dscommerce.dto.ProductMinDTO;
import com.example.dscommerce.entities.Category;
import com.example.dscommerce.entities.Product;
import com.example.dscommerce.dto.ProductDTO;
import com.example.dscommerce.repositories.ProductRepository;
import com.example.dscommerce.services.exceptions.DatabaseException;
import com.example.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Transactional(readOnly = true)
  public ProductDTO findById(Long id){
    Product product = productRepository.findById(id).orElseThrow(
      ()-> new ResourceNotFoundException("Product not found")
    );
    return new ProductDTO(product);
  };

  @Transactional(readOnly = true)
  public Page<ProductMinDTO> findAll(String name, Pageable pageable) {
    Page<Product> result = productRepository.searchByName(name, pageable);
    return  result.map(ProductMinDTO::new);
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
    try {
      Product entity = productRepository.getReferenceById(id);
      copyDtoToEntity(productDTO, entity);
      entity = productRepository.save(entity);
      return new ProductDTO(entity);
    } catch (Exception e) {
      throw new ResourceNotFoundException("Product not found");
    }
  }

  private void copyDtoToEntity(ProductDTO productDTO, Product entity) {
    entity.setName(productDTO.getName());
    entity.setDescription(productDTO.getDescription());
    entity.setPrice(productDTO.getPrice());
    entity.setImgUrl(productDTO.getImgUrl());

    entity.getCategories().clear();
    for (CategoryDTO categoryDTO : productDTO.getCategories()){
      Category cat = new Category();
      cat.setId(categoryDTO.getId());
      entity.getCategories().add(cat);
    }
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  public void delete(Long id) {

    if(!productRepository.existsById(id)){
      throw new ResourceNotFoundException("Product not found");
    }

    try {
      productRepository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException("Referential integrity failure");
    }
  }
}
