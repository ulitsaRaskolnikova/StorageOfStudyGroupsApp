package server;

import commonData.requests.interfaces.Request;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class Server {
    Selector selector;
    ServerSocketChannel ssc;
    ByteBuffer buffer;
    @Getter
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private Server(){}
    public static Server init() {
        while (true){
            try {
                Server server = new Server();
                server.selector = Selector.open();
                server.ssc = ServerSocketChannel.open();
                server.ssc.bind(new InetSocketAddress(3000));
                server.ssc.configureBlocking(false);
                server.ssc.register(server.selector, SelectionKey.OP_ACCEPT);
                server.buffer = ByteBuffer.allocate(1024 * 1024);
                return server;
            } catch (Exception e){
                //ServerController.getLogger().info(e.getMessage());
                logger.info(e.getMessage());
            }
        }

    }
    public void searchChannels(StorageController storageController) throws IOException{
        while (true){
            int select = selector.select();
            if (select== 0) continue;
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                try {
                    if (key.isAcceptable()) {
                        SocketChannel channel = ssc.accept();
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                    } else {
                        try {
                            Request request = getRequest(key);
                            String ans = storageController.executeCommand(request);
                            //System.out.println(request);
                            //System.out.println(ans);
                            logger.info(request.toString());
                            logger.info(ans);
                            sendAns(ans, key);
                            key.channel().close();
                            ssc.register(selector, SelectionKey.OP_ACCEPT);
                        } catch (ClassNotFoundException e){
                            logger.info(e.getMessage());
                        }
                    }
                } finally {
                    iterator.remove();
                }
            }
        }
    }
    public Request getRequest(SelectionKey key) throws IOException, ClassNotFoundException {
        ((SocketChannel) key.channel()).read(buffer);
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
    public void sendAns(String ans, SelectionKey key){
        buffer.put(ans.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        try {
            ((SocketChannel) key.channel()).write(buffer);
        } catch (IOException e){
            logger.info(e.getMessage());
        }
        buffer.clear();

    }


    public static void main(String[] args) throws Exception{
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(3000));
        ssc.configureBlocking(false);
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
        while (true){
            if (selector.select() == 0) continue;
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            //System.out.println(selector.selectedKeys().size());
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                try {
                    if (key.isAcceptable()) {
                        SocketChannel channel = ssc.accept();
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        ((SocketChannel) key.channel()).read(buffer);
//                        if (((SocketChannel) key.channel()).read(buffer) == -1){
//                            System.out.println("-1");
//                            key.interestOps(SelectionKey.OP_WRITE);
//                        }

                        buffer.flip();
                        ByteArrayInputStream byteIn = new ByteArrayInputStream(buffer.array(), buffer.position(), buffer.remaining());
                        ObjectInputStream objectIn = new ObjectInputStream(byteIn);
                        Request request = (Request) objectIn.readObject();
                        System.out.println(request);

                        buffer.clear();
                        //key.channel().register(selector, SelectionKey.OP_WRITE);
                        String ans = "I LIKE SEX\n AND DRUGS";
                        buffer.put(ans.getBytes(StandardCharsets.UTF_8));
                        buffer.flip();
                        System.out.println(new String(buffer.array(), buffer.position(), buffer.remaining()));
                        ((SocketChannel) key.channel()).write(buffer);
                        buffer.clear();
                        key.channel().close();
                    }
                } finally {
                    iterator.remove();
                }
            }
        }
    }
}
