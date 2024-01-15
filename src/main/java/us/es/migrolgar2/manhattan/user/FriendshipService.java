package us.es.migrolgar2.manhattan.user;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class FriendshipService {
	
	private FriendshipRepository friendshipRepository;
	
	public FriendshipService(FriendshipRepository friendshipRepository) {
		this.friendshipRepository = friendshipRepository;
	}
	
	public List<String> findAllFriendsUsernames(String username) {
		return this.friendshipRepository.findAllFriendsUsernames(username);
	}
	
}
