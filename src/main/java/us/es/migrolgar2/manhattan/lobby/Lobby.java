package us.es.migrolgar2.manhattan.lobby;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
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
	@Column(unique = true)
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
	
	@JsonIgnore
	@Transient
	public int getNextFreePosition() {
		int position = 0;
		if(this.player2 == null) {
			position = 2;
		} else if(this.player3 == null) {
			position = 3;
		} else if(this.player4 == null) {
			position = 4;
		}
		return position;
	}
	
	@JsonIgnore
	@Transient
	public void addPlayer(PlayerDetails playerDetails) {
		switch(playerDetails.getPosition()) {
		case 2:
			this.setPlayer2(playerDetails);
			break;
		case 3:
			this.setPlayer3(playerDetails);
			break;
		case 4:
			this.setPlayer4(playerDetails);
			break;
		}
	}
	
	@JsonIgnore
	@Transient
	public List<PlayerDetails> getPlayersAsList() {
		List<PlayerDetails> list = new ArrayList<PlayerDetails>();
		
		list.add(this.owner);
		if(this.player2 != null) {
			list.add(this.player2);
		} else if(this.player3 != null) {
			list.add(this.player3);
		} else if(this.player4 != null) {
			list.add(this.player4);
		}
		
		return list;
	}
	
	@JsonIgnore
	@Transient
	public boolean hasPassword() {
		return this.getPassword() != null && !this.getPassword().isBlank();
	}

	@JsonIgnore
	@Transient
	public boolean isUserInLobby(String username) {
		boolean res = false;
		if(this.owner != null && this.owner.getUsername().equals(username) ||
				this.player2 != null && this.player2.getUsername().equals(username) ||
				this.player3 != null && this.player3.getUsername().equals(username) ||
				this.player4 != null && this.player4.getUsername().equals(username)) {
			res = true;
		}
		return res;
	}
	
}
