package us.es.migrolgar2.manhattan.playerDetails;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import us.es.migrolgar2.manhattan.game.Game;
import us.es.migrolgar2.manhattan.utils.Color;

@Service
@AllArgsConstructor
public class PlayerDetailsService {
	
	private PlayerDetailsRepository playerDetailsRepository;

	public PlayerDetails save(PlayerDetails playerDetails) {
		return this.playerDetailsRepository.save(playerDetails);
	}

	public PlayerDetails createBlankPlayerDetails() {
		PlayerDetails playerDetails = new PlayerDetails();
		playerDetails.setScore(0);
		playerDetails.setReady(false);
		playerDetails.setColor(Color.pickRandom());
		playerDetails.setPlaying(false);
		playerDetails.setGame(null);
		return playerDetails;
	}

	public PlayerDetails findByUsernameAndGame(String username, Game game) {
		return this.playerDetailsRepository.findByUsernameAndGame(username, game).orElse(null);
	}
	
}
