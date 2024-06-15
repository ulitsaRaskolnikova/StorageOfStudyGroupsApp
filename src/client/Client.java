package client;

import commonData.commandData.CommandType;
import commonData.modelHandlers.Respondent;
import commonData.requests.RequestOnlyCommand;
import commonData.requests.interfaces.Request;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private ByteArrayOutputStream byteOut;
    private ObjectOutputStream objectOut;
    private BufferedReader reader;
    @Setter
    @Getter
    private int port;
    public void init() throws IOException {
        setSocket();
        byteOut = new ByteArrayOutputStream(1024 * 1024);
        try {
            objectOut = new ObjectOutputStream(byteOut);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e){
            Respondent.sendToOutput(e.getMessage());
        }
    }
    public void setSocket() throws IOException {
        String message = "";
        while (true) {
            try {
                socket = new Socket("192.168.10.80", port);
                //socket = new Socket("localhost", port);
                break;
            } catch (Exception e){
                if (!message.equals(e.getMessage())){
                    message = e.getMessage();
                    Respondent.sendToOutput(message);
                }
            }
        }
    }
    public String dispatch(Request request) throws IOException {
        init();
        try {
            objectOut.writeObject(request);
        } catch (IOException e){
            Respondent.sendToOutput(e.getMessage());
        }
        try {
            socket.getOutputStream().write(byteOut.toByteArray());
        } catch (IOException e){
            Respondent.sendToOutput(e.getMessage());
            setSocket();
        }
        try {
            return reader.readLine();
        } catch (IOException e) {
            Respondent.sendToOutput(e.getMessage());
            setSocket();
        }
        return "You have a few troubles with server, dump little ass...";
    }
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("localhost", 3000);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream(1024 * 1024);
        ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
        Request request = new RequestOnlyCommand(CommandType.INSERT_AT);
        objectOut.writeObject(request);
        socket.getOutputStream().write(byteOut.toByteArray());

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println(reader.readLine());
        System.out.println(reader.readLine());
        socket.close();
    }
}
