
const position2Map = {
			1: 7,
			2: 4,
			3: 1,
			4: 8,
			5: 5,
			6: 2,
			7: 9,
			8: 6,
			9: 3
	  };
	  
const position3Map = {
			1: 9,
			2: 8,
			3: 7,
			4: 6,
			5: 5,
			6: 4,
			7: 3,
			8: 2,
			9: 1
	  };
	  
const position4Map = {
			1: 3,
			2: 6,
			3: 9,
			4: 2,
			5: 5,
			6: 8,
			7: 1,
			8: 4,
			9: 7
	  };
	
export default function convertIndexToLocal(index, position) {
	switch(position) {
	case 1:
		return index;
	case 2:
		return position2Map[index];
	case 3:
		return position3Map[index];
	case 4:
		return position4Map[index];
	}
}
