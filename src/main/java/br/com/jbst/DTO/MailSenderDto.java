package br.com.jbst.DTO;


import lombok.Data;

@Data
public class MailSenderDto {
	    private String mailTo;
	    private String subject;
	    private String body;
	    private byte[] attachment; // opcional
	    private String attachmentName; // opcional
	}


