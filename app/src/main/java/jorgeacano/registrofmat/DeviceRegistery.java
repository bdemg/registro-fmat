package jorgeacano.registrofmat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class DeviceRegistery extends AppCompatActivity {

    private EditText MACField;
    private Button submitButton;
    private final int COMPLETE_MAC_ADDRESS_LENGTH = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_dispositivos);

        TextView username = (TextView) findViewById(R.id.lblUserName);
        username.setText(getIntent().getStringExtra("USER_NAME"));

        TextView email = (TextView) findViewById(R.id.lblUserEmail);
        email.setText(getIntent().getStringExtra("EMAIL"));

        this.MACField = (EditText) findViewById(R.id.fieldMAC);

        setupMACFieldFormat();
        addSubmitButtonListener();
    }

    private void addSubmitButtonListener() {

        submitButton = (Button) findViewById(R.id.btnSubmitMAC);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = getIntent().getStringExtra("USER_NAME");
                String mac = MACField.getText().toString();

                if(mac.length() == COMPLETE_MAC_ADDRESS_LENGTH) {
                    //registerDevice(user, mac);
                }
                else{
                    Toast.makeText(DeviceRegistery.this, "La dirección mac está incompleta.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerDevice(String user, String mac) {

        try {
            PrintWriter outputToServer = new PrintWriter(
                    ServerConnection.getInstance().getOutputStream());

            outputToServer.println(user);
            outputToServer.println(mac);

            Toast.makeText(DeviceRegistery.this, "Dispositivo registrado.",
                    Toast.LENGTH_SHORT).show();

            cleanMACField();
            outputToServer.close();
        }catch (IOException ex){
            Toast.makeText(DeviceRegistery.this, "Se perdió la conección con el servidor.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void cleanMACField() {

        this.MACField.setText("");
    }

    private void setupMACFieldFormat() {
        MACField.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        MACField.setSingleLine();
        InputFilter[] filters = new InputFilter[1];
        //filter used to allow only MAC style address
        filters[0] = new InputFilter() {


            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                // TODO Auto-generated method stub
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
                    if (resultingTxt.matches("([0-9a-fA-F][0-9a-fA-F]:){0,5}[0-9a-fA-F]"))
                    {

                    }
                    else if(resultingTxt.matches("([0-9a-fA-F][0-9a-fA-F]:){0,4}[0-9a-fA-F][0-9a-fA-F]")){
                        return source.subSequence(start, end)+":" ;
                    }
                    else if(resultingTxt.matches("([0-9a-fA-F][0-9a-fA-F]:){0,5}[0-9a-fA-F][0-9a-fA-F]")){

                    }
                    else
                    {
                        return "";
                    }
                }
                return null;
            }
        };
        MACField.setFilters(filters);
    }
}
