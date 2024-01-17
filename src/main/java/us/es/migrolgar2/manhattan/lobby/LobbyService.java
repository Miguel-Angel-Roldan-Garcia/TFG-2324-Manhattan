package us.es.migrolgar2.manhattan.lobby;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetailsService;
import us.es.migrolgar2.manhattan.user.FriendshipService;
import us.es.migrolgar2.manhattan.user.User;

@Service
@AllArgsConstructor
public class LobbyService {
	
	private LobbyRepository lobbyRepository;
	private PlayerDetailsService playerDetailsService;
	private FriendshipService friendshipService;
	
	public Lobby save(Lobby lobby) {
		return this.lobbyRepository.save(lobby);
	}

	public Set<Lobby> getLobbiesOwnedByFriends(List<String> friendsUsernames) {
		return this.lobbyRepository.getLobbiesOwnedByFriends(friendsUsernames);
	}

	public Lobby findById(int id) {
		return this.lobbyRepository.findById(id).orElse(null);
	}

	public Set<Lobby> getPublicLobbies() {
		return this.lobbyRepository.findAllPublicLobbies();
	}

	public Lobby findByName(String name) {
		return this.lobbyRepository.findByName(name).orElse(null);
	}
	
	@Transactional
	public Lobby addPlayer(Lobby lobby, User user) {
		Lobby retrievedLobby = this.findById(lobby.getId());
		
		// Check if Lobby has been altered in the meantime
		if(retrievedLobby.getVersion() > lobby.getVersion()) {
			lobby = retrievedLobby;
		}
		
		int nextFreePosition = this.getNextFreePosition(lobby.getId());
		
		if(nextFreePosition == 0) {
			// Lobby is full
			return null;
		}
		
		PlayerDetails playerDetails = this.playerDetailsService.createBlankPlayerDetails();
		playerDetails.setPosition(nextFreePosition);
		playerDetails.setUsername(user.getUsername());
		this.addPlayerAndSaveDetails(lobby, playerDetails);
		return lobby;
	}

	public Model loadLobbyList(Model model, String username) {
		List<String> friendsUsernames = this.friendshipService.findAllFriendsUsernames(username);
		Set<Lobby> friendsLobbies = this.getLobbiesOwnedByFriends(friendsUsernames);
		model.addAttribute("friendsLobbies", friendsLobbies);
		
		Set<Lobby> publicLobbies = this.getPublicLobbies();
		publicLobbies.removeAll(friendsLobbies);
		model.addAttribute("publicLobbies", publicLobbies);
		return model;
	}
	
	// -----------------------------------------------------------------------------------------
	
	public Integer getNextFreePosition(Integer lobbyId) {
		List<Integer> positions = List.of(1,2,3,4);
		Set<Integer> usedPositions = this.lobbyRepository.getAllLobbyPlayersPositions(lobbyId);
		
		for(Integer p:positions) {
			if(!usedPositions.contains(p)) {
				return p;
			}
		}
		
		return 0;
	}
	
	
	public void addPlayerAndSaveDetails(Lobby lobby, PlayerDetails playerDetails) {
		playerDetails.setLobby(lobby);
		this.playerDetailsService.save(playerDetails);
	}
	
	public List<PlayerDetails> getLobbyPlayersAsList(Integer lobbyId) {
		return this.lobbyRepository.getAllLobbyPlayers(lobbyId);
	}
	
	public boolean isUserInLobby(Integer lobbyId, String username) {
		List<String> playerUsernames = this.lobbyRepository.getAllLobbyPlayers(lobbyId)
										.stream()
										.map(pd -> pd.getUsername())
										.toList();
		return playerUsernames.contains(username);
	}
	
}
