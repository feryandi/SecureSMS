package feryand.in.securesms;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class WriteActivity extends AppCompatActivity {

    Button sendPlain;
    EditText receiver;
    EditText message;

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

        sendPlain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendSMSMessage(view);
            }
        });
    }

    protected void sendSMSMessage(View v) {
        String r = receiver.getText().toString();
        String m = message.getText().toString();

        SHA1 s = new SHA1(m);
        m += "<ds>" + s.getDigest() + "</ds>";

        try {
            SmsManager smsManager = SmsManager.getDefault();

            ArrayList<String> parts = smsManager.divideMessage(m);
            smsManager.sendMultipartTextMessage(r, null, parts, null, null);

            //smsManager.sendTextMessage(r, null, m, null, null);
            Snackbar.make(v, "Your message already sent.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } catch (Exception e) {
            Snackbar.make(v, "We cannot send the message. Please try again. \n Error: " + e, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

}
