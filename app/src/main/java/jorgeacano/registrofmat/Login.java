package jorgeacano.registrofmat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.Socket;

public class Login extends AppCompatActivity {

    private EditText passwordField;
    private EditText userField;
    private Button submitButton;

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

                verifyInformation(user, password);
            }
        });
    }

    private void verifyInformation(String user, String password) {

        Socket socket = new Socket();

        //send user and password
        //read a boolean to see if the user and password was correct
            //if true call enter registration with active directory info
        //else
            Toast.makeText(Login.this, "Usuario o contraseña incorrecta.",
                    Toast.LENGTH_SHORT).show();
    }

    private void enterRegistration(String userName, String email){

        Intent intent = new Intent(getBaseContext(), RegistroDispositivos.class);
        intent.putExtra("USER_NAME", userName);
        intent.putExtra("EMAIL", email);
        startActivity(intent);
    }
}
