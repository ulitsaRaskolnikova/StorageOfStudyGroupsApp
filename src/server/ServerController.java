package server;

import lombok.Getter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;

public class ServerController {
    public static void main(String[] args){
        Server server = Server.init();
        StorageController storageController = StorageController.init();
        while (true) {
            try {
                server.searchChannels(storageController);
                break;
            } catch (IOException e){
                server = Server.init();
            }
        }

    }
}
