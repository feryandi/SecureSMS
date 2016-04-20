package feryand.in.securesms;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Feryandi on 21 April 16.
 */

public class SSMSAdapter extends ArrayAdapter<SMS> {

    public SSMSAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public SSMSAdapter(Context context, int resource, List<SMS> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.inbox_list, null);
        }

        SMS p = getItem(position);

        if (p != null) {
            TextView sender = (TextView) v.findViewById(R.id.sender);
            TextView message = (TextView) v.findViewById(R.id.message);

            if (sender != null) {
                sender.setText(p.getSender());
            }

            if (message != null) {
                message.setText(p.getPlainMessage());
            }

        }

        return v;
    }

}
