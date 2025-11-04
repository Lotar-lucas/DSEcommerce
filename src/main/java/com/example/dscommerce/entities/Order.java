package com.example.dscommerce.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tb_order")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
  private Instant moment;

  private OrderStatus status;

  // RELAÇÃO: Order -> User (Many-to-One)
  // Muitos pedidos pertencem a UM cliente
  // FK: client_id na tabela tb_order
  @ManyToOne
  @JoinColumn(name = "client_id")
  private User client;

  // RELAÇÃO: Order -> Payment (One-to-One)
  // Um pedido tem UM pagamento
  // mappedBy="order" indica que Payment é o lado proprietário (tem a FK)
  // cascade=ALL propaga operações de persistência para o Payment
  @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
  private Payment payment;

  // RELAÇÃO: Order -> OrderItem (One-to-Many)
  // Um pedido tem MUITOS itens
  // mappedBy="id.order" indica que OrderItem é o lado proprietário
  // Navega através da chave composta: id (OrderItemPK) -> order
  // Set evita duplicatas de itens no pedido
  @OneToMany(mappedBy = "id.order")
  private Set<OrderItem> items = new HashSet<>();

  public Order() {
  }

  public Order(Long id, User client, Instant moment, OrderStatus status, Payment payment) {
    this.id = id;
    this.client = client;
    this.moment = moment;
    this.status = status;
    this.payment = payment;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Instant getMoment() {
    return moment;
  }

  public void setMoment(Instant moment) {
    this.moment = moment;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public User getClient() {
    return client;
  }

  public void setClient(User client) {
    this.client = client;
  }

  public Payment getPayment() {
    return payment;
  }

  public void setPayment(Payment payment) {
    this.payment = payment;
  }

  public Set<OrderItem> getItems() {
    return items;
  }

  /**
   * Método auxiliar para obter apenas os Products de um pedido
   *
   * NAVEGAÇÃO: Order -> OrderItem -> Product
   *
   * Por que esse método existe?
   * - O relacionamento direto é Order -> OrderItem (items)
   * - OrderItem contém: Order + Product + quantity + price
   * - Às vezes precisamos apenas da lista de produtos, sem os detalhes do item
   *
   * Como funciona?
   * 1. items.stream() - Transforma o Set<OrderItem> em Stream
   * 2. .map(x -> x.getProduct()) - Para cada OrderItem, extrai o Product
   * 3. .toList() - Coleta os produtos em uma List<Product>
   *
   *
   * Nota: Retorna List ao invés de Set para manter a ordem e permitir
   * processamento sequencial com streams
   */
  public List<Product> getProducts() {
    return items.stream().map(x -> x.getProduct()).toList();
  }
}
