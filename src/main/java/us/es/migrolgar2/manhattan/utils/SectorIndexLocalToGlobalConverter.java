package us.es.migrolgar2.manhattan.utils;

import java.util.Map;

public class SectorIndexLocalToGlobalConverter {
	
	private static final Map<Integer, Integer> position2Map = Map.of(
			1, 3,
			2, 6,
			3, 9,
			4, 2,
			5, 5,
			6, 8,
			7, 1,
			8, 4,
			9, 7
			);
			
	private static final Map<Integer, Integer> position3Map = Map.of(
			1, 9,
			2, 8,
			3, 7,
			4, 6,
			5, 5,
			6, 4,
			7, 3,
			8, 2,
			9, 1
			);
	
	private static final Map<Integer, Integer> position4Map = Map.of(
			1, 7,
			2, 4,
			3, 1,
			4, 8,
			5, 5,
			6, 2,
			7, 9,
			8, 6,
			9, 3
			);
	
	
	public static Integer convertIndex(Integer index, Integer position) {
		switch(position) {
		case 1:
			return index;
		case 2:
			return position2Map.get(index);
		case 3:
			return position3Map.get(index);
		case 4:
			return position4Map.get(index);
		default:
			throw new IllegalArgumentException("Unexpected position value: " + position);
		}
	}
}
