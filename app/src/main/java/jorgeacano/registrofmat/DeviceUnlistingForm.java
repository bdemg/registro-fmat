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

import java.io.IOException;
import java.io.PrintWriter;

public class DeviceUnlistingForm extends AppCompatActivity {

    private EditText MACDevice1;
    private EditText MACDevice2;
    private final int COMPLETE_MAC_ADDRESS_LENGTH = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_unlisting_form);

        TextView username = (TextView) findViewById(R.id.lblUserName);
        username.setText( getIntent().getStringExtra( IntentResourcesIDs.USER_NAME ) );

        TextView email = (TextView) findViewById(R.id.lblUserEmail);
        email.setText(getIntent().getStringExtra(IntentResourcesIDs.EMAIL));

        this.MACDevice1 = (EditText) findViewById(R.id.fieldMACDevice1);
        this.MACDevice2 = (EditText) findViewById(R.id.fieldMACDevice2);

        setupMACFieldFormat(this.MACDevice1);
        setupMACFieldFormat(this.MACDevice2);

        this.MACDevice1.setKeyListener(null);
        this.MACDevice2.setKeyListener(null);

        addUnlist1rstDeviceButtonListener();
    }

    private void addUnlist1rstDeviceButtonListener() {

        Button unlist1rstDeviceButton = (Button) findViewById(R.id.unlist1rstDevicebutton);

        unlist1rstDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = getIntent().getStringExtra( IntentResourcesIDs.USER_NAME );
                String mac = MACDevice1.getText().toString();

                unlistDevice(user, mac);
            }
        });
    }


    private void addUnlist2ndDeviceButtonListener() {

        Button unlist2ndDeviceButton = (Button) findViewById(R.id.unlist1rstDevicebutton);

        unlist2ndDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = getIntent().getStringExtra(IntentResourcesIDs.USER_NAME);
                String mac = MACDevice2.getText().toString();
                if(mac.length() == COMPLETE_MAC_ADDRESS_LENGTH) {
                    unlistDevice(user, mac);
                }
                else{
                    NotifMessager.getInstance().showMessage(DeviceUnlistingForm.this,
                            NotifMessager.INCOMPLETE_MAC);
                }
            }
        });
    }

    private void unlistDevice(String user, String mac) {

        try {
            PrintWriter outputToServer = new PrintWriter(
                    ServerConnection.getInstance().getOutputStream());

            outputToServer.println(user);
            outputToServer.println(mac.toUpperCase());

            NotifMessager.getInstance().showMessage(this, NotifMessager.DEVICE_UNLISTED);

            outputToServer.close();

        }catch (IOException ex){
            NotifMessager.getInstance().showMessage(this,NotifMessager.SERVER_CONNECTION_LOST);
        }
    }


    private void setupMACFieldFormat(EditText MACField) {
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
