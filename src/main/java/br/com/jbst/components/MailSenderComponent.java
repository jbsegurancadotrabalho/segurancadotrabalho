package br.com.jbst.components;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import br.com.jbst.DTO.MailSenderDto;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;



@Component
public class MailSenderComponent {

    private static final Logger logger = LoggerFactory.getLogger(MailSenderComponent.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String userName;

    public void sendMessage(MailSenderDto dto) {
        logger.info("📧 Iniciando envio de e-mail para: {}", dto.getMailTo());

        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            // true = multipart; UTF-8 for accents; text/html for HTML interpretation
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(userName);
            helper.setTo(dto.getMailTo());
            helper.setSubject(dto.getSubject());

            // ✅ Define corpo como HTML
            helper.setText(dto.getBody(), true);

            // Adiciona o anexo, se houver
            if (dto.getAttachment() != null && dto.getAttachment().length > 0 && dto.getAttachmentName() != null) {
                ByteArrayDataSource dataSource = new ByteArrayDataSource(dto.getAttachment(), "application/octet-stream");
                helper.addAttachment(dto.getAttachmentName(), dataSource);
                logger.debug("📎 Anexo '{}' adicionado ao e-mail.", dto.getAttachmentName());
            } else {
                logger.debug("ℹ️ Nenhum anexo a ser adicionado.");
            }

            javaMailSender.send(message);
            logger.info("✅ E-mail enviado com sucesso para: {}", dto.getMailTo());

        } catch (Exception e) {
            logger.error("❌ Erro ao enviar e-mail para: {}", dto.getMailTo(), e);
        }
    }
}
