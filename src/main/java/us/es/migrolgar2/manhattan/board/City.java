package us.es.migrolgar2.manhattan.board;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import us.es.migrolgar2.manhattan.common.AbstractEntity;
import us.es.migrolgar2.manhattan.game.Game;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class City extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Min(value = 1)
	@Max(value = 6)
	private Integer index_;
	
	@ManyToOne
	private Game game;
	
}
