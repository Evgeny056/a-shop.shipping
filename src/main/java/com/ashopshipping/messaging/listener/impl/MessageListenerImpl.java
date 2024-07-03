package com.ashopshipping.messaging.listener.impl;

import com.ashopshipping.messaging.listener.MessageListener;
import com.ashopshipping.model.dto.CreateOrderRequestDto;
import com.ashopshipping.service.DeliveryService;
import com.ashopshipping.service.ShippingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListenerImpl implements MessageListener {

    private final ShippingService shippingService;
    private final DeliveryService deliveryService;

    private final String topic = "payed_orders";

    @KafkaListener(topics = topic, groupId = "shipping-group")
    public void listenMessage(CreateOrderRequestDto createOrderRequestDto) {
        log.info("Received order message from payment service");
        shippingService.shipment(createOrderRequestDto);
    }
}
