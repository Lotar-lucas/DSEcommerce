package com.example.dscommerce.Entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Classe Associativa do relacionamento Many-to-Many entre Order e Product
 *
 * ESTRUTURA:
 * - Order (1) ---- (N) OrderItem (N) ---- (1) Product
 *
 * MOTIVO PARA CLASSE ASSOCIATIVA:
 * - Relacionamento Many-to-Many COM atributos extras (quantity, price)
 * - Um pedido pode ter vários produtos
 * - Um produto pode estar em vários pedidos
 * - Cada combinação (pedido + produto) tem quantidade e preço específicos
 *
 * CHAVE PRIMÁRIA COMPOSTA:
 * - @EmbeddedId indica que a PK é composta e está definida em OrderItemPK
 * - PK = (order_id + product_id)
 * - Garante unicidade: mesmo produto não pode aparecer duplicado no mesmo pedido
 */
@Entity
@Table(name = "tb_order_item")
public class OrderItem {

  // Chave composta incorporada (order_id + product_id)
  @EmbeddedId
  private OrderItemPK id = new OrderItemPK();

  // Atributos extras do relacionamento (motivo para usar classe associativa)
  private Integer quantity;
  private Double price;

  public OrderItem() {}

  public OrderItem(Order order, Product product, Integer quantity, Double price) {
    id.setOrder(order);
    id.setProduct(product);

    this.quantity = quantity;
    this.price = price;
  }

  public Order getOrder() {
    return id.getOrder();
  }

  public void setOrder(Order order) {
    id.setOrder(order);
  }

  public Product getProduct() {
    return id.getProduct();
  }

  public void setProduct(Product product) {
    id.setProduct(product);
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }
}
