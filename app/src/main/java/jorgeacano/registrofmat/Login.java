package jorgeacano.registrofmat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Login extends AppCompatActivity {

    private EditText passwordField;
    private EditText userField;
    private Button submitButton;

    private String SERVER_ADDRESS = "192.168.0.2";
    private int SERVER_PORT_NUMBER = 8085;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        addSubmitButtonListener();
    }

    private void addSubmitButtonListener() {

        passwordField = (EditText) findViewById(R.id.fieldPassword);
        userField = (EditText) findViewById(R.id.fieldUser);
        submitButton = (Button) findViewById(R.id.btnSubmit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = userField.getText().toString();
                String password = passwordField.getText().toString();

                //verifyInformation(user, password);
                enterRegistration("pedro", "pedro@gmail.com");
            }
        });
    }

    private void verifyInformation(String user, String password) {

        try {
            ServerConnection.getInstance().connectToServer(this.SERVER_ADDRESS, this.SERVER_PORT_NUMBER);

            PrintWriter outputToServer = new PrintWriter(
                    ServerConnection.getInstance().getOutputStream() );

            //send user and password
            outputToServer.println(this.userField.getText().toString());
            outputToServer.println(this.passwordField.getText().toString());

            //read a boolean to see if the user and password was correct
            DataInputStream primitiveInputFromServer = new DataInputStream(
                    ServerConnection.getInstance().getInputStream() );
            Boolean isUsernameAndPasswordCorrect = primitiveInputFromServer.readBoolean();

            if (isUsernameAndPasswordCorrect){
                //if true call enter registration with active directory info

                Scanner stringInputFromServer = new Scanner(
                        ServerConnection.getInstance().getInputStream() );
                String userName = stringInputFromServer.nextLine();
                String email = stringInputFromServer.nextLine();

                enterRegistration(userName, email);

                outputToServer.close();
                stringInputFromServer.close();
                primitiveInputFromServer.close();
            }
            else {
                //else notify that the password + user combo was incorrect
                Toast.makeText(Login.this, "Usuario o contraseña incorrecta.",
                        Toast.LENGTH_SHORT).show();

                outputToServer.close();
                primitiveInputFromServer.close();
            }
        }catch (IOException ex){
            Toast.makeText(Login.this, "Servidor inalcanzable.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void enterRegistration( String userName, String email ){

        Intent intent = new Intent( getBaseContext(), DeviceRegistery.class );
        intent.putExtra( "USER_NAME", userName );
        intent.putExtra( "EMAIL", email );
        startActivity(intent);
    }
}
