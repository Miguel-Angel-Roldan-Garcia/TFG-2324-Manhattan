package us.es.migrolgar2.manhattan.lobby;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import us.es.migrolgar2.manhattan.common.AbstractEntity;

@Entity
@Getter
@Setter
public class Lobby extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private LobbyPrivacyStatus privacyStatus;
	
	@NotBlank
	@Length(max = 30)
	@Column(unique = true)
	private String name;
	
	private String password;
	
	private boolean available;
	
	@JsonIgnore
	@Transient
	public boolean hasPassword() {
		return this.getPassword() != null && !this.getPassword().isBlank();
	}

	
}
