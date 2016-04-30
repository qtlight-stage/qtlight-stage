package simpleSocketNetwork;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public class SimpleSocketNetwork {

    public static void main(String[] args) throws IOException {
        final AsynchronousServerSocketChannel listener =
                AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(5000));

        listener.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            public void completed(AsynchronousSocketChannel ch, Void att) {
                // accept the next connection
                listener.accept(null, this);

                // handle this connection
                handleChannel(ch);
            }

            public void failed(Throwable exc, Void att) {

            }
        });

    }

    private static void handleChannel(final AsynchronousSocketChannel ch) {
        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        final StringBuilder stringBuilder = new StringBuilder();

        ch.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            public void completed(Integer bytesRead, ByteBuffer buffer) {
                buffer.position(0);
                final String str = StandardCharsets.UTF_8.decode(buffer).toString();
                final int newlineIndex = str.indexOf('\n');

                if (newlineIndex != -1) {
                    stringBuilder.append(str);
                } else {
                    stringBuilder.append(str.substring(0, newlineIndex));

                    // new complete message has arrived, handle this!
                    final String built = stringBuilder.toString();
                    System.console().writer().write("Received message: " + built + "\n");
                    if (built.equals("Hi")) {
                        ch.write(StandardCharsets.UTF_8.encode("hello\n"));
                    } else {
                        ch.write(StandardCharsets.UTF_8.encode("hell\n"));
                    }

                    try {
                        ch.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
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
