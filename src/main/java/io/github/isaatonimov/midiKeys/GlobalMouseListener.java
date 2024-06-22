package io.github.isaatonimov.midiKeys;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class GlobalMouseListener implements NativeMouseInputListener
{

	private MidiController midiController;
	private KeysController keysController;
	private Line line;
	private Line line2;
	private Line line3;
	private Line line4;

	private Circle circle;
	private double orgSceneX, orgSceneY;
	private double orgTranslateX, orgTranslateY;

	private float sum;
	private float pin;


	public GlobalMouseListener(KeysController keysController, MidiController midiController)
	{

		line = new Line();
		line.opacityProperty().set(0.5d);

		line.setStrokeWidth(2d);
		line.setStroke(Color.rgb(99,86,171));

		keysController.RootPane.getChildren().add(line);

		this.midiController = midiController;
		this.keysController = keysController;
	}
	@Override
	public void nativeMouseClicked(NativeMouseEvent e) {
		System.out.println("Mouse Clicked: " + e.getClickCount());
	}

	@Override
	public void nativeMousePressed(NativeMouseEvent e)
	{
		if(keysController.isMouseActive)
		{
			try
			{
				midiController.StartSound((int)pin, 100);
			} catch (MidiUnavailableException ex)
			{
				throw new RuntimeException(ex);
			} catch (InvalidMidiDataException ex)
			{
				throw new RuntimeException(ex);
			}
		}

	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent e)
	{
		if(keysController.isMouseActive)
		{
			try
			{
				midiController.StopSound((int)pin, 100);
			} catch (MidiUnavailableException ex)
			{
				throw new RuntimeException(ex);
			} catch (InvalidMidiDataException ex)
			{
				throw new RuntimeException(ex);
			}
		}
	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent e)
	{

	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent e)
	{
		if(keysController.isMouseActive)
		{
			line.setEndX(e.getX());
			line.setStartX(0);

			sum = e.getY() * e.getX();
			pin = ((sum / 130) / 100) + 40;

			try
			{
				midiController.StartSound((int)pin, 100);
			} catch (MidiUnavailableException ex)
			{
				throw new RuntimeException(ex);
			} catch (InvalidMidiDataException ex)
			{
				throw new RuntimeException(ex);
			}
		}
	}
}