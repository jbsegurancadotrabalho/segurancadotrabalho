package br.com.jbst.config;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class InstantUtils {

    // Método para serializar um Instant para uma string formatada
    public static String serializeInstant(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        return formatter.format(instant);
    }

    // Método para desserializar uma string formatada para um Instant
    public static LocalDate deserializeLocalDate(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}

