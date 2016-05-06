package NMindMapServer;

import javax.json.JsonObject;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by sasch on 5/7/2016.
 */
public class NMain {
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        NConnectionManager.acceptConnection((JsonObject json) -> {
            System.out.print(json.toString());
        });
    }
}
