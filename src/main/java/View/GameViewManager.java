package View;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.SHIP;
import model.SmallInfoLabel;
import model.SpaceRunnerButton;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.layout.VBox;


public class GameViewManager {
	private AnchorPane gamePane;
	private Scene gameScene;
	private Stage gameStage;
	private Stage menuStage;
	private ImageView ship;
	private VBox pauseBox;
	private Button resumeButton;
	private Button restartButton;
	private Button quitButton;
	private Rectangle background;
	private boolean isLeftKeyPressed;
	private boolean isRightKeyPressed;
	private boolean isSpacePressed;
	private int angle;

	private GridPane gridPane1, gridPane2;
	private static final String BG_IMAGE = "/viewpng/purple.png";
	public static final String METEOR_BROWN = "/viewpng/index1.png";
	public static final String METEOR_GREY = "/viewpng/index2.png";
	private static final String BULLET = "/viewpng/missile.png";
	private AnimationTimer gameTimer;

	private static final int GAME_WIDTH = 600;
	private static final int GAME_HEIGHT = 700;

	private ImageView[] brownMeteors;
	private ImageView[] greyMeteors;
	private ArrayList<ImageView> bullets;
	private Random randomPosGen;

	private ImageView star;
	private ImageView life;
	private SmallInfoLabel pointsLabel;
	private ImageView[] playerLifes;
	private int playerLife = 3;
	private int points;
	public static final String GOLD_STAR = "/modelpng/star_gold.png";
	public static final String LIFE = "/viewpng/heart.png";
	private static final int STAR_RADIUS = 12;
	private static final int SHIP_RADIUS = 27;
	private static final int METEOR_RADIUS = 40;
	private static final int BULLET_RADIUS = 10;
	private SHIP chosenShip;
	private VBox gameOverBox;
	private Button restartGameButton;
	private Button backToMenuButton;

	private void createGameOverBox() {
		gameOverBox = new VBox(20);
		gameOverBox.setLayoutX(GAME_WIDTH / 2 - 100);
		gameOverBox.setLayoutY(GAME_HEIGHT / 2 - 75);
		gameOverBox.setVisible(false);

		restartGameButton = new SpaceRunnerButton("Restart");
		backToMenuButton = new SpaceRunnerButton("Main Menu");

		restartGameButton.setOnAction(event -> {
			gameTimer.stop();
			resetGame(chosenShip); // Chuyển chosenShip làm tham số nếu cần thiết
			gameTimer.start();
			gameOverBox.setVisible(false);
			background.setVisible(false); // Ẩn phần nền mờ
		});

		backToMenuButton.setOnAction(event -> {
			gameTimer.stop();
			gameStage.close();
			menuStage.show();
		});

		gameOverBox.getChildren().addAll(restartGameButton, backToMenuButton);

		// Tạo nền mờ cho hộp thoại Game Over
		background = new Rectangle(GAME_WIDTH, GAME_HEIGHT);
		background.setFill(Color.BLACK);
		background.setOpacity(0.5);
		background.setVisible(false);

		gamePane.getChildren().add(background);
		gamePane.getChildren().add(gameOverBox);

		// Hiển thị/hủy hiển thị nền khi gameOverBox được hiển thị/hủy hiển thị
		gameOverBox.visibleProperty().addListener((observable, oldValue, newValue) -> {
			background.setVisible(newValue);
		});
	}
	private void showGameOver() {
		gameTimer.stop();
		gameOverBox.setVisible(true);
	}

	public GameViewManager() {
		randomPosGen = new Random();
		initializeStage();
		createKeysListeners();
		bullets = new ArrayList<>();
	}

	private void createKeysListeners() {
		gameScene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.LEFT) {
				isLeftKeyPressed = true;
			} else if (event.getCode() == KeyCode.RIGHT) {
				isRightKeyPressed = true;
			}
			if (event.getCode() == KeyCode.SPACE) {
				isSpacePressed = true;
				fireBullet();
			}
			if (event.getCode() == KeyCode.ESCAPE) {
				togglePause();
			}
		});

		gameScene.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.LEFT) {
				isLeftKeyPressed = false;
			} else if (event.getCode() == KeyCode.RIGHT) {
				isRightKeyPressed = false;
			}
			if (event.getCode() == KeyCode.SPACE) {
				isSpacePressed = false;
			}
		});
	}

	// Phương thức để chuyển đổi trạng thái pause
	private void togglePause() {
		if (pauseBox.isVisible()) {
			gameTimer.start();
			pauseBox.setVisible(false);
		} else {
			gameTimer.stop();
			pauseBox.setVisible(true);
		}
	}


	private void initializeStage() {
		gamePane = new AnchorPane();
		gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
		gameStage = new Stage();
		gameStage.setTitle("Space Runner");
		gameStage.setScene(gameScene);
		gameStage.setResizable(false);
		gameStage.setOnCloseRequest(x -> {
			x.consume();
			gameStage.close();
			menuStage.show();
		});
	}

	public void createNewGame(Stage menuStage, SHIP chosenShip) {
		this.menuStage = menuStage;
		this.menuStage.hide();
		this.chosenShip = chosenShip; // Lưu chosenShip
		createBG();
		createShip(chosenShip);
		createGameElements(chosenShip);
		createPauseBox(); // Gọi phương thức tạo hộp thoại pause
		createGameOverBox(); // Gọi phương thức tạo hộp thoại Game Over
		createGameLoop();
		gameStage.show();
	}




	private void createGameElements(SHIP chosenShip) {
		star = new ImageView(new Image(getClass().getResource(GOLD_STAR).toExternalForm()));
		setNewElementPos(star);

		life = new ImageView(new Image(getClass().getResource(LIFE).toExternalForm()));
		setNewElementPos(life);

		pointsLabel = new SmallInfoLabel("Points:  00");
		pointsLabel.setLayoutX(460);
		gamePane.getChildren().addAll(star, life, pointsLabel);

		playerLifes = new ImageView[playerLife];
		for (int i = 0; i < playerLife; i++) {
			playerLifes[i] = new ImageView(new Image(getClass().getResource(chosenShip.getUrlLife()).toExternalForm()));
			playerLifes[i].setLayoutX(455 + (i * 40));
			playerLifes[i].setLayoutY(50);
			gamePane.getChildren().add(playerLifes[i]);
		}

		brownMeteors = new ImageView[3];
		for (int i = 0; i < brownMeteors.length; i++) {
			brownMeteors[i] = new ImageView(new Image(getClass().getResource(METEOR_BROWN).toExternalForm()));
			setNewElementPos(brownMeteors[i]);
			gamePane.getChildren().add(brownMeteors[i]);
		}
		greyMeteors = new ImageView[3];
		for (int i = 0; i < greyMeteors.length; i++) {
			greyMeteors[i] = new ImageView(new Image(getClass().getResource(METEOR_GREY).toExternalForm()));
			setNewElementPos(greyMeteors[i]);
			gamePane.getChildren().add(greyMeteors[i]);
		}
	}

	private void setNewElementPos(ImageView image) {
		image.setLayoutX(randomPosGen.nextInt(GAME_WIDTH));
		image.setLayoutY(-(randomPosGen.nextInt(3200) + 600));
	}

	private void moveElements() {
		star.setLayoutY(star.getLayoutY() + 5);
		life.setLayoutY(life.getLayoutY() + 5);

		for (ImageView bullet : bullets) {
			bullet.setLayoutY(bullet.getLayoutY() - 10);
		}

		for (int i = 0; i < brownMeteors.length; i++) {
			brownMeteors[i].setLayoutY(brownMeteors[i].getLayoutY() + 7);
			brownMeteors[i].setRotate(brownMeteors[i].getRotate() + 4 * i);
		}
		for (int i = 0; i < greyMeteors.length; i++) {
			greyMeteors[i].setLayoutY(greyMeteors[i].getLayoutY() + 7);
			greyMeteors[i].setRotate(greyMeteors[i].getRotate() + 5 + i);
		}
	}


	private void checkElementsPos() {
		if (star.getLayoutY() > GAME_HEIGHT) {
			setNewElementPos(star);
		}
		if (life.getLayoutY() > GAME_HEIGHT) {
			setNewElementPos(life);
		}

		for (int i = 0; i < brownMeteors.length; i++) {
			if (brownMeteors[i].getLayoutY() > GAME_HEIGHT) {
				setNewElementPos(brownMeteors[i]);
			}
		}
		for (int i = 0; i < greyMeteors.length; i++) {
			if (greyMeteors[i].getLayoutY() > GAME_HEIGHT) {
				setNewElementPos(greyMeteors[i]);
			}
		}

		// Remove bullets that have gone off-screen
		ArrayList<ImageView> bulletsToRemove = new ArrayList<>();
		for (ImageView bullet : bullets) {
			if (bullet.getLayoutY() < 0) {
				bulletsToRemove.add(bullet);
			}
		}
		bullets.removeAll(bulletsToRemove);
		gamePane.getChildren().removeAll(bulletsToRemove);
	}


	private void createShip(SHIP chosenShip) {
		ship = new ImageView(new Image(getClass().getResource(chosenShip.getUrlShip()).toExternalForm()));
		double shipWidth = ship.getImage().getWidth();
		ship.setLayoutX((GAME_WIDTH - shipWidth) / 2); // Đặt thuyền ngay giữa màn hình
		ship.setLayoutY(GAME_HEIGHT - 90);
		gamePane.getChildren().add(ship);
	}


	private void createBG() {
		gridPane1 = new GridPane();
		gridPane2 = new GridPane();

		for (int i = 0; i < 12; i++) {
			ImageView bgImage1 = new ImageView(new Image(getClass().getResource(BG_IMAGE).toExternalForm()));
			ImageView bgImage2 = new ImageView(new Image(getClass().getResource(BG_IMAGE).toExternalForm()));
			GridPane.setConstraints(bgImage1, i % 3, i / 3);
			GridPane.setConstraints(bgImage2, i % 3, i / 3);
			gridPane1.getChildren().add(bgImage1);
			gridPane2.getChildren().add(bgImage2);
		}
		gridPane2.setLayoutY(-1024);

		gamePane.getChildren().addAll(gridPane1, gridPane2);
	}

	private void moveBG() {
		gridPane1.setLayoutY(gridPane1.getLayoutY() + 3);
		gridPane2.setLayoutY(gridPane2.getLayoutY() + 3);

		if (gridPane1.getLayoutY() >= 1024) {
			gridPane1.setLayoutY(-1024);
		}
		if (gridPane2.getLayoutY() >= 1024) {
			gridPane2.setLayoutY(-1024);
		}
	}

	private void fireBullet() {
		ImageView bullet = new ImageView(new Image(getClass().getResource(BULLET).toExternalForm()));
		bullet.setLayoutX(ship.getLayoutX() + 50);
		bullet.setLayoutY(ship.getLayoutY());
		bullets.add(bullet);
		gamePane.getChildren().add(bullet);
	}


	private void createGameLoop() {
		gameTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				moveShip();
				moveBG();
				moveElements();
				checkElementsPos();
				checkIfElementsAreBehindTheShip();
				checkIfElementsCollide();
			}
		};
		gameTimer.start();
	}

	private void moveShip() {
		if (isLeftKeyPressed && !isRightKeyPressed) {
			if (angle > -30) {
				angle -= 5;
			}
			ship.setRotate(angle);
			if (ship.getLayoutX() > -20) {
				ship.setLayoutX(ship.getLayoutX() - 3);
			}
		}
		if (isRightKeyPressed && !isLeftKeyPressed) {
			if (angle < 30) {
				angle += 5;
			}
			ship.setRotate(angle);
			if (ship.getLayoutX() < 522) {
				ship.setLayoutX(ship.getLayoutX() + 3);
			}
		}
		if (!isLeftKeyPressed && !isRightKeyPressed) {
			if (angle < 0) {
				angle += 5;
			} else if (angle > 0) {
				angle -= 5;
			}
			ship.setRotate(angle);
		}
		if (isLeftKeyPressed && isRightKeyPressed) {
			if (angle < 0) {
				angle += 5;
			} else if (angle > 0) {
				angle -= 5;
			}
			ship.setRotate(angle);
		}
	}

	private void checkIfElementsAreBehindTheShip() {
		if (SHIP_RADIUS + STAR_RADIUS > calculateDistance(ship.getLayoutX() + 49, star.getLayoutX() + 15,
				ship.getLayoutY() + 37, star.getLayoutY() + 15)) {
			setNewElementPos(star);
			points++;
			String textToSet = "POINTS : ";
			if (points < 10) {
				textToSet = textToSet + "0";
			}
			pointsLabel.setText(textToSet + points);
		}

		if (SHIP_RADIUS + METEOR_RADIUS > calculateDistance(ship.getLayoutX() + 49, life.getLayoutX() + 20,
				ship.getLayoutY() + 37, life.getLayoutY() + 20)) {
			setNewElementPos(life);
			if (playerLife < 3) {
				playerLife++;
				ImageView newLife = new ImageView(new Image(getClass().getResource(chosenShip.getUrlLife()).toExternalForm()));
				newLife.setLayoutX(455 + (playerLife - 1) * 40);
				newLife.setLayoutY(50);
				playerLifes[playerLife - 1] = newLife;
				gamePane.getChildren().add(newLife);
			}
		}
	}


	private void checkIfElementsCollide() {
		double shipCenterX = ship.getLayoutX() + ship.getImage().getWidth() / 2;
		double shipCenterY = ship.getLayoutY() + ship.getImage().getHeight() / 2;

		for (ImageView meteor : brownMeteors) {
			double meteorCenterX = meteor.getLayoutX() + meteor.getImage().getWidth() / 2;
			double meteorCenterY = meteor.getLayoutY() + meteor.getImage().getHeight() / 2;

			if (SHIP_RADIUS + METEOR_RADIUS > calculateDistance(shipCenterX, meteorCenterX,
					shipCenterY, meteorCenterY)) {
				removeLife();
				setNewElementPos(meteor);
			}

			for (ImageView bullet : bullets) {
				double bulletCenterX = bullet.getLayoutX() + bullet.getImage().getWidth() / 2;
				double bulletCenterY = bullet.getLayoutY() + bullet.getImage().getHeight() / 2;

				if (BULLET_RADIUS + METEOR_RADIUS > calculateDistance(bulletCenterX, meteorCenterX,
						bulletCenterY, meteorCenterY)) {
					setNewElementPos(meteor);
					bullets.remove(bullet);
					gamePane.getChildren().remove(bullet);
					points++;
					pointsLabel.setText("POINTS : " + points);
					break;
				}
			}
		}

		for (ImageView meteor : greyMeteors) {
			double meteorCenterX = meteor.getLayoutX() + meteor.getImage().getWidth() / 2;
			double meteorCenterY = meteor.getLayoutY() + meteor.getImage().getHeight() / 2;

			if (SHIP_RADIUS + METEOR_RADIUS > calculateDistance(shipCenterX, meteorCenterX,
					shipCenterY, meteorCenterY)) {
				removeLife();
				setNewElementPos(meteor);
			}

			for (ImageView bullet : bullets) {
				double bulletCenterX = bullet.getLayoutX() + bullet.getImage().getWidth() / 2;
				double bulletCenterY = bullet.getLayoutY() + bullet.getImage().getHeight() / 2;

				if (BULLET_RADIUS + METEOR_RADIUS > calculateDistance(bulletCenterX, meteorCenterX,
						bulletCenterY, meteorCenterY)) {
					setNewElementPos(meteor);
					bullets.remove(bullet);
					gamePane.getChildren().remove(bullet);
					points++;
					pointsLabel.setText("POINTS : " + points);
					break;
				}
			}
		}
	}





	private void removeLife() {
		gamePane.getChildren().remove(playerLifes[playerLife - 1]);
		playerLife--;
		if (playerLife == 0) {
			showGameOver(); // Hiển thị hộp thoại Game Over
		}
	}

	private double calculateDistance(double x1, double x2, double y1, double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	private void resetGame(SHIP chosenShip) {
		gamePane.getChildren().clear(); // Xóa tất cả các phần tử trong gamePane
		bullets.clear(); // Xóa tất cả các viên đạn
		points = 0; // Đặt lại điểm số
		playerLife = 3; // Đặt lại số mạng
		pointsLabel.setText("Points:  00"); // Đặt lại điểm số hiển thị
		angle = 0; // Đặt lại góc quay của tàu

		createBG();
		createShip(chosenShip);
		createGameElements(chosenShip);
		gamePane.getChildren().add(background); // Thêm lại nền mờ
		gamePane.getChildren().add(pauseBox); // Thêm lại hộp thoại pause nếu cần
		gamePane.getChildren().add(gameOverBox); // Thêm lại hộp thoại game over nếu cần
		pauseBox.setVisible(false); // Đảm bảo hộp thoại pause bị ẩn
		gameOverBox.setVisible(false); // Đảm bảo hộp thoại game over bị ẩn
		background.setVisible(false); // Đảm bảo phần nền mờ bị ẩn
	}
	private void createPauseBox() {
		pauseBox = new VBox(20);
		pauseBox.setLayoutX(GAME_WIDTH / 2 - 100);
		pauseBox.setLayoutY(GAME_HEIGHT / 2 - 75);
		pauseBox.setVisible(false);

		resumeButton = new SpaceRunnerButton("Resume");
		restartButton = new SpaceRunnerButton("Restart");
		quitButton = new SpaceRunnerButton("Quit");

		resumeButton.setOnAction(event -> {
			gameTimer.start();
			pauseBox.setVisible(false);
		});

		restartButton.setOnAction(event -> {
			gameTimer.stop();
			resetGame(chosenShip); // Chuyển chosenShip làm tham số nếu cần thiết
			gameTimer.start();
			pauseBox.setVisible(false);
		});


		quitButton.setOnAction(event -> {
			gameTimer.stop();
			gameStage.close();
			menuStage.show();
		});

		pauseBox.getChildren().addAll(resumeButton, restartButton, quitButton);

		// Tạo nền mờ cho hộp thoại pause
		Rectangle background = new Rectangle(GAME_WIDTH, GAME_HEIGHT);
		background.setFill(Color.BLACK);
		background.setOpacity(0.5);
		background.setVisible(false);

		gamePane.getChildren().add(background);
		gamePane.getChildren().add(pauseBox);

		// Hiển thị/hủy hiển thị nền khi pauseBox được hiển thị/hủy hiển thị
		pauseBox.visibleProperty().addListener((observable, oldValue, newValue) -> {
			background.setVisible(newValue);
		});
	}


}
