package simpleSocketNetwork;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public class SimpleSocketNetwork {

    public static void main(String[] args) throws IOException, InterruptedException {
        final AsynchronousServerSocketChannel listener =
                AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(8080));

        System.out.print("Waiting new connection...");

        listener.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            public void completed(AsynchronousSocketChannel ch, Void att) {
                System.out.print("Connected\n");

                // accept the next connection
                listener.accept(null, this);

                // handle this connection
                handleChannel(ch);
            }

            public void failed(Throwable exc, Void att) {

            }
        });

        System.in.read();
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
                    if (built.equals("hi") || built.equals("hi\r")) {
                        System.out.print("Sent: hello\n");
                        ch.write(StandardCharsets.UTF_8.encode("hello\n"));
                    } else {
                        System.out.print("Sent: hell\n");
                        ch.write(StandardCharsets.UTF_8.encode("hell\n"));
                    }

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
