package br.com.jbst.components;



import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jbst.DTO.MailSenderDto;

@Component
public class MessageConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MailSenderComponent mailSenderComponent;

    @RabbitListener(queues = "${queue.name}") // <<< Pega automaticamente da configuração application.properties
    public void receberMensagem(@Payload String mensagemJson) {
        try {
            System.out.println(">>> Mensagem recebida da fila: " + mensagemJson);
            
            MailSenderDto dto = objectMapper.readValue(mensagemJson, MailSenderDto.class);
            mailSenderComponent.sendMessage(dto);

        } catch (Exception e) {
            System.err.println("Erro ao processar mensagem da fila:");
            e.printStackTrace();
        }
    }
}

