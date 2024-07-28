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

public class SmallInfoLabel extends Label {
	private static final String FONT_PATH = "/modelpng/kenvector_future.ttf";
	private static final String BACKGROUND_IMAGE = "/viewpng/buttonBlue.png";

	public SmallInfoLabel(String text) {
		setPrefHeight(30);
		setPrefWidth(130);
		BackgroundImage bgImg = new BackgroundImage(new Image(getClass().getResource(BACKGROUND_IMAGE).toExternalForm(), 130, 30, false, true),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
		setBackground(new Background(bgImg));
		setAlignment(Pos.CENTER_LEFT);
		setPadding(new Insets(10, 10, 10, 10));
		setLabelFont();
		setText(text);
	}

	private void setLabelFont() {
		try (InputStream fontStream = getClass().getResourceAsStream(FONT_PATH)) {
			setFont(Font.loadFont(fontStream, 10));
		} catch (Exception e) {
			System.out.println("Font file not found. Using default font \"Verdana\"");
			setFont(Font.font("Verdana", 15));
		}
	}
}
