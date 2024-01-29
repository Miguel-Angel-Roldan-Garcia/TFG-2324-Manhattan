package us.es.migrolgar2.manhattan.card;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import us.es.migrolgar2.manhattan.common.AbstractEntity;
import us.es.migrolgar2.manhattan.game.Game;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Min(value = 1)
	@Max(value = 9)
	private Integer sectorIndex;
	
	@NotNull
	@Min(value = 1)
	@Max(value = 55)
	private Integer index_;
	
	private boolean used;
	
	@ManyToOne
	private PlayerDetails player;
	
	@JsonIgnore
	@ManyToOne
	private Game game;
	
	@JsonIgnore
	@Transient
	public boolean isDrawn() {
		return this.player != null;
	}
	
	@JsonGetter("player")
	public String getPlayer() {
		if(this.player != null) {
			return this.player.getUsername();
		} else {
			return null;
		}
		
	}
	
}
