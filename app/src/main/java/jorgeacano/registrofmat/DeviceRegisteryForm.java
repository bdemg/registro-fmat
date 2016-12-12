package jorgeacano.registrofmat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class DeviceRegisteryForm extends AppCompatActivity {

    private EditText MACField;
    private final int COMPLETE_MAC_ADDRESS_LENGTH = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_registro_dispositivos );

        TextView username = (TextView) findViewById(R.id.lblUserName);
        username.setText( getIntent().getStringExtra( IntentResourcesIDs.USER_NAME ) );

        TextView email = (TextView) findViewById(R.id.lblUserEmail);
        email.setText( getIntent().getStringExtra( IntentResourcesIDs.EMAIL ) );

        this.MACField = (EditText) findViewById(R.id.fieldMAC);

        setupMACFieldFormat();
        addSubmitButtonListener();
        addToUnlistFormButtonListener();
    }

    private void addToUnlistFormButtonListener() {

        Button toUnlistFormButton = (Button) findViewById(R.id.ToUnlistDevicebutton);

        toUnlistFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enterUnlisting(
                        getIntent().getStringExtra(IntentResourcesIDs.USER_NAME),
                        getIntent().getStringExtra(IntentResourcesIDs.EMAIL),
                        getIntent().getStringExtra(IntentResourcesIDs.REGISTRATION_NUMBER)
                );

            }
        });
    }

    private void addSubmitButtonListener() {

        Button submitButton = (Button) findViewById(R.id.btnSubmitMAC);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String registrationNumber = getIntent().getStringExtra( IntentResourcesIDs.REGISTRATION_NUMBER );
                String mac = MACField.getText().toString();

                if(mac.length() == COMPLETE_MAC_ADDRESS_LENGTH) {
                    registerDevice(registrationNumber, mac);
                }
                else{
                    NotifMessager.getInstance().showMessage(DeviceRegisteryForm.this,
                            NotifMessager.INCOMPLETE_MAC);
                }
            }
        });
    }

    private void registerDevice(String registrationNumber, String mac) {

        try {

            PrintWriter outputToServer = new PrintWriter(
                    ServerConnection.getInstance().getOutputStream());

            outputToServer.println(ServiceCodes.REGISTER_MAC);
            outputToServer.flush();
            outputToServer.println(registrationNumber.toLowerCase());
            outputToServer.flush();
            outputToServer.println(mac.toUpperCase());
            outputToServer.flush();

            NotifMessager.getInstance().showMessage(this, NotifMessager.DEVICE_REGISTERED);

            cleanMACField();
        }catch (IOException ex){
            NotifMessager.getInstance().showMessage(this, NotifMessager.SERVER_CONNECTION_LOST);
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

    private void enterUnlisting( String userName, String email, String registrationNumber ){

        Intent intent = new Intent( getBaseContext(), DeviceUnlistingForm.class );
        intent.putExtra( IntentResourcesIDs.USER_NAME, userName );
        intent.putExtra( IntentResourcesIDs.EMAIL, email );
        intent.putExtra(IntentResourcesIDs.REGISTRATION_NUMBER, registrationNumber);
        startActivity(intent);
    }
}
