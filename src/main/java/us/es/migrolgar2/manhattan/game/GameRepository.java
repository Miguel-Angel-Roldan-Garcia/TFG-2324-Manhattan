package us.es.migrolgar2.manhattan.game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;

public interface GameRepository extends JpaRepository<Game, Integer> {
	
	@Query("SELECT pd FROM PlayerDetails pd WHERE pd.game = game")
	List<PlayerDetails> getGamePlayerDetails(Game game);

}
