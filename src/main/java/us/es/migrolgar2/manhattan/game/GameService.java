package us.es.migrolgar2.manhattan.game;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import us.es.migrolgar2.manhattan.buildingBlock.BlockService;
import us.es.migrolgar2.manhattan.buildingCard.CardService;
import us.es.migrolgar2.manhattan.city.City;
import us.es.migrolgar2.manhattan.city.CityService;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetailsService;
import us.es.migrolgar2.manhattan.sector.Sector;
import us.es.migrolgar2.manhattan.sector.SectorService;
import us.es.migrolgar2.manhattan.user.UserService;

@Service
@AllArgsConstructor
public class GameService {
	
	private GameRepository gameRepository;
	private PlayerDetailsService playerDetailsService;
	private UserService userService;
	private BlockService blockService;
	private CardService cardService;
	private CityService cityService;
	private SectorService sectorService;
	
	public Game save(Game game) {
		return this.gameRepository.save(game);
	}

	@Transactional
	public void initializeGame(Game game) {
		for(int i = 1; i <= 6; i++) {
			City city = new City(i, game);
			city = this.cityService.save(city);
			
			for(int j = 1; j <= 9; j++) {
				Sector sector = new Sector(j, city);
				this.sectorService.save(sector);
			}
		}
		
		List<PlayerDetails> gamePlayers = this.gameRepository.getGamePlayerDetails(game);
		
		Comparator<PlayerDetails> c = Comparator.comparing(pd -> this.userService.findByUsername(pd.getUsername()).getAge());
		PlayerDetails olderPlayer = Collections.max(gamePlayers, c);
		olderPlayer.setPlaying(true);
		olderPlayer = this.playerDetailsService.save(olderPlayer);
		
		this.cardService.initializeCards(game);
		
		for(PlayerDetails pd:gamePlayers) {
			this.blockService.initializePlayerBlocks(pd);
			this.cardService.drawPlayer4Cards(pd);
		}
	}
	
	public void finishGame(Game game) {
		game.setFinishDate(LocalDateTime.now());
		this.save(game);
	}
}
