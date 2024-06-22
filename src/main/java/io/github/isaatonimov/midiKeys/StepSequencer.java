package io.github.isaatonimov.midiKeys;

import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.effect.Glow;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.util.ArrayList;
import java.util.Timer;

public class StepSequencer
{
	private String pattern1 = "x0x0x0x0";
	private String pattern2 = "xxxx";

	private int currentIndexOfPattern = 0;

	public boolean isActive = false;

	public int millisToWait = 250;

	private Timer timer;

	private ComboController comboController;
	private MidiController midiController;
	private KeysController keysController;

	private ArrayList<String> patterns = new ArrayList<>();

	public StepSequencer(MidiController midiController, ComboController comboController, KeysController keysController)
	{
		patterns.add(pattern1);
		patterns.add(pattern2);

		timer = new Timer();

		this.midiController = midiController;
		this.comboController = comboController;
		this.keysController = keysController;
	}

	public void nextRhythm()
	{

	}

	private ArrayList<ComboBox> getCurrentComboList()
	{
		var clone =  (ArrayList<ComboBox>) comboController.ComboList.clone();
		comboController.ComboList = clone;
		return clone;
	}

	public void Start()
	{
		var currenComboList = getCurrentComboList();

		while(isActive)
		{
			for(ComboBox comboBox : currenComboList)
			{
				try
				{
					Thread.sleep(millisToWait);
				}
				catch (InterruptedException e)
				{
					throw new RuntimeException(e);
				}
				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{
						comboBox.setEffect(new Glow(0.6f));
					}
				});

				try
				{
					Platform.runLater(new Runnable()
					{
						@Override
						public void run()
						{
							keysController.ActivateGreenBulb();

						}
					});

					midiController.StartSound(Integer.parseInt((String)comboBox.getSelectionModel().getSelectedItem()), midiController.currentVelocity);
				} catch (MidiUnavailableException e)
				{
					throw new RuntimeException(e);
				} catch (InvalidMidiDataException e)
				{
					throw new RuntimeException(e);
				}

				try
				{
					Thread.sleep(250);

					Platform.runLater(new Runnable()
					{
						@Override
						public void run()
						{
							comboBox.setEffect(new Glow(0f));
							keysController.DeactivateGreenBulb();
						}
					});

					try
					{
						midiController.StopSound(Integer.parseInt((String)comboBox.getSelectionModel().getSelectedItem()), midiController.currentVelocity);
					} catch (MidiUnavailableException e)
					{
						throw new RuntimeException(e);
					} catch (InvalidMidiDataException e)
					{
						throw new RuntimeException(e);
					}


				}
				catch (InterruptedException e)
				{
					throw new RuntimeException(e);
				}

				currenComboList = getCurrentComboList();
			}
		}
	}

	public void Stop()
	{

	}
}
