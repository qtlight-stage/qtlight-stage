package NMindMap;

import javax.json.JsonObject;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by sasch on 5/6/2016.
 */
public class NConnectionManager {
    private static AsynchronousSocketChannel ch;

    public static void connectToServer() throws IOException, ExecutionException, InterruptedException {
        ch = AsynchronousSocketChannel.open();

        Future<Void> future = ch.connect(new InetSocketAddress("localhost", 8080));
        future.get();

        System.out.print("Connected to server\n");
        handleChannel(ch);
    }

    private static void handleChannel(final AsynchronousSocketChannel ch) throws ExecutionException, InterruptedException, IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        final StringBuilder stringBuilder = new StringBuilder();

        while (ch.isOpen()) {
            read(ch, buffer, stringBuilder);
        }
    }

    private static void read(AsynchronousSocketChannel ch, ByteBuffer buffer, StringBuilder stringBuilder) throws ExecutionException, InterruptedException, IOException {
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

            stringBuilder.setLength(0);
        }

        if (ch.isOpen()) {
            read(ch, buffer, stringBuilder);
        }
    }

    public Future<Integer> sendJson(JsonObject json) {
        return ch.write(StandardCharsets.UTF_8.encode(json.toString()));
    }
}
