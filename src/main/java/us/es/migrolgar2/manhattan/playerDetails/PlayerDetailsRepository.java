package us.es.migrolgar2.manhattan.playerDetails;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import us.es.migrolgar2.manhattan.game.Game;

public interface PlayerDetailsRepository extends JpaRepository<PlayerDetails, Integer> {

	Optional<PlayerDetails> findByUsernameAndGame(String username, Game game);
	
	
}
