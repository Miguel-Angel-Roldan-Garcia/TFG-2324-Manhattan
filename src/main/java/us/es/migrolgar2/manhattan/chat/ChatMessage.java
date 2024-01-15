package us.es.migrolgar2.manhattan.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {

	private String msg;
	
	public ChatMessage() {
		
	}
	
	public ChatMessage(String msg) {
		this.msg = msg;
	}
	
}
