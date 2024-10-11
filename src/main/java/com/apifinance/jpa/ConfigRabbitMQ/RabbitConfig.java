package com.apifinance.jpa.ConfigRabbitMQ;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    @Bean
    public Queue paymentQueue() {
        return new Queue("payment-created", true); // true significa que a fila é durável
    }
}