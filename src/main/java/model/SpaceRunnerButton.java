package model;

import java.io.InputStream;
import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class SpaceRunnerButton extends Button {
    private final String FONT_PATH = "/modelpng/kenvector_future.ttf";
    private final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('" + getClass().getResource("/modelpng/yellow_button_pressed.png") + "')";
    private final String BUTTON_FREE_STYLE = "-fx-background-color: transparent; -fx-background-image: url('" + getClass().getResource("/modelpng/yellow_button.png") + "')";

    public SpaceRunnerButton(String text) {
        setText(text);
        setButtonFont();
        setPrefWidth(190);
        setPrefHeight(49);
        setStyle(BUTTON_FREE_STYLE);
        initializeButtonListeners();
    }

    private void setButtonFont() {
        try (InputStream fontStream = getClass().getResourceAsStream(FONT_PATH)) {
            if (fontStream != null) {
                setFont(Font.loadFont(fontStream, 23));
            } else {
                setFont(Font.font("Verdana", 23));
            }
        } catch (Exception e) {
            setFont(Font.font("Verdana", 23));
        }
    }

    private void setButtonPressedStyle() {
        setStyle(BUTTON_PRESSED_STYLE);
        setPrefHeight(45);
        setLayoutY(getLayoutY() + 4);
    }

    private void setButtonReleasedStyle() {
        setStyle(BUTTON_FREE_STYLE);
        setPrefHeight(49);
        setLayoutY(getLayoutY() - 4);
    }

    private void initializeButtonListeners() {
        setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                setButtonPressedStyle();
            }
        });

        setOnMouseReleased(event -> setButtonReleasedStyle());

        setOnMouseEntered(event -> setEffect(new javafx.scene.effect.DropShadow()));

        setOnMouseExited(event -> setEffect(null));
    }
}
