
package br.com.jbst.entities.map;

import java.time.Instant;
import java.util.UUID;

import br.com.jbst.entities.Instrutor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "formacao")
public class Formacao {	
	
	// Campo 1
	@Id
	@Column(name = "idformacao")
	private UUID idFormacao;

	// Campo 2
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datahoracriacao", nullable = false) 
	private Instant dataHoraCriacao;

	// Campo 3
	@Column(name = "formacao", length = 100, nullable = false)
	private String formacao;

	// Campo 4
	@Column(name = "conselho", length = 50, nullable = true)
	private String conselho;

	// Campo 5
	@Column(name = "registro", length = 100, nullable = true)
	private String registro;

	// Campo 6
	@Column(name = "estado", length = 100, nullable = true)
	private String estado;
		
	// Campo 7
	@Column(name = "proficiencia", nullable = true)
	private byte[] proficiência;
	
	// Campo 8
	@ManyToOne 
	@JoinColumn(name = "id_instrutor", nullable = false)
	private Instrutor instrutores;
	

}
