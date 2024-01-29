package us.es.migrolgar2.manhattan.game;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import us.es.migrolgar2.manhattan.block.Block;
import us.es.migrolgar2.manhattan.block.BlockService;
import us.es.migrolgar2.manhattan.card.Card;
import us.es.migrolgar2.manhattan.card.CardService;
import us.es.migrolgar2.manhattan.city.City;
import us.es.migrolgar2.manhattan.city.CityService;
import us.es.migrolgar2.manhattan.exceptions.BlockAlreadySelectedOrPlacedException;
import us.es.migrolgar2.manhattan.exceptions.NotOwnedException;
import us.es.migrolgar2.manhattan.exceptions.PlayerHasAlreadySelectedBlocks;
import us.es.migrolgar2.manhattan.game.messages.SelectBlocksMessage;
import us.es.migrolgar2.manhattan.game.messages.TurnMessage;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetailsService;
import us.es.migrolgar2.manhattan.sector.Sector;
import us.es.migrolgar2.manhattan.sector.SectorService;
import us.es.migrolgar2.manhattan.user.UserService;
import us.es.migrolgar2.manhattan.utils.SectorIndexLocalToGlobalConverter;

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
		
		Integer globalBlockIndex = 1;
		for(PlayerDetails pd:gamePlayers) {
			this.blockService.initializePlayerBlocks(pd, globalBlockIndex);
			this.cardService.drawPlayer4Cards(pd);
		}
	}
	
	public void finishGame(Game game) {
		game.setFinishDate(LocalDateTime.now());
		this.save(game);
	}

	public Game findById(int gameId) {
		return this.gameRepository.findById(gameId).orElse(null);
	}

	public boolean isUserInGame(int gameId, String username) {
		Game game = this.gameRepository.findById(gameId).orElse(null);
		
		if(game == null)
			return false;
		
		PlayerDetails playerDetails = this.playerDetailsService.findByUsernameAndGame(username, game);
		
		if(playerDetails == null)
			return false;
		
		return true;
	}
	
	@Transactional
	public void selectBlocksByPlayerAndIndexes(String username, Integer gameId, Integer[] blockIndexes) throws BlockAlreadySelectedOrPlacedException, NotOwnedException, PlayerHasAlreadySelectedBlocks {
		Game game = this.findById(gameId);
		PlayerDetails playerDetails = this.playerDetailsService.findByUsernameAndGame(username, game);
		
		if(playerDetails.isHasSelectedBlocks()) {
			throw new PlayerHasAlreadySelectedBlocks();
		}
		
		for(Integer blockIndex:blockIndexes) {
			Block block = this.blockService.findByPlayerAndIndex(playerDetails, blockIndex);
			
			if(!block.getPlayer().getUsername().equals(username)) {
				throw new NotOwnedException();
			}
			
			if(block.isSelected() || block.isPlaced()) {
				throw new BlockAlreadySelectedOrPlacedException();
			}
			
			block.setSelected(true);
			this.blockService.save(block);
		}
		
		playerDetails.setHasSelectedBlocks(true);
		this.playerDetailsService.save(playerDetails);
	}

	@Transactional
	public TurnMessage playTurn(int gameId, String username, TurnMessage msg) {
		// Is message valid and owned by the principal?
		if(!msg.isValid() || !msg.isOwnedBy(username)) {
			return null;
		}
		
		// Is a round ongoing?
		Game game = this.findById(gameId);
		if(!game.isRoundPlaying()) {
			return null;
		}
		
		// Is it the principal's turn?
		PlayerDetails principalDetails = this.playerDetailsService.findByUsernameAndGame(msg.getUsername(), game);
		if(!principalDetails.isPlaying()) {
			return null;
		}
		
		// Is the used card drawn by the principal?
		Card card = this.cardService.findByPlayerAndIndex(principalDetails, msg.getPlayedCardIndex());
		if(!card.isDrawn()) {
			return null;
		}
		
		// Is the block owned by principal, selected and not already placed?
		Block block = this.blockService.findByPlayerAndIndex(principalDetails, msg.getPlacedBlockIndex());
		if(block.isPlaced() || !block.isSelected() || !block.getPlayer().equals(principalDetails)) {
			return null;
		}
		
		// Is the sector selected the correct place defined by card?
		Integer sectorIndex = SectorIndexLocalToGlobalConverter.convertIndex(card.getSectorIndex(), principalDetails.getPosition());
//		if(sectorIndex != msg.getSectorIndex()) {
//			return null;
//		}
		
		// Does the placing comply with skyscraper rules?
		City city = this.cityService.findByGameAndIndex(game, msg.getCityIndex());
		Sector sector = this.sectorService.findByCityAndIndex(city, sectorIndex);
		List<Block> sectorBlocks = this.blockService.getBlocksBySectorOrderedDesc(sector);
		
		if(!sectorBlocks.isEmpty()) {
			PlayerDetails sectorOwner = sectorBlocks.get(0).getPlayer();
			if(!sectorOwner.equals(principalDetails)) {
				Integer ownerStories = sectorBlocks.stream()
												   		 .filter(b -> b.getPlayer().equals(sectorOwner))
												   		 .mapToInt(b -> b.getSize())
												   		 .sum();
				
				Integer storiesAdded = block.getSize();
				Integer principalStories = sectorBlocks.stream()
						   						   	   .filter(b -> b.getPlayer().equals(principalDetails))
						   						   	   .mapToInt(b -> b.getSize())
						   						   	   .sum();
				if(principalStories + storiesAdded < ownerStories) {
					return null;
				}
			}
		}
		
		// Turn valid, proceed with the turn processing
		// Card being used
		card.setUsed(true);
		card.setPlayer(null);
		card = this.cardService.save(card);
		
		// Block being placed
		block.setOrder_(sectorBlocks.size() + 1);
		block.setPlaced(true);
		block.setSector(sector);
		block.setSelected(false);
		block = this.blockService.save(block);
		
		// Draw a card for the principal
		Card drawnCard = this.cardService.drawPlayer1Card(principalDetails);
		msg.setDrawnCardIndex(drawnCard.getIndex_());
		
		// Set the principal to not be playing
		principalDetails.setPlaying(false);
		this.playerDetailsService.save(principalDetails);
		
		// Set the user who is playing next
		Integer nextPosition = (principalDetails.getPosition() % 4) + 1;
		List<PlayerDetails> players = this.gameRepository.getGamePlayerDetails(game);
		for(PlayerDetails pd:players) {
			if(pd.getPosition() == nextPosition) {
				pd.setPlaying(true);
				this.playerDetailsService.save(pd);
				break;
			}
		}
		
		// Set the message's sector index to the global one
		msg.setSectorIndex(sectorIndex);
		
		// Update game's round and turn count
		if(game.getRoundNumber() == 4 && game.getTurnNumber() == 24) {
			// Game is finished 
			game.setFinishDate(LocalDateTime.now());
		} else if(game.getTurnNumber() == 24) {
			// Round is finished
			game.setRoundPlaying(false);
			game.setTurnNumber(1);
			game.setRoundNumber(game.getRoundNumber() + 1);
			
			// Prompt players to select blocks again
			players = this.gameRepository.getGamePlayerDetails(game);
			for(PlayerDetails pd:players) {
				pd.setHasSelectedBlocks(false);
				this.playerDetailsService.save(pd);
			}
			
			// Calculate round points
			this.calculateRoundPoints(game);
			
		} else {
			// Advance to next turn
			game.setTurnNumber(game.getTurnNumber() + 1);
		}
		this.save(game);
		
		return msg;
	}

	@Transactional
	private void calculateRoundPoints(Game game) {
		Map<PlayerDetails, Integer> pointsToAdd = new HashMap<PlayerDetails, Integer>();
		List<PlayerDetails> players = this.gameRepository.getGamePlayerDetails(game);
		for(PlayerDetails pd:players) {
			pointsToAdd.put(pd, 0);
		}
		
		// Currently using order, while story height should've been used
		
		// Tallest building (+3 points)
		Integer tallestBuildingHeight = 0;
		PlayerDetails tallestBuildingOwner = null;
		boolean duplicateTallestBlockHeight = false;
		
		for(City city:this.cityService.getAllByGame(game)) {
			List<Sector> sectors = this.sectorService.getSectorsByCity(city);
			Map<PlayerDetails, Integer> buildingsOwnedByPlayer = new HashMap<PlayerDetails, Integer>();
			
			for(Sector sector:sectors) {
				List<Block> blocks = this.blockService.getBlocksBySectorOrderedDesc(sector);
				
				if(blocks.isEmpty()) {
					continue;
				}
				
				Integer sectorHeight = 0;
				for(Block b:blocks) {
					sectorHeight += b.getSize();
				}
				
				Block tallestBlock = blocks.get(0);
				
				// Keep track of if the tallest building's height is a tie
				if(sectorHeight > tallestBuildingHeight) {
					tallestBuildingHeight = sectorHeight;
					tallestBuildingOwner = tallestBlock.getPlayer();
					duplicateTallestBlockHeight = false;
				} else if(sectorHeight == tallestBuildingHeight) {
					duplicateTallestBlockHeight = true;
				}
				
				// Owner of the building gets +1
				pointsToAdd.put(tallestBlock.getPlayer(), pointsToAdd.get(tallestBlock.getPlayer()) + 1);
				
				// Keep track of owned buildings within the city
				if(buildingsOwnedByPlayer.containsKey(tallestBlock.getPlayer())) {
					buildingsOwnedByPlayer.put(tallestBlock.getPlayer(), buildingsOwnedByPlayer.get(tallestBlock.getPlayer()) + 1);
				} else {
					buildingsOwnedByPlayer.put(tallestBlock.getPlayer(), 1);
				}
			}
			
			// Check if there are any ties of owned buildings number
			PlayerDetails ownerWithMostBuildings = null;
			boolean duplicateOwnedBuildings = true; // True so it fails if map is empty
			for(Entry<PlayerDetails, Integer> e:buildingsOwnedByPlayer.entrySet()) {
				if(ownerWithMostBuildings == null || 
				   buildingsOwnedByPlayer.get(ownerWithMostBuildings) > e.getValue()) {
					ownerWithMostBuildings = e.getKey();
					duplicateOwnedBuildings = false;
				} else if(buildingsOwnedByPlayer.get(ownerWithMostBuildings) == e.getValue()) {
					duplicateOwnedBuildings = true;
				}
			}
			
			// If there are no duplicates, player gets +2
			if(!duplicateOwnedBuildings) {
				pointsToAdd.put(ownerWithMostBuildings, pointsToAdd.get(ownerWithMostBuildings) + 2);
			}
		}
		
		// If there are no duplicates on tallest building, player gets +3
		if(!duplicateTallestBlockHeight) {
			pointsToAdd.put(tallestBuildingOwner, pointsToAdd.get(tallestBuildingOwner) + 3);
		}
		
		// Adding the points and saving players
		for(Entry<PlayerDetails, Integer> pd:pointsToAdd.entrySet()) {
			pd.getKey().setScore(pd.getKey().getScore() + pd.getValue());
			this.playerDetailsService.save(pd.getKey());
		}
		
	}

	public void checkIfAllPlayersHaveSelectedBlocks(Game game) {
		List<PlayerDetails> players = this.gameRepository.getGamePlayerDetails(game);
		
		boolean res = true;
		for(int i = 0; i < players.size() && res; i++) {
			res = players.get(i).isHasSelectedBlocks();
		}
		
		if(res) {
			game.setRoundPlaying(true);
			this.save(game);
		}
	}

	public Map<String, Object> getGameData(int gameId) {
		Game game = this.findById(gameId);
		if(game == null || game.isFinished()) {
			return null;
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("game", game);
		data.put("players", this.gameRepository.getGamePlayerDetails(game));
		data.put("cards", this.cardService.getAllByGameId(gameId));
		data.put("blocks", this.blockService.getAllByGameId(gameId));
		
		return data;
	}

	public SelectBlocksMessage getSelectedBlocksMsg(Integer gameId, String username) {
		Game game = this.findById(gameId);
		PlayerDetails playerDetails = this.playerDetailsService.findByUsernameAndGame(username, game);
		
		Integer[] blocks = this.blockService.getBlocksSelectedByPlayer(playerDetails)
								 .stream()
								 .mapToInt(b -> b.getIndex_())
								 .boxed()
								 .toArray(Integer[]::new);
		
		return new SelectBlocksMessage(username, blocks);
	}
	
}
