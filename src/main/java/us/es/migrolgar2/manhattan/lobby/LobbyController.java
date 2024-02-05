package us.es.migrolgar2.manhattan.lobby;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import us.es.migrolgar2.manhattan.chat.ChatMessage;
import us.es.migrolgar2.manhattan.game.Game;
import us.es.migrolgar2.manhattan.game.GameService;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetailsService;
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
			if(lobby.hasPassword()) {
				lobby.setPassword(this.passwordEncoder.encode(lobby.getPassword()));
			}
			
			lobby = this.lobbyService.save(lobby);
			
			User user = this.userService.findByUsername(principal.getName());
			PlayerDetails playerDetails = this.playerDetailsService.createBlankPlayerDetails();
			playerDetails.setPosition(1);
			playerDetails.setUsername(user.getUsername());
			playerDetails.setLobby(lobby);
			playerDetails.setLobbyOwner(true);
			playerDetails = this.playerDetailsService.save(playerDetails);
			
		}
		
		return "redirect:/lobby/" + lobby.getId();
	}
	
	@GetMapping("/lobby/{id}")
	public String getLobby(@PathVariable("id") int id, Model model, Principal principal) {
		Lobby lobby = this.lobbyService.findById(id);
		
		if(lobby == null || !lobby.isAvailable()) {
			return "redirect:/index";
		}
		List<PlayerDetails> players = this.lobbyService.getLobbyPlayersAsList(lobby.getId());
		
		// TODO Check if player actually is in that lobby/game
		if(players.size() > 0 && players.get(0).getGame() != null) {
			return "redirect:/game/" + players.get(0).getGame().getId();
		}
		
		model.addAttribute("lobby", lobby);
		
		for(PlayerDetails pd:players) {
			model.addAttribute("player" + pd.getPosition(), pd);
			if(pd.getUsername().equals(principal.getName())) {
				model.addAttribute("principal", pd);
			}
		}
		
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
				if(this.lobbyService.isUserInLobby(retrievedLobby.getId(), principal.getName())) {
					if(retrievedLobby.isAvailable()) {
						return "redirect:/lobby/" + retrievedLobby.getId();
					} else {
						Game game = this.playerDetailsService.findByUsernameAndLobbyId(principal.getName(), retrievedLobby.getId()).getGame();
						return "redirect:/game/" + game.getId();
					}
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
						this.simpMessagingTemplate
							.convertAndSend("/lobby/" + retrievedLobby.getId() + "/lobbyReload", 
											this.lobbyService.getLobbyPlayersAsList(retrievedLobby.getId())
										   );
						return "redirect:/lobby/" + retrievedLobby.getId();
					}
				}
			}
		}
		
		model = this.lobbyService.loadLobbyList(model, principal.getName());
		return LOBBY_LIST;
	}
	
	// Lobby web server messaging methods
	@MessageMapping("/lobby/{lobbyId}/start")
	public void getStartGame(@DestinationVariable("lobbyId") int id, Principal principal) {
		String responseMsg = "";
		
		Lobby lobby = this.lobbyService.findById(id);
		if(lobby == null || !lobby.isAvailable()) {
			return; 
		} else {
			PlayerDetails owner = this.playerDetailsService.findByUsernameAndLobbyId(principal.getName(), id);
			if(owner == null || !owner.isLobbyOwner()) {
				responseMsg = "Solo el dueño de la sala puede empezar la partida.";
			}
			
			List<PlayerDetails> players = this.lobbyService.getLobbyPlayersAsList(lobby.getId());
			if(players.size() != 4) {
				responseMsg = "No hay suficientes jugadores para empezar la partida.";
			}
			
			Set<Color> selectedColors = new HashSet<Color>();
			for(PlayerDetails playerDetails:players) {
				if(!playerDetails.isReady()) {
					responseMsg = "Todavía hay algún jugador que no está listo.";
					break;
				} else if(selectedColors.contains(playerDetails.getColor())) {
					responseMsg = "Uno o más jugadores tienen seleccionado el mismo color.";
					break;
				} else {
					selectedColors.add(playerDetails.getColor());
				}
			}
			
			if(responseMsg.isBlank()) {
				lobby.setAvailable(false);
				lobby = this.lobbyService.save(lobby);
				
				Game game = new Game();
				game.setRoundNumber(1);
				game.setTurnNumber(1);
				game.setRoundPlaying(false);
				game.setStartDate(LocalDateTime.now());
				game = this.gameService.save(game);
				
				for(PlayerDetails playerDetails:players) {
					playerDetails.setGame(game);
					this.playerDetailsService.save(playerDetails);
				}
				
				this.gameService.initializeGame(game);
				
				responseMsg = "start-" + game.getId();
			}
		}
		
		// TODO if player numbers is < 4, introduce AI player details
		
		this.simpMessagingTemplate.convertAndSend("/lobby/" + id + "/start", responseMsg);
	}
	
	@MessageMapping("/lobby/{lobbyId}/chat")
	@SendTo("/lobby/{lobbyId}/chat")
	public ChatMessage relayChatMessage(@DestinationVariable int lobbyId, @Payload ChatMessage msg, Principal principal) throws Exception {
		return new ChatMessage(String.format("%s: %s", principal.getName(), msg.getMsg()));
	}
	
	@MessageMapping("/lobby/{lobbyId}/change-color/{position}")
	public void changeColor(@DestinationVariable("lobbyId") int lobbyId, @DestinationVariable("position") int position, Principal principal) throws Exception {
		PlayerDetails pd = this.playerDetailsService.findByPositionAndLobbyId(position, lobbyId);
		
		if(pd != null && pd.getUsername().equals(principal.getName())) {
			pd.setColor(pd.getColor().getNext());
			this.playerDetailsService.save(pd);
			
			this.simpMessagingTemplate.convertAndSend("/lobby/" + lobbyId + "/lobbyReload", 
							this.lobbyService.getLobbyPlayersAsList(lobbyId));
		}
		
	}
	
	@MessageMapping("/lobby/{lobbyId}/toggle-ready/{position}")
	public void toggleReady(@DestinationVariable("lobbyId") int lobbyId, @DestinationVariable("position") int position, Principal principal) throws Exception {
		PlayerDetails pd = this.playerDetailsService.findByPositionAndLobbyId(position, lobbyId);
		
		if(pd != null && pd.getUsername().equals(principal.getName())) {
			pd.setReady(!pd.isReady());
			this.playerDetailsService.save(pd);
			
			this.simpMessagingTemplate.convertAndSend("/lobby/" + lobbyId + "/lobbyReload", 
							this.lobbyService.getLobbyPlayersAsList(lobbyId));
		}
		
	}
	
	@MessageMapping("/lobby/{lobbyId}/abandon/{position}")
	public void abandon(@DestinationVariable("lobbyId") int lobbyId, @DestinationVariable("position") int position, Principal principal) throws Exception {
		PlayerDetails principalPD = this.playerDetailsService.findByUsernameAndLobbyId(principal.getName(), lobbyId);
		PlayerDetails pd = this.playerDetailsService.findByPositionAndLobbyId(position, lobbyId);
		
		if(pd != null && (principalPD.isLobbyOwner() || pd.getUsername().equals(principal.getName()))) {
			Lobby lobby = this.lobbyService.findById(lobbyId);
			if(lobby.isAvailable()) {
				playerDetailsService.delete(pd);
				
				if(pd.isLobbyOwner()) {
					List<PlayerDetails> players = this.lobbyService.getLobbyPlayersAsList(lobbyId);
					
					if(players.size() == 0) {
						this.lobbyService.delete(lobbyId);
					} else {
						players.get(0).setLobbyOwner(true);
						this.playerDetailsService.save(players.get(0));
					}
			}
				
				this.simpMessagingTemplate.convertAndSend("/lobby/" + lobbyId + "/lobbyReload", 
								this.lobbyService.getLobbyPlayersAsList(lobbyId));
			}
		}
		
	}
	
}
