package application;
import Traindata.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import javafx.animation.AnimationTimer;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;

public class MagicFXController implements Initializable {

	public int width;
	public int height;
	private GraphicsContext gc1, gc2;
	private Image BACKGROUND;
	private WritableImage wImage;
	private File file = new File("res/white.png");
	private String musicFile = "res/Theme.mp3";
	private MediaPlayer mediaPlayer;
	private Media sound;
	private Box tempBox;
	SVMTrainData mySVM=new SVMTrainData();
	private ScheduledExecutorService timer;
	// Save image //
	@FXML
	private Canvas canvas1;
	// Background only // 
	@FXML
	private Canvas canvas2;
	@FXML
	private Slider volumeSlider;
	@FXML
	private Button setting;
	@FXML
	private AnchorPane content;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		width = (int) canvas1.getWidth();
		height = (int) canvas1.getHeight();
		
		try {
			BACKGROUND = new Image(new FileInputStream("res/background.jpg"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		wImage = new WritableImage(width, height);

		gc1 = canvas1.getGraphicsContext2D();
		gc1.setStroke(Color.RED);
		gc1.setLineWidth(5);

		gc2 = canvas2.getGraphicsContext2D();
		gc2.setStroke(Color.BLACK);
		gc2.setLineWidth(15);

		gc1.drawImage(BACKGROUND, 0, 0);
//		playSound();
//		adjustVolume();
		
		Runnable update=new Runnable() {
			@Override
			public void run() {
				update();
			}
		};
		Runnable render=new Runnable() {
			@Override
			public void run() {
				render();
			}
		};
		timer=Executors.newSingleThreadScheduledExecutor();
		timer.scheduleAtFixedRate(update, 0, 10, TimeUnit.MILLISECONDS);
		timer.scheduleAtFixedRate(render, 0, 10, TimeUnit.MILLISECONDS);

	}

	@FXML
	public void onMouseDragged(MouseEvent event) {
		gc1.lineTo(event.getX(), event.getY());
		gc1.stroke();

		gc2.lineTo(event.getX(), event.getY());
		gc2.stroke();
	}

	@FXML
	public void onMousePressed(MouseEvent event) {
		gc1.beginPath();
		gc1.moveTo(event.getX(), event.getY());
		gc1.stroke();
		
		gc2.beginPath();
		gc2.moveTo(event.getX(), event.getY());
		gc2.stroke();
	}

	@FXML
	public void onMouseReleased(MouseEvent event) {
		canvas2.snapshot(null, wImage);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(wImage, null), "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Matrix.setUpMatrix();
		Matrix.resizeMatrix();
		mySVM.createHOG(Matrix.mat3);
//		System.out.println(mySVM.predict());
		
		gc1.drawImage(BACKGROUND, 0, 0);
		gc2.clearRect(0, 0, canvas2.getWidth(), canvas2.getHeight());
	}
	
	public void playSound() {
		sound = new Media(new File(musicFile).toURI().toString());
		mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}
	public void adjustVolume() {
		volumeSlider.setValue(mediaPlayer.getVolume() * 100);
		volumeSlider.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable ob) {
				mediaPlayer.setVolume(volumeSlider.getValue() / 100);
			}
		});
	}
	
	public void update() {
		System.out.println("123");
	}
	
	public void render() {
		System.out.println("asdfjlasdkfj");
	}
}
