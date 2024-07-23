package br.com.jbst.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import javax.security.auth.login.AccountNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import br.com.jbst.DTO.GetTurmasDTO;
import br.com.jbst.DTO.GetTurmasDTOs;
import br.com.jbst.DTO.PostTurmasDTO;
import br.com.jbst.DTO.PutTurmasDTO;
import br.com.jbst.DTO.PutTurmasInstrutor;
import br.com.jbst.entities.Instrutor;
import br.com.jbst.entities.Matriculas;
import br.com.jbst.entities.Turmas;
import br.com.jbst.repositories.CursoRepository;
import br.com.jbst.repositories.InstrutorRepository;
import br.com.jbst.repositories.TurmasRepository;
import br.com.jbst.repositories.UnidadeDeTreinamentoRepository;
import jakarta.transaction.Transactional;



@Service
public class TurmasService {
	
    @Autowired
    InstrutorRepository instrutorRepository;

    @Autowired
	CursoRepository cursoRepository;
    
    
    @Autowired
    TurmasRepository turmasRepository;
  
    @Autowired
	ModelMapper modelMapper;
    
    @Autowired
	UnidadeDeTreinamentoRepository unidadeRepository;
    
    
    
    public GetTurmasDTO criarTurmas(PostTurmasDTO dto) throws Exception {
			
		//criando uma Turma
		Turmas turmas = modelMapper.map(dto, Turmas.class);
		turmas.setIdTurmas(UUID.randomUUID());
		turmas.setCurso(cursoRepository.findById(dto.getIdCurso()).get());
		turmas.setUnidadeDeTreinamento(unidadeRepository.findById(dto.getIdUnidadeDeTreinamento()).get());
		int numeroTurma = gerarNumeroTurma();
        turmas.setNumeroTurma(numeroTurma);
		turmasRepository.save(turmas);
		return modelMapper.map(turmasRepository.find
				(turmas.getIdTurmas()), GetTurmasDTO.class);
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
		modelMapper.map(dto, turmas );
		turmas.setCurso(cursoRepository.findById(dto.getIdcurso()).get());
		turmas.setUnidadeDeTreinamento(unidadeRepository.findById(dto.getIdUnidadedetreinamento()).get());
		turmasRepository.save(turmas);
		return modelMapper.map(turmasRepository.find
				(turmas.getIdTurmas()), GetTurmasDTO.class);
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
	
	//consultando o produto através do ID
	Turmas turmas = turmasRepository.find(id);
	if(turmas == null)
		throw new IllegalArgumentException("Turma não encontrada: " + id);
	
	//retornando os dados
	return modelMapper.map(turmas, GetTurmasDTOs.class);		
}

public GetTurmasDTOs excluirTurmas(UUID id) throws Exception {
	
	//verificando se o produto existe (baseado no ID informado)
	Optional<Turmas> registro = turmasRepository.findById(id);
	if(registro.isEmpty())
		throw new IllegalArgumentException("Turma não existe: " + id);		
	
	//capturando o produto do banco de dados
	Turmas turmas = registro.get();
			
	//excluindo o produto no banco de dados
	turmasRepository.delete(turmas);
	
	//copiar os dados do produto para o DTO de resposta
	//e retornar os dados (ProdutosGetDTO)
	return modelMapper.map(turmas, GetTurmasDTOs.class);
}


public GetTurmasDTO incluirInstrutor(PutTurmasInstrutor dto) throws Exception {
    try {
        UUID id = dto.getIdTurmas();
        Turmas turmas = turmasRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Turmas não encontrada com o ID: " + id));

        modelMapper.map(dto, turmas);

        List<Instrutor> instrutores = new ArrayList<>();

        if (dto.getIdinstrutor() != null && !dto.getIdinstrutor().isEmpty()) {
            for (UUID idInstrutor : dto.getIdinstrutor()) {
                Instrutor instrutor = instrutorRepository.findById(idInstrutor)
                        .orElseThrow(() -> new NoSuchElementException("Instrutor não encontrado com o ID: " + idInstrutor));

                instrutores.add(instrutor);
            }
        }

        turmas.setInstrutores(instrutores);
        turmasRepository.save(turmas);

        return modelMapper.map(turmas, GetTurmasDTO.class);
    } catch (Exception ex) {
        // Trate a exceção de forma apropriada, registre logs, etc.
        throw new Exception("Erro ao incluir instrutores na turma.", ex);
    }
}


@Transactional
public GetTurmasDTO excluirInstrutor(UUID idTurmas, UUID idInstrutor) throws AccountNotFoundException {
    Turmas turmas = turmasRepository.findById(idTurmas)
            .orElseThrow();

    Instrutor instrutor = instrutorRepository.findById(idInstrutor)
            .orElseThrow(() -> new AccountNotFoundException("Instrutor não encontrado"));

    turmas.getInstrutores().remove(instrutor);
    turmasRepository.save(turmas);

    return modelMapper.map(turmas, GetTurmasDTO.class);
}



@Transactional
public void turmaAberta(UUID turmaId) {
    Turmas turma = turmasRepository.findById(turmaId).orElseThrow(() -> new RuntimeException("Turma não encontrada"));
    turma.turmaAberta();
    turmasRepository.save(turma);
}

@Transactional
public void turmaFechada(UUID turmaId) {
    Turmas turma = turmasRepository.findById(turmaId).orElseThrow(() -> new RuntimeException("Turma não encontrada"));
    turma.turmaFechada();
    turmasRepository.save(turma);
}


}


