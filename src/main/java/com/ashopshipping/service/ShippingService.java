package com.ashopshipping.service;

import com.ashopshipping.exception.DeliveryException;
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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingService {

    private final OrderRepository orderRepository;
    private final MessageProducer messageProducer;
    private final OrderMapper orderMapper;
    private final DeliveryService deliveryService;

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    @Transactional
    public void shipment(CreateOrderRequestDto createOrderRequestDto) {
        if (createOrderRequestDto.getStatus().equalsIgnoreCase("PAID")) {
            Order order = orderRepository.findById(createOrderRequestDto.getOrderId())
                    .orElseThrow(() -> new OrderNotFoundException("Order not found"));

            log.info("Order found. Delivery simulation...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new DeliveryException("During delivery, something went wrong...");
            }

            order.setStatus("SHIPPED");
            orderRepository.save(order);

            CreateOrderRequestDto sendDto = orderMapper.INSTANCE.toDto(order);
            messageProducer.sendChangeOrderStatus(sendDto);

            // Запланировать завершение доставки через 5 секунд
            executorService.schedule(() -> deliveryService.deliveryComplete(sendDto), 5, TimeUnit.SECONDS);
        }
    }
}

