package br.com.jbst.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.jbst.MatriculasDTO.GetPessoaFisicaDTO;
import br.com.jbst.entities.map.PessoaFisica;
import br.com.jbst.repositories.PessoaFisicaRepository;

@Service
public class PessoaFisicaService {

	
    @Autowired
	PessoaFisicaRepository pessoaFisicaRepository;
  
    @Autowired
	ModelMapper modelMapper;
    
    public List<GetPessoaFisicaDTO> consultarPessoaFisica() throws Exception {
		List<PessoaFisica> pessoafisica = pessoaFisicaRepository.findAll();
		List<GetPessoaFisicaDTO> lista = modelMapper.map(pessoafisica, new TypeToken<List<GetPessoaFisicaDTO>>() {
		}.getType());
		return lista;
	} 
    
    
    public GetPessoaFisicaDTO consultarUmaPessoaFisica(UUID idPessoaFisica) {
        Optional<PessoaFisica> pessoafisica = pessoaFisicaRepository.findById(idPessoaFisica);

        if (pessoafisica.isPresent()) {
        	PessoaFisica registro = pessoafisica.get();
            return modelMapper.map(registro, GetPessoaFisicaDTO.class);
        } else {
          
			throw new RuntimeException("Pessoa Fisica não encontrada"); // Lançar exceção quando não encontrada
        }
    }
    
 
}
