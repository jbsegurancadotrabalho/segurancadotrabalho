package br.com.jbst.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DTO.GetCursoDTO;
import br.com.jbst.DTO.PostCursoDTO;
import br.com.jbst.DTO.PutCursoDTO;
import br.com.jbst.entities.Curso;
import br.com.jbst.repositories.CursoRepository;

@Service
public class CursoService {

	@Autowired
	CursoRepository cursoRepository;

	@Autowired
	ModelMapper modelMapper;

	public GetCursoDTO criarCurso(PostCursoDTO dto) throws Exception {
	    // Verifica se o curso já foi registrado
	    if (cursoJaRegistrado(dto)) {
	        // Curso já registrado, pode adicionar sua lógica de barramento aqui
	        throw new Exception("Este curso já foi registrado, por favor tente outro.");
	    }

	    // O curso ainda não foi registrado, pode continuar com o processo de criação
	    Curso curso = modelMapper.map(dto, Curso.class);
	    curso.setIdcurso(UUID.randomUUID());
	    curso.setDataHoraCriacao(Instant.now());
	    int codigo = gerarNumeroCurso();
	    curso.setCodigo(codigo);
	    cursoRepository.save(curso);
	    return modelMapper.map(curso, GetCursoDTO.class);
	}

	private boolean cursoJaRegistrado(PostCursoDTO dto) {
	    // Consultar o banco de dados para verificar se já existe um curso com os mesmos valores dos campos fornecidos
	    List<Curso> cursos = cursoRepository.findByAllFields(
	            dto.getCurso(),
	            dto.getDescricao(), 
	            dto.getConteudo(),
	            dto.getModelo_certificado(),
	            dto.getCampo_especifico(), 
	            dto.getTituloautorizacao(), 
	            dto.getItemdaautorizacao(),
	            dto.getConteudodaautorizacao(),
	            dto.getValorFormacao(),
	            dto.getValorReciclagem(),
	            dto.getComposicaoOrcamentaria(),
	            dto.getValorEad(),
	            dto.getObservacoesGerais());
	    return !cursos.isEmpty();
	}


	public GetCursoDTO editarCurso(PutCursoDTO dto) {
		UUID id = dto.getIdcurso();
		Curso curso = cursoRepository.findById(id).orElseThrow();
		modelMapper.map(dto, curso);
		cursoRepository.save(curso);
		return modelMapper.map(curso, GetCursoDTO.class);
	}

	public List<GetCursoDTO> consultarCursos(String curso) throws Exception {
		List<Curso> cursos = cursoRepository.findAll();
		List<GetCursoDTO> lista = modelMapper.map(cursos, new TypeToken<List<GetCursoDTO>>() {
		}.getType());
		return lista;
	}

	public GetCursoDTO consultarUmCurso(UUID idCurso) {
		Optional<Curso> curso = cursoRepository.findById(idCurso);

		if (curso.isPresent()) {
			Curso cursos = curso.get();
			return modelMapper.map(cursos, GetCursoDTO.class);
		} else {

			throw new RuntimeException("Curso não encontrado"); // Lançar exceção quando não encontrada
		}
	}

	public GetCursoDTO excluirCurso(UUID id) throws Exception {
		Optional<Curso> curso = cursoRepository.findById(id);
		if (curso.isEmpty())
			throw new IllegalArgumentException("Instrutor não existe: " + id);
		// capturando o funcionario do banco de dados
		Curso cursos = curso.get();
		// excluindo o funcionario no banco de dados
		cursoRepository.delete(cursos);
		GetCursoDTO dto = modelMapper.map(cursos, GetCursoDTO.class);
		dto.getCurso();
		return dto;

	}

	public GetCursoDTO incluirAvaliacao(UUID id, byte[] avaliacao) throws Exception {
		Optional<Curso> registro = cursoRepository.findById(id);
		if (registro.isEmpty())
			throw new IllegalArgumentException(" inválido: " + id);
		Curso curso = registro.get();
		curso.setAvaliacao(avaliacao);
		cursoRepository.save(curso);
		return modelMapper.map(curso, GetCursoDTO.class);
	}

	public GetCursoDTO incluirGabarito(UUID id, byte[] gabarito) throws Exception {
		Optional<Curso> registro = cursoRepository.findById(id);
		if (registro.isEmpty())
			throw new IllegalArgumentException(" inválido: " + id);
		Curso curso = registro.get();
		curso.setGabarito(gabarito);
		cursoRepository.save(curso);
		return modelMapper.map(curso, GetCursoDTO.class);
	}

	public GetCursoDTO incluirMaterial(UUID id, byte[] material) throws Exception {
		Optional<Curso> registro = cursoRepository.findById(id);
		if (registro.isEmpty())
			throw new IllegalArgumentException(" inválido: " + id);
		Curso curso = registro.get();
		curso.setMaterial(material);
		cursoRepository.save(curso);
		return modelMapper.map(curso, GetCursoDTO.class);
	}

	private Integer gerarNumeroCurso() {
		Integer ultimoNumero = cursoRepository.findMaxNumeroCurso();
		if (ultimoNumero == null) {
			ultimoNumero = 0;
		}
		return ultimoNumero + 1;
	}

	public byte[] getCursoMaterial(UUID material) {
		Optional<Curso> cursoOptional = cursoRepository.findById(material);

		if (cursoOptional.isPresent()) {
			Curso curso = cursoOptional.get();

			if (curso.getMaterial() != null) {
				return curso.getMaterial();
			} else {
				throw new RuntimeException("Os dados binários do Curso estão vazios.");
			}
		} else {
			throw new RuntimeException("Material não encontrado para o ID: " + material);
		}
	}

	public byte[] getCursoAvaliacao(UUID avaliacao) {
		Optional<Curso> cursoOptional = cursoRepository.findById(avaliacao);

		if (cursoOptional.isPresent()) {
			Curso curso = cursoOptional.get();

			if (curso.getAvaliacao() != null) {
				return curso.getAvaliacao();
			} else {
				throw new RuntimeException("Os dados binários do Curso estão vazios.");
			}
		} else {
			throw new RuntimeException("Avaliação não encontrada para o ID: " + avaliacao);
		}
	}

	public byte[] getCursoGabarito(UUID gabarito) {
		Optional<Curso> cursoOptional = cursoRepository.findById(gabarito);

		if (cursoOptional.isPresent()) {
			Curso curso = cursoOptional.get();

			if (curso.getGabarito() != null) {
				return curso.getGabarito();
			} else {
				throw new RuntimeException("Os dados binários do Curso estão vazios.");
			}
		} else {
			throw new RuntimeException("Gabarito não encontrado para o ID: " + gabarito);
		}
	}

}
