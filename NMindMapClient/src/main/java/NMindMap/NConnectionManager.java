package NMindMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Created by sasch on 5/6/2016.
 */
public class NConnectionManager {
    private static AsynchronousSocketChannel ch = null;
    private static final ExecutorService pool = Executors.newFixedThreadPool(10);

    public static void connectToServer(InetSocketAddress address, Consumer<JsonObject> onMessage) throws IOException, ExecutionException, InterruptedException {
        if (ch != null) {
            ch.close();
        }
        ch = AsynchronousSocketChannel.open();

        Future<Void> future = ch.connect(address);
        future.get();

        System.out.print("Connected to server\n");
        handleChannel(ch, onMessage);
    }

    private static Future handleChannel(final AsynchronousSocketChannel ch, Consumer<JsonObject> onMessage) throws ExecutionException, InterruptedException, IOException {
        return pool.submit(() -> {
            final ByteBuffer buffer = ByteBuffer.allocate(1024);
            final StringBuilder stringBuilder = new StringBuilder();

            try {
                while (ch.isOpen()) {
                    read(ch, buffer, stringBuilder, onMessage);
                }
            } catch (ExecutionException e) {
                if (e.getCause() instanceof AsynchronousCloseException) {
                    System.out.print("Connection closed");
                }
                else {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        });
    }

    private static void read(AsynchronousSocketChannel ch, ByteBuffer buffer, StringBuilder stringBuilder, Consumer<JsonObject> onMessage) throws ExecutionException, InterruptedException, IOException {
        Future<Integer> future = ch.read(buffer);

        int bytesRead = future.get();
        if (bytesRead == 0) {
            return;
        }

        buffer.flip();
        final String str = StandardCharsets.UTF_8.decode(buffer).toString();
        buffer.flip();
        System.out.print("Received partially: " + str + "\n");
        final int newlineIndex = str.indexOf('\n');

        if (newlineIndex == -1) {
            stringBuilder.append(str);
        } else {
            stringBuilder.append(str.substring(0, newlineIndex));

            // new complete message has arrived, handle this!
            final String built = stringBuilder.toString();
            System.out.print("Received message: " + built + "\n");
            onMessage.accept(parseJson(built));

            stringBuilder.setLength(0);
        }
    }

    public static Future<Integer> sendJson(JsonObject json) {
        return ch.write(StandardCharsets.UTF_8.encode(json.toString() + "\n"));
    }

    private static JsonObject parseJson(String str) {
        JsonReader reader = Json.createReader(new StringReader(str));
        return reader.readObject();
    }
}
