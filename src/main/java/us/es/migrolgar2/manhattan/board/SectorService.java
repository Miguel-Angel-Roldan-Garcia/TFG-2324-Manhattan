package us.es.migrolgar2.manhattan.board;

import org.springframework.stereotype.Service;

@Service
public class SectorService {

	private SectorRepository sectorRepository;
	
	public SectorService(SectorRepository sectorRepository) {
		this.sectorRepository = sectorRepository;
	}
	
	public void save(Sector sector) {
		this.sectorRepository.save(sector);
	}
	
}
