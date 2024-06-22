package io.github.isaatonimov.midiKeys;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ComboController
{
	public ArrayList<ComboBox> ComboList = new ArrayList<>();
	public HashMap<Button, Integer> ButtonValues = new HashMap<>();
	GlobalKeyListener GlobalKeyListener;

	public void AssignKeyListener(GlobalKeyListener globalKeyListener)
	{
		GlobalKeyListener = globalKeyListener;
	}

	public void ColorCodeAllButtonsRandomly()
	{
		Random random = new Random();

		for(ComboBox comboBox : ComboList)
		{
			comboBox.setStyle("-fx-background-color: " + toRGBCode(Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble())));
		}
	}

	public static String toRGBCode( Color color )
	{
		return String.format( "#%02X%02X%02X",
				(int)( color.getRed() * 255 ),
				(int)( color.getGreen() * 255 ),
				(int)( color.getBlue() * 255 ) );
	}

	public void ColorCodeToGenericKeyboard()
	{

	}
}
