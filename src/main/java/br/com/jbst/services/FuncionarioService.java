package br.com.jbst.services;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.jbst.DTO.GetFuncionarioDTO;
import br.com.jbst.DTOs.GetEmpresaDTOs;
import br.com.jbst.DTOs.GetFuncaoDTOs;
import br.com.jbst.DTOs.GetFuncionarioDTOs;
import br.com.jbst.entities.map.Empresa;
import br.com.jbst.entities.map.Funcao;
import br.com.jbst.entities.map.Funcionario;
import br.com.jbst.repositories.FuncaoRepository;
import br.com.jbst.repositories.FuncionarioRepository;

@Service
public class FuncionarioService {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	FuncionarioRepository funcionarioRepository;



	@Autowired
	FuncaoRepository funcaoRepository;
	
	
	
	public ResponseEntity<List<GetFuncionarioDTOs>> consultarFuncionarios() {
	    try {
	        List<Funcionario> funcionarios = funcionarioRepository.findAll();
	        List<GetFuncionarioDTOs> lista = new ArrayList<>();

	        for (Funcionario funcionario : funcionarios) {
	            lista.add(mapFuncionarioToDTO(funcionario));
	        }

	        return ResponseEntity.status(200).body(lista);
	    } catch (Exception e) {
	        // HTTP 500 (INTERNAL SERVER ERROR)
	        return ResponseEntity.status(500).body(null);
	    }
	}


	 public GetFuncionarioDTOs consultarUmFuncionario(UUID idFuncionario) throws Exception {
	        Optional<Funcionario> funcionarioOptional = funcionarioRepository.findById(idFuncionario);
	        
	        if (funcionarioOptional.isPresent()) {
	            Funcionario funcionario = funcionarioOptional.get();
	            
	            GetFuncionarioDTOs dto = new GetFuncionarioDTOs();
	            dto.setIdFuncionario(funcionario.getFuncao().getIdFuncao()); // Assume que a associação funcao está mapeada corretamente
	            dto.setIdFuncionario(funcionario.getIdFuncionario());
	            dto.setDataHoraCriacao(funcionario.getDataHoraCriacao());
	            dto.setNome(funcionario.getNome());
	            dto.setCpf(funcionario.getCpf());
	            dto.setRg(funcionario.getRg());
	            dto.setStatus(funcionario.getStatus());
	            dto.setAssinatura(funcionario.getAssinatura());
	            return dto;
	        } else {
	            // Se o funcionário não for encontrado, você pode lidar com isso como preferir
	            return null; // Ou lançar uma exceção, retornar um DTO de erro, etc.
	        }
	    }	    
	    
	    private GetFuncionarioDTOs mapFuncionarioToDTO(Funcionario funcionario) {
	        GetFuncionarioDTOs dto = new GetFuncionarioDTOs();
	        dto.setIdFuncionario(funcionario.getIdFuncionario());
	        dto.setDataHoraCriacao(funcionario.getDataHoraCriacao());
	        dto.setNome(funcionario.getNome());
	        dto.setCpf(funcionario.getCpf());
	        dto.setRg(funcionario.getRg());
	        dto.setStatus(funcionario.getStatus());
	        dto.setAssinatura(funcionario.getAssinatura());

	        if (funcionario.getFuncao() != null) {
	            dto.setFuncao(mapFuncionarioToGetFuncaoDTOs(funcionario.getFuncao()));
	        }
	        if (funcionario.getEmpresa() != null) {
                dto.setEmpresa(mapEmpresaToDTO(funcionario.getEmpresa()));
            }
	        return dto;
	    }

	    private GetFuncaoDTOs mapFuncionarioToGetFuncaoDTOs(Funcao funcao) {
	        GetFuncaoDTOs funcaoDTO = new GetFuncaoDTOs();
	        funcaoDTO.setIdFuncao(funcao.getIdFuncao());
	        funcaoDTO.setFuncao(funcao.getFuncao());
	        funcaoDTO.setCbo(funcao.getCbo());
	        funcaoDTO.setDescricao(funcao.getDescricao());
	        return funcaoDTO;
	    }
	    private GetEmpresaDTOs mapEmpresaToDTO(Empresa empresa) {
	        GetEmpresaDTOs empresaDTO = new GetEmpresaDTOs();
	        empresaDTO.setRazaosocial(empresa.getRazaosocial());
	        empresaDTO.setNomefantasia(empresa.getNomefantasia());
	        empresaDTO.setCnpj(empresa.getCnpj());
	        // Mapeie outros campos da empresa aqui, se necessário
	        return empresaDTO;
	    }
	    public List<GetFuncionarioDTO> consultarTodosFuncionarios(String nome) throws Exception {
			List<Funcionario> funcionario = funcionarioRepository.findAll();
			List<GetFuncionarioDTO> lista = modelMapper.map(funcionario, new TypeToken<List<GetFuncionarioDTO>>() {
			}.getType());
			return lista;
		} 

}

	

