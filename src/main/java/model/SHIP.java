package model;

public enum SHIP {
	BLUE("/viewpng/shipchooser/ships/ship_1.png", "/viewpng/shipchooser/playerLife_blue.png"),
	GREEN("/viewpng/shipchooser/ships/ship_2.png", "/viewpng/shipchooser/playerLife_green.png"),
	ORANGE("/viewpng/shipchooser/ships/ship_3.png", "/viewpng/shipchooser/playerLife_orange.png"),
	RED("/viewpng/shipchooser/ships/ship_4.png", "/viewpng/shipchooser/playerLife_red.png");

	private final String urlShip;
	private final String urlLife;

	SHIP(String urlShip, String urlLife) {
		this.urlShip = urlShip;
		this.urlLife = urlLife;
	}

	public String getUrlShip() {
		return urlShip;
	}

	public String getUrlLife() {
		return urlLife;
	}
}
