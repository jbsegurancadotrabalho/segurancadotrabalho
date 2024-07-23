package br.com.jbst.services;

import java.math.BigDecimal;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jbst.DTO.GetFuncionarioDTO;
import br.com.jbst.DTO.GetTurmasDTO;
import br.com.jbst.DTO.RelatorioFaturamentoPfDto;
import br.com.jbst.DTO.RelatorioMatriculaDTO;
import br.com.jbst.DTOs.GetFaturamentoDTO;
import br.com.jbst.DTOs.GetFaturamentopfDto;
import br.com.jbst.DTOs.PostFaturamentoDTO;
import br.com.jbst.DTOs.PostFaturamentopfDto;
import br.com.jbst.DTOs.PutFaturamentopfDto;
import br.com.jbst.entities.Faturamento;
import br.com.jbst.entities.FaturamentoPf;
import br.com.jbst.entities.Matriculas;
import br.com.jbst.entities.Turmas;
import br.com.jbst.entities.map.Empresa;
import br.com.jbst.entities.map.Funcionario;
import br.com.jbst.entities.map.PessoaFisica;
import br.com.jbst.repositories.FaturamentopfRepository;
import br.com.jbst.repositories.PessoaFisicaRepository;
import jakarta.transaction.Transactional;
import br.com.jbst.MatriculasDTO.GetPessoaFisicaDTO;

@Service
public class FaturamentopfServices {
	
	
	@Autowired
	FaturamentopfRepository faturamentopfRepository;
	

	@Autowired
	PessoaFisicaRepository pessoafisicaRepository;
  
    @Autowired
	ModelMapper modelMapper;
    
    @Transactional
    public GetFaturamentopfDto criarFaturamentoPf(PostFaturamentopfDto dto) throws Exception {
        try {
      
        	
       	 ZoneId zone = ZoneId.systemDefault();

         // Converter para ZonedDateTime
         ZonedDateTime dataInicio = ZonedDateTime.ofInstant(dto.getData_inicio(), zone);

         // Obter a data atual sem informações de fuso horário
         LocalDate dataAtual = LocalDate.now(zone);

         // Verificar se a data de início está entre o primeiro e o último dia do mês atual
         LocalDate primeiroDiaDoMes = dataAtual.withDayOfMonth(1);
         LocalDate ultimoDiaDoMes = dataAtual.withDayOfMonth(dataAtual.lengthOfMonth());

         // Ajuste para considerar apenas a data, ignorando o horário
         LocalDate dataInicioAjustada = dataInicio.toLocalDate();

         if (dataInicioAjustada.isBefore(primeiroDiaDoMes) || dataInicioAjustada.isAfter(ultimoDiaDoMes)) {
             throw new Exception("A data de início deve estar entre o primeiro e o último dia do mês atual. Data de início: " + dataInicio);
         }
         
         
         
        // Verificar se já foi feito um faturamento no período daquele mês para a empresa
        if (existsFaturamentoNoPeriodo(dto.getIdpessoafisica(), dto.getData_inicio(), dto.getData_fim())) {
            throw new Exception("Já foi cadastrado um faturamento para a empresa no período especificado.");
        }
        	
        	
            PessoaFisica pessoaFisica = pessoafisicaRepository.findById(dto.getIdpessoafisica())
                    .orElseThrow(() -> new RuntimeException("Pessoa Fisica não encontrada com o ID: " + dto.getIdpessoafisica()));

            FaturamentoPf faturamentoPf = new FaturamentoPf();
            faturamentoPf.setIdfaturamentopf(UUID.randomUUID());
            faturamentoPf.setDataHoraCriacao(Instant.now());
            faturamentoPf.setData_inicio(dto.getData_inicio());
            faturamentoPf.setData_fim(dto.getData_fim());
            faturamentoPf.setNumeroFaturamento(gerarNumeroFaturamentoPf());
            faturamentoPf.setFaturaFechada(false);
            faturamentoPf.setPessoaFisica(pessoaFisica);
            faturamentoPf.setVenda(dto.getVenda());
            faturamentoPf.setNotafiscal(dto.getNotafiscal()); // Adicionado
            faturamentoPf.setComprador(dto.getComprador()); // Adicionado
            faturamentoPf.setTelefone(dto.getTelefone()); // Adicionado
            faturamentoPf.setEmail(dto.getEmail()); // Adicionado
            faturamentoPf.setResponsavelfinanceiro(dto.getResponsavelfinanceiro()); // Adicionado
            faturamentoPf.setTelefonefinanceiro(dto.getTelefonefinanceiro()); // Adicionado
            faturamentoPf.setWhatsapp(dto.getWhatsapp()); // Adicionado
            faturamentoPf.setEmailfinanceiro(dto.getEmailfinanceiro()); // Adicionado
            faturamentoPf.setData_de_pagamento(dto.getData_de_pagamento()); // Adicionado
            faturamentoPf.setParcelas(dto.getParcelas()); // Adicionado
            faturamentoPf.setForma_de_pagamento(dto.getForma_de_pagamento()); // Adicionado
            faturamentoPf.setObservacoes(dto.getObservacoes());
            faturamentoPf.setValor(dto.getValor());
            faturamentoPf.setTotal(null);
            // Salvar o faturamento
            faturamentopfRepository.save(faturamentoPf);

            return modelMapper.map(faturamentoPf, GetFaturamentopfDto.class);
        } catch (Exception e) {
            // Log ou manipule a exceção conforme necessário
            throw new RuntimeException("Erro ao criar faturamento para pessoa física.", e);
        }
    }


private boolean existsFaturamentoNoPeriodo(UUID idPessoaFisica, Instant dataInicio, Instant dataFim) {
    return faturamentopfRepository.existsPessoaFisicaNoPeriodo(idPessoaFisica, dataInicio, dataFim);
}

    private int gerarNumeroFaturamentoPf() {
        Integer ultimoNumero = faturamentopfRepository.findMaxNumeroFaturamento();
        if (ultimoNumero == null) {
            ultimoNumero = 0;
        }
        return ultimoNumero + 1;
    }

    
	public GetFaturamentopfDto editarFaturamentopf(PutFaturamentopfDto dto) {
	UUID id = dto.getIdfaturamentopf();
	FaturamentoPf faturamentopf = faturamentopfRepository.findById(id).orElseThrow();
	modelMapper.map(dto, faturamentopf );
	faturamentopfRepository.save(faturamentopf);
	return modelMapper.map(faturamentopf, GetFaturamentopfDto.class);
			}
	public List<GetFaturamentopfDto> consultarFaturamentospf(String data_inicio) throws Exception {
	    List<FaturamentoPf> faturamentopf = faturamentopfRepository.findAll();
	    List<GetFaturamentopfDto> lista = mapFaturamentopfListToDTO(faturamentopf);
	    return lista;
	}

	private List<GetFaturamentopfDto> mapFaturamentopfListToDTO(List<FaturamentoPf> faturamentopfList) {
	    return faturamentopfList.stream()
	        .map(this::mapFaturamentoToDTO)
	        .collect(Collectors.toList());
	}
	
	
	 
	    

    public GetFaturamentopfDto consultarUmFaturamento(UUID idFaturamento) throws Exception {
        Optional<FaturamentoPf> faturamentoOptional = faturamentopfRepository.findById(idFaturamento);

        if (faturamentoOptional.isPresent()) {
            FaturamentoPf faturamento = faturamentoOptional.get();
            return mapFaturamentoToDTO(faturamento);
        } else {
            return null; // Or throw an exception, return an error DTO, etc.
        }
    }

    // Your existing method for mapping Faturamento to DTO
    private GetFaturamentopfDto mapFaturamentoToDTO(FaturamentoPf faturamento) {
        GetFaturamentopfDto dto = new GetFaturamentopfDto();
        dto.setIdfaturamentopf(faturamento.getIdfaturamentopf());
        dto.setNumeroFaturamento(faturamento.getNumeroFaturamento());
        dto.setDataHoraCriacao(faturamento.getDataHoraCriacao());
        dto.setData_inicio(faturamento.getData_inicio());
        dto.setData_fim(faturamento.getData_fim());
        dto.setVenda(faturamento.getVenda());
        dto.setNotafiscal(faturamento.getNotafiscal());
        dto.setValor(faturamento.getValor());
        dto.setComprador(faturamento.getComprador());
        dto.setTelefone(faturamento.getTelefone());
        dto.setEmail(faturamento.getEmail());
        dto.setResponsavelfinanceiro(faturamento.getResponsavelfinanceiro());
        dto.setTelefonefinanceiro(faturamento.getTelefonefinanceiro());
        dto.setWhatsapp(faturamento.getWhatsapp());
        dto.setEmailfinanceiro(faturamento.getEmailfinanceiro());
        dto.setData_de_pagamento(faturamento.getData_de_pagamento());
        dto.setParcelas(faturamento.getParcelas());
        dto.setForma_de_pagamento(faturamento.getForma_de_pagamento());
        dto.setObservacoes(faturamento.getObservacoes());
        dto.setPessoafisica(mapPessoaFisicaToDTO(faturamento.getPessoaFisica()));
        dto.setMatriculas(mapMatriculasToDTO(faturamento.getMatriculas()));
        dto.setTotal(faturamento.getTotal());
        dto.setFaturaFechada(faturamento.isFaturaFechada());
        return dto;
    }


    private GetPessoaFisicaDTO mapPessoaFisicaToDTO(PessoaFisica pessoaFisica) {
    	
    	 if (pessoaFisica == null) {
    	        return null;  // Or create and return a default DTO if needed
    	    }
        GetPessoaFisicaDTO pessoaFisicaDTO = new GetPessoaFisicaDTO();
        pessoaFisicaDTO.setIdpessoafisica(pessoaFisica.getIdpessoafisica());
        pessoaFisicaDTO.setDataHoraCriacao(pessoaFisica.getDataHoraCriacao());
        pessoaFisicaDTO.setPessoafisica(pessoaFisica.getPessoafisica());
        pessoaFisicaDTO.setRg(pessoaFisica.getRg());
        pessoaFisicaDTO.setCpf(pessoaFisica.getCpf());
        pessoaFisicaDTO.setTelefone_1(pessoaFisica.getTelefone1());
        pessoaFisicaDTO.setTelefone_2(pessoaFisica.getTelefone1());
        pessoaFisicaDTO.setEmail(pessoaFisica.getEmail());
        pessoaFisicaDTO.setAssinaturaPessoaFisica(pessoaFisica.getAssinaturaPessoaFisica());
        return pessoaFisicaDTO;
    }
    
   

    public GetFaturamentopfDto excluirFaturamentopf(UUID id) throws Exception  {
		Optional<FaturamentoPf> faturamentopf = faturamentopfRepository.findById(id);
		if (faturamentopf.isEmpty())
			throw new IllegalArgumentException("Faturamento não existe: " + id);
		// capturando o funcionario do banco de dados
	    FaturamentoPf faturamentos = faturamentopf.get();
		// excluindo o funcionario no banco de dados
		faturamentopfRepository.delete(faturamentos);
		GetFaturamentopfDto dto = modelMapper.map(faturamentos, GetFaturamentopfDto.class);
		dto.getIdfaturamentopf();
	    return dto;

	}

    public RelatorioFaturamentoPfDto calcularEAtualizarTotal(UUID idFaturamento) {
        Optional<FaturamentoPf> faturamentoOptional = faturamentopfRepository.findById(idFaturamento);

        if (faturamentoOptional.isPresent()) {
            FaturamentoPf faturamentoPf = faturamentoOptional.get();
            BigDecimal totalValue = BigDecimal.ZERO;

            if (faturamentoPf.getMatriculas() != null && !faturamentoPf.getMatriculas().isEmpty()) {
                for (Matriculas matricula : faturamentoPf.getMatriculas()) {
                    if (matricula.getValor() != null) {
                        totalValue = totalValue.add(matricula.getValor());
                    }
                }
            }

            faturamentoPf.setTotal(totalValue);

            // Mapear a PessoaFisica associada
            GetPessoaFisicaDTO pessoaFisicaDTO = mapPessoaFisicaToDTO(faturamentoPf.getPessoaFisica());
            
            // Adicionar a DTO da PessoaFisica ao RelatorioFaturamentoPfDto
            RelatorioFaturamentoPfDto relatorioFaturamentoPfDto = modelMapper.map(faturamentoPf, RelatorioFaturamentoPfDto.class);
            relatorioFaturamentoPfDto.setPessoafisica(pessoaFisicaDTO);

            faturamentopfRepository.save(faturamentoPf);

            return relatorioFaturamentoPfDto;
        } else {
            throw new RuntimeException("Faturamento não encontrado");
        }
    }

    
    @Transactional
    public void fecharFaturaManualmente(UUID idFaturamento) {
        FaturamentoPf faturamento = faturamentopfRepository.findById(idFaturamento)
                .orElseThrow(() -> new RuntimeException("Faturamento não encontrado com o ID: " + idFaturamento));

        // Carregue a PessoaFisica associada ao faturamento
        PessoaFisica pessoaFisica = faturamento.getPessoaFisica();
        if (pessoaFisica == null) {
            throw new RuntimeException("A fatura não tem uma Pessoa Fisica associada.");
        }

        faturamento.setFaturaFechada(true);

        // Atualize a entidade no repositório
        faturamentopfRepository.save(faturamento);

        // Outras operações relacionadas, se necessário...
    }
    

    @Transactional
    public void reabrirFatura(UUID idFaturamento) {
        FaturamentoPf faturamento = faturamentopfRepository.findById(idFaturamento)
                .orElseThrow(() -> new RuntimeException("Faturamento não encontrado com o ID: " + idFaturamento));

        if (!faturamento.isFaturaFechada()) {
            throw new IllegalStateException("A fatura não está fechada para reabrir.");
        }

        faturamento.setFaturaFechada(false);

        // Atualize outras propriedades ou execute ações necessárias ao reabrir a fatura

        faturamentopfRepository.save(faturamento);
    }
    

    private List<RelatorioMatriculaDTO> mapMatriculasToDTO(List<Matriculas> matriculas) {
        return matriculas.stream()
                .map(this::mapMatriculaToDTO)
                .collect(Collectors.toList());
    }

    private RelatorioMatriculaDTO mapMatriculaToDTO(Matriculas matricula) {
        RelatorioMatriculaDTO dto = new RelatorioMatriculaDTO();
        dto.setNumeroMatricula(matricula.getNumeroMatricula());
        dto.setValor(matricula.getValor());
        dto.setVenda(matricula.getVenda());
        dto.setStatus(matricula.getStatus());

        // Mapear a PessoaFisica associada
        GetPessoaFisicaDTO pessoaFisicaDTO = mapPessoaFisicaToDTO(matricula.getPessoafisica());
        dto.setPessoafisica(pessoaFisicaDTO);

        // Se houver uma entidade Funcionario associada, mapeie também
        if (matricula.getFuncionario() != null) {
            GetFuncionarioDTO funcionarioDTO = mapFuncionarioToDTO(matricula.getFuncionario());
            dto.setFuncionario(funcionarioDTO);
        }

        // Se houver uma entidade Turmas associada, mapeie também
        if (matricula.getTurmas() != null) {
            GetTurmasDTO turmasDTO = mapTurmasToDTO(matricula.getTurmas());
            dto.setTurmas(turmasDTO);
        }

        return dto;
    }


    private GetTurmasDTO mapTurmasToDTO(Turmas turmas) {
        ModelMapper modelMapper = new ModelMapper();
        GetTurmasDTO turmasDTO = modelMapper.map(turmas, GetTurmasDTO.class);
        return turmasDTO;
    }
    private GetFuncionarioDTO mapFuncionarioToDTO(Funcionario funcionario) {
        ModelMapper modelMapper = new ModelMapper();
        GetFuncionarioDTO funcionarioDTO = modelMapper.map(funcionario, GetFuncionarioDTO.class);
        return funcionarioDTO;
    }
    
  

}





