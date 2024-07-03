package com.ashopshipping.messaging.producer;


import com.ashopshipping.model.dto.CreateOrderRequestDto;

public interface MessageProducer {
    void sendChangeOrderStatus(CreateOrderRequestDto createOrderRequestDto);
    void sendToNotification(CreateOrderRequestDto createOrderRequestDto);
}
