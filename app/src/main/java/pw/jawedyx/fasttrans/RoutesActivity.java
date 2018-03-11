package pw.jawedyx.fasttrans;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Request;
import okhttp3.Response;


public class RoutesActivity extends AppCompatActivity {

    public static String URL;
    public static final String DELIMITER = "@@@";
    public static final String URL_TAG = "url_tag";
    public static final String NEXT_URL_TAG = "next_url_tag";


    private int id;
    private String currentUrl;
    private RecyclerView recycler;
    private ProgressBar progressBar;
    private  ArrayList<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        id = App.getChoiceId();
        Log.wtf("jawex", " " + id);

        recycler = findViewById(R.id.recycler_choice);
        progressBar = findViewById(R.id.progress);
        URL = getResources().getString(R.string.routes_url);

        currentUrl = URL + id;
        String[] args = {currentUrl};


        Getter getter = new Getter();
        getter.execute(args);

        recycler.setAdapter(new RoutesCardAdapter());

    }

    @Override
    protected void onPause() {
        super.onPause();
        id = App.getChoiceId();
    }



    class Getter extends AsyncTask<String, Void, ArrayList<String>>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recycler.setVisibility(View.GONE);
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            String result;

            Request request = new Request.Builder().url(strings[0]).build();

            try(Response response = App.getClient().newCall(request).execute()){

                if (!response.isSuccessful()) throw new IOException("Не удалось получить данные " + response);
                result = StringEscapeUtils.unescapeHtml4 (response.body().string());

                Pattern hrefPattern = Pattern.compile("<a href='(.*?)'>(.*?)</a>(.*?)<br/>");
                Matcher hrefMatcher = hrefPattern.matcher(result);


                while (hrefMatcher.find()){
                    if(hrefMatcher.group(1).trim().length() > 20){
                        int href = hrefMatcher.group(1).indexOf('&');
                        String hrefFragment = hrefMatcher.group(1).substring(href);

                        data.add(hrefMatcher.group(2) + DELIMITER + hrefMatcher.group(3).trim() + DELIMITER + hrefFragment);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }


        @Override
        protected void onPostExecute(ArrayList<String> data) {
            super.onPostExecute(data);
            setRoutes();
        }
    }

    private void setRoutes() {
        RoutesCardAdapter adapter = new RoutesCardAdapter(data);
        adapter.setListener(position -> {
            Intent cStreet = new Intent(getApplicationContext(), StreetActivity.class);
            currentUrl = currentUrl.replace("list.php", "config.php");
            String[] sData = data.get(position).split(DELIMITER);

            cStreet.putExtra(URL_TAG, currentUrl);
            cStreet.putExtra(NEXT_URL_TAG, sData[2]);
            startActivity(cStreet);
        });
        recycler.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);
        recycler.setVisibility(View.VISIBLE);


    }


}
