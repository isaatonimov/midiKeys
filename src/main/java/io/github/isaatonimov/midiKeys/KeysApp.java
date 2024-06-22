package io.github.isaatonimov.midiKeys;

import com.github.kwhat.jnativehook.GlobalScreen;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class KeysApp extends Application
{
	@Override
	public void start(Stage stage) throws IOException
	{
		//Create Global Key Listener and give to ButtonController
		ComboController comboController = new ComboController();

		FXMLLoader fxmlLoader = new FXMLLoader(KeysApp.class.getResource("/io/github/isaatonimov/midiKeys/dynamicKeys.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		stage.setTitle("Keys \uD83C\uDFB9");
		stage.initStyle(StageStyle.UNDECORATED);
		scene.setFill(Color.rgb(40,42,54));

		KeysController keysController = fxmlLoader.getController();
		MidiController midiController = new MidiController(keysController);
		StepSequencer stepSequencer = new StepSequencer(midiController, comboController, keysController);
		keysController.stepSequencer = stepSequencer;
		keysController.MainScene = scene;
		keysController.MainStage = stage;

		GlobalKeyListener globalKeyListener = new GlobalKeyListener(comboController, keysController);
		comboController.AssignKeyListener(globalKeyListener);
		globalKeyListener.AddMidiController(midiController);

		GlobalMouseListener globalMouseListener = new GlobalMouseListener(keysController, midiController);

		GlobalScreen.addNativeKeyListener(globalKeyListener);
		GlobalScreen.addNativeMouseListener(globalMouseListener);
		GlobalScreen.addNativeMouseMotionListener(globalMouseListener);


		keysController.ComboController = comboController;
		keysController.midiController = midiController;

		keysController.positionKeys(180);
		stage.setScene(scene);

		stage.show();
		stage.setAlwaysOnTop(true);

		loadIconsForStage(stage);
	}

	private static void loadIconsForStage(Stage stage) throws IOException {
		// This is required for Windows and Linux. On macOS there's no distinction between window
		// icons and app icons, so we don't bundle the icon PNGs separately and thus the loop here
		// doesn't do anything.
		var appDir = System.getProperty("app.dir");
		if (appDir == null)
			return;
		var iconsDir = Paths.get(appDir);
		try (var dirEntries = Files.newDirectoryStream(iconsDir, "icon-*.png")) {
			for (Path iconFile : dirEntries) {
				try (var icon = Files.newInputStream(iconFile)) {
					stage.getIcons().add(new Image(icon));
				}
			}
		}
	}

	public static void main(String[] args)
	{
		launch();
	}
}