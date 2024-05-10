package us.es.migrolgar2.manhattan.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import us.es.migrolgar2.manhattan.game.Game;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;

@Service
@AllArgsConstructor
public class CardService {
	
	private CardRepository cardRepository;

	public void initializeCards(Game game) {
		int globalCardIndex = 1;
		for(int i = 1; i <= 9; i++) {
			// HINT: 5 cards of each type/sector. This is not cities!
			for(int j = 1; j <= 5; j++) { 
				Card card = new Card(i, globalCardIndex++, false, null, game);
				this.cardRepository.save(card);
			}
		}
	}
	
	public List<Card> drawPlayer4Cards(PlayerDetails playerDetails) {
		Random random = new Random();
		List<Card> cards = this.cardRepository.getAllCardsNotDrawn();
		List<Card> drawnCards = new ArrayList<Card>();
		
		if(cards.size() < 4) {
			this.reshuflePile();
			cards = this.cardRepository.getAllCardsNotDrawn();
		}
		
		for(int i = 0; i < 4; i++) {
			Card selectedCard = cards.remove(random.nextInt(cards.size()));
			selectedCard.setPlayer(playerDetails);
			drawnCards.add(this.cardRepository.save(selectedCard));
		}
		
		return drawnCards;
	}
	
	public Card drawPlayer1Card(PlayerDetails playerDetails) {
		Random random = new Random();
		List<Card> cards = this.cardRepository.getAllCardsNotDrawn();
		Card selectedCard = null;
		
		if(cards.size() <= 0) {
			this.reshuflePile();
			cards = this.cardRepository.getAllCardsNotDrawn();
		}
		
		if(cards.size() == 1) {
			selectedCard = cards.get(0);
		} else {
			selectedCard = cards.get(random.nextInt(cards.size()));
		}
		
		selectedCard.setPlayer(playerDetails);
		return this.cardRepository.save(selectedCard);
	}
	
	public void useCard(Card card) {
		card.setUsed(true);
		card.setPlayer(null);
		this.cardRepository.save(card);
	}
	
	private void reshuflePile() {
		List<Card> cards = this.cardRepository.getAllUsedCards();
		
		for(Card c:cards) {
			c.setUsed(false);
			this.cardRepository.save(c);
		}
	}

	public Card findById(Integer id) {
		return this.cardRepository.findById(id).orElse(null);
	}
	
	public List<Card> findByPlayer(PlayerDetails pd) {
		return this.cardRepository.findByPlayer(pd);
	}

	public Card save(Card card) {
		return this.cardRepository.save(card);
	}
	
	public Card findByPlayerAndIndex(PlayerDetails player, Integer cardIndex) {
		return this.cardRepository.findByPlayerAndIndex_(player, cardIndex).orElse(null);
	}

	public List<Card> getAllByGameId(int gameId) {
		return this.cardRepository.findAllByGameId(gameId);
	}
	
}
