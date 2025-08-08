
package br.com.jbst.services;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.security.auth.login.AccountNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DTO.GetAssinaturaInstrutorDTO;
import br.com.jbst.DTO.GetTurmasDTO;
import br.com.jbst.DTO.GetTurmasDTOs;
import br.com.jbst.DTO.MailSenderDto;
import br.com.jbst.DTO.PostTurmasDTO;
import br.com.jbst.DTO.PutTurmasDTO;
import br.com.jbst.DTO.PutTurmasInstrutor;
import br.com.jbst.DTO.SendMessageZapDTO;
import br.com.jbst.DTO.TurmasAssinaturaInstrutorDTO;
import br.com.jbst.components.MailSenderComponent;
import br.com.jbst.components.OnlySocialClient;
import br.com.jbst.components.ZApiSenderComponent;
import br.com.jbst.entities.Instrutor;
import br.com.jbst.entities.Turmas;
import br.com.jbst.repositories.AssinaturaInstrutorD4SignRepository;
import br.com.jbst.repositories.CursoRepository;
import br.com.jbst.repositories.InstrutorRepository;
import br.com.jbst.repositories.TurmasRepository;
import br.com.jbst.repositories.UnidadeDeTreinamentoRepository;
import jakarta.transaction.Transactional;



@Service
public class TurmasService {

	@Autowired InstrutorRepository instrutorRepository;
	@Autowired CursoRepository cursoRepository;
	@Autowired TurmasRepository turmasRepository;
	@Autowired ModelMapper modelMapper;
	@Autowired UnidadeDeTreinamentoRepository unidadeRepository;
	@Autowired private D4SignService d4SignService;
	@Autowired private AssinaturaInstrutorD4SignRepository assinaturaInstrutorRepository;
	@Autowired private OnlySocialClient onlySocialClient;
	@Autowired private MailSenderComponent mailSenderComponent;
	@Autowired private ZApiSenderComponent zapSenderComponent;
    private static final String UUID_SAFE_FIXO = "1ddf5f84-7a8d-4b02-8d78-bafcb1e3d6ff";

	@Transactional
	public GetTurmasDTO criarTurmas(PostTurmasDTO dto) throws Exception {
	    Turmas turmas = modelMapper.map(dto, Turmas.class);
	    turmas.setIdTurmas(UUID.randomUUID());

	    // === Associações obrigatórias ===
	    turmas.setCurso(cursoRepository.findById(dto.getIdCurso())
	            .orElseThrow(() -> new Exception("Curso não encontrado")));
	    turmas.setUnidadeDeTreinamento(unidadeRepository.findById(dto.getIdUnidadeDeTreinamento())
	            .orElseThrow(() -> new Exception("Unidade não encontrada")));

	    // === Definições padrão ===
	    int numeroTurma = gerarNumeroTurma();
	    turmas.setNumeroTurma(numeroTurma);
	    turmas.setUuidCofre(UUID_SAFE_FIXO);

	    // === Persistência inicial ===
	    turmasRepository.save(turmas);

	    // === Criação de Pasta ===
	    String nomeCurso = turmas.getCurso().getCurso();
	    String nomePastaTurma = "Turma " + numeroTurma + " - " + nomeCurso;

	    String uuidPastaTurma = d4SignService.buscarUuidPastaExistente(UUID_SAFE_FIXO, nomePastaTurma);
	    if (uuidPastaTurma == null) {
	        uuidPastaTurma = d4SignService.criarPastaNoCofre(UUID_SAFE_FIXO, nomePastaTurma, null);
	    }

	    turmas.setUuidPasta(uuidPastaTurma);

	    // === Criação do Documento por Template ===
	    String uuidTemplate = "NzMyMDA=";
	    String nomeDocumento = "Termo - Turma Nº " + numeroTurma;

	    Map<String, String> camposTemplate = Map.of(
	            "curso", nomeCurso,
	            "data_inicio", turmas.getDatainicio() != null ? turmas.getDatainicio().toString() : "",
	            "data_fim", turmas.getDatafim() != null ? turmas.getDatafim().toString() : "",
	            "instrutor", turmas.getInstrutor() != null ? turmas.getInstrutor() : "",
	            "numero_turma", String.valueOf(numeroTurma)
	    );

	    String uuidDocumento = null;
	    try {
	        uuidDocumento = d4SignService.criarDocumentoPorTemplate(uuidTemplate, uuidPastaTurma, nomeDocumento, camposTemplate);
	        if (uuidDocumento != null && !uuidDocumento.isEmpty()) {
	            turmas.setUuidDocumentoD4Sign(uuidDocumento);
	            turmasRepository.saveAndFlush(turmas); // SALVA após obter o UUID
	        } else {
	            System.err.println("⚠️ Documento criado mas UUID retornado vazio.");
	        }
	    } catch (Exception e) {
	        System.err.println("❌ Erro ao criar documento na D4Sign: " + e.getMessage());
	    }

	    // === Publicação OnlySocial ===
	    try {
	        ZoneId zona = ZoneId.of("America/Sao_Paulo");
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	        String dataInicio = turmas.getDatainicio() != null
	                ? turmas.getDatainicio().atZone(zona).toLocalDate().format(formatter)
	                : "";

	        String dataFim = turmas.getDatafim() != null
	                ? turmas.getDatafim().atZone(zona).toLocalDate().format(formatter)
	                : "";

	        String validadeCurso = turmas.getValidadedocurso() != null
	                ? turmas.getValidadedocurso().atZone(zona).toLocalDate().format(formatter)
	                : "";

	        String texto = String.format(
	                "\uD83D\uDEA8 *Nova Turma Disponível!*\n\n" +
	                        "\uD83D\uDCDA *Curso:* %s\n" +
	                        "\uD83D\uDCC5 *Início:* %s\n" +
	                        "\uD83D\uDCC6 *Término:* %s\n" +
	                        "✅ *Validade:* %s\n" +
	                        "⏰ *Carga Horária:* %s\n" +
	                        "🍿 *Modalidade:* %s\n" +
	                        "🏫 *Local:* %s - %s\n" +
	                        "👨‍🏫 *Instrutor:* %s\n" +
	                        "📌 *Turma:* Nº %d\n" +
	                        "📣 *Observações:* %s\n\n" +
	                        "📸 *Confira também em nosso site www.jbsegurancadotrabalho.com.br*",
	                nomeCurso,
	                dataInicio,
	                dataFim,
	                validadeCurso,
	                turmas.getCargahoraria(),
	                turmas.getModalidade(),
	                turmas.getUnidadeDeTreinamento().getUnidade(),
	                turmas.getUnidadeDeTreinamento().getEndereco().getLocalidade(),
	                turmas.getInstrutor(),
	                numeroTurma,
	                turmas.getObservacoes()
	        );

	        onlySocialClient.enviarPostTurma(texto, 408450);
	    } catch (Exception e) {
	        System.err.println("❌ Erro ao integrar com OnlySocial: " + e.getMessage());
	    }

	    return modelMapper.map(turmas, GetTurmasDTO.class);
	}

	

	

	private int gerarNumeroTurma() {
		Integer ultimoNumero = turmasRepository.findMaxNumeroTurmas();
		if (ultimoNumero == null) {
			ultimoNumero = 0;
		}
		return ultimoNumero + 1;
	}

	public GetTurmasDTO editarTurmas(PutTurmasDTO dto) throws Exception {

		UUID id = dto.getIdTurmas();
		Turmas turmas = turmasRepository.findById(id).orElseThrow();
		modelMapper.map(dto, turmas);
		turmas.setCurso(cursoRepository.findById(dto.getIdcurso()).get());
		turmas.setUnidadeDeTreinamento(unidadeRepository.findById(dto.getIdUnidadedetreinamento()).get());
		turmasRepository.save(turmas);
		return modelMapper.map(turmasRepository.find(turmas.getIdTurmas()), GetTurmasDTO.class);
	}

	public List<GetTurmasDTOs> consultarTurmas(String descricao) throws Exception {
		List<Turmas> turmas = turmasRepository.findAllTurmas();
		List<GetTurmasDTOs> lista = new ArrayList<>();
		for (Turmas turma : turmas) {
			GetTurmasDTOs dto = modelMapper.map(turma, GetTurmasDTOs.class);
			lista.add(dto);
		}

		return lista;
	}

	public GetTurmasDTOs consultarTurmasPorId(UUID id) throws Exception {
	    // Busca a entidade Turmas
	    Turmas turma = turmasRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada: " + id));

	    // Mapeia a turma para o DTO
	    GetTurmasDTOs dto = modelMapper.map(turma, GetTurmasDTOs.class);

	    List<GetAssinaturaInstrutorDTO> assinaturaDtos = assinaturaInstrutorRepository
	            .findByTurma_IdTurmasOrderByOrdemAsc(turma.getIdTurmas())
	            .stream()
	            .map(a -> {
	                GetAssinaturaInstrutorDTO assinaturaDto = new GetAssinaturaInstrutorDTO();
	                assinaturaDto.setId_assinatura_instrutor_D4Sign(a.getId_assinatura_instrutor_D4Sign());
	                assinaturaDto.setOrdem(a.getOrdem());
	                assinaturaDto.setImagem(a.getImagem());
	                return assinaturaDto;
	            })
	            .collect(Collectors.toList()); // ✅ Corrigido para Java 8+
	    dto.setAssinaturasInstrutores(assinaturaDtos);

	    return dto;
	}

	
	public TurmasAssinaturaInstrutorDTO buscarTurmasPorId(UUID id) throws Exception {
		Turmas turmas = turmasRepository.find(id);
				if (turmas == null)
					throw new IllegalArgumentException("Turma não encontrada: " + id);
				return modelMapper.map(turmas, TurmasAssinaturaInstrutorDTO.class);
			}

	public GetTurmasDTOs excluirTurmas(UUID id) throws Exception {

		// verificando se o produto existe (baseado no ID informado)
		Optional<Turmas> registro = turmasRepository.findById(id);
		if (registro.isEmpty())
			throw new IllegalArgumentException("Turma não existe: " + id);

		// capturando o produto do banco de dados
		Turmas turmas = registro.get();

		// excluindo o produto no banco de dados
		turmasRepository.delete(turmas);

		// copiar os dados do produto para o DTO de resposta
		// e retornar os dados (ProdutosGetDTO)
		return modelMapper.map(turmas, GetTurmasDTOs.class);
	}

	@Transactional
	public GetTurmasDTO incluirInstrutor(PutTurmasInstrutor dto) throws Exception {
	    try {
	        UUID id = dto.getIdTurmas();
	        Turmas turma = turmasRepository.findById(id)
	                .orElseThrow(() -> new NoSuchElementException("Turma não encontrada com o ID: " + id));

	        List<Instrutor> instrutores = new ArrayList<>();

	        if (dto.getIdinstrutor() != null && !dto.getIdinstrutor().isEmpty()) {
	            for (UUID idInstrutor : dto.getIdinstrutor()) {
	                Instrutor instrutor = instrutorRepository.findById(idInstrutor)
	                        .orElseThrow(() -> new NoSuchElementException("Instrutor não encontrado com o ID: " + idInstrutor));

	                instrutores.add(instrutor);

	                if (turma.getUuidDocumentoD4Sign() != null &&
	                    instrutor.getEmail() != null &&
	                    instrutor.getCpf() != null) {
	                    try {
	                        // 1. Adicionar signatário à D4Sign
	                        d4SignService.adicionarSignatarioInstrutor(turma.getUuidDocumentoD4Sign(), instrutor);
	                    } catch (Exception e) {
	                        System.err.printf("❌ Erro ao adicionar signatário %s: %s%n", instrutor.getEmail(), e.getMessage());
	                    }
	                }
	            }

	            // 2. Após adicionar todos os signatários, enviar para assinatura
	            if (turma.getUuidDocumentoD4Sign() != null) {
	                try {
	                    d4SignService.enviarDocumentoParaAssinatura(turma.getUuidDocumentoD4Sign());
	                } catch (Exception e) {
	                    System.err.printf("❌ Erro ao enviar documento para assinatura: %s%n", e.getMessage());
	                }
	            }

	            // 3. Enviar e-mail e WhatsApp com o link de assinatura
	            for (Instrutor instrutor : instrutores) {
	                if (turma.getUuidDocumentoD4Sign() != null &&
	                    instrutor.getEmail() != null &&
	                    instrutor.getCpf() != null) {
	                    try {
	                        String linkAssinatura = d4SignService.obterLinkDeAssinatura(
	                                turma.getUuidDocumentoD4Sign(),
	                                instrutor.getCpf()
	                        );

	                        // E-mail
	                        String corpoEmail = String.format("""
	                            <p>Olá <strong>%s</strong>,</p>
	                            <p>Você foi cadastrado como instrutor da turma <strong>Nº %d</strong>.</p>
	                            <p>Por favor, clique no botão abaixo para assinar o termo de responsabilidade:</p>
	                            <p><a href="%s" style="display:inline-block;padding:12px 20px;background:#28a745;color:#fff;text-decoration:none;border-radius:6px;">Assinar Documento</a></p>
	                            <p>Se o botão não funcionar, copie e cole este link no navegador:</p>
	                            <p><small>%s</small></p>
	                            """, instrutor.getInstrutor(), turma.getNumeroTurma(), linkAssinatura, linkAssinatura);

	                        MailSenderDto emailDto = new MailSenderDto();
	                        emailDto.setMailTo(instrutor.getEmail());
	                        emailDto.setSubject("Assinatura de Documento da Turma");
	                        emailDto.setBody(corpoEmail);
	                        mailSenderComponent.sendMessage(emailDto);

	                        // WhatsApp
	                        if (instrutor.getTelefone_1() != null && !instrutor.getTelefone_1().isBlank()) {
	                            String telefoneFormatado = instrutor.getTelefone_1().replaceAll("[^0-9]", "");
	                            if (!telefoneFormatado.startsWith("55")) {
	                                telefoneFormatado = "55" + telefoneFormatado;
	                            }

	                            String numeroZap = "+" + telefoneFormatado;
	                            String msgZap = String.format(
	                                    "Olá %s! Assine o termo da turma Nº %d:\n%s",
	                                    instrutor.getInstrutor(), turma.getNumeroTurma(), linkAssinatura
	                            );

	                            SendMessageZapDTO zapDto = new SendMessageZapDTO();
	                            zapDto.setPhone(numeroZap);
	                            zapDto.setMessage(msgZap);
	                            zapSenderComponent.sendMessage(zapDto);
	                        }

	                    } catch (Exception e) {
	                        System.err.printf("❌ Erro ao enviar link para %s: %s%n", instrutor.getEmail(), e.getMessage());
	                    }
	                }
	            }
	        }

	        turma.setInstrutores(instrutores);
	        turmasRepository.save(turma);
	        return modelMapper.map(turma, GetTurmasDTO.class);

	    } catch (Exception ex) {
	        throw new Exception("Erro ao incluir instrutores na turma.", ex);
	    }
	}




	@Transactional
	public GetTurmasDTO excluirInstrutor(UUID idTurmas, UUID idInstrutor) throws AccountNotFoundException {
		Turmas turmas = turmasRepository.findById(idTurmas).orElseThrow();

		Instrutor instrutor = instrutorRepository.findById(idInstrutor)
				.orElseThrow(() -> new AccountNotFoundException("Instrutor não encontrado"));

		turmas.getInstrutores().remove(instrutor);
		turmasRepository.save(turmas);

		return modelMapper.map(turmas, GetTurmasDTO.class);
	}

	@Transactional
	public void turmaAberta(UUID turmaId) {
		Turmas turma = turmasRepository.findById(turmaId)
				.orElseThrow(() -> new RuntimeException("Turma não encontrada"));
		turma.turmaAberta();
		turmasRepository.save(turma);
	}

	@Transactional
	public void turmaFechada(UUID turmaId) {
		Turmas turma = turmasRepository.findById(turmaId)
				.orElseThrow(() -> new RuntimeException("Turma não encontrada"));
		turma.turmaFechada();
		turmasRepository.save(turma);
	}
	
	



}
