package us.es.migrolgar2.manhattan.buildingBlock;

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
import us.es.migrolgar2.manhattan.board.City;
import us.es.migrolgar2.manhattan.board.Sector;
import us.es.migrolgar2.manhattan.common.AbstractEntity;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Block extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Max(value = 4)
	@Min(value = 1)
	private Integer size;
	
	private boolean selected;
	
	private boolean placed;
	
	@Max(value = 96)
	@Min(value = 1)
	private Integer order_;
	
	@ManyToOne
	private Sector sector;
	
	@NotNull
	@ManyToOne
	private PlayerDetails player;
	
	@Transient
	public City getCity() {
		return this.sector.getCity();
	}
	
}
