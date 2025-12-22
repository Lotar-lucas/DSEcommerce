package com.example.dscommerce.services;

import com.example.dscommerce.entities.User;
import com.example.dscommerce.services.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired
  private UserService userService;

  public void validateSelfOrAdmin(Long userId) {
    User user = userService.authenticated();
    if (!user.hasRole("ROLE_ADMIN") && !user.getId().equals(userId)) {
      throw new ForbiddenException("Access denied");
    }
  }
}
