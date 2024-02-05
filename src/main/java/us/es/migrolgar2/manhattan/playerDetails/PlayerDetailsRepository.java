package us.es.migrolgar2.manhattan.playerDetails;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import us.es.migrolgar2.manhattan.game.Game;

public interface PlayerDetailsRepository extends JpaRepository<PlayerDetails, Integer> {

	Optional<PlayerDetails> findByUsernameAndGame(String username, Game game);

	@Query("SELECT pd FROM PlayerDetails pd WHERE pd.position = :position AND pd.lobby.id = :lobbyId")
	Optional<PlayerDetails> findByPositionAndLobbyId(@Param("position") int position, @Param("lobbyId") int id);
	
	@Query("SELECT pd FROM PlayerDetails pd WHERE pd.username = :username AND pd.lobby.id = :lobbyId")
	Optional<PlayerDetails> findByUsernameAndLobbyId(@Param("username") String username, @Param("lobbyId") int id);

	
	
	
}
