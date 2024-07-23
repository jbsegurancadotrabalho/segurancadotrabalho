package br.com.jbst.services;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.time.Instant;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

import br.com.jbst.DTO.RelatorioFaturamentoDTO;
import br.com.jbst.DTOs.GetFaturamentoDTO;
import br.com.jbst.DTOs.PostFaturamentoDTO;
import br.com.jbst.DTOs.PutFaturamentoDTO;
import br.com.jbst.entities.Faturamento;
import br.com.jbst.entities.Matriculas;
import br.com.jbst.entities.map.Empresa;
import br.com.jbst.repositories.EmpresaRepository;
import br.com.jbst.repositories.FaturamentoRepository;
import jakarta.transaction.Transactional;




@Service
public class FaturamentoService {


    @Autowired
	FaturamentoRepository faturamentoRepository;
    
    @Autowired
	EmpresaRepository empresaRepository;
  
    @Autowired
	ModelMapper modelMapper;
    
    public GetFaturamentoDTO criarFaturamento(PostFaturamentoDTO dto) throws Exception {
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
            if (existeFaturamentoNoPeriodo(dto.getIdEmpresa(), dto.getData_inicio(), dto.getData_fim())) {
                throw new Exception("Já foi cadastrado um faturamento para a empresa no período especificado.");
            }
            // O faturamento está dentro do limite, continue com o processo de criação
            Faturamento faturamento = modelMapper.map(dto, Faturamento.class);
            faturamento.setIdfaturamento(UUID.randomUUID());
            faturamento.setDataHoraCriacao(Instant.now());
            int numeroFaturamento = gerarNumeroFaturamento();
            faturamento.setNumeroFaturamento(numeroFaturamento);

            // Definir a fatura como aberta
            faturamento.setFaturaFechada(false);

            // Obter a empresa pelo ID e definir no faturamento
            Empresa empresa = empresaRepository.findById(dto.getIdEmpresa())
                    .orElseThrow(() -> new RuntimeException("Empresa não encontrada com o ID: " + dto.getIdEmpresa()));

            faturamento.setEmpresa(empresa);

            // Salvar o faturamento
            faturamentoRepository.save(faturamento);

            return modelMapper.map(faturamento, GetFaturamentoDTO.class);
        } catch (Exception e) {
            // Log ou manipule a exceção conforme necessário
            throw new RuntimeException("Erro ao criar faturamento.", e);
        }
    }


    private boolean existeFaturamentoNoPeriodo(UUID idEmpresa, Instant dataInicio, Instant dataFim) {
        return faturamentoRepository.existsFaturamentoNoPeriodo(idEmpresa, dataInicio, dataFim);
    }

    private int gerarNumeroFaturamento() {       
        Integer ultimoNumero = faturamentoRepository.findMaxNumeroFaturamento();
        if (ultimoNumero == null) {
            ultimoNumero = 0;
        }
        return ultimoNumero + 1;
    }

		
    
    public GetFaturamentoDTO editarFaturamento(PutFaturamentoDTO dto) {
        UUID idFaturamento = dto.getIdfaturamento();
        
        // Busca o faturamento no repositório
        Faturamento faturamento = faturamentoRepository.findById(idFaturamento).orElseThrow();

        // Mapeia os campos do DTO para o objeto Faturamento
        modelMapper.map(dto, faturamento);

        // Obtém o ID da empresa do DTO


        // Define a data de criação para o momento atual
        faturamento.setDataHoraCriacao(Instant.now());

        // Salva as alterações no repositório
        faturamentoRepository.save(faturamento);

        // Mapeia o objeto Faturamento para um DTO de resposta
        return modelMapper.map(faturamento, GetFaturamentoDTO.class);
    }


    
    public List<GetFaturamentoDTO> consultarFaturamentos(String data_inicio) throws Exception {
		List<Faturamento> faturamento = faturamentoRepository.findAll();
		List<GetFaturamentoDTO> lista = modelMapper.map(faturamento, new TypeToken<List<GetFaturamentoDTO>>() {
		}.getType());
		return lista;
	} 
    
    
    public GetFaturamentoDTO consultarUmFaturamento(UUID idFaturamento) {
        Optional<Faturamento> faturamento = faturamentoRepository.findById(idFaturamento);

        if (faturamento.isPresent()) {
        	Faturamento faturamentos = faturamento.get();
            return modelMapper.map(faturamentos, GetFaturamentoDTO.class);
        } else {
          
			throw new RuntimeException("Faturamento não encontrado"); // Lançar exceção quando não encontrada
        }
    }
    

    public GetFaturamentoDTO excluirFaturamento(UUID id) throws Exception  {
		Optional<Faturamento> faturamento = faturamentoRepository.findById(id);
		if (faturamento.isEmpty())
			throw new IllegalArgumentException("Faturamento não existe: " + id);
		// capturando o funcionario do banco de dados
	    Faturamento faturamentos = faturamento.get();
		// excluindo o funcionario no banco de dados
		faturamentoRepository.delete(faturamentos);
		GetFaturamentoDTO dto = modelMapper.map(faturamentos, GetFaturamentoDTO.class);
		dto.getIdfaturamento();
	    return dto;

	}

    public RelatorioFaturamentoDTO calcularEAtualizarTotal(UUID idFaturamento) {
        Optional<Faturamento> faturamentoOptional = faturamentoRepository.findById(idFaturamento);

        if (faturamentoOptional.isPresent()) {
            Faturamento faturamento = faturamentoOptional.get();
            BigDecimal totalValue = BigDecimal.ZERO;

            if (faturamento.getMatriculas() != null && !faturamento.getMatriculas().isEmpty()) {
                for (Matriculas matricula : faturamento.getMatriculas()) {
                    if (matricula.getValor() != null) {
                        totalValue = totalValue.add(matricula.getValor());
                    }
                }
            }

            faturamento.setTotal(totalValue);
            faturamentoRepository.save(faturamento);

            return modelMapper.map(faturamento, RelatorioFaturamentoDTO.class);
        } else {
            throw new RuntimeException("Faturamento não encontrado");
        }
    }
    
    @Transactional
    public void fecharFaturaManualmente(UUID idFaturamento) {
        Faturamento faturamento = faturamentoRepository.findById(idFaturamento)
                .orElseThrow();

        // Feche a fatura manualmente
        faturamento.setFaturaFechada(true);

        // Atualize a entidade no repositório
        faturamentoRepository.save(faturamento);

        // Outras operações relacionadas, se necessário...
    }
    
    @Transactional
    public void reabrirFatura(UUID idFaturamento) {
        Faturamento faturamento = faturamentoRepository.findById(idFaturamento)
                .orElseThrow();

        if (!faturamento.isFaturaFechada()) {
            throw new IllegalStateException("A fatura não está fechada para reabrir.");
        }

        faturamento.setFaturaFechada(false);

        // Atualize outras propriedades ou execute ações necessárias ao reabrir a fatura

        faturamentoRepository.save(faturamento);
    }
    
    public List<GetFaturamentoDTO> buscarFaturamentosPorIdUsuario(UUID idUsuario, int mes, int ano) {
        List<GetFaturamentoDTO> faturamentosDTO = new ArrayList<>();

        List<Empresa> empresas = empresaRepository.findByUsuario_Id(idUsuario);
        for (Empresa empresa : empresas) {
            List<Faturamento> faturamentos = empresa.getFaturamentos();
            for (Faturamento faturamento : faturamentos) {
                Instant dataInicio = faturamento.getData_inicio();
                Instant dataFim = faturamento.getData_fim();

                // Extrair mês e ano da data de início do faturamento
                int mesFaturamento = dataInicio.atZone(ZoneId.systemDefault()).getMonthValue();
                int anoFaturamento = dataInicio.atZone(ZoneId.systemDefault()).getYear();

                // Verificar se o faturamento está no mês e ano especificados
                if (mesFaturamento == mes && anoFaturamento == ano) {
                    GetFaturamentoDTO faturamentoDTO = modelMapper.map(faturamento, GetFaturamentoDTO.class);
                    // Mapeie outros dados do faturamento, se necessário
                    faturamentosDTO.add(faturamentoDTO);
                }
            }
        }

        return faturamentosDTO;
    }
    
    public List<GetFaturamentoDTO> buscarFaturamentosPorIdUsuarioEmAberto(UUID idUsuario) {
        List<GetFaturamentoDTO> faturamentosDTO = new ArrayList<>();

        List<Empresa> empresas = empresaRepository.findByUsuario_Id(idUsuario);
        for (Empresa empresa : empresas) {
            List<Faturamento> faturamentos = empresa.getFaturamentos();
            for (Faturamento faturamento : faturamentos) {
                if (faturamento.isFaturaAberta()) { // Verifica se a fatura está aberta
                    GetFaturamentoDTO faturamentoDTO = modelMapper.map(faturamento, GetFaturamentoDTO.class);
                    // Mapeie outros dados do faturamento, se necessário
                    faturamentosDTO.add(faturamentoDTO);
                }
            }
        }

        return faturamentosDTO;
    }

  
    public List<GetFaturamentoDTO> buscarFaturamentosPorIdUsuarioFaturaAberta(UUID idUsuario) {
        List<GetFaturamentoDTO> faturamentosDTO = new ArrayList<>();

        List<Empresa> empresas = empresaRepository.findByUsuario_Id(idUsuario);
        for (Empresa empresa : empresas) {
            List<Faturamento> faturamentos = empresa.getFaturamentos();
            for (Faturamento faturamento : faturamentos) {
                if (faturamento.isFaturaAberta() && !faturamento.isFaturaFechada()) {
                    GetFaturamentoDTO faturamentoDTO = modelMapper.map(faturamento, GetFaturamentoDTO.class);
                    // Mapeie outros dados do faturamento, se necessário
                    faturamentosDTO.add(faturamentoDTO);
                }
            }
        }

        return faturamentosDTO;
    }
    
}





    

