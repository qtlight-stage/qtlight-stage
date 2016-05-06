package simpleSocketNetwork;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SimpleSocketNetwork {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        final AsynchronousServerSocketChannel listener =
                AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(8080));

        System.out.print("Waiting new connection...");

        Future<AsynchronousSocketChannel> future;
        while (true) {
            future = listener.accept();
            AsynchronousSocketChannel ch = future.get();
            System.out.print("Connected\n");

            // handle this connection
            handleChannel(ch);
        }
    }

    private static void handleChannel(final AsynchronousSocketChannel ch) throws ExecutionException, InterruptedException {
        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        final StringBuilder stringBuilder = new StringBuilder();

        while (ch.isOpen()) {
            read(ch, buffer, stringBuilder);
        }
    }

    private static void read(AsynchronousSocketChannel ch, ByteBuffer buffer, StringBuilder stringBuilder) throws ExecutionException, InterruptedException {
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
            if (built.equals("hi") || built.equals("hi\r")) {
                System.out.print("Sent: hello\n");
                ch.write(StandardCharsets.UTF_8.encode("hello\n"));
            } else {
                System.out.print("Sent: hell\n");
                ch.write(StandardCharsets.UTF_8.encode("hell\n"));
            }

            stringBuilder.setLength(0);
        }
    }
}
