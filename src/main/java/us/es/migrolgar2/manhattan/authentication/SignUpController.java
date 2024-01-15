package us.es.migrolgar2.manhattan.authentication;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import us.es.migrolgar2.manhattan.user.Authority;
import us.es.migrolgar2.manhattan.user.AuthorityService;
import us.es.migrolgar2.manhattan.user.User;
import us.es.migrolgar2.manhattan.user.UserService;

@Controller
public class SignUpController {
	
	private final PasswordEncoder passwordEncoder;
	
	private static final String VIEWS_SIGN_UP = "authentication/signUp";
	
	private final UserService userService;
	private final AuthorityService authorityService;

	public SignUpController(UserService userService, AuthorityService authorityService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.authorityService = authorityService;
		this.passwordEncoder = passwordEncoder;
	}
	
	@GetMapping(value = "/signup")
	public String signUp(Model model) {
		User user = new User();
		user.setEnabled(false);
		model.addAttribute("user", user);
		return VIEWS_SIGN_UP;
	}
	
	@PostMapping(value = "/signup")
	@Transactional
	public String processCreationForm(@ModelAttribute("User") @Valid User user, Model model, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_SIGN_UP;
		} else {
			
//			List<String> usernames = userService.findAll().stream().map(u -> u.getUsername()).collect(Collectors.toList());
//			if(usernames.contains(user.getUsername())) {
//				result.rejectValue("username", "ExistingUsername", "An user with that username already exists");
//				return VIEWS_SIGN_UP;
//			}

			user.setPassword(this.passwordEncoder.encode(user.getPassword()));
			user.setEnabled(true);
			user.setCreationDate(LocalDateTime.now());
			this.userService.save(user);
			
			Authority authority = new Authority(user, "User");
			this.authorityService.save(authority);
			
			return "redirect:/index";
		}
	}
	
	
}
