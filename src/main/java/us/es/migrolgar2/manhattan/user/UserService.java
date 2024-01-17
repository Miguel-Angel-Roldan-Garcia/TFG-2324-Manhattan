package us.es.migrolgar2.manhattan.user;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

@Service
public class UserService {

	private UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findByUsername(String name) {
		return this.userRepository.findByUsername(name).orElse(null);
	}
	
	public void save(@Valid User user) {
		this.userRepository.save(user);
	}
	
}
