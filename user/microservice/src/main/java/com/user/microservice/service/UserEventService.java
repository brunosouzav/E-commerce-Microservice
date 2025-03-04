package com.user.microservice.service;

import com.user.microservice.dto.UserResponseCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class UserEventService {


   @Value("${spring.rabbitmq.template.exchange}")
   private String exchangeName;

   @Autowired
   private RabbitTemplate rabbitTemplate;


  public UserResponseCode enfileirarRegistros(UserResponseCode userResponseCode) {
        rabbitTemplate.convertAndSend(exchangeName, "" , userResponseCode);
       return userResponseCode;
    }

}