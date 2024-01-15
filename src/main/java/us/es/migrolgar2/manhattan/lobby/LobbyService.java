package us.es.migrolgar2.manhattan.lobby;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class LobbyService {
	
	private LobbyRepository lobbyRepository;
	
	public LobbyService(LobbyRepository lobbyRepository) {
		this.lobbyRepository = lobbyRepository;
	}
	
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
	
}
