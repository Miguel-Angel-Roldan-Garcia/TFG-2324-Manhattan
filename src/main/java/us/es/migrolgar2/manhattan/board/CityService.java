package us.es.migrolgar2.manhattan.board;

import org.springframework.stereotype.Service;

@Service
public class CityService {
	
	private CityRepository cityRepository;
	
	public CityService(CityRepository cityRepository) {
		this.cityRepository = cityRepository;
	}
	
	public void save(City city) {
		this.cityRepository.save(city);
	}
	
}
