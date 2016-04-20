package feryand.in.securesms;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    /* Variables */
    ListView inbox;
    SimpleCursorAdapter adapter;
    String[] readSmsPerms = {Manifest.permission.READ_SMS};
    String[] writeSmsPerms = {Manifest.permission.SEND_SMS};
    String[] internetPerms = {Manifest.permission.INTERNET};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Inbox");

        inbox = (ListView) findViewById(R.id.inbox);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, readSmsPerms, 1);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, writeSmsPerms, 2);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, internetPerms, 3);
            return;
        }

        Uri inboxURI = Uri.parse("content://sms/inbox");
        String[] reqCols = new String[] { "_id", "address", "body" };
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(inboxURI, reqCols, null, null, null);
        adapter = new SimpleCursorAdapter(this, R.layout.inbox_list, c,
                new String[] { "body", "address" }, new int[] {
                R.id.message, R.id.sender }, 2);
        inbox.setAdapter(adapter);

        inbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Object o = inbox.getItemAtPosition(position);
                Cursor item = (Cursor) o;
                Intent i = new Intent(getApplicationContext(), ReadActivity.class);
                i.putExtra("message", item.getString(item.getColumnIndex("body")));
                i.putExtra("sender", item.getString(item.getColumnIndex("address")));
                startActivity(i);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), WriteActivity.class);
                startActivity(i);
            }
        });
    }

    //fungsi request permision
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                    return;
                }

            case 2:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                    return;
                }
            case 3:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                    return;
                }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
