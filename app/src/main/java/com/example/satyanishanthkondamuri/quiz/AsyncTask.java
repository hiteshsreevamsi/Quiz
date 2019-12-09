package com.example.satyanishanthkondamuri.quiz;

import android.util.Log;

import org.json.*;
import com.loopj.android.http.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class AsyncTask {

    AsyncHttpClient client = new AsyncHttpClient();


    public void getPublicTimeline() throws JSONException {
        client.get("https://www.google.com/", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray

                Log.e("google",response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline

                // Do something with the response System.out.println(tweetText);
            }
        });
    }
    public String getTime() {
        try{
            //Make the Http connection so we can retrieve the time
            HttpClient httpclient = new DefaultHttpClient();
            // I am using yahoos api to get the time
            HttpResponse response = httpclient.execute(new
                    HttpGet("http://developer.yahooapis.com/TimeService/V1/getTime?appid=YahooDemo"));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                // The response is an xml file and i have stored it in a string
                String responseString = out.toString();
                Log.d("Response", responseString);
                //We have to parse the xml file using any parser, but since i have to
                //take just one value i have deviced a shortcut to retrieve it
                int x = responseString.indexOf("<Timestamp>");
                int y = responseString.indexOf("</Timestamp>");
                //I am using the x + "<Timestamp>" because x alone gives only the start value
                Log.d("Response", responseString.substring(x + "<Timestamp>".length(),y) );
                String timestamp =  responseString.substring(x + "<Timestamp>".length(),y);
                // The time returned is in UNIX format so i need to multiply it by 1000 to use it
                Date d = new Date(Long.parseLong(timestamp) * 1000);
                Log.d("Response", d.toString() );
                return d.toString() ;
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        }catch (ClientProtocolException e) {
            Log.d("Response", e.getMessage());
        }catch (IOException e) {
            Log.d("Response", e.getMessage());
        }
        return null;
    }
}
