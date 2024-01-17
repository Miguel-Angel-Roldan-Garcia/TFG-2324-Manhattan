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
		
		int nextFreePosition = lobby.getNextFreePosition();
		
		if(nextFreePosition == 0) {
			// Lobby is full
			return null;
		}
		
		PlayerDetails playerDetails = this.getAndSavePlayerDetails(nextFreePosition, user);
		lobby.addPlayer(playerDetails);
		lobby = this.save(lobby);
		return lobby;
	}

	private PlayerDetails getAndSavePlayerDetails(int position, User user) {
		PlayerDetails playerDetails = this.playerDetailsService.createBlankPlayerDetails();
		playerDetails.setPosition(position);
		playerDetails.setUsername(user.getUsername());
		playerDetails = this.playerDetailsService.save(playerDetails);
		return playerDetails;
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
	
}
