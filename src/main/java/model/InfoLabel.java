package model;

import java.io.InputStream;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;

public class InfoLabel extends Label {
	public final static String FONT_PATH = "/modelpng/kenvector_future.ttf";
	public final static String BG_IMAGE = "/modelpng/yellow_button.png"; // Ensure the correct extension

	public InfoLabel(String text) {
		setPrefWidth(600);
		setPrefHeight(49);
		setPadding(new Insets(10, 200, 40, 0));
		setText(text);
		setWrapText(true);
		setLabelFont();
		setAlignment(Pos.CENTER);

		BackgroundImage bgImage = new BackgroundImage(new Image(getClass().getResourceAsStream(BG_IMAGE), 380, 50, false, true),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

		setBackground(new Background(bgImage));
	}

	private void setLabelFont() {
		try (InputStream fontStream = getClass().getResourceAsStream(FONT_PATH)) {
			if (fontStream != null) {
				setFont(Font.loadFont(fontStream, 25));
			} else {
				System.out.println("Font file not found. Using default font.");
				setFont(Font.font("Verdana", 25));
			}
		} catch (Exception e) {
			System.out.println("Could not load font. Using defaults...");
			setFont(Font.font("Verdana", 25));
		}
	}
}
