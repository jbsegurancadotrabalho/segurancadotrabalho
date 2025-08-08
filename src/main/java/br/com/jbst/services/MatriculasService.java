package br.com.jbst.services;
import java.rmi.NotBoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.security.auth.login.AccountNotFoundException;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;

import br.com.jbst.DTO.GetMatriculaDTO;
import br.com.jbst.DTO.MailSenderDto;
import br.com.jbst.DTO.SendMessageZapDTO;
import br.com.jbst.MatriculasDTO.AdicionarUsuariosMatriculaDTO;
import br.com.jbst.MatriculasDTO.GetMatriculaFaturamentoPfDTO;
import br.com.jbst.MatriculasDTO.GetMatriculaFaturamentoPjDTO;
import br.com.jbst.MatriculasDTO.GetMatriculaPedidosDTO;
import br.com.jbst.MatriculasDTO.PostMatriculaFaturamentoPfDTO;
import br.com.jbst.MatriculasDTO.PostMatriculaFaturamentoPjDTO;
import br.com.jbst.MatriculasDTO.PostMatriculaPedidosDTO;
import br.com.jbst.MatriculasDTO.PutMatriculaCliente;
import br.com.jbst.MatriculasDTO.PutMatriculaFaturamentoPfDTO;
import br.com.jbst.MatriculasDTO.PutMatriculaFaturamentoPjDTO;
import br.com.jbst.MatriculasDTO.PutMatriculaPedidosDTO;
import br.com.jbst.components.DataUtils;
import br.com.jbst.components.MailSenderComponent;
import br.com.jbst.components.ZApiSenderComponent;
import br.com.jbst.entities.Faturamento;
import br.com.jbst.entities.FaturamentoPf;
import br.com.jbst.entities.Matriculas;
import br.com.jbst.entities.Pedidos;
import br.com.jbst.entities.Turmas;
import br.com.jbst.entities.Usuario;
import br.com.jbst.entities.map.Funcionario;
import br.com.jbst.entities.map.PessoaFisica;
import br.com.jbst.repositories.FaturamentoRepository;
import br.com.jbst.repositories.FaturamentopfRepository;
import br.com.jbst.repositories.FuncionarioRepository;
import br.com.jbst.repositories.MatriculasRepository;
import br.com.jbst.repositories.PedidosRepository;
import br.com.jbst.repositories.PessoaFisicaRepository;
import br.com.jbst.repositories.TurmasRepository;
import br.com.jbst.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;




@Service
public class MatriculasService {

	
	private static final Logger logger = LoggerFactory.getLogger(MatriculasService.class);

	
	@Autowired
	PedidosService pedidosService;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	MatriculasRepository matriculasRepository;

	@Autowired
	PessoaFisicaRepository pessoafisicaRepository;

	@Autowired
	TurmasRepository turmasRepository;

	@Autowired
	FuncionarioRepository funcionarioRepository;

	@Autowired
	PedidosRepository pedidosRepository;

	@Autowired
	FaturamentoRepository faturamentoRepository;

	@Autowired
	FaturamentopfRepository faturamentopfRepository;

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired ZApiSenderComponent zApiSenderComponent;
	
	@Autowired private MailSenderComponent mailSenderComponent;
	
	@Transactional
	public GetMatriculaFaturamentoPjDTO criarMatriculaFaturamentoPj(PostMatriculaFaturamentoPjDTO dto) {
	    UUID idMatricula = UUID.randomUUID();

	    // 1. Buscar e validar a turma
	    Turmas turma = turmasRepository.findById(dto.getIdTurmas())
	        .orElseThrow(() -> new RuntimeException("Turma não encontrada para o ID: " + dto.getIdTurmas()));
	    if (turma.isTurmaFechada()) {
	        throw new RuntimeException("Não é possível criar uma matrícula em uma turma fechada.");
	    }

	    // 2. Buscar e validar o funcionário
	    Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionario())
	        .orElseThrow(() -> new RuntimeException("Funcionário não encontrado para o ID: " + dto.getFuncionario()));

	    // 3. Verificar duplicidade de matrícula
	    if (matriculasRepository.findByFuncionarioAndTurma(funcionario.getIdFuncionario(), turma.getIdTurmas()).isPresent()) {
	        throw new TurmaAlreadyExistsException("Este funcionário já está matriculado nesta turma.");
	    }

	    // 4. Buscar e validar o faturamento
	    Faturamento faturamento = faturamentoRepository.findById(dto.getFaturamento())
	        .orElseThrow(() -> new RuntimeException("Faturamento não encontrado para o ID: " + dto.getFaturamento()));
	    if (faturamento.isFaturaFechada()) {
	        throw new RuntimeException("Não é possível criar uma matrícula com faturamento fechado.");
	    }
	    if (!funcionario.getEmpresa().getIdEmpresa().equals(faturamento.getEmpresa().getIdEmpresa())) {
	        throw new RuntimeException("O funcionário não pertence à mesma empresa do faturamento.");
	    }

	    // 5. Criar a matrícula
	    Matriculas matricula = new Matriculas();
	    matricula.setIdMatricula(idMatricula);
	    matricula.setDataHoraCriacao(Instant.now());
	    matricula.setNumeroMatricula(gerarNumeroMatricula());
	    modelMapper.map(dto, matricula);
	    matricula.setTurmas(turma);
	    matricula.setFuncionario(funcionario);
	    matricula.setFaturamento(faturamento);
	    Usuario usuario = usuarioRepository.findById(dto.getId())
	        .orElseThrow(() -> new RuntimeException("Usuário não encontrado para o ID: " + dto.getId()));
	    if (usuario.getMatriculas() == null) {
	        usuario.setMatriculas(new ArrayList<>());
	    }
	    usuario.getMatriculas().add(matricula);

	    // 7. Salvar matrícula e usuário
	    matricula = matriculasRepository.save(matricula);
	    usuarioRepository.save(usuario);

	    // 8. Enviar mensagem via WhatsApp
	    enviarMensagemWhatsapp(matricula);

	    // 9. Retornar o DTO da matrícula criada
	    return modelMapper.map(matricula, GetMatriculaFaturamentoPjDTO.class);
	}


	public class TurmaAlreadyExistsException extends RuntimeException {
	    public TurmaAlreadyExistsException(String message) {
	        super(message);
	    }
	}


	// 2
	public GetMatriculaFaturamentoPjDTO editarMatriculaPj(PutMatriculaFaturamentoPjDTO dto) throws Exception {
		
		try {

		
		Optional<Matriculas> registro = matriculasRepository.findById(dto.getIdMatricula());

		if (registro.isEmpty()) {
			throw new IllegalArgumentException("Matrícula inválida: " + dto.getIdMatricula());
		}

		Matriculas matriculas = registro.get();

		modelMapper.map(dto, matriculas); // Utiliza o ModelMapper para mapear os dados do DTO para a entidade



		Optional<Turmas> turma = turmasRepository.findById(dto.getIdTurmas());

		if (turma.isPresent()) {
			matriculas.setTurmas(turma.get());
		} else {
			throw new IllegalArgumentException("Turma inválida: " + dto.getIdTurmas());
		}


		 // Busque a turma no repositório
        Turmas turma1 = turmasRepository.findById(dto.getIdTurmas())
                .orElseThrow(() -> new NotFoundException());

		matriculasRepository.save(matriculas);
        enviarMensagemWhatsapp(matriculas);
		return modelMapper.map(matriculas, GetMatriculaFaturamentoPjDTO.class);
	
		} catch (NoSuchElementException e) {
		    logger.error("Elemento não encontrado: {}", e.getMessage(), e);
		    throw new NotFoundException();
		} catch (Exception e) {
		    logger.error("Erro ao editar matrícula: {}", e.getMessage(), e);
		    throw new RuntimeException("Erro ao editar matrícula: " + e.getMessage(), e);
		}
		
		}
	

	@Transactional
	public GetMatriculaFaturamentoPfDTO criarMatriculasFaturamentoPf(PostMatriculaFaturamentoPfDTO matriculaDTO) {
	    try {
	        Matriculas matricula = modelMapper.map(matriculaDTO, Matriculas.class);

	        // Gere um número de matrícula
	        int numeroMatricula = gerarNumeroMatricula();
	        matricula.setNumeroMatricula(numeroMatricula);
	        
	        Turmas turma = turmasRepository.findById(matriculaDTO.getIdTurmas())
	                .orElseThrow(() -> new AccountNotFoundException("Turma não encontrada com ID: " + matriculaDTO.getIdTurmas()));

	        PessoaFisica pessoaFisica = pessoafisicaRepository.findById(matriculaDTO.getIdpessoafisica())
	                .orElseThrow(() -> new AccountNotFoundException("Pessoa física não encontrada com ID: " + matriculaDTO.getIdpessoafisica()));

	        FaturamentoPf faturamentoPf = faturamentopfRepository.findById(matriculaDTO.getIdfaturamentopf())
	                .orElseThrow(() -> new AccountNotFoundException("FaturamentoPF não encontrado com ID: " + matriculaDTO.getIdfaturamentopf()));

	        // Busque o funcionário no repositório
	        PessoaFisica pessoaFisica1 = pessoafisicaRepository.findById(matriculaDTO.getIdpessoafisica())
	                .orElseThrow(() -> new NotFoundException());

	        // Verifique se o funcionário já está matriculado nesta turma
	        boolean pessoaFisicaJaMatriculado = matriculasRepository.existsByPessoafisicaAndTurmas(pessoaFisica1, turma);

	        if (pessoaFisicaJaMatriculado) {
	            throw new TurmaAlreadyExistsException("Esta Pessoa  já está matriculado nesta turma.");
	        }
	        
	        Turmas turmafechada = turmasRepository.findById(matriculaDTO.getIdTurmas())
	                .orElseThrow(() -> new NotFoundException());
	        if (turmafechada.isTurmaFechada()) {
	            throw new RuntimeException("Não é possível criar uma matrícula em uma turma fechada.");
	        }

	        
	        // Busque o faturamento no repositório
	        FaturamentoPf faturamento = faturamentopfRepository.findById(matriculaDTO.getIdfaturamentopf())
	                .orElseThrow(() -> new NotFoundException());

	        // Verifique se o faturamento está fechado
	        if (faturamento.isFaturaFechada()) {
	            throw new Exception("Não é possível criar uma matrícula com faturamento fechado.");
	        }
	        
	        // Verificar duplicidade de CPF na turma
	        boolean cpfDuplicadoNaTurma = matriculasRepository.existsByTurmasAndPessoafisica_Cpf(turma, pessoaFisica.getCpf());

	        if (cpfDuplicadoNaTurma) {
	            throw new Exception("Duplicidade de CPF na turma.");
	        }

	        UUID idPessoaFisicaAssociadaAoFaturamento = faturamentoPf.getPessoaFisica().getIdpessoafisica();

	     // Verificar se o ID da pessoa física na matrícula corresponde ao ID associado ao faturamento
	     if (!pessoaFisica.getIdpessoafisica().equals(idPessoaFisicaAssociadaAoFaturamento)) {
	         throw new Exception("O ID da pessoa física na matrícula não corresponde ao ID associado ao faturamento.");
	     }
	        // Configurar as associações na entidade Matriculas
	        matricula.setIdMatricula(UUID.randomUUID());
	        matricula.setDataHoraCriacao(Instant.now());
	        matricula.setTurmas(turma);
	        matricula.setPessoafisica(pessoaFisica);
	        matricula.setFaturamentopf(faturamentoPf);

	        // Obter o usuário com base no ID fornecido no DTO
	        Usuario usuario = usuarioRepository.findById(matriculaDTO.getId())
	                .orElseThrow(() -> new NotBoundException("Usuário não encontrado com ID: " + matriculaDTO.getId()));

	        // Inicializar a lista de matrículas do usuário se for nula
	        if (usuario.getMatriculas() == null) {
	            usuario.setMatriculas(new ArrayList<>());
	        }

	        // Adicionar a matrícula ao usuário
	        usuario.getMatriculas().add(matricula);

	        // Salvar a matrícula para persistir as associações
	        matriculasRepository.save(matricula);

	        // Salvar o usuário novamente para persistir a associação com Matriculas
	        usuarioRepository.save(usuario);

	        // Mapear a entidade Matriculas para DTO usando ModelMapper
	        return modelMapper.map(matricula, GetMatriculaFaturamentoPfDTO.class);
	    } catch (Exception e) {
	        // Logar a exceção ou lançar uma exceção mais específica, se necessário
	        throw new RuntimeException("Já existe uma matricula nesta Turma para este cpf.", e);
	    }
	}



	// 4
	public GetMatriculaFaturamentoPfDTO editarMatriculaPf(PutMatriculaFaturamentoPfDTO dto) throws Exception {
		Optional<Matriculas> registro = matriculasRepository.findById(dto.getIdMatricula());

		if (registro.isEmpty()) {
			throw new IllegalArgumentException("Matrícula inválida: " + dto.getIdMatricula());
		}

		Matriculas matriculas = registro.get();
		// Atualiza apenas os campos que não são IDs
		modelMapper.map(dto, matriculas);
		matriculas.setDataHoraCriacao(Instant.now());
		
		// Verifique se a matrícula já possui o ID de pessoa física
		if (matriculas.getFuncionario() != null) {
		    throw new RuntimeException("Não é permitido inserir o ID de funcionário em uma matrícula que já possui ID de pessoa física.");
		}



		// Atualiza Turma
		Optional<Turmas> turma = turmasRepository.findById(dto.getIdTurmas());
		if (turma.isPresent()) {
			matriculas.setTurmas(turma.get());
		} else {
			throw new IllegalArgumentException("Turma inválida: " + dto.getIdTurmas());
		}

	

		matriculasRepository.save(matriculas);
		return modelMapper.map(matriculas, GetMatriculaFaturamentoPfDTO.class);
	}

	@Transactional
	public GetMatriculaPedidosDTO criarMatriculasPedidos(UUID idPedidos, PostMatriculaPedidosDTO dto) {
	    try {
	        // Gere um ID único para a matrícula
	        UUID idMatricula = UUID.randomUUID();

	        // Mapeie os dados do DTO para a entidade Matriculas
	        Matriculas matricula = modelMapper.map(dto, Matriculas.class);

	        // Configure os atributos da matrícula
	        matricula.setIdMatricula(idMatricula);
	        matricula.setDataHoraCriacao(Instant.now());
	        int numeroMatricula = gerarNumeroMatricula();
	        matricula.setNumeroMatricula(numeroMatricula);
	        // Busque o funcionário no repositório
	        Funcionario funcionario1 = funcionarioRepository.findById(dto.getFuncionario())
	                .orElseThrow(() -> new NotFoundException());

	        // Busque o faturamento no repositório
	        Pedidos pedidos = pedidosRepository.findById(dto.getIdPedidos())
	                .orElseThrow(() -> new NotFoundException());

	        if (!idPedidos.equals(dto.getIdPedidos())) {
	            throw new RuntimeException("O Pedido está Diferente do Selecionado na Consulta de Pedidos ! Por Favor selecione o pedido correto");
	        }

	        
	        // Verifique se o funcionário pertence à mesma empresa do faturamento
	        if (!funcionario1.getEmpresa().getIdEmpresa().equals(pedidos.getEmpresa().getIdEmpresa())) {
	            throw new RuntimeException("O funcionário não pertence à mesma empresa do pedido.");
	        }
	        Pedidos pedidofechado = pedidosRepository.findById(dto.getIdPedidos())
	                .orElseThrow(() -> new NotFoundException());

	        // Verifique se o faturamento está fechado
	        if (pedidofechado.isPedidoFechado()) {
	            throw new Exception("Não é possível criar uma matrícula com pedido fechado.");
	        }
	        
	        // Busque a turma no repositório
	        Turmas turma = turmasRepository.findById(dto.getIdTurmas()).orElse(null);
	        if (turma == null) {
	            // Trate a situação em que a turma não é encontrada
	            // Pode lançar uma exceção ou retornar uma resposta adequada
	        }

	        // Verifique se o funcionário já está matriculado nesta turma
	        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionario())
	                .orElseThrow(() -> new NotFoundException());
	        boolean funcionarioJaMatriculado = matriculasRepository.existsByFuncionarioAndTurmas(funcionario, turma);
	        if (funcionarioJaMatriculado) {
	            throw new TurmaAlreadyExistsException("Este funcionário já está matriculado nesta turma.");
	        }
	        
	     
	        Turmas turmafechada = turmasRepository.findById(dto.getIdTurmas())
	                .orElseThrow(() -> new NotFoundException());
	        if (turmafechada.isTurmaFechada()) {
	            throw new RuntimeException("Não é possível criar uma matrícula em uma turma fechada.");
	        }

	        // Configure a turma e o funcionário na matrícula
	        matricula.setTurmas(turma);
	        matricula.setFuncionario(funcionario);

	        // Busque o pedido no repositório
	        Optional<Pedidos> pedidoOptional = pedidosRepository.findById(idPedidos);
	        if (pedidoOptional.isPresent()) {
	            // Prossiga com a lógica para associar usuários à matrícula
	            Pedidos pedido = pedidoOptional.get();
	            Integer creditos = pedido.getCreditos() != null ? pedido.getCreditos() : 0;
	            Integer matriculasRealizadas = pedido.getMatriculasrealizadas() != null ? pedido.getMatriculasrealizadas() : 0;
	            creditos--;
	            matriculasRealizadas++;
	            pedido.setCreditos(creditos);
	            pedido.setMatriculasrealizadas(matriculasRealizadas);
	            matricula.setPedidos(pedido);

	            // Busque o usuário no repositório
	            Usuario usuario = usuarioRepository.findById(dto.getId()).orElseThrow(() -> new NotFoundException());

	            // Inicialize a lista de usuários se for nula
	            if (matricula.getUsuarios() == null) {
	                matricula.setUsuarios(new ArrayList<>());
	            }

	            // Adicione a matrícula ao usuário
	            matricula.getUsuarios().add(usuario);

	            // Salve novamente a matrícula para persistir a associação com usuários
	            matricula = matriculasRepository.save(matricula);
		        enviarMensagemWhatsapp(matricula);

	        } else {
	            throw new RuntimeException("Pedido não encontrado");
	        }

	        // Converta a entidade Matriculas para DTO e retorne
	        return modelMapper.map(matricula, GetMatriculaPedidosDTO.class);
	    } catch (Exception e) {
	        // Log ou manipule a exceção conforme necessário
	        throw new RuntimeException("Este Funcionário já está matriculado nesta Turma.", e);
	    }
	}
	// 6
	@Transactional
	public GetMatriculaPedidosDTO editarMatriculasPedidos(UUID idMatricula, PutMatriculaPedidosDTO dto) {
	    try {
	        // Busque a matrícula no repositório
	        Matriculas matricula = matriculasRepository.findById(idMatricula)
	                .orElseThrow(() -> new NotFoundException());

	

	        // Mapeie os dados do DTO para a entidade Matriculas
	        modelMapper.map(dto, matricula);
	        if (!matricula.getTurmas().getIdTurmas().equals(dto.getIdTurmas())) {
	            // Busque a nova turma no repositório
	            Turmas novaTurma = turmasRepository.findById(dto.getIdTurmas())
	                    .orElseThrow(() -> new NotFoundException());

	            // Atualize a turma na matrícula
	            matricula.setTurmas(novaTurma);
	        }
	        // Salve a matrícula para persistir as alterações
	        matricula = matriculasRepository.save(matricula);
	        enviarMensagemWhatsapp(matricula);

	        // Converta a entidade Matriculas para DTO e retorne
	        return modelMapper.map(matricula, GetMatriculaPedidosDTO.class);
	    } catch (Exception e) {
	        // Log ou manipule a exceção conforme necessário
	        throw new RuntimeException("Erro ao editar matrícula.", e);
	    }
	}


	public List<GetMatriculaDTO> consultarMatriculas(String numeroMatricula) throws Exception {
		List<Matriculas> matriculas = matriculasRepository.findAllMatriculas();
		List<GetMatriculaDTO> lista = modelMapper.map(matriculas, new TypeToken<List<GetMatriculaDTO>>() {
		}.getType());
		return lista;
	}

	public GetMatriculaDTO consultarUmaMatricula(UUID idMatriculas) {
		Optional<Matriculas> registro = matriculasRepository.findById(idMatriculas);

		if (registro.isPresent()) {
			Matriculas matriculas = registro.get();
			return modelMapper.map(matriculas, GetMatriculaDTO.class);
		} else {

			throw new RuntimeException("Matricula não encontrada"); // Lançar exceção quando não encontrada
		}
	}
	
	public byte[] comprimirImagem(byte[] imagemBytes) throws IOException {
	    BufferedImage imagem = ImageIO.read(new ByteArrayInputStream(imagemBytes));
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageIO.write(imagem, "jpg", baos);
	    baos.flush();
	    byte[] imagemComprimida = baos.toByteArray();
	    baos.close();
	    return imagemComprimida;
	}

	private int gerarNumeroMatricula() {
		Integer ultimoNumero = matriculasRepository.findMaxNumeroMatricula();
		if (ultimoNumero == null) {
			ultimoNumero = 0;
		}
		return ultimoNumero + 1;
	}

	// 7 - Adicionar Usuários

	
	public GetMatriculaDTO adicionarUsuariosMatricula(AdicionarUsuariosMatriculaDTO dto) throws Exception {
	    try {
	        UUID matriculaId = dto.getIdMatricula();
	        Matriculas matricula = matriculasRepository.findById(matriculaId)
	                .orElseThrow(() -> new NoSuchElementException("Matricula não encontrada com o ID: " + matriculaId));
	        modelMapper.map(dto, matricula);
	        List<Usuario> usuarios = obterUsuariosPorIds(dto.getIdsUsuarios());

	        // Salva a matrícula separadamente
	        matricula = matriculasRepository.save(matricula);

	        // Adiciona os usuários à matrícula
	        matricula.getUsuarios().addAll(usuarios);
	        matriculasRepository.save(matricula);

	        return modelMapper.map(matricula, GetMatriculaDTO.class);
	    } catch (Exception ex) {
	        // Trate a exceção de forma apropriada, registre logs, etc.
	        throw new Exception("Erro ao incluir Matriculas.", ex);
	    }
	}

	private List<Usuario> obterUsuariosPorIds(List<UUID> usuarioIds) {
	    List<Usuario> usuarios = new ArrayList<>();

	    if (usuarioIds != null && !usuarioIds.isEmpty()) {
	        for (UUID id : usuarioIds) {
	            Usuario usuario = usuarioRepository.findById(id)
	                    .orElseThrow(() -> new NoSuchElementException("Usuario não encontrado com o ID: " + id));

	            usuarios.add(usuario);
	        }
	    }

	    return usuarios;
	}
	
	public GetMatriculaDTO excluirUsuariosMatricula(UUID matriculaId, List<UUID> usuarioIds) throws Exception {
	    try {
	        Matriculas matricula = matriculasRepository.findById(matriculaId)
	                .orElseThrow(() -> new NoSuchElementException("Matricula não encontrada com o ID: " + matriculaId));

	        if (usuarioIds != null && !usuarioIds.isEmpty()) {
	            // Remove os usuários da matrícula
	            matricula.getUsuarios().removeIf(usuario -> usuarioIds.contains(usuario.getId()));
	            matriculasRepository.save(matricula);
	        }

	        return modelMapper.map(matricula, GetMatriculaDTO.class);
	    } catch (Exception ex) {
	        // Trate a exceção de forma apropriada, registre logs, etc.
	        throw new Exception("Erro ao excluir usuários da Matricula.", ex);
	    }
	}
	
	
	// Edição Cliente
		public PutMatriculaCliente editarMatriculaCliente(PutMatriculaCliente dto) throws Exception {	
			try {
			Optional<Matriculas> registro = matriculasRepository.findById(dto.getIdMatricula());
			if (registro.isEmpty()) {
				throw new IllegalArgumentException("Matrícula inválida: " + dto.getIdMatricula());
			}
			Matriculas matriculas = registro.get();
			modelMapper.map(dto, matriculas); // Utiliza o ModelMapper para mapear os dados do DTO para a entidade
			matriculasRepository.save(matriculas);
	        enviarMensagemWhatsapp(matriculas);

			return modelMapper.map(matriculas,  PutMatriculaCliente.class);
		
			} catch (NoSuchElementException e) {
			    logger.error("Elemento não encontrado: {}", e.getMessage(), e);
			    throw new NotFoundException();
			} catch (Exception e) {
			    logger.error("Erro ao editar matrícula: {}", e.getMessage(), e);
			    throw new RuntimeException("Erro ao editar matrícula: " + e.getMessage(), e);
			   }
			
			}
		
		


		private void enviarMensagemWhatsapp(Matriculas matricula) {
		    Funcionario funcionario = matricula.getFuncionario();
		    Turmas turma = matricula.getTurmas();

		    if (funcionario != null && funcionario.getWhatsapp_funcionario() != null && turma != null) {
		        String numero = funcionario.getWhatsapp_funcionario().replaceAll("[^0-9]", "");
		        if (!numero.startsWith("55")) {
		            numero = "55" + numero;
		        }

		        String enderecoRJ = "Rua Moncorvo Filho, 99 - Loja - Centro - Rio de Janeiro - RJ - Brasil";
		        String horarioRJ = "08:00 às 17:00";
		        String enderecoSP = "Rua Siqueira Bueno, 1321, Belém - São Paulo - SP - Brasil";
		        String horarioSP = "09:00 às 17:00";

		        String linkCurso = "http://jbseguranca.s3-website.us-east-2.amazonaws.com/fazer-curso-ead/" + matricula.getIdMatricula();
		        String linkAssinatura = "http://jbseguranca.s3-website.us-east-2.amazonaws.com/assinatura-funcionario/" + funcionario.getIdFuncionario();
		        UUID idCurso = turma.getCurso() != null ? turma.getCurso().getIdcurso() : null;
		        String linkAvaliacao = idCurso != null
		                ? "http://jbseguranca.s3-website.us-east-2.amazonaws.com/responder-avaliacao/" + idCurso
		                : "Link de avaliação indisponível";

		        String dataInicioFormatada = turma.getDatainicio() != null ? DataUtils.formatarDataIsoParaPortugues(turma.getDatainicio().toString()) : "Não informado";
		        String dataFimFormatada = turma.getDatafim() != null ? DataUtils.formatarDataIsoParaPortugues(turma.getDatafim().toString()) : "Não informado";

		        // Eliminar dias duplicados
		        Set<String> diasUnicos = new LinkedHashSet<>();
		        if (turma.getPrimeirodia() != null && !turma.getPrimeirodia().isBlank()) diasUnicos.add(turma.getPrimeirodia());
		        if (turma.getSegundodia() != null && !turma.getSegundodia().isBlank()) diasUnicos.add(turma.getSegundodia());
		        if (turma.getTerceirodia() != null && !turma.getTerceirodia().isBlank()) diasUnicos.add(turma.getTerceirodia());
		        if (turma.getQuartodia() != null && !turma.getQuartodia().isBlank()) diasUnicos.add(turma.getQuartodia());
		        if (turma.getQuintodia() != null && !turma.getQuintodia().isBlank()) diasUnicos.add(turma.getQuintodia());

		        String diasFormatados = String.join(" ", diasUnicos);

		        String mensagem = String.format("""
		            Olá %s! Sua matrícula nº %d - Turma nº %d foi registrada com sucesso.

		            📚 Curso: %s
		            📝 Tipo: %s | Nível: %s
		            🕒 Carga Horária: %s
		            📅 Período: %s a %s
		            🗓️ Dias: %s
		            📝 Observações: %s

		            Endereços das Unidades:
		            🏢 Rio de Janeiro: %s (Horário: %s)
		            🏢 São Paulo: %s (Horário: %s)

		            📝 Realize sua assinatura no link:
		            %s

		            📚 Acesse o curso no link:
		            %s

		            📝 Responda sua avaliação no link:
		            %s

		            ⚠️ Compareça de calça, camisa e sapato ou bota. Traga caneta e papel para anotações.
		            """,
		            funcionario.getNome(),
		            matricula.getNumeroMatricula(),
		            turma.getNumeroTurma() != null ? turma.getNumeroTurma() : 0,
		            turma.getCurso() != null ? turma.getCurso().getCurso() : "Informação indisponível",
		            turma.getTipo() != null ? turma.getTipo() : "Informação indisponível",
		            turma.getNivel() != null ? turma.getNivel() : "Informação indisponível",
		            turma.getCargahoraria() != null ? turma.getCargahoraria() : "Informação indisponível",
		            dataInicioFormatada,
		            dataFimFormatada,
		            diasFormatados.isBlank() ? "Dias não informados" : diasFormatados,
		            turma.getObservacoes() != null ? turma.getObservacoes() : "Nenhuma observação",
		            enderecoRJ, horarioRJ,
		            enderecoSP, horarioSP,
		            linkAssinatura,
		            linkCurso,
		            linkAvaliacao
		        ).trim();

		        zApiSenderComponent.sendMessage(new SendMessageZapDTO(numero, mensagem));

		        if (funcionario.getEmail_funcionario() != null && !funcionario.getEmail_funcionario().isBlank()) {
		            MailSenderDto emailDto = new MailSenderDto();
		            emailDto.setMailTo(funcionario.getEmail_funcionario());
		            emailDto.setSubject("Confirmação de Matrícula - JB Segurança do Trabalho");
		            emailDto.setBody(mensagem.replace("\n", "<br>")); // HTML para e-mail
		            mailSenderComponent.sendMessage(emailDto);
		        }
		    }
		}

		
}
