package pw.jawedyx.fasttrans;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RoutesActivity extends AppCompatActivity {
    public static int BUS_ID = 1;
    public static int TROLL_ID = 2;
    public static int TRAIN_ID = 3;
    public static String URL;
    public static final String DELIMITER = "@@@";
    private OkHttpClient client = new OkHttpClient();
    private int id;
    private RecyclerView recycler;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        recycler = findViewById(R.id.recycler_choice);
        progressBar = findViewById(R.id.progress);
        URL = getResources().getString(R.string.routes_url);

        switch (getIntent().getStringExtra(ChoiceActivity.CHOICE_TAG)){
            case "bus": id = BUS_ID; break;
            case "troll": id = TROLL_ID; break;
            case "train": id = TRAIN_ID; break;
        }

        String[] args = {URL + id};
        Getter getter = new Getter();
        getter.execute(args);

        recycler.setAdapter(new RoutesCardAdapter());

    }


    class Getter extends AsyncTask<String, Void, ArrayList<String>>{

        ArrayList<String> data = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recycler.setVisibility(View.GONE);
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            String result = null;

            Request request = new Request.Builder().url(strings[0]).build();

            try(Response response = client.newCall(request).execute()){

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

            RoutesCardAdapter adapter = new RoutesCardAdapter(data);
            adapter.setListener(position -> {
                // TODO: 11.03.2018 Реализовать клик
            });
            recycler.setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);


        }
    }
}
