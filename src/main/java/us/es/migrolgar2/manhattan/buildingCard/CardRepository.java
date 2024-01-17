package us.es.migrolgar2.manhattan.buildingCard;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CardRepository extends JpaRepository<Card, Integer> {
	
	@Query("SELECT c FROM Card c WHERE NOT c.isDrawn()")
	List<Card> getAllCardsNotDrawn();

	@Query("SELECT c FROM Card c WHERE c.used")
	List<Card> getAllUsedCards();
	
}
