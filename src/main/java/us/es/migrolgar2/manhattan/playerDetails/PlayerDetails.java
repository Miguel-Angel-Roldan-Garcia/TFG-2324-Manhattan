package us.es.migrolgar2.manhattan.playerDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import us.es.migrolgar2.manhattan.common.AbstractEntity;
import us.es.migrolgar2.manhattan.game.Game;
import us.es.migrolgar2.manhattan.user.User;
import us.es.migrolgar2.manhattan.utils.Color;

@Entity
@Getter
@Setter
public class PlayerDetails extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	private boolean ready;
	
	@NotNull
	@Max(value = 96) 
	@Min(value = 0)
	private Integer score;	
	
	@NotNull
	private Color color;
	
	@NotNull
	@Max(value = 4)
	@Min(value = 1)
	private Integer position;
	
	private boolean playing;
	
	@ManyToOne
	private Game game;
	
	@ManyToOne
	private User user;
}
