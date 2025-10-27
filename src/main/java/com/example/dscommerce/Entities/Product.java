package com.example.dscommerce.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tb_Product")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  private Double price;

  private String imgUrl;

  public Product() {}

  public Product(Long id, String name, String description, Double price, String imgUrl) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.imgUrl = imgUrl;
  }

  // RELAÇÃO: Product <-> Category (Many-to-Many SIMPLES)
  // Um produto pode ter VÁRIAS categorias
  // Uma categoria pode ter VÁRIOS produtos
  // SEM atributos extras, então usa @ManyToMany direto (sem classe associativa)
  // @JoinTable cria tabela intermediária tb_product_category com as FKs
  // Set evita duplicatas (uma categoria não pode estar associada ao mesmo produto mais de uma vez)
  @ManyToMany
  @JoinTable( name = "tb_product_category",
          joinColumns = @JoinColumn(name = "product_id"),
          inverseJoinColumns = @JoinColumn(name = "category_id"))
  private Set<Category> categories = new HashSet<>();

  // RELAÇÃO: Product -> OrderItem (One-to-Many)
  // Um produto pode estar em MUITOS itens de pedido
  // mappedBy="id.product" indica que OrderItem é o lado proprietário
  // Navega através da chave composta: id (OrderItemPK) -> product
  @OneToMany(mappedBy = "id.product")
  private Set<OrderItem> items = new HashSet<>();


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  public Set<Category> getCategories() {
    return categories;
  }

  public Set<OrderItem> getItems() {
    return items;
  }

  /**
   * Método auxiliar para obter todos os Orders que contêm este produto
   *
   * NAVEGAÇÃO: Product -> OrderItem -> Order
   *
   * Por que esse método existe?
   * - O relacionamento direto é Product -> OrderItem (items)
   * - OrderItem contém: Order + Product + quantity + price
   * - Às vezes precisamos apenas saber em quais pedidos este produto aparece
   *
   * Como funciona?
   * 1. items.stream() - Transforma o Set<OrderItem> em Stream
   * 2. .map(x -> x.getOrder()) - Para cada OrderItem, extrai o Order
   * 3. .toList() - Coleta os pedidos em uma List<Order>
   *
   * Casos de uso comuns:
   * - Histórico de vendas de um produto
   * - Análise de quais clientes compraram determinado produto
   * - Relatórios de pedidos por produto
   *
   * Nota: Retorna List ao invés de Set para manter a ordem cronológica
   * e facilitar processamento com streams
   */
  public List<Order> getOrders() {
    return items.stream().map(x -> x.getOrder()).toList();
  }
}
