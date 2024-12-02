package com.triings.user_service.kafka.consumer;

import com.triings.user_service.vo.AuthRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

/*    @KafkaListener(topics = "myTopic",groupId = "myGroup")
    public void consumeMsg(String msg){
        log.info(format("Consuming the message from myTopic: : %s",msg));
    }*/

    @KafkaListener(topics = "my-topic",groupId = "user-service-group")
    public void consumeJSONMsg(List<AuthRequest>  msg){
        log.info(format("Consuming the message from my-topic: : %s",msg));


    }
}
