package server;

import commonData.requests.interfaces.Request;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private ServerSocketChannel ssc;
    private static final int MAX_COUNT_OF_THREADS = 100;
    private ExecutorService readPool;
    private ForkJoinPool processPool;
    private Semaphore semaphore;
    private ByteBuffer buffer;
    @Getter
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private Server(){}
    public static Server init() {
        while (true){
            try {
                Server server = new Server();
                server.ssc = ServerSocketChannel.open();
                server.ssc.bind(new InetSocketAddress(55555));
                server.ssc.configureBlocking(false);
                server.buffer = ByteBuffer.allocate(1024 * 1024);
                server.readPool = Executors.newFixedThreadPool(MAX_COUNT_OF_THREADS);
                server.processPool = new ForkJoinPool();
                server.semaphore = new Semaphore(MAX_COUNT_OF_THREADS);
                return server;
            } catch (Exception e){
                //ServerController.getLogger().info(e.getMessage());
                logger.info(e.getMessage());
            }
        }
    }
    public void searchChannels(StorageController storageController) throws IOException, InterruptedException {
        while (true){
            semaphore.acquire();
            SocketChannel accept = ssc.accept();
            readPool.execute(() -> {
                try {
                    Request request = getRequest(accept);
                    System.out.println(accept);
                    processPool.execute(() -> {
                        System.out.println(accept);
                        String ans = storageController.executeCommand(request);
                        System.out.println(accept);
                        new Thread(() -> sendAns(ans, accept)).start();
                    });
                } catch (Exception e) {
                    // log
                } finally {
                    semaphore.release();
                }
            });
        }
    }
    public Request getRequest(SocketChannel channel) throws IOException, ClassNotFoundException {
        channel.read(buffer);
        buffer.flip();
        ByteArrayInputStream byteIn = new ByteArrayInputStream(buffer.array(), buffer.position(), buffer.remaining());
        ObjectInputStream objectIn = new ObjectInputStream(byteIn);
        Request request = null;
        try {
            request = (Request) objectIn.readObject();
        } catch (Exception e){
            logger.info(e.getMessage());
        }
        buffer.clear();
        return request;
    }
    public void sendAns(String ans, SocketChannel socket){
        buffer.put(ans.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        try {
            socket.write(buffer);
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        buffer.clear();
        try{
            socket.close();
        } catch (IOException e){
            //log
        }
    }
}
