package View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.InfoLabel;
import model.SHIP;
import model.ShipPicker;
import model.SpaceRunnerButton;
import model.SpaceRunnerSubScene;
import org.w3c.dom.Text;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ViewManager {
    private static final int HEIGHT = 768;
    private static final int WIDTH = 1024;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;

    private final static int MENU_BUTTONS_START_X = 100;
    private final static int MENU_BUTTONS_START_Y = 150;
    List<SpaceRunnerButton> menuButtons;


    private SpaceRunnerSubScene creditsSubScene;
    private SpaceRunnerSubScene helpSubScene;
    private SpaceRunnerSubScene scoreSubScene;
    private SpaceRunnerSubScene shipChooserSubScene;
    private SpaceRunnerSubScene sceneToHide;

    List<ShipPicker> shipsList;
    private SHIP choosenShip;

    public ViewManager() {
        menuButtons = new ArrayList<>();
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        createSubScene();
        createButtons();
        createBackground();
        // createBackgroundMusic();
        createLogo();
    }

    private void showSubScene(SpaceRunnerSubScene subScene){
        if(sceneToHide != null){
            sceneToHide.moveSubScene();
        }
        subScene.moveSubScene();
        sceneToHide = subScene;
    }

    private void createSubScene() {
        creditsSubScene = new SpaceRunnerSubScene();

        mainPane.getChildren().add(creditsSubScene);
        createCreditsContent();

        helpSubScene = new SpaceRunnerSubScene();
        mainPane.getChildren().add(helpSubScene);

        scoreSubScene = new SpaceRunnerSubScene();
        mainPane.getChildren().add(scoreSubScene);

        createShipChooserSubScene();
    }

    private void createShipChooserSubScene() {
        shipChooserSubScene = new SpaceRunnerSubScene();
        mainPane.getChildren().add(shipChooserSubScene);

        InfoLabel chooseShipLabel = new InfoLabel("CHOOSE YOUR SHIP");
        chooseShipLabel.setLayoutX(110);
        chooseShipLabel.setLayoutY(25);
        shipChooserSubScene.getPane().getChildren().add(chooseShipLabel);
        shipChooserSubScene.getPane().getChildren().add(createShipsToChoose());
        shipChooserSubScene.getPane().getChildren().add(createButtonToStart());
    }

    public Stage getMainStage(){
        return mainStage;
    }

    private HBox createShipsToChoose(){
        HBox box = new HBox();
        box.setSpacing(20);
        shipsList = new ArrayList<>();
        for(SHIP ship : SHIP.values()){
            ShipPicker shipToPick = new ShipPicker(ship);
            shipsList.add(shipToPick);
            box.getChildren().add(shipToPick);
            shipToPick.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    for (ShipPicker ship : shipsList){
                        ship.setIsCircleChosen(false);
                    }
                    shipToPick.setIsCircleChosen(true);
                    choosenShip = shipToPick.getShip();
                }
            });
        }
        box.setLayoutX(300 - (118*2));
        box.setLayoutY(100);
        return box;
    }

    private SpaceRunnerButton createButtonToStart(){
        SpaceRunnerButton startButton = new SpaceRunnerButton("START");
        startButton.setLayoutX(350);
        startButton.setLayoutY(300);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(choosenShip != null){
                    GameViewManager gameManager =new GameViewManager();
                    gameManager.createNewGame(mainStage, choosenShip);
                }
            }
        });

        return startButton;
    }

    private void createButtons() {
        createStartButton();
        createScoresButton();
        createHelpButton();
        createCreditsButton();
        createExitButton();
    }

    private void createStartButton(){
        SpaceRunnerButton startButton = new SpaceRunnerButton("PLAY");
        addMenuButton(startButton);

//	        String soundPath = getClass().getResource("view/resources/sounds/click-a.ogg").toString();
//	        Media clickSound = new Media(soundPath);
//	        MediaPlayer clickMediaPlayer = new MediaPlayer(clickSound);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//	            	clickMediaPlayer.play();
                showSubScene(shipChooserSubScene);
            }
        });
    }
    private void createScoresButton(){
        SpaceRunnerButton scoresButton = new SpaceRunnerButton("SCORES");
        addMenuButton(scoresButton);

        scoresButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSubScene(scoreSubScene);
            }
        });
    }
    private void createHelpButton(){
        SpaceRunnerButton helpButton = new SpaceRunnerButton("HELP");
        addMenuButton(helpButton);

        helpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSubScene(helpSubScene);
            }
        });
    }
    private void createCreditsButton(){
        SpaceRunnerButton creditsButton = new SpaceRunnerButton("CREDITS");
        addMenuButton(creditsButton);
        creditsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSubScene(creditsSubScene);
            }
        });
    }
    private void createCreditsContent() {
        Label title = new Label("Đồ án game");
        title.setFont(Font.font(35));
        title.setLayoutX(50);
        title.setLayoutY(50);
        title.getStyleClass().add("credit-label");

        Label lecturer = new Label("Giảng viên hướng dẫn:");
        lecturer.setFont(Font.font(20));
        lecturer.setLayoutX(50);
        lecturer.setLayoutY(120);
        lecturer.getStyleClass().add("credit-label");
        Label lecturer2 = new Label("      Ths.Nguyễn Thị Mỹ Hạnh");
        lecturer2.setFont(Font.font(20));
        lecturer2.setLayoutX(50);
        lecturer2.setLayoutY(160);
        lecturer2.getStyleClass().add("credit-label");

        Label studentLabel = new Label("Sinh viên thực hiện:");
        studentLabel.setFont(Font.font(20));
        studentLabel.setLayoutX(50);
        studentLabel.setLayoutY(200);
        studentLabel.getStyleClass().add("credit-label");
        Label student1 = new Label("      Nguyễn Minh Quân - 22DH112989");
        student1.setFont(Font.font(20));
        student1.setLayoutX(50);
        student1.setLayoutY(250);
        student1.getStyleClass().add("credit-label");

        Label student2 = new Label("      Hồ Quang Long - 22DH111985");
        student2.setFont(Font.font(20));
        student2.setLayoutX(50);
        student2.setLayoutY(280);
        student2.getStyleClass().add("credit-label");

        creditsSubScene.getPane().getChildren().addAll(title, lecturer, lecturer2, studentLabel, student1, student2);

        // Kiểm tra và thêm CSS vào scene
        URL cssResource = getClass().getResource("/application.css");
        if (cssResource != null) {
            String css = cssResource.toExternalForm();
            mainScene.getStylesheets().add(css);
        } else {
            System.err.println("Không tìm thấy tệp CSS: /application.css");
        }
    }




    private void createExitButton(){
        SpaceRunnerButton exitButton = new SpaceRunnerButton("EXIT");
        addMenuButton(exitButton);

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainStage.close();
            }
        });
    }



    private void createBackground() {
        Image backgroundImage = new Image(getClass().getResourceAsStream("/viewpng/deep_blue.png"),
                256,
                256,
                false,
                true);
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                null);
        mainPane.setBackground(new Background(background));
    }

    private void addMenuButton(SpaceRunnerButton button){
        button.setLayoutX(MENU_BUTTONS_START_X);
        button.setLayoutY(MENU_BUTTONS_START_Y + menuButtons.size() * 100);
        menuButtons.add(button);
        mainPane.getChildren().add(button);
    }

    private void createLogo(){
        ImageView logo = new ImageView(getClass().getResource("/viewpng/space_runner.png").toString());
        logo.setLayoutX(400);
        logo.setLayoutY(50);

        logo.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                logo.setEffect(new DropShadow());
            }
        });
        logo.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                logo.setEffect(null);
            }
        });
        mainPane.getChildren().add(logo);
    }
}
