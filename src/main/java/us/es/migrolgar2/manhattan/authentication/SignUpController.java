package us.es.migrolgar2.manhattan.authentication;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
		SignUpForm userForm = new SignUpForm();
		model.addAttribute("signUpForm", userForm);
		return VIEWS_SIGN_UP;
	}
	
	@PostMapping(value = "/signup")
	@Transactional
	public String processCreationForm(@Valid SignUpForm userForm, BindingResult result, Model model) {
		if(System.getenv("MANHATTAN_ACCESS_CODE") != null && !this.passwordEncoder.matches(userForm.getAccessCode(), System.getenv("MANHATTAN_ACCESS_CODE"))) {
			result.rejectValue("accessCode", "WrongAccessCode", "El código de acceso es incorrecto");
		}
		
		if(this.userService.findByUsername(userForm.getUsername()) != null) {
			result.rejectValue("username", "ExistingUsername", "Ya existe un usuario con ese nombre de usuario.");
		}
		
		if (result.hasErrors()) {
			return VIEWS_SIGN_UP;
		} else {
			User user = new User();
			user.setUsername(userForm.getUsername());
			user.setPassword(this.passwordEncoder.encode(userForm.getPassword()));
			user.setEnabled(true);
			user.setCreationDate(LocalDateTime.now());
			this.userService.save(user);
			
			Authority authority = new Authority(user, "User");
			this.authorityService.save(authority);
			
			return "redirect:/index";
		}
	}
	
	
}
