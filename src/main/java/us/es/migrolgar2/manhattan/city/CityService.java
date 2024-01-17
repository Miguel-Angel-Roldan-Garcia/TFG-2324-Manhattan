package us.es.migrolgar2.manhattan.city;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CityService {
	
	private CityRepository cityRepository;
	
	public City save(City city) {
		return this.cityRepository.save(city);
	}
	
}
