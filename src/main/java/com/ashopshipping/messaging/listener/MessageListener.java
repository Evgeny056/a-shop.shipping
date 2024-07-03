package com.ashopshipping.messaging.listener;

import com.ashopshipping.model.dto.CreateOrderRequestDto;
import org.springframework.messaging.MessagingException;

public interface MessageListener {
    void listenMessage(CreateOrderRequestDto createOrderRequestDto) throws MessagingException;
}
