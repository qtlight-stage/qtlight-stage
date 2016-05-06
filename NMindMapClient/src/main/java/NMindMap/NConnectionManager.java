package NMindMap;

import com.sun.xml.internal.ws.util.CompletedFuture;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Created by sasch on 5/6/2016.
 */
public class NConnectionManager {
    private static AsynchronousSocketChannel ch;

    public static Future<Void> connectToServer() throws IOException {
        CompletableFuture<Void> future = new CompletableFuture<>();

        ch = AsynchronousSocketChannel.open();

        ch.connect(new InetSocketAddress("localhost", 8080), null, new CompletionHandler<Void, Void>() {
            @Override
            public void completed(Void result, Void attachment) {
                System.out.print("Connected to server\n");

                handleChannel(ch);
                future.complete(null);
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                future.completeExceptionally(exc);
            }
        });

        return future;
    }

    private static void handleChannel(final AsynchronousSocketChannel ch) {
        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        final StringBuilder stringBuilder = new StringBuilder();

        readContinuously(ch, buffer, stringBuilder);
    }

    private static void readContinuously(AsynchronousSocketChannel ch, ByteBuffer buffer, StringBuilder stringBuilder) {
        ch.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            public void completed(Integer bytesRead, ByteBuffer buffer) {
                if (bytesRead == 0) {
                    if (ch.isOpen()) {
                        readContinuously(ch, buffer, stringBuilder);
                    }
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
                    readContinuously(ch, buffer, stringBuilder);
                }
            }

            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    ch.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
