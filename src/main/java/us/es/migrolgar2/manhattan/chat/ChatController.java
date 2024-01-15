package us.es.migrolgar2.manhattan.chat;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

	//@MessageMapping("/game/{gameId}/chat")
	//@SendTo("/topic/game/{gameId}/chat")
	@MessageMapping("/chat")
	@SendTo("/topic/chat")
	public ChatMessage greeting(
			/* @DestinationVariable int gameId, */ @Payload ChatMessage msg, Principal principal) throws Exception {
		return new ChatMessage(String.format("%s: %s", principal.getName(), msg.getMsg()));
	}

}
