package com.apifinance.jpa.rabbitmqConfig;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.enums.rabbitmqMessageStatus;
import com.apifinance.jpa.models.RabbitMQMessage;
import com.apifinance.jpa.repositories.RabbitMQMessageRepository;
import com.rabbitmq.client.Channel;

@Service
public class RabbitmqMessageConsumer implements ChannelAwareMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RabbitmqMessageConsumer.class);
    private final RabbitMQMessageRepository rabbitmqMessageRepository;

    public RabbitmqMessageConsumer(RabbitMQMessageRepository rabbitmqMessageRepository) {
        this.rabbitmqMessageRepository = rabbitmqMessageRepository;
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        
        String messageContent = new String(message.getBody());

        // Verificar se a mensagem está vazia ou apenas espaços em branco
        if (messageContent.trim().isEmpty()) {
            logger.error("Mensagem vazia recebida");
            return; 
        }

        try {
            
            // Aguardar 30 segundos antes de processar a mensagem
            Thread.sleep(30000); // 30 segundos em milissegundos

            // Criar e salvar a mensagem no banco de dados
            RabbitMQMessage rabbitMQMessage = new RabbitMQMessage();
            rabbitMQMessage.setMessageContent(messageContent);
            rabbitMQMessage.setStatus(rabbitmqMessageStatus.PENDING);
            rabbitMQMessage.setSentAt(ZonedDateTime.now());
            rabbitmqMessageRepository.save(rabbitMQMessage);

            // Processar a mensagem
            processMessage(rabbitMQMessage);

            // Confirmar a mensagem após o processamento
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (IOException e) {
            logger.error("Erro ao processar mensagem: {}", e.getMessage(), e);
            // Se ocorrer um erro, você pode re-encaminhar a mensagem ou fazer uma negativa
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        } catch (InterruptedException e) {
            logger.error("Processamento interrompido: {}", e.getMessage());
            Thread.currentThread().interrupt(); // Restaura o estado de interrupção
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }

    // Método que processa a mensagem
    private void processMessage(RabbitMQMessage message) {
        // Lógica de processamento
        System.out.println("Processando a mensagem: " + message.getMessageContent());
        // Lógica adicional pode ser adicionada aqui
    }
}
