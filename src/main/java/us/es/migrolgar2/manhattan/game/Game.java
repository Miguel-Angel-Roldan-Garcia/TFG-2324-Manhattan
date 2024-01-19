package us.es.migrolgar2.manhattan.game;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import us.es.migrolgar2.manhattan.common.AbstractEntity;

@Entity
@Getter
@Setter
public class Game extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	private boolean roundPlaying;
	
	private int roundNumber;
	
	private int turnNumber;
	
	private LocalDateTime startDate;
	
	private LocalDateTime finishDate;
	
	@JsonIgnore
	@Transient
	public boolean isFinished() {
		return this.finishDate != null;
	}
	
}
