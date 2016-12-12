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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
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
        addUnlist2ndDeviceButtonListener();

        try {

            retriveMACs();
        } catch (IOException e) {
            NotifMessager.getInstance().showMessage(DeviceUnlistingForm.this,
                    NotifMessager.SERVER_CONNECTION_LOST);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void retriveMACs() throws IOException, ClassNotFoundException{

        PrintWriter outputToServer = new PrintWriter(
                ServerConnection.getInstance().getOutputStream());

        outputToServer.println(ServiceCodes.REQUEST_REGISTERED_MACS);
        outputToServer.flush();
        outputToServer.println(getIntent().getStringExtra(IntentResourcesIDs.REGISTRATION_NUMBER));
        outputToServer.flush();

        BufferedReader inputFromServer =
                new BufferedReader(new InputStreamReader(
                        ServerConnection.getInstance().getInputStream()));

        String firstMAC;
        String secondMAC;
        do{

            firstMAC = inputFromServer.readLine();
        }while(firstMAC==null);

        do{

            secondMAC = inputFromServer.readLine();
        }while(secondMAC==null);


        putUpRegisteredMACs(firstMAC, secondMAC);

    }


    private void putUpRegisteredMACs(String firstMAC, String secondMAC) {

        this.MACDevice1.setText(firstMAC);
        this.MACDevice2.setText(secondMAC);
    }


    private void addUnlist1rstDeviceButtonListener() {

        Button unlist1rstDeviceButton = (Button) findViewById(R.id.unlist1rstDevicebutton);

        unlist1rstDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = getIntent().getStringExtra( IntentResourcesIDs.REGISTRATION_NUMBER );
                String mac = MACDevice1.getText().toString();

                if(mac.length() == COMPLETE_MAC_ADDRESS_LENGTH) {
                    unlistDevice(user, mac);
                    MACDevice1.setText("");
                }
                else{
                    NotifMessager.getInstance().showMessage(DeviceUnlistingForm.this,
                            NotifMessager.INCOMPLETE_MAC);
                }
            }
        });
    }


    private void addUnlist2ndDeviceButtonListener() {

        Button unlist2ndDeviceButton = (Button) findViewById(R.id.unlist2ndtDevicebutton);

        unlist2ndDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = getIntent().getStringExtra(IntentResourcesIDs.REGISTRATION_NUMBER);
                String mac = MACDevice2.getText().toString();
                if(mac.length() == COMPLETE_MAC_ADDRESS_LENGTH) {
                    unlistDevice(user, mac);
                    MACDevice2.setText("");
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

            outputToServer.println(ServiceCodes.UNLIST_MAC);
            outputToServer.flush();
            outputToServer.println(mac.toUpperCase());
            outputToServer.flush();

            NotifMessager.getInstance().showMessage(this, NotifMessager.DEVICE_UNLISTED);


        }catch (IOException ex){
            NotifMessager.getInstance().showMessage(this, NotifMessager.SERVER_CONNECTION_LOST);
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
