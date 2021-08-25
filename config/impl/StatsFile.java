package Notorious.config.impl;

import java.io.File;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import Notorious.Client;
import Notorious.config.IFile;
import Notorious.module.modules.player.Scaffold;

public class StatsFile implements IFile {

	private File file;
	private int saveduptime = 0;
	private int savedScaffoldTime = 0;
	private int savedScaffoldPlaced = 0;

	@Override
	public void save(Gson gson) {
		JsonObject object = new JsonObject();

		JsonObject statsObject = new JsonObject();

		statsObject.addProperty("uptime", Client.instance.getTotalUptime());
		statsObject.addProperty("scaffoldtime", Scaffold.sessionScaffoldTime);
		statsObject.addProperty("scaffoldplaced", Scaffold.sessionScaffoldPlaced);

		object.add("Stats", statsObject);

		writeFile(gson.toJson(object), file);

	}

	@Override
	public void load(Gson gson) {
		if (!file.exists()) {
			return;
		}

		JsonObject object = gson.fromJson(readFile(file), JsonObject.class);
		if (object.has("Stats")) {
			JsonObject statsObject = object.getAsJsonObject("Stats");
			if (statsObject.has("uptime")) {
				JsonObject uptimeObject = statsObject.getAsJsonObject();
				saveduptime = uptimeObject.get("uptime").getAsInt();
				Client.instance.savedStartTime = saveduptime;
			}
			if (statsObject.has("scaffoldtime")) {
				JsonObject stObject = statsObject.getAsJsonObject();
				savedScaffoldTime = stObject.get("scaffoldtime").getAsInt();
				Scaffold.sessionScaffoldTime = savedScaffoldTime;
			}
			if(statsObject.has("scaffoldplaced")) {
				JsonObject spObject = statsObject.getAsJsonObject();
				savedScaffoldPlaced = spObject.get("scaffoldplaced").getAsInt();
				Scaffold.sessionScaffoldPlaced = savedScaffoldPlaced;
			}
		}
	}

	@Override
	public void setFile(File root) {
		file = new File(root, "/stats.json");
	}

}
