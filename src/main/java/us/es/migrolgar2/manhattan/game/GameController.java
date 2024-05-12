package us.es.migrolgar2.manhattan.game;

import java.security.Principal;
import java.util.Optional;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import us.es.migrolgar2.manhattan.exceptions.BlockAlreadySelectedOrPlacedException;
import us.es.migrolgar2.manhattan.exceptions.NotOwnedException;
import us.es.migrolgar2.manhattan.exceptions.PlayerHasAlreadySelectedBlocks;
import us.es.migrolgar2.manhattan.game.messages.InvalidTurnMessage;
import us.es.migrolgar2.manhattan.game.messages.SelectBlocksMessage;
import us.es.migrolgar2.manhattan.game.messages.TurnMessage;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;

@AllArgsConstructor
@Slf4j
@Controller
public class GameController {
	
	private GameService gameService;
	private final SimpMessagingTemplate simpMessagingTemplate;
	
	@GetMapping("/game/{id}")
	public String getGame(@PathVariable("id") int gameId, Model model, Principal principal) {
		if(!this.gameService.isUserInGame(gameId, principal.getName())) {
			return "redirect:/index";
		}
		
		Game game = this.gameService.findById(gameId);
		Optional<PlayerDetails> hostDetails = this.gameService.getAllPlayerDetailsByGame(game)
										  .stream()
										  .filter(pd -> pd.isLobbyOwner())
										  .findFirst();
		
		if(hostDetails.isPresent()) {
			model.addAttribute("isHost", principal.getName().equals(hostDetails.get().getUsername()));
		} else {
			// Should realistically never happen
			log.error("The lobby owner of the game with id: " + gameId + " was not found.");
		}
		
		model.addAttribute("username", principal.getName());
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
			} catch(PlayerHasAlreadySelectedBlocks e) {
				return this.gameService.getSelectedBlocksMsg(gameId, principal.getName());
			}
		} else {
			return null;
		}
		
		return msg;
	}
	
	@MessageMapping("/{gameId}/play-turn")
	public TurnMessage playTurn(@DestinationVariable int gameId, @Payload TurnMessage msg, Principal principal) throws Exception {
		try {
			boolean validAndDone = this.gameService.playTurn(gameId, principal.getName(), msg);
			
			if(!validAndDone) {
				InvalidTurnMessage failMsg = new InvalidTurnMessage("Invalid", msg.getUsername());
				this.simpMessagingTemplate.convertAndSend("/game/" + gameId + "/play-turn", failMsg);
			}
			
			return null;
		} catch(Exception e) {
			return null;
		}
	}
	
}
