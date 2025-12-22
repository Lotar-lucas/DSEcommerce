package com.example.dscommerce.controllers;


import com.example.dscommerce.dto.OrderDTO;
import com.example.dscommerce.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping(value = "/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
  @GetMapping(value = "/{id}")
  public ResponseEntity<OrderDTO> findById(@PathVariable Long id) {
    OrderDTO dto = orderService.findById(id);
    return ResponseEntity.ok(dto);
  }

  @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
  @PostMapping
  public ResponseEntity<OrderDTO> insert(@Valid @RequestBody OrderDTO orderDTO) {
    orderDTO = orderService.insert(orderDTO);

    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
      .buildAndExpand(orderDTO.getId()).toUri();

    return ResponseEntity.created(uri).body(orderDTO);
  }
}
