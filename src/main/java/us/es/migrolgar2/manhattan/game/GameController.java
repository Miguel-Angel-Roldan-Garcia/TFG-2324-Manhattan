package us.es.migrolgar2.manhattan.game;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GameController {
	
	private GameService gameService;
	
	public GameController(GameService gameService) {
		this.gameService = gameService;
	}
	
	@GetMapping("/game")
	public String getGame(Model model) {
		return "game";
	}
	
}
