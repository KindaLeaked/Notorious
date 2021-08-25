package Notorious.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Notorious.Client;
import Notorious.utils.Manager;

public final class ConfigManager extends Manager<Config> {

    public static final File CONFIGS_DIR = new File(Client.instance.name, "configs");
    public static final String EXTENTION = ".json";

    public ConfigManager() {
        setContents(loadConfigs());

        CONFIGS_DIR.mkdirs();
    }

    private static ArrayList<Config> loadConfigs() {
        final ArrayList<Config> loadedConfigs = new ArrayList<>();
        File[] files = CONFIGS_DIR.listFiles();
        if (files != null) {
            for (File file : files) {
                if (FilenameUtils.getExtension(file.getName()).equals("json"))
                    loadedConfigs.add(new Config(FilenameUtils.removeExtension(file.getName())));
            }
        }
        return loadedConfigs;
    }
    
    public static void downloadConfigs() {
    	
    	try {
    	
    		//TODO check if config already exists.
    		
        	URL url = new URL("https://filesamples.com/samples/document/txt/sample3.txt");
    		System.out.println("Downloading config from " + url.toString());
			ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
			FileOutputStream fileOutputStream = new FileOutputStream("Notorious/configs/mineplex.json");
			FileChannel fileChannel = fileOutputStream.getChannel();
			fileOutputStream.getChannel()
			  .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
			fileOutputStream.close();
			System.out.println("Config successfully downloaded!");
		} catch (IOException e) {
			System.out.println("Failed to download config!");
			e.printStackTrace();
		}
    }

    public boolean loadConfig(String configName) {
        if (configName == null)
            return false;
        Config config = findConfig(configName);
        if (config == null)
            return false;
        try {
            FileReader reader = new FileReader(config.getFile());
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(reader);
            config.load(object);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public boolean saveConfig(String configName) {
        if (configName == null)
            return false;
        Config config;
        if ((config = findConfig(configName)) == null) {
            Config newConfig = (config = new Config(configName));
            getContents().add(newConfig);
        }

        String contentPrettyPrint = new GsonBuilder().setPrettyPrinting().create().toJson(config.save());
        try {
            FileWriter writer = new FileWriter(config.getFile());
            writer.write(contentPrettyPrint);
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Config findConfig(String configName) {
        if (configName == null) return null;
        for (Config config : getContents()) {
            if (config.getName().equalsIgnoreCase(configName))
                return config;
        }

        if (new File(CONFIGS_DIR, configName + EXTENTION).exists())
            return new Config(configName);

        return null;
    }

    public boolean deleteConfig(String configName) {
        if (configName == null)
            return false;
        Config config;
        if ((config = findConfig(configName)) != null) {
            final File f = config.getFile();
            getContents().remove(config);
            return f.exists() && f.delete();
        }
        return false;
    }
}
