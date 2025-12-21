package com.example.dscommerce.dto;

import com.example.dscommerce.entities.Payment;

import java.time.Instant;

public class PaymentDTO {
  private Long id;
  private Instant moment;

  public PaymentDTO(Long id, Instant momennt) {
    this.id = id;
    this.moment = momennt;
  }

  public PaymentDTO(Payment entity) {
    id = entity.getId();
    moment = entity.getMoment();
  }

  public Long getId() {
    return id;
  }

  public Instant getMoment() {
    return moment;
  }
}
