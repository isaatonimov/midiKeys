package io.github.isaatonimov.midiKeys;


import javax.sound.midi.*;
import java.util.ArrayList;

public class MidiController
{
	Receiver receiver;
	MidiDevice midiDevice;


	private KeysController keysController;

	Integer		currentVelocity = 100;
	Boolean 		currentlySending = false;
	ArrayList<Integer>	currentlySendingSignals = new ArrayList<>();

	public MidiController(KeysController keysController)
	{
		try
		{
			receiver = MidiSystem.getReceiver();
		} catch (MidiUnavailableException e)
		{
			throw new RuntimeException(e);
		}

		this.keysController = keysController;
	}

	public boolean isAlreadySending(int pitch)
	{
		for(var item : currentlySendingSignals)
		{
			if(item == pitch)
				return true;
		}

		return false;
	}

	public void StartSound(int pitch, int velocity) throws MidiUnavailableException, InvalidMidiDataException
	{
		System.out.println("Sending midi");

		keysController.ActivateRedBulb();

		ShortMessage myMsg = new ShortMessage();
		myMsg.setMessage(ShortMessage.NOTE_ON, 1, pitch, currentVelocity);
		long timeStamp = -1;
		receiver.send(myMsg, timeStamp);

		currentlySendingSignals.add(pitch);
	}

	public void StopSound(int pitch, int velocity) throws MidiUnavailableException, InvalidMidiDataException
	{
		keysController.DeactivateRedBulb();

		System.out.println("Stopping Midi");

		ShortMessage myMsg = new ShortMessage();
		myMsg.setMessage(ShortMessage.NOTE_OFF, 1, pitch, currentVelocity);
		long timeStamp = -1;
		receiver.send(myMsg, timeStamp);

		currentlySendingSignals.remove((Object)pitch);
	}

	public void StopAllSounds()
	{
		ShortMessage myMsg = new ShortMessage();

		for (int i = 1; i < 128; i++)
		{
			System.out.println("Stopping Midi");

			try
			{
				myMsg.setMessage(ShortMessage.NOTE_OFF, 1, i, 100);
			} catch (InvalidMidiDataException e)
			{
				throw new RuntimeException(e);
			}
			long timeStamp = -1;
			receiver.send(myMsg, timeStamp);

		}

		currentlySendingSignals.clear();
	}
}
