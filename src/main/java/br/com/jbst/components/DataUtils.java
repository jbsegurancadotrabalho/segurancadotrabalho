package br.com.jbst.components;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataUtils {

    private static final DateTimeFormatter FORMATADOR_PT_BR = 
        DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));

    public static String formatarDataIsoParaPortugues(String dataIso) {
        if (dataIso == null || dataIso.isBlank()) {
            return "Data não informada";
        }

        try {
            Instant instant = Instant.parse(dataIso);
            LocalDateTime dataHora = LocalDateTime.ofInstant(instant, ZoneId.of("America/Sao_Paulo"));
            return dataHora.format(FORMATADOR_PT_BR);
        } catch (Exception e) {
            return "Data inválida";
        }
    }
}
