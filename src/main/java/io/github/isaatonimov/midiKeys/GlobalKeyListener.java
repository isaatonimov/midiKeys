package io.github.isaatonimov.midiKeys;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import javafx.application.Platform;
import javafx.scene.effect.Glow;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class GlobalKeyListener implements NativeKeyListener
{

	ComboController comboController;
	KeysController keysController;
	MidiController midiController;

	public GlobalKeyListener(ComboController buttonController, KeysController keysController)
	{
		try
		{
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex)
		{
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}

		this.comboController = buttonController;
		this.keysController = keysController;
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e)
	{
		System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

		if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Space"))
			keysController.RootPane.setEffect(new Glow(1f));

		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				keysController.AddCombobox(NativeKeyEvent.getKeyText(e.getKeyCode()));
			}
		});

		for(var ComboBox : comboController.ComboList)
		{

			if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(ComboBox.getId()))
			{
				var indexOfCurrent = comboController.ComboList.indexOf(ComboBox);
				var siblingLeft = indexOfCurrent -1;
				var siblingRight = indexOfCurrent +1;

				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{
						ComboBox.setEffect(new Glow(1f));
					}
				});

				if(ComboBox.getSelectionModel().getSelectedItem() != null && midiController.isAlreadySending(Integer.parseInt((String)ComboBox.getSelectionModel().getSelectedItem())) == false)
				{
					if(ComboBox.getSelectionModel().getSelectedItem() != null)
					{
						try
						{
							var note = Integer.parseInt((String) ComboBox.getSelectionModel().getSelectedItem());
							midiController.StartSound(note, 100);
							var frequency = 440 * 2^( (note - 69) / 12 );

							Platform.runLater(new Runnable()
							{
								@Override
								public void run()
								{
									keysController.KeyLabel.setText(frequency + "hz");

								}
							});
						}
						catch (MidiUnavailableException ex)
						{
							throw new RuntimeException(ex);
						} catch (InvalidMidiDataException ex)
						{
							throw new RuntimeException(ex);
						}
					}
				}
			}
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e)
	{
		keysController.CurrentSentence += e.getKeyChar();
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e)
	{
		System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

		if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Space"))
		{
			midiController.StopAllSounds();
			keysController.RootPane.setEffect(new Glow(0.2f));
			keysController.Speak();
			keysController.CurrentSentence = "";
		}


		for(var ComboBox : comboController.ComboList)
		{
			if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(ComboBox.getId()))
			{
				//ComboBox.setStyle("-fx-background-color: transparent; -fx-opacity: 100%;");
				ComboBox.setEffect(new Glow(0f));

				if(ComboBox.getSelectionModel().getSelectedItem() != null)
				{
					try
					{
						midiController.StopSound(Integer.parseInt(((String) ComboBox.getSelectionModel().getSelectedItem())), 100);
					}
					catch (MidiUnavailableException ex)
					{
						throw new RuntimeException(ex);
					}
					catch (InvalidMidiDataException ex)
					{
						throw new RuntimeException(ex);
					}
				}
			}
		}
	}

	public void AddMidiController(MidiController midiController)
	{
		this.midiController = midiController;
	}
}

