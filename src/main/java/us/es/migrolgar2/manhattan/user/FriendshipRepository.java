package us.es.migrolgar2.manhattan.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {

	@Query("SELECT DISTINCT CASE WHEN u1.username = :username THEN u2.username ELSE u1.username END " +
		       "FROM Friendship f " +
		       "JOIN f.user1 u1 " +
		       "JOIN f.user2 u2 " +
		       "WHERE u1.username = :username OR u2.username = :username")
	List<String> findAllFriendsUsernames(String username);

}
