package NMindMapServer;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;

/**
 * Created by sasch on 5/13/2016.
 */
class NFileManager {
    static JsonObject loadJson(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        char[] buffer = new char[(int)file.length()];
        //noinspection ResultOfMethodCallIgnored
        fileReader.read(buffer);
        fileReader.close();
        JsonReader jsonReader = Json.createReader(new StringReader(new String(buffer)));
        return jsonReader.readObject();
    }

    static void saveJson(JsonObject json, String path) throws IOException {
        String str = json.toString();

        FileWriter writer = new FileWriter(path);
        writer.write(str);
        writer.flush();
        writer.close();
    }
}
