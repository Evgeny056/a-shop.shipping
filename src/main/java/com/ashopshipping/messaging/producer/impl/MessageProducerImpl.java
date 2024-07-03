package com.ashopshipping.messaging.producer.impl;

import com.ashopshipping.messaging.producer.MessageProducer;
import com.ashopshipping.model.dto.CreateOrderRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProducerImpl implements MessageProducer {

    private String changeOrdersStatusTopic = "change_orders_status";
    private String newSentOrders = "sent_orders";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void sendChangeOrderStatus(CreateOrderRequestDto createOrderRequestDto) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(createOrderRequestDto);
            log.info("Change order status {}:", jsonMessage);
            kafkaTemplate.send(changeOrdersStatusTopic, jsonMessage);
        } catch (JsonProcessingException e) {
            log.error("Error while converting object to JSON: {}", e.getMessage());
        }
    }

    @Override
    public void sendToNotification(CreateOrderRequestDto createOrderRequestDto) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(createOrderRequestDto);
            log.info("Sending message to {}: {}", newSentOrders, jsonMessage);
            kafkaTemplate.send(newSentOrders, jsonMessage);
        } catch (JsonProcessingException e) {
            log.error("Error while converting object to JSON: {}", e.getMessage());
        }
    }
}
