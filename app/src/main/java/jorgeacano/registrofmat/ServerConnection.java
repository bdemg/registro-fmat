package jorgeacano.registrofmat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Jorge A. Cano on 29/11/2016.
 */
public class ServerConnection {

    private static ServerConnection serverConnection = new ServerConnection();

    private Socket socket;

    //private String SERVER_ADDRESS = "172.16.69.164";
    private String SERVER_ADDRESS = "172.16.69.118";
    private int SERVER_PORT_NUMBER = 8086;

    public static ServerConnection getInstance() {
        return serverConnection;
    }

    private ServerConnection() {
    }


    public void connectToServer() throws IOException{

        socket = new Socket(this.SERVER_ADDRESS,this.SERVER_PORT_NUMBER);
    }

    public OutputStream getOutputStream() throws IOException{

        return socket.getOutputStream();
    }

    public InputStream getInputStream() throws IOException{

        return socket.getInputStream();
    }
}
