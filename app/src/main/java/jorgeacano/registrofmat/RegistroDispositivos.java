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

import java.net.Socket;

public class RegistroDispositivos extends AppCompatActivity {

    private EditText MACField;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_dispositivos);

        TextView username = (TextView) findViewById(R.id.lblUserName);
        username.setText(getIntent().getStringExtra("USER_NAME"));

        TextView email = (TextView) findViewById(R.id.lblUserEmail);
        email.setText(getIntent().getStringExtra("EMAIL"));


        setupMACFieldFormat();
        addSubmitButtonListener();
    }

    private void addSubmitButtonListener() {

        submitButton = (Button) findViewById(R.id.btnSubmitMAC);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = getIntent().getStringExtra("USER_NAME");
                String MAC = MACField.getText().toString();

                registerDevice(user, MAC);
            }
        });
    }

    private void registerDevice(String user, String mac) {

        Socket socket = new Socket();

        //send user and device MAC to server to be registered

        Toast.makeText(RegistroDispositivos.this, "Dispositivo registrado.",
                Toast.LENGTH_SHORT).show();

        cleanMACField();
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
