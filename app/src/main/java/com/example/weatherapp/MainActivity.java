package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    TextView weather,city;
    EditText editText;

    class DownLoadTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... urls) {

            URL url = null;
            HttpURLConnection httpURLConnection = null;
            String result="";

            try{

                url=new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data!=-1){
                    char curr = (char)data;
                    result+=curr;
                    data=reader.read();
                }

            }catch(Exception e){
                e.printStackTrace();
            }

            return result;

        }

        @Override
        protected void onPostExecute(String Json) {
            try {

                JSONObject jsonrootobject = new JSONObject(Json);
                JSONArray jsonArray = jsonrootobject.optJSONArray("weather");

                String message = "";
                for(int i=0;i<jsonArray.length();i++){

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String main = jsonObject.optString("main").toString();

                    String description = jsonObject.optString("description").toString();

                    message+=main+":"+description+"\n";
                }

                city.setText(editText.getText());
                weather.setText(message);
                editText.setText("");

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error! Could not find weather  ", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(Json);
        }
    }

    public void run(View view){

        String cityname = editText.getText().toString();

        DownLoadTask task = new DownLoadTask();
        task.execute("https://api.openweathermap.org/data/2.5/weather?q="+cityname+"&appid=b4d589ae5e2e7da2aa9016e6966acb04");

        InputMethodManager manager =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getWindowToken(),0);


    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weather = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        city=  (TextView) findViewById(R.id.city);



    }
}