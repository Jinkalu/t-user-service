package com.triings.user_service.kafka.producer;



import com.triings.trringscommon.vo.AuthRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaJsonProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(List<AuthRequest> msg){
        Message<List<AuthRequest>> message = MessageBuilder.withPayload(msg)
                .setHeader(KafkaHeaders.TOPIC, "my-topic")
                .build();
        kafkaTemplate.send(message);
    }
}
