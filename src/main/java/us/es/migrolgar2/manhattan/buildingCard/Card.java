package us.es.migrolgar2.manhattan.buildingCard;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import us.es.migrolgar2.manhattan.common.AbstractEntity;
import us.es.migrolgar2.manhattan.game.Game;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;

@Entity
@Getter
@Setter
public class Card extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Min(value = 1)
	@Max(value = 9)
	private Integer sectorIndex;
	
	private boolean used;
	
	@ManyToOne
	private PlayerDetails player;
	
	@ManyToOne
	private Game game;
	
	@Transient
	public boolean isDrawed() {
		return this.player != null;
	}
	
}
