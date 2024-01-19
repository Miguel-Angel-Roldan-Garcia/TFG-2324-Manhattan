package us.es.migrolgar2.manhattan.city;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import us.es.migrolgar2.manhattan.game.Game;

@Service
@AllArgsConstructor
public class CityService {
	
	private CityRepository cityRepository;
	
	public City save(City city) {
		return this.cityRepository.save(city);
	}

	public City findByGameAndIndex(Game game, Integer cityIndex) {
		return this.cityRepository.findByGameAndIndex_(game, cityIndex).orElse(null);
	}
	
}
