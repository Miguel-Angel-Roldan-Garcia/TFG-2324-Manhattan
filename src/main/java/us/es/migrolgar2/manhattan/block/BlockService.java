package us.es.migrolgar2.manhattan.block;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import us.es.migrolgar2.manhattan.game.Game;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;
import us.es.migrolgar2.manhattan.sector.Sector;

@Service
@AllArgsConstructor
public class BlockService {

	private BlockRepository blockRepository;

	public void initializePlayerBlocks(PlayerDetails pd, Integer globalIndex) {
		Block block4Stories;
		for(int i = 0; i < 3; i++) {
			block4Stories = new Block(4, globalIndex++, false, false, null, null, pd);
			this.blockRepository.save(block4Stories);
		}
		
		Block block3Stories;
		for(int i = 0; i < 4; i++) {
			block3Stories = new Block(3, globalIndex++, false, false, null, null, pd);
			this.blockRepository.save(block3Stories);
		}
		
		Block block2Stories;
		for(int i = 0; i < 6; i++) {
			block2Stories = new Block(2, globalIndex++, false, false, null, null, pd);
			this.blockRepository.save(block2Stories);
		}
		
		Block block1Stories;
		for(int i = 0; i < 11; i++) {
			block1Stories = new Block(1, globalIndex++, false, false, null, null, pd);
			this.blockRepository.save(block1Stories);
		}
	}

	public Block findById(Integer blockId) {
		return this.blockRepository.findById(blockId).orElse(null);
	}

	public Block save(Block block) {
		return this.blockRepository.save(block);
	}

	public Block findByPlayerAndIndex(PlayerDetails playerDetails, Integer blockIndex) {
		return this.blockRepository.findByPlayerAndIndex_(playerDetails, blockIndex).orElse(null);
	}

	public List<Block> getBlocksBySectorOrderedDesc(Sector sector) {
		return this.blockRepository.getBlocksBySectorOrderedDesc(sector);
	}

	public List<Block> getAllByGameId(int gameId) {
		return this.blockRepository.getAllByGameId(gameId);
	}

	public List<Block> getBlocksSelectedByPlayer(PlayerDetails player) {
		return this.blockRepository.findAllBlocksSelectedByPlayer(player);
	}
	
	public List<Block> getBlocksByGameAndCityIndex(Game game, int cityIndex) {
		return this.blockRepository.findBlocksByGameAndCityIndex(game, cityIndex);
	}

}
