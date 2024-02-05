package us.es.migrolgar2.manhattan.index;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {
	
	@GetMapping("/about")
	public String getIndex(Model model) {
		return "about";
	}
	
	@GetMapping("/how-to-play")
	public String getHowToPlay(Model model) {
		return "howToPlay";
	}

}
