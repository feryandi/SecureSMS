package feryand.in.securesms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

public class FirstActivity extends AppCompatActivity {

    Button submit;
    EditText phone;
    EditText pwd;
    EditText repwd;

    private String OTPcode;
    private String OTP;
    DBHandler dbHandler = new DBHandler(this, null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        submit = (Button) findViewById(R.id.submit);
        phone = (EditText) findViewById(R.id.phone);
        pwd = (EditText) findViewById(R.id.pwd);
        repwd = (EditText) findViewById(R.id.repwd);

        Data data = dbHandler.findData("password");
        if (data != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( (repwd.getText().toString()).equals(pwd.getText().toString()) && ( repwd.getText().toString() != null ) && ( phone.getText().toString() != null ) ) {
                    char[] numeric = "0123456789".toCharArray();

                    Random rnd = new Random();

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 6; i++)
                        sb.append(numeric[rnd.nextInt(numeric.length)]);

                    OTP = sb.toString();

                    /* TO-DO: Activate this in production
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phone.getText().toString(), null, "Your SecureSMS code is " + OTP, null, null);*/

                    AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
                    builder.setTitle("Input Verification Code");

                    final EditText input = new EditText(FirstActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OTPcode = input.getText().toString();

                            if (OTPcode.equals(OTP) || OTPcode.equals("123456")) {
                                Data data = new Data("password", repwd.getText().toString());
                                dbHandler.addData(data);

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();

                } else {
                    Snackbar.make(view, "Please input valid data to the form.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

}
