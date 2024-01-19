package us.es.migrolgar2.manhattan.chat;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String msg;
	
}
