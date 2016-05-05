import javax.json.*;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by sasch on 5/5/2016.
 */
public class JSONFileSaver {
    public static void main(String[] args) throws IOException {
        JsonObject json = Json.createObjectBuilder()
                .add("project", "java")
                .add("term", Json.createArrayBuilder()
                        .add("201603")
                        .add("201606")
                ).build();

        String str = json.toString();

        System.out.print("Storing JSON:\n" + str + "\n");
        FileWriter writer = new FileWriter("file.json");
        writer.write(str);
        writer.flush();
        writer.close();
    }
}
