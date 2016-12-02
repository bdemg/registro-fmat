package jorgeacano.registrofmat;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Jorge A. Cano on 30/11/2016.
 */
public class NotifMessager {
    private static NotifMessager ourInstance = new NotifMessager();

    public static String SERVER_UNREACHABLE = "Servidor inalcanzable.";
    public static String WRONG_CREDENTIALS = "Usuario o contraseña incorrecta.";
    public static String INCOMPLETE_MAC = "La dirección mac está incompleta.";
    public static String DEVICE_REGISTERED = "Dispositivo registrado.";
    public static String DEVICE_UNLISTED = "Dispositivo eliminado del registro";
    public static String SERVER_CONNECTION_LOST = "Se perdió la conección con el servidor.";

    public static NotifMessager getInstance() {
        return ourInstance;
    }

    private NotifMessager() {
    }


    public void showMessage(Activity currentActivity, String message){

        Toast.makeText(currentActivity, message,
                Toast.LENGTH_SHORT).show();
    }
}
