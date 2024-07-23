package br.com.jbst.config;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomDateConverter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

    public static Instant parseStringToInstant(String dateString) throws DateTimeParseException {
        try {
            return Instant.from(formatter.parse(dateString));
        } catch (DateTimeParseException e) {
            // Trate o erro de formato de data inválido aqui ou relance a exceção
            throw new DateTimeParseException("Formato de data inválido: " + dateString, dateString, 0, e);
        }
    }

	
}
