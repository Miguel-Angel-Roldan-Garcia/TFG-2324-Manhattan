package us.es.migrolgar2.manhattan.authentication;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	
	private static final String VIEWS_SIGN_UP = "authentication/signUp";
	
	private final UserService userService;
	private final AuthorityService authorityService;

	@Autowired
	public SignUpController(UserService userService, AuthorityService authorityService) {
		this.userService = userService;
		this.authorityService = authorityService;
	}
	
	@GetMapping(value = "/signup")
	public String signUp(Map<String, Object> model) {
		return VIEWS_SIGN_UP;
	}
	
	@PostMapping(value = "/signup")
	@Transactional
	public String processCreationForm(@ModelAttribute("User") @Valid User user, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_SIGN_UP;
		} else {
			
//			List<String> usernames = userService.findAll().stream().map(u -> u.getUsername()).collect(Collectors.toList());
//			if(usernames.contains(user.getUsername())) {
//				result.rejectValue("username", "ExistingUsername", "An user with that username already exists");
//				return VIEWS_SIGN_UP;
//			}
			
			user.setEnabled(true);
			this.userService.save(user);
			
			Authority authority = new Authority(user, "User");
			this.authorityService.save(authority);
			
			return "redirect:/index";
		}
	}
	
	
}
