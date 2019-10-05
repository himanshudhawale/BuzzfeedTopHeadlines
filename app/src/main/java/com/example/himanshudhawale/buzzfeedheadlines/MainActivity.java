package com.example.himanshudhawale.buzzfeedheadlines;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView publishedtv, descriptiontv, title, pub, textView4,headline;
    private Button quitButton;
    ImageView prev, next, image;
    ArrayList<News> listGlobal= new ArrayList<>();
    int index;
    AlertDialog.Builder builder;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Views
        publishedtv = findViewById(R.id.publishId);
        descriptiontv = findViewById(R.id.scrollTextViewId);
        quitButton = findViewById(R.id.buttonId);
        prev = findViewById(R.id.prevId);
        next = findViewById(R.id.nextId);
        title = findViewById(R.id.headlineId);
        image = findViewById(R.id.imageViewId);
        pub=findViewById(R.id.publishedId);
        headline=findViewById(R.id.headlineId);
        textView4=findViewById(R.id.textView4);


        publishedtv.setVisibility(View.INVISIBLE);
        descriptiontv.setVisibility(View.INVISIBLE);
        quitButton.setVisibility(View.INVISIBLE);
        prev.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);
        title.setVisibility(View.INVISIBLE);
        image.setVisibility(View.INVISIBLE);

        pub.setVisibility(View.INVISIBLE);
        headline.setVisibility(View.INVISIBLE);
        textView4.setVisibility(View.INVISIBLE);



        if (isConnected()) {


             builder = new AlertDialog.Builder(this);

            LayoutInflater inflater = this.getLayoutInflater();

            builder.setTitle("Loading News").setView(inflater.inflate(R.layout.dialog_bar, null));

            dialog = builder.create();
            dialog.show();

            new GetDataAsync().execute("https://newsapi.org/v2/top-headlines?sources=buzzfeed&apiKey=3ebe0585da3f428b92a968a3ec24f600");


        }



            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (index - 1 <= -1) {
                        index = 0;
                        Toast.makeText(MainActivity.this, "You're on the first time!", Toast.LENGTH_SHORT).show();
                    } else {
                        index--;
                    }
                    String date = listGlobal.get(index).publishedAt.split("T")[0];
                    publishedtv.setText(date);
                    title.setText(listGlobal.get(index).title);
                    descriptiontv.setText(listGlobal.get(index).description);

                    Picasso.get().load(listGlobal.get(index).urlToImage).into(image);
                }
            });



            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (index + 1 >= listGlobal.size()) {

                        index = index;
                        Toast.makeText(MainActivity.this, "You're on the last item!", Toast.LENGTH_SHORT).show();
                    } else {
                        index=index+1;
                    }


                    String date = listGlobal.get(index).publishedAt.split("T")[0];
                    publishedtv.setText(date);
                    title.setText(listGlobal.get(index).title);
                    descriptiontv.setText(listGlobal.get(index).description);

                    Picasso.get().load(listGlobal.get(index).urlToImage).into(image);


                }
            });


            quitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                    System.exit(0);

                }
            });

        }


    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }


    private class GetDataAsync extends AsyncTask<String, Void, ArrayList<News>> {
        @Override
        protected ArrayList<News> doInBackground(String... params) {

            StringBuilder stringBuilder = new StringBuilder();

            String result=null;
            int i;
            HttpURLConnection connection = null;
            ArrayList<News> listOfNews = new ArrayList<>();
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }



                    result = stringBuilder.toString();

                    JSONObject root = new JSONObject(result);

                    JSONArray articles=root.getJSONArray("articles");

                    for( i=0;i<articles.length(); i++)
                    {
                        JSONObject newsJson= articles.getJSONObject(i);
                        News news= new News();
                        news.title=newsJson.getString("title");
                        news.description=newsJson.getString("description");
                        news.urlToImage=newsJson.getString("urlToImage");
                        news.publishedAt=newsJson.getString("publishedAt");


                        listOfNews.add(news);

                    }


                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return listOfNews;
        }

        @Override
        protected void onPostExecute(ArrayList<News> news) {
            super.onPostExecute(news);


            publishedtv.setVisibility(View.VISIBLE);
            descriptiontv.setVisibility(View.VISIBLE);
            quitButton.setVisibility(View.VISIBLE);
            prev.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);

            pub.setVisibility(View.VISIBLE);
            headline.setVisibility(View.VISIBLE);
            textView4.setVisibility(View.VISIBLE);



            listGlobal=news;
            index=0;


                 News news1= news.get(0);



                String date=news1.publishedAt.split("T")[0];
                publishedtv.setText(date);
                title.setText(news1.title);
                descriptiontv.setText(news1.description);

                Picasso.get().load(news1.urlToImage).into(image);



                dialog.dismiss();



        }
    }
}
