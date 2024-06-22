package io.github.isaatonimov.midiKeys;

import animatefx.animation.FadeIn;
import animatefx.animation.RollIn;
import animatefx.animation.Shake;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;


public class KeysController implements Initializable
{
	public String MyKeymap = "1234567890ß´qwertzuiopü+asdfghjklöä#<yxcvbnm,.-";
	public String CurrentSentence = "";

	public MidiController midiController;
	public StepSequencer stepSequencer;
	public ComboController ComboController;
	public Scene MainScene;

	private boolean isMaximized = false;
	public boolean isMouseActive = false;

	public Slider VelocitySlider;
	public Slider OctaveSlider;

	private int midiKey = 72;

	private double lastY = 0;
	private Random random = new Random();
	@FXML
	public Button PadButton;

	@FXML
	public ImageView logo;
	@FXML
	public Label KeyLabel;
	@FXML
	public AnchorPane RootPane;
	@FXML
	public FlowPane MasterFlowPane;
	@FXML
	public Button PlusButton;
	@FXML
	public Button MinusButton;
	@FXML
	public Button FullButton;
	@FXML
	public Stage MainStage;

	@FXML
	public Circle BulbRed;


	@FXML
	public Circle BulbGreen;

	private Paint bulbRedDefault;
	private Paint bulbGreenDefault;

	private double lastX;

	//
	//Positions keys at bottom of screen,
	//if given 0 -> maximized
	public void positionKeys(double WindowHeight)
	{


		playSound("click2.wav");

		//Screen Position Information
		var Bounds = Screen.getPrimary().getVisualBounds();

		MainStage.setWidth(Bounds.getWidth());

		if(WindowHeight == 0)
		{
			isMaximized = true;
			MainStage.setHeight(Bounds.getHeight());
			MainStage.centerOnScreen();
		}
		else
		{
			isMaximized = false;

			MainStage.setHeight(WindowHeight);

			MainStage.setY(Bounds.getMaxY() - WindowHeight);
			MainStage.setX(0);
		}
	}

	public void SwitchToMaximized()
	{
		FullButton.setText("Min");
		positionKeys(0);
	}

	public void SwitchToBottomView()
	{
		FullButton.setText("Full");
		positionKeys(180);
	}

	public void ToggleBottomView()
	{
		if(!isMaximized)
			SwitchToMaximized();
		else
			SwitchToBottomView();
	}

	public void MakeBigger()
	{
		positionKeys(MainStage.getHeight() + 10);
	}

	public void MakeSmaller()
	{
		positionKeys(MainStage.getHeight() - 10);
	}

	public void ActivateRedBulb()
	{
		BulbRed.setEffect(new Glow(1f));
	}

	public void ToggleStepSequencer()
	{
		if(stepSequencer.isActive)
			stepSequencer.isActive = false;
		else
		{
			stepSequencer.isActive = true;

			Thread sequencerThread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					stepSequencer.Start();
				}
			});
			sequencerThread.start();
		}
	}

	public void DeactivateRedBulb()
	{
		BulbRed.setEffect(new Glow(0f));
	}

	public void ActivateGreenBulb()
	{
		BulbGreen.setEffect(new Glow(1));
	}

	public void DeactivateGreenBulb()
	{
		BulbGreen.setEffect(new Glow(0));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		new FadeIn(RootPane).setSpeed(0.2f).play();

		bulbGreenDefault = BulbGreen.getFill();
		bulbRedDefault = BulbRed.getFill();

		OctaveSlider.valueProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1)
			{

			}
		});

		VelocitySlider.valueProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1)
			{
				midiController.currentVelocity = t1.intValue();
			}
		});
	}

	public void AddCombobox(String text)
	{
		if (midiKey >= 130)
			midiKey = 70;

		boolean alreadyAdded = false;

		if(text.equals("Space") || text.equals("F11"))
		{
			alreadyAdded = true;
		}
		else
		{
			 alreadyAdded = false;
		}


		if(MasterFlowPane.getChildren().size() == 13)
			MasterFlowPane.getChildren().remove(0);

		for(var node : MasterFlowPane.getChildren())
		{
			if(((Group)node).getChildren().get(1).getId().equals(text))
			{
				ComboBox combo = (ComboBox) ((Group)node).getChildren().getLast();
				//129 / combo.getSelectionModel().getSelectedIndex()
				new Shake(combo).setSpeed(1f).play();



				alreadyAdded = true;
			}
		}

		if(!alreadyAdded)
		{
			playSound("synth.wav");

			System.out.println("Trying to add Button " + text);
			ComboBox comboBox = new ComboBox();

			ArrayList<String> arrayList = new ArrayList();

			for (int i = 0; i < 130; i++)
			{
				arrayList.add(i + "");
			}

			Group group = new Group();

			Label label = new Label(text);
			label.setStyle("-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-padding: -10px;");


			//comboBox.scro
			comboBox.setFocusTraversable(false);
			comboBox.setItems(FXCollections.observableArrayList(arrayList));
			comboBox.setId(text);
			comboBox.setPrefWidth(95);
			comboBox.setPrefHeight(95);
			comboBox.setStyle("-fx-text-alignment: center; -fx-font-size: 12pt; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-padding: 0px;");
			comboBox.setScaleX(0.6);
			comboBox.setScaleY(0.6);
			comboBox.setScaleZ(0.6);
			comboBox.setPromptText(text);

			comboBox.getSelectionModel().select(midiKey);

			midiKey+=1;

			comboBox.setVisibleRowCount(3);

			comboBox.setOnScroll(new EventHandler<ScrollEvent>()
			{
				@Override
				public void handle(ScrollEvent event)
				{
					if(comboBox.getSelectionModel().getSelectedIndex() >= 2 && comboBox.getSelectionModel().getSelectedIndex() <= comboBox.getItems().size() - 5)
					{
						if(event.getDeltaY() < 0)
						{
							comboBox.getSelectionModel().select(comboBox.getSelectionModel().getSelectedIndex() + 1);
						}
						else
						{
							comboBox.getSelectionModel().select(comboBox.getSelectionModel().getSelectedIndex() -1);
						}
					}
				}
			});

			comboBox.setOnMouseClicked(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent event)
				{
					if(event.getClickCount() == 2)
					{
						comboBox.getSelectionModel().clearSelection();
					}
				}
			});

			comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
			{
				@Override
				public void changed(ObservableValue observable, Object oldValue, Object newValue)
				{
					var scale = 128 / (Double.parseDouble( (String) newValue));

					if(scale <= 0.8 && scale >= 0.2)
					{
						comboBox.scaleXProperty().set(scale);
						comboBox.scaleYProperty().set(scale);
					}


				}
			});


			group.getChildren().add(label);
			group.getChildren().add(comboBox);

			new RollIn(comboBox).setSpeed(2f).play();

			MasterFlowPane.getChildren().add(group);
			ComboController.ComboList.add(comboBox);
		}
	}

	public void endApp()
	{
		playSound("click.wav");

		MainStage.close();
		System.exit(0);
	}

	public void playSound(String resource)
	{
		Media hit = new javafx.scene.media.Media(getClass().getResource("/io/github/isaatonimov/midiKeys/audio/" + resource).toString());
		MediaPlayer mediaPlayer = new MediaPlayer(hit);
		mediaPlayer.setRate(random.nextDouble(5));
		mediaPlayer.setVolume(0.05f);
		mediaPlayer.play();
	}

	public void ToggleMouse()
	{
		playSound("click.wav");

		if(isMouseActive)
		{
			isMouseActive = false;
			PadButton.setText("M");
		}
		else
		{
			isMouseActive = true;
			PadButton.setText("X");
		}

	}

	public void ClearAllCombos()
	{
		playSound("click.wav");

		MasterFlowPane.getChildren().clear();
	}

	public int getPositionInFlowPane(Node node)
	{
		for (int i = 0; i < MasterFlowPane.getChildren().size(); i++)
		{
			if(node.equals(MasterFlowPane.getChildren().get(i)))
				return i;
		}

		return 0;
	}

	public static ArrayList<ComboBox> GetAllComboBoxesInParent(Parent parent)
	{
		ArrayList<ComboBox> comboBoxes = new ArrayList<ComboBox>();

		for(Node node: parent.getChildrenUnmodifiable())
		{
			if(node instanceof ComboBox<?>)
			{
				node.setFocusTraversable(false);
				if(((ComboBox) node).getId() == null)
					comboBoxes.add((ComboBox) node);
			}
			else if(node instanceof Pane || node instanceof Group)
			{
				if(((Pane) node).getChildrenUnmodifiable().size() != 0)
				{
					node.setFocusTraversable(false);
					comboBoxes.addAll(GetAllComboBoxesInParent(((Pane) node).getChildrenUnmodifiable().get(0).getParent()));
				}
			}
		}

		return comboBoxes;
	}

	public void increaseAllNotes()
	{
		playSound("click2.wav");


		for(var combo : ComboController.ComboList)
		{
			var selIndex = combo.getSelectionModel().getSelectedIndex();

			if(selIndex < 128)
				combo.getSelectionModel().select(selIndex+1);
		}
	}

	public void decreaseAllNotes()
	{
		playSound("click2.wav");

		for(var combo : ComboController.ComboList)
		{
			var selIndex = combo.getSelectionModel().getSelectedIndex();

			if(selIndex > 0)
				combo.getSelectionModel().select(selIndex-1);
		}
	}

	public int getPositionInMap(String text)
	{
		return 0;
	}



	public void Speak()
	{

	}
}