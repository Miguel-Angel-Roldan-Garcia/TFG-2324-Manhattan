package us.es.migrolgar2.manhattan.game;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import us.es.migrolgar2.manhattan.block.BlockAlreadySelectedOrPlacedException;
import us.es.migrolgar2.manhattan.exceptions.NotOwnedException;
import us.es.migrolgar2.manhattan.game.messages.SelectBlocksMessage;
import us.es.migrolgar2.manhattan.game.messages.TurnMessage;

@Controller
public class GameController {
	
	private GameService gameService;
	
	public GameController(GameService gameService) {
		this.gameService = gameService;
	}
	
	@GetMapping("/game/{id}")
	public String getGame(@PathVariable("id") int gameId, Model model, Principal principal) {
		if(!this.gameService.isUserInGame(gameId, principal.getName())) {
			return "redirect:/index";
		}
		
		model.addAttribute("gameId", gameId);
		return "game";
	}
	
	@MessageMapping("/{gameId}/select-blocks")
	@SendTo("/game/{gameId}/select-blocks")
	public SelectBlocksMessage selectBlocks(@DestinationVariable int gameId, @Payload SelectBlocksMessage msg, Principal principal) throws Exception {
		Game game = this.gameService.findById(gameId);
		if(!game.isRoundPlaying() && msg.isValid() && msg.isOwnedBy(principal.getName())) {
			try {
				this.gameService.selectBlocksByPlayerAndIndexes(principal.getName(), gameId, msg.getSelectedBlockIds());
				this.gameService.checkIfAllPlayersHaveSelectedBlocks(game);
				
			} catch(BlockAlreadySelectedOrPlacedException|NotOwnedException e) {
				return null;
			}
		} else {
			return null;
		}
		
		return msg;
	}
	
	@MessageMapping("/{gameId}/play-turn")
	@SendTo("/game/{gameId}/play-turn")
	public TurnMessage playTurn(@DestinationVariable int gameId, @Payload TurnMessage msg, Principal principal) throws Exception {
		try {
			return this.gameService.playTurn(gameId, principal.getName(), msg);
		} catch(Exception e) {
			return null;
		}
	}
	
}
