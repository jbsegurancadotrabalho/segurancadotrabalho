package br.com.jbst.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DTOs.GetEnderecoDTO;
import br.com.jbst.DTOs.PostEnderecoDTO;
import br.com.jbst.DTOs.PutEnderecoDTO;
import br.com.jbst.entities.map.Endereco;
import br.com.jbst.repositories.EnderecoRepository;
import br.com.jbst.repositories.UnidadeDeTreinamentoRepository;


@Service
public class EnderecoService {
	  @Autowired
	  EnderecoRepository enderecoRepository;
	  
	  
	  @Autowired
	  UnidadeDeTreinamentoRepository unidadeRepository;
	  
	    @Autowired
		ModelMapper modelMapper;
	    
	    public GetEnderecoDTO criarEndereco(PostEnderecoDTO dto) throws Exception {
	        // Verificar se a unidade de treinamento já está associada a outro endereço
	        if (unidadeJaAssociada(dto.getIdUnidadedetreinamento())) {
	            throw new Exception("Esta unidade de treinamento já está associada a outro endereço, por favor cadastre uma nova unidade de Treinamento.");
	        }

	        // Verificar se o endereço já existe com base nos dados de PostEnderecoDTO
	        if (enderecoJaRegistrado(dto)) {
	            // Endereço com os mesmos dados já registrado, você pode adicionar sua lógica de bloqueio aqui
	            throw new Exception("Este endereço já está cadastrado no sistema. Por favor, verifique os dados fornecidos e tente novamente.");
	        }

	        // O endereço não está registrado, continue com o processo de criação
	        Endereco endereco = modelMapper.map(dto, Endereco.class);
	        endereco.setIdEndereco(UUID.randomUUID());
	        endereco.setDataHoraCriacao(Instant.now());
	        endereco.setUnidadedetreinamento(unidadeRepository.findById(dto.getIdUnidadedetreinamento()).orElse(null));
	        enderecoRepository.save(endereco);
	        return modelMapper.map(endereco, GetEnderecoDTO.class);
	    }

	    private boolean unidadeJaAssociada(UUID idUnidade) {
	        // Verificar se a unidade de treinamento já está associada a outro endereço
	        return enderecoRepository.existsByUnidadedetreinamentoId(idUnidade);
	    }

	    private boolean enderecoJaRegistrado(PostEnderecoDTO dto) {
	        // Consulta no banco de dados para verificar se já existe um endereço com os mesmos dados
	        Optional<Endereco> enderecoExistente = enderecoRepository.findByCepAndLogradouroAndComplementoAndNumeroAndBairroAndLocalidadeAndUf(
	                dto.getCep(),
	                dto.getLogradouro(),
	                dto.getComplemento(),
	                dto.getNumero(),
	                dto.getBairro(),
	                dto.getLocalidade(),
	                dto.getUf()
	        );

	        return enderecoExistente.isPresent();
	    }


	    
	    
	    public GetEnderecoDTO editarEndereco(PutEnderecoDTO dto) {
			UUID id = dto.getIdEndereco();
			Endereco endereco = enderecoRepository.findById(id).orElseThrow();
			modelMapper.map(dto, endereco );
			endereco.setDataHoraCriacao(Instant.now());
			enderecoRepository.save(endereco);
			return modelMapper.map(endereco, GetEnderecoDTO.class);
		}

	    
	    public List<GetEnderecoDTO> consultarEndereco(String curso) throws Exception {
			List<Endereco> endereco = enderecoRepository.findAll();
			List<GetEnderecoDTO> lista = modelMapper.map(endereco, new TypeToken<List<GetEnderecoDTO>>() {
			}.getType());
			return lista;
		} 
	    
	    
	    public GetEnderecoDTO consultarUmEndereco(UUID idEndereco) {
	        Optional<Endereco> endereco = enderecoRepository.findById(idEndereco);

	        if (endereco.isPresent()) {
	            Endereco enderecos = endereco.get();
	            return modelMapper.map(enderecos, GetEnderecoDTO.class);
	        } else {
	          
				throw new RuntimeException("Endereço não encontrado"); // Lançar exceção quando não encontrada
	        }
	    }
	    

	    public GetEnderecoDTO excluirEndereco(UUID id) throws Exception  {
			Optional<Endereco> endereco = enderecoRepository.findById(id);
			if (endereco.isEmpty())
				throw new IllegalArgumentException("Endereço não existe: " + id);
			// capturando o funcionario do banco de dados
			Endereco enderecos = endereco.get();
			// excluindo o funcionario no banco de dados
			enderecoRepository.delete(enderecos);
			GetEnderecoDTO dto = modelMapper.map(enderecos, GetEnderecoDTO.class);
			dto.getIdEndereco();
		    return dto;

		}

	}

	    



