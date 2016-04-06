package feryand.in.securesms;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ReadActivity extends AppCompatActivity {

    TextView snd;
    TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Message");

        snd = (TextView) findViewById(R.id.sender);
        msg = (TextView) findViewById(R.id.message);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            snd.setText(extras.getString("sender"));
            msg.setText(extras.getString("message"));
        }
    }



}
