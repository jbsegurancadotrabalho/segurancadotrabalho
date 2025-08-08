package br.com.jbst.entities;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assinaturas_instrutores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssinaturaInstrutorD4Sign {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id_assinatura_instrutor_D4Sign;

  
    @JsonIgnore
    @Column(name = "imagem_jpg", nullable = false, columnDefinition = "BYTEA")
    private byte[] imagem;

    @Column(name = "ordem", nullable = false)
    private int ordem;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idturmas", nullable = false)
    @JsonIgnoreProperties("assinaturasInstrutores")
    private Turmas turma;


}
