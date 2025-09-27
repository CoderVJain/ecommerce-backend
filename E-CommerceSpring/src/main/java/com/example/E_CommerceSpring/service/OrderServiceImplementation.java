package com.example.E_CommerceSpring.service;

import com.example.E_CommerceSpring.exception.OrderException;
import com.example.E_CommerceSpring.model.*;
import com.example.E_CommerceSpring.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImplementation implements OrderService {

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override

    public Order createOrder(User user, Address shippingAddress) {
        Address address;

        // Case 1: Existing Address

            shippingAddress.setUser(user);
            address = addressRepository.save(shippingAddress);


        // Always fetch fresh cart
        Cart cart = cartService.getUserCart(user.getId());
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());
            orderItem.setSize(item.getSize());
            orderItem.setUserId(item.getUserId());
            orderItem.setDiscountedPrice(item.getDiscountedPrice());
            orderItems.add(orderItem);
        }

        // Create new order every time
        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(orderItems);
        order.setShippingAddress(address);
        order.setTotalPrice(cart.getTotalPrice());
        order.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
        order.setDiscount(cart.getDiscount());
        order.setTotalItems(cart.getTotalItem());
        order.setDeliveryDate(LocalDateTime.now().plusDays(5));
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus("PENDING");

        if (order.getPaymentDetails() != null) {
            order.getPaymentDetails().setStatus("PENDING");
        }

        order.setCreatedAt(LocalDateTime.now());

        // Save order first to generate ID
        Order createdOrder = orderRepository.save(order);

        // Link each orderItem to this new order
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(createdOrder);
        }
        orderItemRepository.saveAll(orderItems);

        // Clear cart after placing order
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return createdOrder;
    }



    @Override
    public Order getOrderById(Long id) throws OrderException {
        Optional<Order> optionalOrder = orderRepository.findById(id);

        if(optionalOrder.isPresent()) {
            return optionalOrder.get();
        } else {
            throw new OrderException("Order not found with id: " + id);
        }

    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        List<Order> orders = orderRepository.getUserOrders(userId);
        return orders;
    }

    @Override
    public Order placedOrder(Long id) throws OrderException {
        Order order = getOrderById(id);
        order.setOrderStatus("PLACED");
        order.getPaymentDetails().setStatus("COMPLETED");
        return orderRepository.save(order);
    }

    @Override
    public Order confirmedOrder(Long id) throws OrderException {
        Order order = getOrderById(id);
        order.setOrderStatus("CONFIRMED");
        return orderRepository.save(order);
    }

    @Override
    public Order shippedOrder(Long id) throws OrderException {
        Order order = getOrderById(id);
        order.setOrderStatus("SHIPPED");
        return orderRepository.save(order);
    }

    @Override
    public Order deliveredOrder(Long id) throws OrderException {
        Order order = getOrderById(id);
        order.setOrderStatus("DELIVERED");
        return orderRepository.save(order);
    }

    @Override
    public Order cancelledOrder(Long id) throws OrderException {
        Order order = getOrderById(id);
        order.setOrderStatus("CANCELLED");
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(Long id) throws OrderException {
        orderRepository.deleteById(id);
    }
}
