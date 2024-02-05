package us.es.migrolgar2.manhattan.utils;

import java.util.Random;

import lombok.Getter;

@Getter
public enum Color {
	BLUE("#3e59f8"),
	RED("#eb0000"),
	GREEN("#00fe00"),
	YELLOW("#ffff01"),
	CYAN("#00ffff"),
	PURPLE("#bd2bbb"),
	GREY("#797979"),
	ORANGE("#f99200");
	
	private final String colorCode;

	Color(String colorCode) {
		this.colorCode = colorCode;
	} 
	
	public Color getNext() {
		switch(this) {
		case BLUE:
			return Color.RED;
		case RED:
			return Color.GREEN;
		case GREEN:
			return Color.YELLOW;
		case YELLOW:
			return Color.CYAN;
		case CYAN:
			return Color.PURPLE;
		case PURPLE:
			return Color.GREY;
		case GREY:
			return Color.ORANGE;
		case ORANGE:
			return Color.BLUE;
		default:
			return null;
		}
	}
	
	public static Color pickRandom() {
		Random random = new Random();
		int randomIndex = random.nextInt(Color.values().length);
		return Color.values()[randomIndex];
	}
}
