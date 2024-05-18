package us.es.migrolgar2.manhattan.game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;

public interface GameRepository extends JpaRepository<Game, Integer> {
	
	@Query("SELECT pd FROM PlayerDetails pd WHERE pd.game = :game")
	List<PlayerDetails> getGamePlayerDetails(@Param("game") Game game);

	@Query("SELECT DISTINCT game FROM PlayerDetails pd WHERE pd.username = :username")
	List<Game> findAllByUsername(@Param("username") String username);

}
