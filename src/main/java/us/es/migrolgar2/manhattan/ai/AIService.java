package us.es.migrolgar2.manhattan.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import us.es.migrolgar2.manhattan.block.Block;
import us.es.migrolgar2.manhattan.block.BlockService;
import us.es.migrolgar2.manhattan.card.Card;
import us.es.migrolgar2.manhattan.card.CardService;
import us.es.migrolgar2.manhattan.game.messages.TurnMessage;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetailsService;

@Service
@AllArgsConstructor
public class AIService {
	
	private CardService cardService;
	private BlockService blockService;
	private PlayerDetailsService playerDetailsService;
	
	public TurnMessage calculatePlacingTurn(PlayerDetails pd) {
		Random random = new Random();
		TurnMessage turn = new TurnMessage();
		
		turn.setUsername(pd.getUsername());
		turn.setCityIndex(random.nextInt(1, 7));
		
		List<Card> cards = cardService.findByPlayer(pd);
		turn.setPlayedCardIndex(cards.get(random.nextInt(0, cards.size())).getIndex_()); 
		
		List<Block> selectedBlocks = blockService.getBlocksSelectedByPlayer(pd);
		turn.setPlacedBlockIndex(selectedBlocks.get(random.nextInt(0, selectedBlocks.size())).getIndex_());
		
		turn.setSectorIndex(-1);
		
		return turn;
	}
	
	public void calculateInitialSelectBlocks(PlayerDetails pd) {
		Random random = new Random();
		List<Block> availableBlocks = blockService.getAvailableBlocksByPlayer(pd);
		for(int i = 0; i < 6; i++) {
			Block select = availableBlocks.get(random.nextInt(0, availableBlocks.size()));
			
			availableBlocks.remove(select);
			select.setSelected(true);
			blockService.save(select);
		}
		
		pd.setHasSelectedBlocks(true);
		this.playerDetailsService.save(pd);
	}
	
	public Integer[] calculateSelectBlocks(PlayerDetails pd) {
		Random random = new Random();
		List<Block> availableBlocks = blockService.getAvailableBlocksByPlayer(pd);
		List<Block> selectedBlocks = new ArrayList<Block>();
		for(int i = 0; i < 6; i++) {
			Block select = availableBlocks.get(random.nextInt(0, availableBlocks.size()));
			
			availableBlocks.remove(select);
			selectedBlocks.add(select);
		}
		
		return selectedBlocks.stream().mapToInt(b -> b.getIndex_()).boxed().toArray(Integer[]::new);
	}
	
}
