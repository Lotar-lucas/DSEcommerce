package com.example.dscommerce.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Classe de Chave Composta para OrderItem
 *
 * PORQUE USAR CHAVE COMPOSTA (OrderItemPK)?
 *
 * Em relacionamentos Many-to-Many com CLASSE ASSOCIATIVA (OrderItem), precisamos de uma
 * chave composta quando:
 * 1. A tabela associativa possui atributos extras (quantity, price)
 * 2. A chave primária deve ser composta pelas duas FKs (order_id + product_id)
 * 3. Esta combinação garante que não haverá duplicatas: um produto só pode aparecer
 *    UMA VEZ em cada pedido (se precisar de mais quantidade, ajusta o campo quantity)
 *
 * ALTERNATIVAS:
 * - Sem atributos extras: @ManyToMany simples (sem classe associativa)
 * - Com atributos extras: Classe associativa + @EmbeddedId (ESTE CASO)
 * - Com atributos extras: Classe associativa + @Id próprio (menos comum)
 *
 * @Embeddable indica que esta classe será incorporada em outra entidade (OrderItem)
 * e não terá tabela própria no banco de dados.
 */
@Embeddable
public class OrderItemPK {

  // RELAÇÃO: OrderItem -> Order (Many-to-One)
  // Muitos itens de pedido pertencem a UM pedido
  // Parte da chave composta: order_id
  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  // RELAÇÃO: OrderItem -> Product (Many-to-One)
  // Muitos itens de pedido podem referenciar o MESMO produto
  // Parte da chave composta: product_id
  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  public OrderItemPK() {}

  public OrderItemPK(Product product, Order order) {
    this.product = product;
    this.order = order;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }
}
