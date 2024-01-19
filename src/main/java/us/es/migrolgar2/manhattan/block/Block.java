package us.es.migrolgar2.manhattan.block;

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
import us.es.migrolgar2.manhattan.city.City;
import us.es.migrolgar2.manhattan.common.AbstractEntity;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;
import us.es.migrolgar2.manhattan.sector.Sector;

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
	
	@NotNull
	@Min(value = 1)
	@Max(value = 96)
	private Integer index_;
	
	private boolean selected;
	
	private boolean placed;
	
	@Min(value = 1)
	@Max(value = 96)
	private Integer order_;
	
	@ManyToOne
	private Sector sector;
	
	@NotNull
	@ManyToOne
	private PlayerDetails player;
	
	@JsonGetter("player")
	public String getPlayerUsername() {
		return this.getPlayer().getUsername();
	}
	
	@JsonIgnore
	@Transient
	public City getCity() {
		return this.sector.getCity();
	}
	
}
