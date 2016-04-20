package feryand.in.securesms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import feryand.in.securesms.ECDSA.ECDSA;
import feryand.in.securesms.blockchipher.Block;
import feryand.in.securesms.blockchipher.Bonek;

public class ReadActivity extends AppCompatActivity {

    TextView snd;
    TextView msg;
    String message;
    String deckey;
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
            message= extras.getString("message");
            snd.setText(extras.getString("sender"));
            msg.setText(message);

            if(isModifiedMessage(message)){
                AlertDialog.Builder builder = new AlertDialog.Builder(ReadActivity.this);
                builder.setTitle("Input Decryption Key");

                final EditText input = new EditText(ReadActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deckey = input.getText().toString();
                        String bodymessage= getMessage(message);
                        byte[] bmessage = Bonek.hexa_to_byte(bodymessage);
                        ArrayList<Byte> lmessage=Bonek.arrayToList(bmessage);
                        Bonek bonek = new Bonek();
                        ArrayList<Block> to = bonek.decrypt(Bonek.byte_to_block(lmessage),Bonek.hexa_to_key(deckey));
                        ArrayList<Byte> listByte= Bonek.block_to_byte(to);
                        byte[] arrbyte = Bonek.listToArray(listByte);
                        msg.setText(arrbyte.toString());
                    }
                });

            }

        }
    }

    String getMessage(String str){
        int startpos=str.indexOf("</ss>")+5;
        int endpos= str.indexOf("<ds>");
        return str.substring(startpos,endpos);

        //return str.substring(11,str.length()-<panjang digital signaturenya>) atau pake panjang fixed
    }

    String getOption(String str){
        return str.substring(4,6);
    }

    String getdigitalsignature(String str){
        int startpos=str.indexOf("<ds>")+4;
        int endpos= str.indexOf("</ds>");
        return str.substring(startpos,endpos);
    }

    boolean isModifiedMessage(String str){
        if(str.indexOf("<ss>")>=0){
            return true;
        }
        else
            return false;
    }



}
