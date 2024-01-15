package us.es.migrolgar2.manhattan.lobby;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetailsService;
import us.es.migrolgar2.manhattan.user.FriendshipService;
import us.es.migrolgar2.manhattan.user.User;
import us.es.migrolgar2.manhattan.user.UserService;
import us.es.migrolgar2.manhattan.utils.Color;

@Controller
@AllArgsConstructor
public class LobbyController {
	
	private static final String LOBBY_LIST = "lobby/list";
	private static final String LOBBY_NEW = "lobby/new";
	private static final String LOBBY_SHOW = "lobby/show";

	private LobbyService lobbyService;
	private FriendshipService friendshipService;
	private UserService userService;
	private PlayerDetailsService playerDetailsService;
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/lobby/list")
	public String getLobbyList(Model model, Principal principal) {
		String username = principal.getName();
		
		List<String> friendsUsernames = friendshipService.findAllFriendsUsernames(username);
		Set<Lobby> friendsLobbies = this.lobbyService.getLobbiesOwnedByFriends(friendsUsernames);
		model.addAttribute("friendsLobbies", friendsLobbies);
		
		Set<Lobby> publicLobbies = this.lobbyService.getPublicLobbies();
		publicLobbies.removeAll(friendsLobbies);
		model.addAttribute("publicLobbies", publicLobbies);
		
		return LOBBY_LIST;
	}
	
	@GetMapping("/lobby/new")
	public String getLobbyNew(Model model) {
		LobbyPrivacyStatus[] privacyStatuses = LobbyPrivacyStatus.values();
		model.addAttribute("attributeStatuses", privacyStatuses);
		
		Lobby lobby = new Lobby();
		lobby.setName("");
		lobby.setPrivacyStatus(LobbyPrivacyStatus.PRIVATE);
		model.addAttribute("lobby", lobby);
		return LOBBY_NEW;
	}
	
	@PostMapping("/lobby/new")
	public String postLobbyNew(@Valid Lobby lobby, Model model, Principal principal, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return LOBBY_NEW;
		} else {
			User user = this.userService.findByUsername(principal.getName());
			PlayerDetails playerDetails = new PlayerDetails();
			playerDetails.setScore(0);
			playerDetails.setReady(false);
			playerDetails.setColor(Color.pickRandom());
			playerDetails.setPosition(1);
			playerDetails.setPlaying(false);
			playerDetails.setGame(null);
			playerDetails.setUser(user);
			playerDetails = this.playerDetailsService.save(playerDetails);
			
			if(!(lobby.getPassword() == null) && !lobby.getPassword().isBlank()) {
				lobby.setPassword(this.passwordEncoder.encode(lobby.getPassword()));
			}
			
			lobby.setOwner(playerDetails);
			lobby = this.lobbyService.save(lobby);
		}
		return "redirect:/lobby/" + lobby.getId();
	}
	
	@GetMapping("/lobby/{id}")
	public String getLobby(@PathVariable("id") int id, Model model) {
		Lobby lobby = this.lobbyService.findById(id);
		
		if(lobby == null) {
			return "redirect:/index";
		}
		
		model.addAttribute("lobby", lobby);
		
		return LOBBY_SHOW;
	}
	
}
