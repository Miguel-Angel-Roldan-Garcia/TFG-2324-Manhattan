package us.es.migrolgar2.manhattan.game.messages;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
public class PlayCardMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private Integer cardIndex;
	private boolean isPlayed;
	
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
		
		res &= cardIndex != null;
		
		return res;
	}
}
