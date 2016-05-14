package NMindMapServer;

import net.maritimecloud.internal.core.javax.json.Json;
import net.maritimecloud.internal.core.javax.json.JsonObject;
import net.maritimecloud.internal.core.javax.json.JsonReader;
import java.io.*;

/**
 * Created by sasch on 5/13/2016.
 */
public class NFileManager {
    public static JsonObject loadJson(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        char[] buffer = new char[(int)file.length()];
        fileReader.read(buffer);
        fileReader.close();
        JsonReader jsonReader = Json.createReader(new StringReader(new String(buffer)));
        JsonObject json = jsonReader.readObject();
        return json;
    }

    public static void saveJson(JsonObject json, String path) throws IOException {
        String str = json.toString();

        FileWriter writer = new FileWriter(path);
        writer.write(str);
        writer.flush();
        writer.close();
    }
}
