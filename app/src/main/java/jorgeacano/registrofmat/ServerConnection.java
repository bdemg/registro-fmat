package jorgeacano.registrofmat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Jorge A. Cano on 29/11/2016.
 */
public class ServerConnection {

    private static ServerConnection ourInstance = new ServerConnection();

    private Socket socket;

    public static ServerConnection getInstance() {
        return ourInstance;
    }

    private ServerConnection() {
    }


    public void connectToServer(String ipAddress, int portnumber) throws IOException{

        socket = new Socket(ipAddress,portnumber);
    }

    public OutputStream getOutputStream() throws IOException{

        return socket.getOutputStream();
    }

    public InputStream getInputStream() throws IOException{

        return socket.getInputStream();
    }
}
