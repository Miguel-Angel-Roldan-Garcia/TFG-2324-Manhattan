package us.es.migrolgar2.manhattan.game.messages;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InvalidTurnMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String isInvalid;
	private String username;
	
}
