package feryand.in.securesms;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

import feryand.in.securesms.ECDSA.ECDSA;
import feryand.in.securesms.ECDSA.Point;
import feryand.in.securesms.ECDSA.SHA1;

public class WriteActivity extends AppCompatActivity {

    Button sendPlain;
    EditText receiver;
    EditText message;

    CheckBox E;
    EditText EK;
    CheckBox C;

    DBHandler dbHandler = new DBHandler(this, null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Message");

        sendPlain = (Button) findViewById(R.id.btnPlain);
        receiver = (EditText) findViewById(R.id.receiver);
        message = (EditText) findViewById(R.id.message);

        E = (CheckBox) findViewById(R.id.encryption);
        EK = (EditText) findViewById(R.id.encKey);
        C = (CheckBox) findViewById(R.id.signature);

        sendPlain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendSMSMessage(view);
            }
        });
    }

    protected void sendSMSMessage(View v) {
        String r = receiver.getText().toString();
        String m = message.getText().toString();

        String option = "";

        if ( E.isChecked() ) {
            option += "E";
        } else {
            option += "D";
        }

        if ( C.isChecked() ) {
            option += "C";
        } else {
            option += "N";
        }

        m = "<ss>" + option + "</ss>" + m;

        if ( C.isChecked() ) {
            ECDSA ec = new ECDSA();
            Data dataPri = dbHandler.findData("pri");
            ec.setPri(dataPri.getValue());
            SHA1 s = new SHA1(m);
            Point rs = ec.generateSignature(s.getDigest());
            m += "<ds>04" + (rs.getX()).toString(16) + "" + (rs.getY()).toString(16) + "</ds>";
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();

            ArrayList<String> parts = smsManager.divideMessage(m);
            smsManager.sendMultipartTextMessage(r, null, parts, null, null);

            //smsManager.sendTextMessage(r, null, m, null, null);
            Snackbar.make(v, "Your message already sent.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            //finish();
        } catch (Exception e) {
            Snackbar.make(v, "We cannot send the message. Please try again. \n Error: " + e, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

}
