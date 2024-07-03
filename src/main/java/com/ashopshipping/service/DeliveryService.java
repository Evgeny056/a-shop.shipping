package com.ashopshipping.service;

import com.ashopshipping.exception.OrderNotFoundException;
import com.ashopshipping.mapper.OrderMapper;
import com.ashopshipping.messaging.producer.MessageProducer;
import com.ashopshipping.model.dto.CreateOrderRequestDto;
import com.ashopshipping.model.entity.Order;
import com.ashopshipping.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final OrderRepository orderRepository;
    private final MessageProducer messageProducer;
    private final OrderMapper orderMapper;

    @Transactional
    public void deliveryComplete(CreateOrderRequestDto createOrderRequestDto) {

        if (createOrderRequestDto.getStatus().equalsIgnoreCase("SHIPPED")) {
            Order order = orderRepository.findById(createOrderRequestDto.getOrderId())
                    .orElseThrow(() -> new OrderNotFoundException("Order not found"));

            order.setStatus("DELIVERED");
            orderRepository.save(order);

            CreateOrderRequestDto sendDto = orderMapper.INSTANCE.toDto(order);
            messageProducer.sendChangeOrderStatus(sendDto);
            messageProducer.sendToNotification(sendDto);
        }
    }
}
