package com.github.kegszool.messaging.consumer;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;

import org.springframework.amqp.support.AmqpHeaders;
import com.github.kegszool.messaging.util.ServiceMessageUtils;
import com.github.kegszool.messaging.RequestExecutor;
import com.github.kegszool.exception.request.RequestException;
import com.github.kegszool.messaging.dto.service.ServiceException;
import com.github.kegszool.messaging.dto.service.ServiceMessage;
import com.github.kegszool.messaging.producer.ProducerService;

@Log4j2
public abstract class BaseRequestConsumer<I, E extends RequestExecutor> implements RequestConsumer<I> {

    @Value("${spring.rabbitmq.template.routing-key.service_exception}")
    private String SERVICE_EXCEPTION_ROUTING_KEY;

    private final ProducerService responseProducer;
    private final E executor;

    public BaseRequestConsumer(ProducerService responseProducer, E executor) {
        this.responseProducer = responseProducer;
        this.executor = executor;
    }

    @Override
    public void consume(ServiceMessage<I> serviceMessage, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        if (ServiceMessageUtils.isDataValid(serviceMessage, routingKey)) {
            ServiceMessageUtils.logReceivedRequest(serviceMessage, routingKey);
            processRequest(serviceMessage, routingKey);
        } else {
            throw ServiceMessageUtils.handleInvalidServiceMessage(serviceMessage, routingKey);
        }
    }

    private void processRequest(ServiceMessage<I> serviceMessage, String routingKey) {
        try {
            ServiceMessage<?> response = executor.execute(serviceMessage);
            if (response != null) {
                String responseRoutingKey = executor.getResponseRoutingKey();
                responseProducer.produce(response, responseRoutingKey);
            }
        } catch (RequestException ex) {
            sendServiceException(ex, routingKey, serviceMessage.getMessageId(), serviceMessage.getChatId());
        }
    }

    private void sendServiceException(RequestException ex, String routingKey, Integer messageId, String chatId) {
        log.error("Error while handling request for routing key: {}", routingKey, ex);

        String exceptionName = ex.getClass().getSimpleName();
        String exceptionMessage = ex.getMessage();
        var serviceException = new ServiceException(exceptionName, exceptionMessage);

        ServiceMessage<ServiceException> serviceMessage = new ServiceMessage<>(messageId, chatId, serviceException);
        responseProducer.produce(serviceMessage, SERVICE_EXCEPTION_ROUTING_KEY);
    }
}