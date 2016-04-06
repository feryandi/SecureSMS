package feryand.in.securesms;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FirstActivity extends AppCompatActivity {

    Button submit;
    EditText pwd;
    EditText repwd;

    DBHandler dbHandler = new DBHandler(this, null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        submit = (Button) findViewById(R.id.submit);
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
                if ( (repwd.getText().toString()).equals(pwd.getText().toString()) ) {
                    Data data = new Data("password", repwd.getText().toString());
                    dbHandler.addData(data);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Snackbar.make(view, "Please insert the same password when retyping.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

}
