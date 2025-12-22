package com.example.dscommerce.services;

import com.example.dscommerce.dto.OrderDTO;
import com.example.dscommerce.dto.OrderItemDTO;
import com.example.dscommerce.entities.Order;
import com.example.dscommerce.entities.OrderItem;
import com.example.dscommerce.entities.OrderStatus;
import com.example.dscommerce.entities.Product;
import com.example.dscommerce.entities.User;
import com.example.dscommerce.repositories.OrderItemRepository;
import com.example.dscommerce.repositories.OrderRepository;
import com.example.dscommerce.repositories.ProductRepository;
import com.example.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final OrderItemRepository orderItemRepository;
  private final UserService userService;


  public OrderService(OrderRepository orderRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository, UserService userService) {
    this.orderRepository = orderRepository;
    this.productRepository = productRepository;
    this.orderItemRepository = orderItemRepository;
    this.userService = userService;
  }

  @Transactional(readOnly = true)
  public OrderDTO findById(Long id){
    Order order = orderRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Order not found"));
    return new OrderDTO(order);
  }

  /**
   * Método principal de inserção de pedido
   * Responsabilidade: Coordenar o fluxo de criação do pedido
   */
  @Transactional
  public @Valid OrderDTO insert(OrderDTO orderDTO) {
    Order order = createNewOrder();
    associateAuthenticatedClient(order);
    addItemsToOrder(order, orderDTO.getItems());
    persistOrder(order);

    return new OrderDTO(order);
  }

  /**
   * Responsabilidade: Criar uma nova entidade Order com valores iniciais
   */
  private Order createNewOrder() {
    Order order = new Order();
    order.setMoment(Instant.now());
    order.setStatus(OrderStatus.WAITING_PAYMENT);
    return order;
  }

  /**
   * Responsabilidade: Associar o cliente autenticado ao pedido
   */
  private void associateAuthenticatedClient(Order order) {
    User user = userService.authenticated();
    order.setClient(user);
  }

  /**
   * Responsabilidade: Adicionar todos os itens ao pedido
   */
  private void addItemsToOrder(Order order, List<OrderItemDTO> itemsDTO) {
    for (OrderItemDTO itemDTO : itemsDTO) {
      OrderItem orderItem = createOrderItem(order, itemDTO);
      order.getItems().add(orderItem);
    }
  }

  /**
   * Responsabilidade: Criar um único OrderItem a partir do DTO
   */
  private OrderItem createOrderItem(Order order, OrderItemDTO itemDTO) {
    Product product = productRepository.getReferenceById(itemDTO.getProductId());
    return new OrderItem(order, product, itemDTO.getQuantity(), product.getPrice());
  }

  /**
   * Responsabilidade: Persistir o pedido e seus itens no banco de dados
   */
  private void persistOrder(Order order) {
    orderRepository.save(order);
    orderItemRepository.saveAll(order.getItems());
  }
}