package us.es.migrolgar2.manhattan.lobby;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import us.es.migrolgar2.manhattan.common.AbstractEntity;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;

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
	private String name;
	
	private String password;
	
	@ManyToOne
	private PlayerDetails owner;
	
	@ManyToOne
	private PlayerDetails player2;
	
	@ManyToOne
	private PlayerDetails player3;
	
	@ManyToOne
	private PlayerDetails player4;
	
	
	
}
