package br.com.jbst.services;

import java.io.IOException;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.jbst.entities.Instrutor;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class D4SignService {

    private static final Logger logger = LoggerFactory.getLogger(D4SignService.class);

    private static final String API_KEY = "live_ef97bcf71b0eaec4fd8000c5fd6d763bb73458477d11c0367bba7a1c87d53310";
    private static final String CRYPT_KEY = "live_crypt_1KYmQoB3dmMamb5gGBZKlGb5k0suc1RG"; 		
    private static final String BASE_URL = "https://secure.d4sign.com.br";
    private static final String UUID_SAFE_TURMAS = "12748c6a-8b28-4441-8cc3-3f0ee3762df2"
    		+ "";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();


    public String criarPastaTurma(String nomeDaTurma) {
        return criarPastaNoCofre(UUID_SAFE_TURMAS, nomeDaTurma, null);
    }

    public String criarPastaNoCofre(String uuidSafe, String nomePasta, String uuidPastaPai) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("folder_name", nomePasta);
            if (uuidPastaPai != null && !uuidPastaPai.isBlank()) {
                jsonBody.put("uuid_folder", uuidPastaPai);
            }

            RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host(BASE_URL)
                    .addPathSegments("api/v1/folders/" + uuidSafe + "/create")
                    .addQueryParameter("tokenAPI", API_KEY)
                    .addQueryParameter("cryptKey", CRYPT_KEY)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                if (!response.isSuccessful()) {
                    throw new RuntimeException("Erro ao criar pasta: " + response.code() + " - " + responseBody);
                }

                JSONObject json = new JSONObject(responseBody);
                return json.optString("uuid", null);
            }

        } catch (Exception e) {
            logger.error("Erro ao criar pasta na D4Sign", e);
            throw new RuntimeException("Erro ao criar pasta na D4Sign", e);
        }
    }

    public String buscarUuidPastaExistente(String uuidSafe, String nomePasta) {
        try {
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host(BASE_URL)
                    .addPathSegments("api/v1/folders/" + uuidSafe)
                    .addQueryParameter("tokenAPI", API_KEY)
                    .addQueryParameter("cryptKey", CRYPT_KEY)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .header("accept", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String body = response.body().string();

                if (!body.trim().startsWith("{")) {
                    logger.warn("Resposta inválida ao buscar pastas: {}", body);
                    return null;
                }

                JSONObject json = new JSONObject(body);
                JSONArray pastas = json.optJSONArray("data");
                if (pastas == null) return null;

                for (Object obj : pastas) {
                    JSONObject pasta = (JSONObject) obj;
                    if (pasta.getString("folder_name").equalsIgnoreCase(nomePasta)) {
                        return pasta.optString("uuid", null);
                    }
                }

            }
        } catch (Exception e) {
            logger.error("Erro ao buscar UUID da pasta", e);
        }
        return null;
    }


    public String criarDocumentoPorTemplate(String idTemplate, String uuidFolder, String nomeDocumento, Map<String, String> variaveis) {
        try {
            JSONObject root = new JSONObject();
            root.put("name_document", nomeDocumento);
            root.put("uuid_folder", uuidFolder);

            JSONObject templates = new JSONObject();
            JSONObject variaveisJson = new JSONObject();
            variaveis.forEach(variaveisJson::put);
            templates.put(idTemplate, variaveisJson);

            root.put("templates", templates);

            RequestBody body = RequestBody.create(root.toString(), JSON);

            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host(BASE_URL)
                    .addPathSegments("api/v1/documents/" + UUID_SAFE_TURMAS + "/makedocumentbytemplate")
                    .addQueryParameter("tokenAPI", API_KEY)
                    .addQueryParameter("cryptKey", CRYPT_KEY)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body().string();

                if (!response.isSuccessful()) {
                    throw new RuntimeException("Erro ao criar documento: " + response.code() + " - " + responseBody);
                }

                JSONObject json = new JSONObject(responseBody);
                if (json.has("uuid")) {
                    return json.getString("uuid"); // ✅ Aqui está o UUID correto da resposta
                } else {
                    throw new RuntimeException("UUID do documento não encontrado na resposta: " + responseBody);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro de comunicação com D4Sign", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar documento por template", e);
        }
    }



    public void adicionarSignatarioInstrutor(String uuidDocumento, Instrutor instrutor) throws IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(BASE_URL)
                .addPathSegments("api/v1/documents/" + uuidDocumento + "/createlist")
                .addQueryParameter("tokenAPI", API_KEY)
                .addQueryParameter("cryptKey", CRYPT_KEY)
                .build();

        JSONObject signer = new JSONObject();
        signer.put("email", instrutor.getEmail());
        signer.put("act", "1"); // Ação: assinar
        signer.put("foreign", "0"); // Não é estrangeiro
        signer.put("certificadoicpbr", "0"); // Com certificado ICP-Brasil
        signer.put("assinatura_presencial", "0"); // Assinatura remota
        signer.put("embed_methodauth", "email"); // Autenticação por e-mail
        signer.put("key_signer", instrutor.getCpf());
        signer.put("name", instrutor.getInstrutor());
        signer.put("action", "SIGN");
        signer.put("send_email", "1");
        // Dados adicionais do signatário
        signer.put("display_name", instrutor.getInstrutor()); // Nome do instrutor
        signer.put("documentation", instrutor.getCpf()); // CPF
        signer.put("certificadoicpbr_cpf", instrutor.getCpf()); // CPF para assinatura ICP

      

        JSONArray signersArray = new JSONArray();
        signersArray.put(signer);

        JSONObject payload = new JSONObject();
        payload.put("signers", signersArray);

        RequestBody body = RequestBody.create(payload.toString(), JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            if (!response.isSuccessful()) {
                throw new IOException("❌ Erro ao adicionar signatário via /createlist: " + responseBody);
            } else {
                System.out.printf("✅ Signatário %s adicionado com sucesso.%n", instrutor.getEmail());
            }
        }
    }

    public void enviarDocumentoParaAssinatura(String uuidDocumentoD4Sign) throws IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(BASE_URL)
                .addPathSegments("api/v1/documents/" + uuidDocumentoD4Sign + "/sendtosigner")
                .addQueryParameter("tokenAPI", API_KEY)
                .addQueryParameter("cryptKey", CRYPT_KEY)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(new byte[0])) // corpo vazio
                .addHeader("accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao enviar documento para assinatura: " + response.body().string());
            }
        }
    }
    public String obterLinkDeAssinatura(String uuidDocumento, String keySigner) throws IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(BASE_URL)
                .addPathSegments("api/v1/documents/" + uuidDocumento + "/signaturelink/" + keySigner)
                .addQueryParameter("tokenAPI", API_KEY)
                .addQueryParameter("cryptKey", CRYPT_KEY)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao obter link de assinatura: " + responseBody);
            }

            JSONObject json = new JSONObject(responseBody);
            return json.optString("url", null); // O link está na chave "url"
        }
    }

    public byte[] baixarDocumentoAssinado(String uuidDocumento) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // 1. Obter a URL segura de download
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
            "{\"type\":\"pdf\",\"language\":\"pt\",\"encoding\":false,\"document\":false}");

        HttpUrl url = new HttpUrl.Builder()
            .scheme("https")
            .host("secure.d4sign.com.br")
            .addPathSegments("api/v1/documents/" + uuidDocumento + "/download")
            .addQueryParameter("tokenAPI", API_KEY)
            .addQueryParameter("cryptKey", CRYPT_KEY)
            .build();

        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .addHeader("accept", "application/json")
            .addHeader("content-type", "application/json")
            .build();

        String downloadUrl;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao buscar URL de download: " + response.body().string());
            }

            JSONObject json = new JSONObject(response.body().string());
            downloadUrl = json.getString("url");
        }

        // 2. Download direto do PDF (binário)
        Request downloadRequest = new Request.Builder()
            .url(downloadUrl)
            .get()
            .build();

        try (Response downloadResponse = client.newCall(downloadRequest).execute()) {
            if (!downloadResponse.isSuccessful()) {
                throw new IOException("Erro ao baixar PDF assinado: " + downloadResponse.body().string());
            }

            return downloadResponse.body().bytes(); // ← Aqui está o conteúdo binário do PDF
        }
    }

    

}
