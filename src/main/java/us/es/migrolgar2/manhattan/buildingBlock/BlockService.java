package us.es.migrolgar2.manhattan.buildingBlock;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;

@Service
@AllArgsConstructor
public class BlockService {

	private BlockRepository blockRepository;

	public void initializePlayerBlocks(PlayerDetails pd) {
		Block block4Stories;
		for(int i = 0; i < 3; i++) {
			block4Stories = new Block(4, false, false, null, null, pd);
			this.blockRepository.save(block4Stories);
		}
		
		Block block3Stories;
		for(int i = 0; i < 4; i++) {
			block3Stories = new Block(3, false, false, null, null, pd);
			this.blockRepository.save(block3Stories);
		}
		
		Block block2Stories;
		for(int i = 0; i < 6; i++) {
			block2Stories = new Block(2, false, false, null, null, pd);
			this.blockRepository.save(block2Stories);
		}
		
		Block block1Stories;
		for(int i = 0; i < 11; i++) {
			block1Stories = new Block(1, false, false, null, null, pd);
			this.blockRepository.save(block1Stories);
		}
	}
	
}
