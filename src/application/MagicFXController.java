package application;
import Traindata.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.plaf.synth.SynthSpinnerUI;

import javafx.animation.AnimationTimer;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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
	private Wizard tempWizard;
	private String value="";
	SVMTrainData mySVM=new SVMTrainData();

	private ScheduledExecutorService timer1;
	
	private EventController event=new EventController();
	private int delayTimeBox;
	private int delayTimeWizard;
	public static int testing1 = 0;
	//public static int testing2 = 0;
	//public double XposBox = Math.random()*935;
	private double XposWizard = Math.random()*935;
	private double count=0;
	Parent fxml;
	
	@FXML
    private ImageView wizard;
	// Save image //
	@FXML
	private Canvas canvas1;
	// Background only // 
	@FXML
	private  Canvas canvas2;
	@FXML
	private Slider volumeSlider;
	@FXML
	private Button setting;
	@FXML
	private AnchorPane content;
	@FXML
	private TextField textField;
	@FXML
	private Text idscore;
    @FXML
    private Pane contentArea;
    @FXML
    private Pane contentArea1;
    @FXML
    private Pane EndArea;
    @FXML
    private ImageView resume;
    @FXML
    private ImageView retry;
    @FXML
    private ImageView exit;

    @FXML
    private Text idscore1;


	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		contentArea1.setVisible(false);
		EndArea.setVisible(false);
		width = (int) canvas1.getWidth();
		height = (int) canvas1.getHeight();
		
		try {
			BACKGROUND = new Image(new FileInputStream("res/ground.png"));
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

		gc1.setFont(Font.font("Consolas",30));

		Matrix.setUpMatrix();
    
		gc1.drawImage(BACKGROUND, 0, 0);
   
		event.addWizard(XposWizard,200.0);
//		playSound();
//		adjustVolume();
	Runnable update=new Runnable() {
			@Override
			public void run() {
				if( testing1 == 0) {
				update();
//				if(testing2 == 1) {
//					contentArea.getChildren().remove(fxml);
//					testing2 = 0 ; 
//				  }
				}
			}
		};
		Runnable render=new Runnable() {
			@Override
			public void run() {
			
				render();
			}
			
		};
		
		timer1=Executors.newSingleThreadScheduledExecutor();
		timer1.scheduleAtFixedRate(update, 0, 10, TimeUnit.MILLISECONDS);
		timer1.scheduleAtFixedRate(render, 0, 10, TimeUnit.MILLISECONDS);
    delayTimeBox=(int)(Math.random()*500);

	}

	@FXML
	public void onMouseDragged(MouseEvent event) {

		gc1.lineTo(event.getX(), event.getY());
		gc1.stroke();

		gc2.lineTo(event.getX(), event.getY());
		gc2.stroke();
		
		if((int)event.getX()>20 && (int)event.getX()<width-20 && (int)event.getY()>20 && (int)event.getY()<height-20 ) {
			Matrix.fillOne((int)event.getY(), (int)event.getX());
		}
		
	}

	@FXML
	public void onMousePressed(MouseEvent event) {
		System.out.println("press");
		
		gc1.beginPath();
		gc1.moveTo(event.getX(), event.getY());
		gc1.stroke();

		gc2.beginPath();
		gc2.moveTo(event.getX(), event.getY());
		gc2.stroke();
		
		gc1.drawImage(BACKGROUND, 0, 0);
//		if(test.checkbutton == true) {
//			contentArea.getChildren().remove(fxml);
//			//testing2 = 0 ; 
//		  }
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
	//	mySVM.createHOG(Matrix.mat3);
//		System.out.println(mySVM.predict());
		gc1.drawImage(BACKGROUND, 0, 0);
		gc2.clearRect(0, 0, canvas2.getWidth(), canvas2.getHeight());
	}
	
	@FXML
	public void setValueText() {
		value=textField.getText();
		textField.setText("");
		System.out.println("action");
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
		
		event.fallBoxes();
		count++;
		if(count==delayTimeBox) {
			event.addBox(Math.random()*935, 0);
			delayTimeBox=(int)(Math.random()*500);
			count=0;
			System.out.println(event.box.size());
		}
		event.checkBoxApearence(this.value);
		updateScore();
		checkgame();
//		if (test.checkbutton == true ) {
//			contentArea.getChildren().remove(test);
//		}
		//while()
		//event.move();
		
	}
	
	public void render() {
		gc1.drawImage(BACKGROUND, 0, 0);
		event.drawBoxes(canvas1);
		event.drawWizardes(canvas1);
	}
	
	public void updateScore () {
		idscore.setText(" " + event.getScore());
		idscore1.setText(" " + event.getScore());
	}
	
	public void checkgame() {
		if (event.box.size()>0) {
			if (event.box.get(0).yPos>EnumSprite.HEIGHT.getValue()) {
				EndArea.setVisible(true);
				timer1.shutdown();
			}
		}
	}
//	public void restart() {
//		
//		Runnable update1=new Runnable() {
//			@Override
//			public void run() {
//				
//				update();
//				
//			}
//		};
//		Runnable render1=new Runnable() {
//			@Override
//			public void run() {
//				
//				render();
//				
//			}
//		};
//		timer1.shutdown();
//		timer1=Executors.newSingleThreadScheduledExecutor();
//		timer1.scheduleAtFixedRate(update1, 0, 10, TimeUnit.MILLISECONDS);
//		timer1.scheduleAtFixedRate(render1, 0, 10, TimeUnit.MILLISECONDS);
//	}

// 		canvas2.setOnKeyPressed(new EventHandler<KeyEvent>() {
// 			@Override
// 			public void handle(KeyEvent event) {
// 				switch(event.getCode())
// 				{
// 				case DIGIT1:
// 					eventController.remove(eventController.getOb1());
// 					System.out.println("KeyPress "+KeyCode.DIGIT1.toString());
// 					break;
// 				case DIGIT2:
// 					eventController.remove(eventController.getOb2());
// 					System.out.println("KeyPress "+KeyCode.DIGIT2.toString());
// 					break;
// 				case DIGIT3:
// 					eventController.remove(eventController.getOb3());
// 					System.out.println("KeyPress "+KeyCode.DIGIT3.toString());
// 					break;
// 				default:
// 					break;
// 				}
// 			}
// 		}
// 		);

	  @FXML
	    public void onMouseClickedSetting(MouseEvent event) {
		  System.out.println("test");
		  contentArea1.setVisible(true);
		  testing1 = 1 ;
//		  if (testing2 == 0 ) {
//
//		try {
//			fxml = FXMLLoader.load(getClass().getResource("pauseGame.fxml"));
//			contentArea.getChildren().removeAll();
//	        contentArea.getChildren().setAll(fxml);
//	        
//	        testing1 = 1;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		  }
//		  else {
//			contentArea.getChildren().remove(fxml);
//			testing2 = 0 ; 
//		  }
	  }
	  @FXML
	    public void onMouseExit(MouseEvent event) {
		  System.exit(0);
	    }
	  @FXML
	    public void onMouseRestart(MouseEvent event) {

	    }
	    @FXML
	    public void onMouseResume(MouseEvent event) {
	    	
	    	contentArea1.setVisible(false);
	    	testing1 = 0 ;
	    }
}