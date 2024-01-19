package us.es.migrolgar2.manhattan.game.messages;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TurnMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private Integer playedCardIndex;
	private Integer placedBlockIndex;
	private Integer cityIndex;
	private Integer sectorIndex;
	private Integer drawnCardIndex;
	
	@JsonIgnore
	@Transient
	public boolean isOwnedBy(String username) {
		return this.username.equals(username);
	}
	
	@JsonIgnore
	@Transient
	public boolean isValid() {
		boolean res = true;
		res &= (this.username != null || this.username.isBlank());
		
		res &= playedCardIndex  != null && playedCardIndex  >= 1 && playedCardIndex  <= 45;
		res &= placedBlockIndex != null && placedBlockIndex >= 1 && placedBlockIndex <= 96;
		res &= cityIndex        != null && cityIndex        >= 1 && cityIndex        <= 6;
		res &= sectorIndex      != null && sectorIndex      >= 1 && sectorIndex      <= 9;
		
		return res;
	}
}
