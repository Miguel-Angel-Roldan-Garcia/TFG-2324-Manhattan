package us.es.migrolgar2.manhattan.playerDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import us.es.migrolgar2.manhattan.common.AbstractEntity;
import us.es.migrolgar2.manhattan.game.Game;
import us.es.migrolgar2.manhattan.user.User;

@Entity
@Getter
@Setter
public class PlayerDetails extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Max(value = 96) 
	@Min(value = 0)
	private Integer score;	
	
	@NotNull
	@Pattern(regexp = "^#[0-9a-fA-F]{6}$")
	private String color;
	
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
