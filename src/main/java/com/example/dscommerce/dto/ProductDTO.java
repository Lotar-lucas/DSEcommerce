package com.example.dscommerce.dto;

import com.example.dscommerce.entities.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductDTO {

  private Long id;

  @Size(min = 3, max = 80, message = "Field name must have between 3 and 80 characters")
  @NotBlank(message = "Field name is required")
  private String name;

  @Size(min = 10, message = "Field description must have at least 10 characters")
  @NotBlank(message = "Field description is required")
  private String description;

  @Positive(message = "Field price must be positive")
  private Double price;
  private String imgUrl;

  public ProductDTO(Long id, String name, String description, Double price, String imgUrl) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.imgUrl = imgUrl;
  }
  public ProductDTO() {}

  //Sobrecarga do construtor
  public ProductDTO(Product entity) {
    id = entity.getId();
    name = entity.getName();
    description = entity.getDescription();
    price = entity.getPrice();
    imgUrl = entity.getImgUrl();
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Double getPrice() {
    return price;
  }

  public String getImgUrl() {
    return imgUrl;
  }
}
