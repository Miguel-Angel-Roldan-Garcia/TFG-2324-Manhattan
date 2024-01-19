package us.es.migrolgar2.manhattan.game;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class GameRestController {

	private GameService gameService;
	
	@GetMapping("/game/{id}/get-data")
	Map<String, Object> getData(@PathVariable("id") int gameId) {
		Map<String, Object> data = this.gameService.getGameData(gameId);
		return data;
	}
	
}
