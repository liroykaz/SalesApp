package com.company.salescafe.services;

import com.company.salescafe.entity.Order;
import com.company.salescafe.entity.OrderStatus;

public interface OrderService {
    String NAME = "sales_OrderService";

    int generateNewOrderNumber();

    void setOrderStatus(Order order, OrderStatus status);
}
