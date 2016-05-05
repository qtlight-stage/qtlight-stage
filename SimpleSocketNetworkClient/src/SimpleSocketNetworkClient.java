import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

/**
 * Created by sasch on 5/5/2016.
 */
public class SimpleSocketNetworkClient {
    public static void main(String[] args) throws IOException {
        final AsynchronousSocketChannel ch = AsynchronousSocketChannel.open();
        ch.connect(new InetSocketAddress("localhost", 8080), null, new CompletionHandler<Void, Void>() {
            @Override
            public void completed(Void result, Void attachment) {
                System.out.print("Connected to server\n");


            }

            @Override
            public void failed(Throwable exc, Void attachment) {

            }
        });

        handleChannel(ch);

        StringBuilder builder = new StringBuilder();

        while (true) {
            int read = System.in.read();
            Character c = (char)read;
            builder.append(c);

            if (c.equals('\n')) {
                String str = builder.toString();
                ByteBuffer buffer = ByteBuffer.wrap(builder.toString().getBytes("UTF-8"));
                ch.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer result, ByteBuffer attachment) {
                        System.out.print("\nSent: " + str);
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {

                    }
                });
                builder.setLength(0);
            }
        }
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
