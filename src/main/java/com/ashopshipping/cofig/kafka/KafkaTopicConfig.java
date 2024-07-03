package com.ashopshipping.cofig.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka
public class KafkaTopicConfig {
    private String PaymentTopic = "payed_topic";
    private String newSentOrdersTopic = "sent_orders";
    private String changeOrdersStatusTopic = "change_orders_status";

    @Bean
    public NewTopic newSentOrdersTopic() {
        return TopicBuilder
                .name(newSentOrdersTopic)
                .build();
    }

    @Bean
    public NewTopic PaymentTopic(){
        return TopicBuilder
                .name(PaymentTopic)
                .build();
    }

    @Bean
    public NewTopic changeOrdersStatusTopic(){
        return TopicBuilder
                .name(changeOrdersStatusTopic)
                .build();
    }
}
