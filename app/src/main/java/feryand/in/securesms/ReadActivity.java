package feryand.in.securesms;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import feryand.in.securesms.API.HTTP;
import feryand.in.securesms.ECDSA.ECDSA;
import feryand.in.securesms.ECDSA.Point;
import feryand.in.securesms.ECDSA.SHA1;
import feryand.in.securesms.blockchipher.Block;
import feryand.in.securesms.blockchipher.Bonek;

public class ReadActivity extends AppCompatActivity {

    TextView snd;
    TextView msg;
    TextView cert;
    String deckey;

    SMS sms;

    private class AsyncGetKey extends AsyncTask<String, String, String> {

        String response;
        private ProgressDialog dialog;

        public AsyncGetKey(ReadActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Verifying message signature. Please wait.");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            response = HTTP.getInstance().getKey(params[0].substring(1, params[0].length()));
            return null;
        }

        protected void onPostExecute(String result){
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            try {
                ECDSA ec = new ECDSA();
                JSONObject json = new JSONObject(response);

                Point pub = new Point(new BigInteger(json.getString("x"), 16), new BigInteger(json.getString("y"), 16), ec.prime);
                Log.d("SSMS", "x: " + json.getString("x") + " | y: " + json.getString("y"));
                SHA1 s = new SHA1(sms.getPlainMessage());

                String ds = sms.getDigitalSignature();
                Log.d("SSMS", "ds: " + ds);
                String dsx = ds.substring(0, 64);
                Log.d("SSMS", "dsX: " + dsx);
                String dsy = ds.substring(64, 128);
                Log.d("SSMS", "dsY: " + dsy);


                Point rs = new Point(new BigInteger(dsx, 16), new BigInteger(dsy, 16), ec.prime);

                if (ec.verifySignature(rs, s.getDigest(), pub)) {
                    cert.setText("Verified");
                } else {
                    cert.setText("Verification Failed");
                }

            } catch (Exception e) {
                Log.d("SSMS", "Exception: " + e);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Message");

        snd = (TextView) findViewById(R.id.sender);
        msg = (TextView) findViewById(R.id.message);
        cert = (TextView) findViewById(R.id.cert);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sms = new SMS(extras.getString("message"), extras.getString("sender"));

            snd.setText(sms.getSender());

            if(sms.isModifiedMessage()) {
                msg.setText(sms.getPlainMessage());

                if (sms.isEncrypted()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ReadActivity.this);
                    builder.setTitle("Input Decryption Key");

                    final EditText input = new EditText(ReadActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deckey = input.getText().toString();
                            String bodymessage = sms.getPlainMessage();
                            byte[] bmessage = Bonek.hexa_to_byte(bodymessage);
                            ArrayList<Byte> lmessage = Bonek.arrayToList(bmessage);
                            Bonek bonek = new Bonek();
                            ArrayList<Block> to = bonek.decrypt(Bonek.byte_to_block(lmessage), Bonek.hexa_to_key(deckey));
                            ArrayList<Byte> listByte = Bonek.block_to_byte(to);
                            byte[] arrbyte = Bonek.listToArray(listByte);
                            Log.d("masuk", arrbyte.toString());
                            msg.setText(new String(arrbyte));
                        }
                    });
                    builder.show();
                }

                if (sms.isHaveSignature()) {

                    try {

                        new AsyncGetKey(this).execute(snd.getText().toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                msg.setText(sms.getMessage());
            }

        }
    }

}
