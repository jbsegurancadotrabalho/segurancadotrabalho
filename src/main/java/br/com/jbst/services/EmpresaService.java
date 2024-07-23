package br.com.jbst.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DTOs.GetEmpresaDTOs;
import br.com.jbst.entities.map.Empresa;
import br.com.jbst.repositories.EmpresaRepository;

@Service
public class EmpresaService {
	
	
	  @Autowired
	  EmpresaRepository empresaRepository;
	  
	  @Autowired
	ModelMapper modelMapper;
	  
	  public List<GetEmpresaDTOs> consultarEmpresas(String empresa) throws Exception {
			List<Empresa> empresas = empresaRepository.findAll();
			List<GetEmpresaDTOs> lista = modelMapper.map(empresas, new TypeToken<List<GetEmpresaDTOs>>() {
			}.getType());
			return lista;
		} 
	    
	    
	    public GetEmpresaDTOs consultarUmaEmpresa(UUID idEmpresa) {
	        Optional<Empresa> registro = empresaRepository.findById(idEmpresa);

	        if (registro.isPresent()) {
	            Empresa empresa = registro.get();
	            return modelMapper.map(empresa, GetEmpresaDTOs.class);
	        } else {
	          
				throw new RuntimeException("Empresa não encontrada"); // Lançar exceção quando não encontrada
	        }
	    }
	    
}
