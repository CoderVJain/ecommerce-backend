package com.example.E_CommerceSpring.service;

import com.example.E_CommerceSpring.exception.OrderException;
import com.example.E_CommerceSpring.model.Address;
import com.example.E_CommerceSpring.model.Order;
import com.example.E_CommerceSpring.model.User;

import java.util.List;

public interface OrderService {

    public Order createOrder(User user, Address shippingAddress);
    public Order getOrderById(Long id) throws OrderException;
    public List<Order> usersOrderHistory(Long userId);
    public Order placedOrder(Long id) throws OrderException;
    public Order confirmedOrder(Long id) throws OrderException;
    public Order shippedOrder(Long id) throws OrderException;
    public Order deliveredOrder(Long id) throws OrderException;
    public Order cancelledOrder(Long id) throws OrderException;
    public List<Order> getAllOrders();
    public void deleteOrder(Long id) throws OrderException;
}
