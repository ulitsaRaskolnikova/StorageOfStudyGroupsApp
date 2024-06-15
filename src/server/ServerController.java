package server;

import lombok.Getter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;

public class ServerController {
    public static void main(String[] args){
        Server server = Server.init();
        var sqlController = new SQLController();
        var storageController = StorageController.initDB(sqlController);
        //StorageController storageController = StorageController.initXML();
        while (true) {
            try {
                server.searchChannels(storageController);
                break;
            } catch (Exception e) {
                server = Server.init();
            }
        }
    }
}
