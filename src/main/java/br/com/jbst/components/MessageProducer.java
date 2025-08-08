package br.com.jbst.components;


import org.springframework.amqp.core.Queue; // ✅ IMPORTAÇÃO CERTA
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jbst.DTO.MailSenderDto;

@Component
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessage(MailSenderDto dto) {
        try {
            String mensagemJson = objectMapper.writeValueAsString(dto);
            rabbitTemplate.convertAndSend(queue.getName(), mensagemJson);
            System.out.println(">>> Mensagem enviada à fila: " + mensagemJson);
        } catch (Exception e) {
            System.err.println("Erro ao converter DTO para JSON ou enviar para a fila:");
            e.printStackTrace();
        }
    }
}

