package com.example.dscommerce.controllers;


import com.example.dscommerce.dto.ProductDTO;
import com.example.dscommerce.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
    ProductDTO dto = productService.findById(id);
    return ResponseEntity.ok(dto);
  }

  @GetMapping
  public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
    Page<ProductDTO> dto = productService.findAll(pageable);
    return ResponseEntity.ok(dto);
  }

  @PostMapping
  public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO productDTO) {
    productDTO = productService.insert(productDTO);

    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(productDTO.getId()).toUri();

    return ResponseEntity.created(uri).body(productDTO);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<ProductDTO> update(@PathVariable Long id , @RequestBody ProductDTO productDTO) {
    productDTO = productService.update(id, productDTO);

    return ResponseEntity.ok(productDTO);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> update(@PathVariable Long id) {
     productService.delete(id);

    return ResponseEntity.noContent().build();
  }
}
