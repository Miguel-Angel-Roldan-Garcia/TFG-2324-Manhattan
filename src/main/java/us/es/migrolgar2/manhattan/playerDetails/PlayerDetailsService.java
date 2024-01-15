package us.es.migrolgar2.manhattan.playerDetails;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlayerDetailsService {
	
	private PlayerDetailsRepository playerDetailsRepository;

	public PlayerDetails save(PlayerDetails playerDetails) {
		return this.playerDetailsRepository.save(playerDetails);
	}
	
}
