package us.es.migrolgar2.manhattan.card;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import us.es.migrolgar2.manhattan.game.Game;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;

public interface CardRepository extends JpaRepository<Card, Integer> {
	
	@Query("SELECT c FROM Card c WHERE c.player IS NULL AND c.game = :game")
	List<Card> getAllCardsNotDrawnByGame(@Param("game") Game game);

	@Query("SELECT c FROM Card c WHERE c.used AND c.game = :game")
	List<Card> getAllUsedCardsByGame(@Param("game") Game game);

	@Query("SELECT c FROM Card c WHERE c.player = :pd AND c.index_ = :cardIndex")
	Optional<Card> findByPlayerAndIndex_(@Param("pd") PlayerDetails player, @Param("cardIndex") Integer cardIndex);

	List<Card> findAllByGameId(int gameId);

	@Query("SELECT c FROM Card c WHERE c.player = :pd")
	List<Card> findByPlayer(@Param("pd") PlayerDetails pd);
	
}
