package NMindMapServer;

import net.maritimecloud.internal.core.javax.json.Json;
import net.maritimecloud.internal.core.javax.json.JsonObject;
import net.maritimecloud.internal.core.javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;

/**
 * Created by sasch on 5/7/2016.
 */
public class NServerConnectionManager {
    private static final ExecutorService pool = Executors.newFixedThreadPool(10);

    public static void acceptConnection(BiConsumer<AsynchronousSocketChannel, JsonObject> onMessage) throws IOException, ExecutionException {
        final AsynchronousServerSocketChannel listener =
                AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(8080));

        System.out.print("Waiting new connection...");

        pool.submit(() -> {
            Future<AsynchronousSocketChannel> future;
            while (true) {
                future = listener.accept();
                AsynchronousSocketChannel ch = future.get();
                System.out.print("Connected\n");

                // handle this connection
                handleChannel(ch, onMessage);
            }
        });
    }

    private static void handleChannel(final AsynchronousSocketChannel ch, BiConsumer<AsynchronousSocketChannel, JsonObject> onMessage) {
        pool.submit(() -> {
            final ByteBuffer buffer = ByteBuffer.allocate(1024);
            final StringBuilder stringBuilder = new StringBuilder();

            while (ch.isOpen()) {
                try {
                    read(ch, buffer, stringBuilder, onMessage);
                } catch (ExecutionException ex) {
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void read(AsynchronousSocketChannel ch, ByteBuffer buffer, StringBuilder stringBuilder, BiConsumer<AsynchronousSocketChannel, JsonObject> onMessage) throws ExecutionException, InterruptedException {
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
            onMessage.accept(ch, parseJson(built));

            stringBuilder.setLength(0);
        }
    }

    private static JsonObject parseJson(String str) {
        JsonReader reader = Json.createReader(new StringReader(str));
        return reader.readObject();
    }

    public static Future<Integer> sendJson(AsynchronousSocketChannel ch, JsonObject json) {
        return ch.write(StandardCharsets.UTF_8.encode(json.toString() + "\n"));
    }
}
