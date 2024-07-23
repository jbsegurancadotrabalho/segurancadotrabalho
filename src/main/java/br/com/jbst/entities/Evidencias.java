package br.com.jbst.entities;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;




@Data
@Entity
@Table(name = "evidencias")
public class Evidencias {

// Campo 1
@Id 
@Column(name = "idevidencias")
private UUID idEvidencias;

//Campo 2
@Temporal(TemporalType.TIMESTAMP)
@Column(name = "datahoracriacao", nullable = false)
private Instant dataHoraCriacao;

//Campo 3
@Column(name = "evidencias", length = 500, nullable = false)
private String nome;

//Campo 4
@Column(name = "descricao", length = 500, nullable = true)
private String descricao;	

//Campo 5
@Column(name = "inserir_evidencias", nullable = true)
private byte[] inserir_evidencias;

//Campo 6
@ManyToOne 
@JoinColumn(name = "idmatriculas", nullable = false)
private Matriculas matriculas;

}