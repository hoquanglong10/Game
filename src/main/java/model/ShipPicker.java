package model;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ShipPicker extends VBox {

	private static final String CIRCLE_NOT_CHOSEN_URL = "/modelpng/grey_circle.png";
	private static final String CIRCLE_CHOSEN_URL = "/modelpng/missile.png";
	private static final String[] SHIP_URLS = {
			"/viewpng/shipchooser/ships/ship_1.png",
			"/viewpng/shipchooser/ships/ship_2.png",
			"/viewpng/shipchooser/ships/ship_3.png",
			"/viewpng/shipchooser/ships/ship_4.png"
	};

	private ImageView circleImage;
	private ImageView shipImage;

	private SHIP ship;
	private boolean isCircleChosen;

	public ShipPicker(SHIP ship) {
		this.ship = ship;
		this.isCircleChosen = false;

		// Debug statements
		System.out.println("CIRCLE_NOT_CHOSEN_URL: " + CIRCLE_NOT_CHOSEN_URL);
		System.out.println("CIRCLE_CHOSEN_URL: " + CIRCLE_CHOSEN_URL);
		System.out.println("ship URL: " + ship.getUrlShip());

		this.circleImage = new ImageView(loadImage(CIRCLE_NOT_CHOSEN_URL));
		this.shipImage = new ImageView(loadImage(ship.getUrlShip()));

		this.setAlignment(Pos.CENTER);
		this.setSpacing(20);
		this.getChildren().addAll(circleImage, shipImage);
	}

	public SHIP getShip() {
		return ship;
	}

	public boolean isCircleChosen() {
		return isCircleChosen;
	}

	public void setIsCircleChosen(boolean isCircleChosen) {
		this.isCircleChosen = isCircleChosen;
		String imageToSet = this.isCircleChosen ? CIRCLE_CHOSEN_URL : CIRCLE_NOT_CHOSEN_URL;
		circleImage.setImage(loadImage(imageToSet));
	}

	private Image loadImage(String path) {
		// Debug statements
		System.out.println("Loading image: " + path);
		var resourceStream = getClass().getResourceAsStream(path);
		if (resourceStream == null) {
			System.out.println("Resource not found: " + path);
			throw new IllegalArgumentException("Resource not found: " + path);
		}
		return new Image(resourceStream);
	}
}
