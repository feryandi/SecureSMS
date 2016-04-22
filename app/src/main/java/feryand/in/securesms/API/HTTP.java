package feryand.in.securesms.API;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Feryandi on 20 April 16.
 */
public class HTTP {
    private static HTTP mInstance = null;
    String dataUrl = "http://feryand.in/api/ssms.php";
    URL url;
    HttpURLConnection connection = null;

    private HTTP(){

    }

    public static HTTP getInstance(){
        if(mInstance == null)
        {
            mInstance = new HTTP();
        }
        return mInstance;
    }

    public String getKey(String phone) {
        String responseStr = "";
        try {
            String dataUrlParameters = "phone=" + URLEncoder.encode(phone, "UTF-8");

            url = new URL(dataUrl + "?" + dataUrlParameters);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            responseStr = response.toString();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (connection != null) {
                connection.disconnect();
            }

            Log.d("HTTP", responseStr);

            return responseStr;
        }
    }

    public String setKey(String phone, String x, String y) {
        String responseStr = "";
        try {
            if ( phone.substring(0,1).equals("0") ) {
                phone = "+62" + phone.substring(1, phone.length());
            }
            String dataUrlParameters = "phone=" + phone + "&x=" + x + "&y=" + y;
            url = new URL(dataUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length","" + Integer.toString(dataUrlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(dataUrlParameters);
            wr.flush();
            wr.close();

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            responseStr = response.toString();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (connection != null) {
                connection.disconnect();
            }

            Log.d("HTTP", responseStr);
            return responseStr;
        }
    }
}
