package us.es.migrolgar2.manhattan.lobby;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import us.es.migrolgar2.manhattan.game.Game;
import us.es.migrolgar2.manhattan.game.GameService;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetailsService;
import us.es.migrolgar2.manhattan.user.User;
import us.es.migrolgar2.manhattan.user.UserService;

@Controller
@AllArgsConstructor
public class LobbyController {
	
	private static final String LOBBY_LIST = "lobby/list";
	private static final String LOBBY_NEW = "lobby/new";
	private static final String LOBBY_SHOW = "lobby/show";

	private LobbyService lobbyService;
	private UserService userService;
	private PlayerDetailsService playerDetailsService;
	private GameService gameService;
	private PasswordEncoder passwordEncoder;
	private final SimpMessagingTemplate simpMessagingTemplate;
	
	@GetMapping("/lobby/list")
	public String getLobbyList(Model model, Principal principal) {
		String username = principal.getName();
		
		model = this.lobbyService.loadLobbyList(model, username);
		
		Lobby lobby = new Lobby();
		model.addAttribute("lobby", lobby);
		
		return LOBBY_LIST;
	}
	
	@GetMapping("/lobby/new")
	public String getLobbyNew(Model model) {
		LobbyPrivacyStatus[] privacyStatuses = LobbyPrivacyStatus.values();
		model.addAttribute("attributeStatuses", privacyStatuses);
		
		Lobby lobby = new Lobby();
		lobby.setName("");
		lobby.setPrivacyStatus(LobbyPrivacyStatus.PUBLIC);
		model.addAttribute("lobby", lobby);
		return LOBBY_NEW;
	}
	
	@PostMapping("/lobby/new")
	public String postLobbyNew(@Valid Lobby lobby, BindingResult bindingResult, Model model, Principal principal) {
		if(this.lobbyService.findByName(lobby.getName()) != null) {
			bindingResult.rejectValue("name", "", "A lobby with this name already exists.");
		}
		
		if (bindingResult.hasErrors()) {
			LobbyPrivacyStatus[] privacyStatuses = LobbyPrivacyStatus.values();
			model.addAttribute("attributeStatuses", privacyStatuses);
			return LOBBY_NEW;
		} else {
			User user = this.userService.findByUsername(principal.getName());
			PlayerDetails playerDetails = this.playerDetailsService.createBlankPlayerDetails();
			playerDetails.setPosition(1);
			playerDetails.setUsername(user.getUsername());
			playerDetails = this.playerDetailsService.save(playerDetails);
			
			if(lobby.hasPassword()) {
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
	
	@GetMapping("/lobby/join")
	public String getJoinLobby() {
		return "redirect:/lobby/list";
	}
	
	@PostMapping("/lobby/join")
	public String postJoinLobby(Lobby lobby, Principal principal, Model model, BindingResult bindingResult) {
		if(lobby.getName() == null || lobby.getName().isBlank()) {
			bindingResult.rejectValue("name", "", "A name is required.");
		} else {
			Lobby retrievedLobby = this.lobbyService.findByName(lobby.getName());
			
			if(retrievedLobby == null) {
				String notFoundMsg = String.format("We couldn't found a lobby with name '%s'", lobby.getName());
				bindingResult.rejectValue("name", "", notFoundMsg);
				
			} else {
				if(retrievedLobby.isUserInLobby(principal.getName())) {
					return "redirect:/lobby/" + retrievedLobby.getId();
				}
				
				String passwordMatchError = null;
				if(retrievedLobby.hasPassword()) {
					if(!lobby.hasPassword()) {
						passwordMatchError = "A password is required.";
					} else {
						if(!this.passwordEncoder.matches(lobby.getPassword(), retrievedLobby.getPassword())) {
							passwordMatchError = "The password is incorrect.";
						}
					}
				}
				
				if(passwordMatchError != null) {
					bindingResult.rejectValue("password", "", passwordMatchError);
				} else {
					User user = this.userService.findByUsername(principal.getName());
					retrievedLobby = this.lobbyService.addPlayer(retrievedLobby, user);
					
					if(retrievedLobby == null) {
						bindingResult.rejectValue("", "", "The lobby is full");
					} else {
						retrievedLobby.setPassword(null);
						this.simpMessagingTemplate.convertAndSend("/lobby/" + retrievedLobby.getId() + "/lobbyReload", retrievedLobby);
						return "redirect:/lobby/" + retrievedLobby.getId();
					}
				}
			}
		}
		
		model = this.lobbyService.loadLobbyList(model, principal.getName());
		return LOBBY_LIST;
	}
	
	@Transactional
	@GetMapping("/lobby/{id}/start")
	public String getStartGame(@PathVariable("id") int id, Model model) {
		// TODO Check if players are ready
		
		Lobby lobby = this.lobbyService.findById(id);
		Game game = new Game();
		game.setStartDate(LocalDateTime.now());
		game = this.gameService.save(game);
		
		for(PlayerDetails playerDetails:lobby.getPlayersAsList()) {
			playerDetails.setGame(game);
			this.playerDetailsService.save(playerDetails);
		}
		
		// TODO if player numbers is < 4, introduce AI player details
		
		this.gameService.initializeGame(game);
		
		return "redirect:/game/" + game.getId();
	}
	
}
