package us.es.migrolgar2.manhattan.lobby;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;

public interface LobbyRepository extends JpaRepository<Lobby, Integer> {
	
	@Query("SELECT pd.lobby FROM PlayerDetails pd WHERE pd.isLobbyOwner AND pd.username = :username")
	Set<Lobby> getOwnedLobbies(@Param("username") String username);

	@Query("SELECT pd.lobby FROM PlayerDetails pd WHERE pd.isLobbyOwner AND pd.lobby.privacyStatus != 'PRIVATE' AND pd.username IN :friendsUsernames")
	Set<Lobby> getLobbiesOwnedByFriends(@Param("friendsUsernames") List<String> friendsUsernames);
	
	@Query("SELECT l FROM Lobby l WHERE l.privacyStatus = 'PUBLIC'")
	Set<Lobby> findAllPublicLobbies();

	Optional<Lobby> findByName(String name);

	@Query("SELECT pd FROM PlayerDetails pd WHERE pd.lobby.id = :lobbyId")
	List<PlayerDetails> getAllLobbyPlayers(@Param("lobbyId") int lobbyId);

	@Query("SELECT pd.position FROM PlayerDetails pd WHERE pd.lobby.id = :lobbyId")
	Set<Integer> getAllLobbyPlayersPositions(@Param("lobbyId") int lobbyId);

}
