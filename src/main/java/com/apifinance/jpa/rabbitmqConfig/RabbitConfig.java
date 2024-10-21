package com.apifinance.jpa.rabbitmqconfig;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String QUEUE_NAME = "payment-queue";
    public static final String EXCHANGE_NAME = "payment-exchange";
    public static final String PAYMENT_ROUTING_KEY = "payment.created";
    public static final String DLQ_NAME = "payment-dlq";

    // Definir fila durável
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true); // 'true' indica que a fila será durável
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DLQ_NAME, true);
    }

    @Bean
    public Exchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // Definir binding entre fila e exchange
    @Bean
    public Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(PAYMENT_ROUTING_KEY).noargs();
    }

    @Bean
    public Binding dlqBinding(Queue deadLetterQueue, Exchange exchange) {
        return BindingBuilder.bind(deadLetterQueue).to(exchange).with(PAYMENT_ROUTING_KEY + ".dlq").noargs();
    }


    // Definir RabbitTemplate para enviar mensagens
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(EXCHANGE_NAME);
        rabbitTemplate.setRoutingKey(PAYMENT_ROUTING_KEY);
        return rabbitTemplate;
    }

    // contêineres com prefetch count
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);

        // Definir o prefetch count (exemplo: 10)
        factory.setPrefetchCount(10);

        // Outras configurações opcionais
        factory.setConcurrentConsumers(1); // consumidores simultâneos
        factory.setMaxConcurrentConsumers(5); // consumidores simultâneos
        return factory;
    }
}
