package NMindMapServer;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
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
import java.util.function.Consumer;

/**
 * Created by sasch on 5/7/2016.
 */
class NServerConnectionManager {
    private static final ExecutorService pool = Executors.newFixedThreadPool(10);

    static void acceptConnection(Consumer<AsynchronousSocketChannel> onConnect, BiConsumer<AsynchronousSocketChannel, JsonObject> onMessage, Consumer<AsynchronousSocketChannel> onDisconnect) throws IOException, ExecutionException {
        final AsynchronousServerSocketChannel listener =
                AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(8080));

        System.out.print("Waiting new connection...");

        pool.submit(() -> {
            Future<AsynchronousSocketChannel> future;
            //noinspection InfiniteLoopStatement
            while (true) {
                future = listener.accept();
                AsynchronousSocketChannel ch = future.get();
                System.out.print("Connected\n");
                onConnect.accept(ch);

                // handle this connection
                handleChannel(ch, onMessage, onDisconnect);
            }
        });
    }

    private static void handleChannel(final AsynchronousSocketChannel ch, BiConsumer<AsynchronousSocketChannel, JsonObject> onMessage, Consumer<AsynchronousSocketChannel> onDisconnect) {
        pool.submit(() -> {
            final ByteBuffer buffer = ByteBuffer.allocate(1024);
            final StringBuilder stringBuilder = new StringBuilder();
            // TODO: Enable appending UTF-8 partial bytes

            try {
                while (ch.isOpen()) {
                    read(ch, buffer, stringBuilder, onMessage);
                }
            } catch (ExecutionException ex) {
                if (ex.getCause() instanceof IOException) {
                    System.out.print("IO failed");
                } else {
                    ex.printStackTrace();
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            onDisconnect.accept(ch);
            try {
                ch.close();
            } catch (IOException e) {
                e.printStackTrace();
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

    static Future<Integer> sendJson(AsynchronousSocketChannel ch, JsonObject json) {
        return ch.write(StandardCharsets.UTF_8.encode(json.toString() + "\n"));
    }
}
