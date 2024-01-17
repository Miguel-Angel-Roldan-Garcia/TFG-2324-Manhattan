package us.es.migrolgar2.manhattan.lobby;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LobbyRepository extends JpaRepository<Lobby, Integer> {

//	+ "WHERE l.privacyStatus != 'PRIVATE' "
//	+ "AND l.privacyStatus != 'PUBLIC' "
	
	@Query("SELECT l FROM Lobby l WHERE l.owner.username IN :friendsUsernames")
	Set<Lobby> getLobbiesOwnedByFriends(@Param("friendsUsernames") List<String> friendsUsernames);
	
	@Query("SELECT l FROM Lobby l WHERE l.privacyStatus = 'PUBLIC'")
	Set<Lobby> findAllPublicLobbies();

	Optional<Lobby> findByName(String name);

}
