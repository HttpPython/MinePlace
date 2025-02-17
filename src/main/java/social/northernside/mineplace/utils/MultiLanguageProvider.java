// Created by Torben R.
package social.northernside.mineplace.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import social.northernside.mineplace.MinePlace;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MultiLanguageProvider {

    private static final Gson GSON = new GsonBuilder().setLenient().setPrettyPrinting().create();
    private static final JsonParser PARSER = new JsonParser();
    private final File dir = new File(MinePlace.getInstance().getDataFolder().getPath() + "data/lang/");
    private final File selectedLanguagesFile = new File(dir + "selected.json");
    private final Map<String, Properties> languages = new HashMap<>();
    private static JsonObject jsonObject = new JsonObject();

    @SneakyThrows
    public MultiLanguageProvider() {
        if (!selectedLanguagesFile.exists()) {
            jsonObject = new JsonObject();
        }
        jsonObject = (JsonObject) PARSER.parse(GSON.newJsonReader(new FileReader(selectedLanguagesFile)));
        loadLanguages();
    }

    public String getLang(Player player) {
        String selected = jsonObject.has(player.getUniqueId().toString()) ? jsonObject.get(player.getUniqueId().toString()).getAsString() : "en";
        if (!languages.containsKey(selected)) return "en";
        return selected;
    }

    @SneakyThrows(IOException.class)
    public void saveObject() {
        if(selectedLanguagesFile.delete()) selectedLanguagesFile.createNewFile();
        FileWriter writer = new FileWriter(selectedLanguagesFile);
        writer.write(GSON.toJson(jsonObject));
        writer.close();
    }

    private void loadLanguages() {
        Arrays.stream(Objects.requireNonNull(dir.listFiles())).forEach(file -> {
            Properties properties = new Properties();
            try {
                properties.load(new FileReader(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            languages.put(file.getName(), properties);
        });
    }
}
