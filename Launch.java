package Notorious;

import java.io.File;

import net.minecraft.client.main.Main;

public class Launch {

	public static void launch(final boolean fullscreen) {
		final String userHome = System.getProperty("user.home", ".");
		final String applicationData = System.getenv("APPDATA");
		final String folder = (applicationData != null) ? applicationData : userHome;
		final File workingDirectory = new File(folder, ".minecraft/");
		Main.main(new String[] { "--version", "1.8.9", "--accessToken", "0", fullscreen ? "--fullscreen" : "",
				"--assetIndex", "1.8", "--userProperties", "{}", "--assetsDir",
				new File(workingDirectory, "assets/").getAbsolutePath() });
	}

	public static void main(String[] args) {
		launch(false);
	}

}
