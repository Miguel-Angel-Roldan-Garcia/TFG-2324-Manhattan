package us.es.migrolgar2.manhattan.playerDetails;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import us.es.migrolgar2.manhattan.common.AbstractEntity;
import us.es.migrolgar2.manhattan.game.Game;
import us.es.migrolgar2.manhattan.lobby.Lobby;
import us.es.migrolgar2.manhattan.utils.Color;

@Entity
@Getter
@Setter
public class PlayerDetails extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	private boolean ready;
	
	@NotNull
	@Min(value = 0)
	private Integer score;	
	
	@NotNull
	private Color color;
	
	@NotNull
	@Min(value = 1)
	@Max(value = 4)
	private Integer position;
	
	private boolean hasSelectedBlocks;
	
	private boolean playing;
	
	private boolean isLobbyOwner;
	
	private boolean isAIControlled = false;
	
	@NotBlank
	private String username;
	
	@JsonIgnore
	@ManyToOne
	private Game game;
	
	@JsonIgnore
	@ManyToOne
	private Lobby lobby;
	
	@JsonGetter("color")
	public String getColorCode() {
		return color.getColorCode();
	}
	
	public void addScore(Integer score) {
		this.score += score;
	}
}
