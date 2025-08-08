package br.com.jbst.services;

import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jbst.entities.AssinaturaInstrutorD4Sign;
import br.com.jbst.entities.Turmas;
import br.com.jbst.repositories.AssinaturaInstrutorD4SignRepository;

import br.com.jbst.repositories.TurmasRepository;
import jakarta.persistence.EntityNotFoundException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class AssinaturaImportService {

    private static final Logger logger = LoggerFactory.getLogger(AssinaturaImportService.class);

    private static final String D4SIGN_BASE_URL = "https://secure.d4sign.com.br/api/v1/documents/";
    private static final String PDF_ENDPOINT = "/download";
    private static final String API_KEY = "live_ef97bcf71b0eaec4fd8000c5fd6d763bb73458477d11c0367bba7a1c87d53310";
    private static final String CRYPT_KEY = "live_crypt_1KYmQoB3dmMamb5gGBZKlGb5k0suc1RG";
    private static final String IMAGE_FORMAT = "jpg";
    private static final int IMAGE_DPI = 300;

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
    .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
    .readTimeout(90, java.util.concurrent.TimeUnit.SECONDS)   // ⬅️ mais importante
    .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
    .build();
    
    @Autowired private TurmasRepository turmasRepository;
    @Autowired private AssinaturaInstrutorD4SignRepository assinaturaInstrutorRepository;

    @Transactional
    public void importarAssinaturasComoImagens(UUID idTurma, String uuidDocumentoD4Sign) throws IOException {
        logger.info("🚀 Iniciando importação de assinaturas para Turma ID: {} | Documento UUID: {}", idTurma, uuidDocumentoD4Sign);

        Turmas turma = turmasRepository.findById(idTurma)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com ID: " + idTurma));

        String pdfDownloadUrl = getD4SignPdfDownloadUrl(uuidDocumentoD4Sign);
        byte[] pdfBytes = downloadPdfBytes(pdfDownloadUrl);

        savePdfPagesAsImages(pdfBytes, turma);

        logger.info("✅ Importação concluída para Turma ID: {}", idTurma);
    }

    private String getD4SignPdfDownloadUrl(String uuidDocumento) throws IOException {
        logger.debug("📥 Solicitando URL do PDF para UUID: {}", uuidDocumento);

        String url = String.format("%s%s%s?tokenAPI=%s&cryptKey=%s",
                D4SIGN_BASE_URL, uuidDocumento, PDF_ENDPOINT, API_KEY, CRYPT_KEY);

        String jsonPayload = "{\"type\":\"pdf\",\"language\":\"pt\",\"encoding\":false,\"document\":false}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonPayload);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = Objects.requireNonNull(response.body()).string();

            if (!response.isSuccessful()) {
                logger.error("❌ Erro ao obter URL do PDF. Status: {}, Resposta: {}", response.code(), responseBody);
                throw new IOException("Erro ao obter URL do PDF do D4Sign: " + response.code() + " - " + responseBody);
            }

            JsonNode json = new ObjectMapper().readTree(responseBody);

            if (!json.has("url")) {
                logger.error("❌ Resposta inválida: campo 'url' não encontrado. Resposta: {}", responseBody);
                throw new IOException("Resposta inválida da D4Sign: campo 'url' ausente.");
            }

            String downloadUrl = json.get("url").asText();
            logger.debug("🔗 URL do PDF obtida: {}", downloadUrl);
            return downloadUrl;
        }
    }

    private byte[] downloadPdfBytes(String pdfUrl) throws IOException {
        logger.debug("📡 Baixando PDF da URL: {}", pdfUrl);

        Request request = new Request.Builder().url(pdfUrl).get().build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao baixar PDF. HTTP " + response.code() + ": " + response.message());
            }

            String contentType = response.header("Content-Type", "");
            if (!contentType.toLowerCase().contains("pdf")) {
                String erroTexto = Objects.requireNonNull(response.body()).string();
                logger.error("⚠️ Conteúdo recebido não é PDF: {}", erroTexto);
                throw new IOException("Conteúdo inválido recebido. Esperado PDF, recebido: " + erroTexto);
            }

            byte[] bytes = Objects.requireNonNull(response.body()).bytes();
            logger.info("📄 PDF baixado com sucesso. Tamanho: {} bytes", bytes.length);

            // Debug opcional: salvar PDF local
            // Files.write(Paths.get("debug.pdf"), bytes);

            return bytes;
        }
    }

    private void savePdfPagesAsImages(byte[] pdfBytes, Turmas turma) throws IOException {
        logger.debug("🖼️ Convertendo PDF em imagens para a turma: {}", turma.getIdTurmas());

        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
            PDFRenderer renderer = new PDFRenderer(document);
            int totalPages = document.getNumberOfPages();
            logger.info("📚 Total de páginas no PDF: {}", totalPages);

            for (int i = 0; i < totalPages; i++) {
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    BufferedImage image = renderer.renderImageWithDPI(i, IMAGE_DPI, ImageType.RGB);
                    ImageIO.write(image, IMAGE_FORMAT, baos);

                    AssinaturaInstrutorD4Sign assinatura = new AssinaturaInstrutorD4Sign();
                    assinatura.setTurma(turma);
                    assinatura.setImagem(baos.toByteArray());
                    assinatura.setOrdem(i + 1);

                    assinaturaInstrutorRepository.save(assinatura);
                    logger.debug("✅ Página {} convertida e salva.", i + 1);
                }
            }

        } catch (IOException e) {
            logger.error("❌ Erro ao processar PDF da turma {}: {}", turma.getIdTurmas(), e.getMessage());
            throw new IOException("Erro ao processar o PDF: " + e.getMessage(), e);
        }
    }

    public List<AssinaturaInstrutorD4Sign> buscarAssinaturasDaTurma(UUID idTurma) {
        logger.debug("🔎 Buscando assinaturas da turma ID: {}", idTurma);
        return assinaturaInstrutorRepository.findByTurma_IdTurmasOrderByOrdemAsc(idTurma);
    }
}
