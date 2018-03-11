package pw.jawedyx.fasttrans;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Request;
import okhttp3.Response;

import static pw.jawedyx.fasttrans.RoutesActivity.DELIMITER;

public class StreetActivity extends AppCompatActivity {
    private TextView fromSide, toSide;
    private ImageView swap;
    private RecyclerView recycler;
    private ArrayList<String> data = new ArrayList<>();
    private StreetCardAdapter adapter;
    private ConstraintLayout swapper;
    private ProgressBar streetBar;
    private int q = 0; //0 - прямое направление, 1 - обратное

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street);

        fromSide = findViewById(R.id.street_from);
        toSide = findViewById(R.id.street_to);
        swap = findViewById(R.id.street_swap);
        recycler = findViewById(R.id.recycler_street);
        swapper = findViewById(R.id.swapper);
        streetBar = findViewById(R.id.streetbar);

        if(data != null && data.size() > 0) {
            setData();

        }else{ //Загрузить данные, если их нет
            // TODO: 11.03.2018 backstack\states
            String[] args = {getIntent().getStringExtra(RoutesActivity.URL_TAG) + getIntent().getStringExtra(RoutesActivity.NEXT_URL_TAG)};

            StreetGetter streetGetter = new StreetGetter();
            streetGetter.execute(args);
            Log.wtf("jawex", "Выкачка");
        }
    }


    class StreetGetter extends AsyncTask<String, Void, ArrayList<String>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recycler.setVisibility(View.GONE);
            streetBar.setVisibility(View.VISIBLE);
            swapper.setVisibility(View.GONE);
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            String result;

            Request request = new Request.Builder().url(strings[0]).build();

            try(Response response = App.getClient().newCall(request).execute()){

                if (!response.isSuccessful()) throw new IOException("Не удалось получить данные " + response);

                result = StringEscapeUtils.unescapeHtml4 (response.body().string());

                Pattern hrefPattern = Pattern.compile("<a href='rasp(.*?)q=0(.*?)'>(.*?)</a><br/>");
                Matcher hrefMatcher = hrefPattern.matcher(result);

                while (hrefMatcher.find()){
                    String title = hrefMatcher.group(3).trim();
                    title = title.replaceFirst("�", "«");
                    title = title.replaceFirst("�", "»");
                    data.add("rasp" + hrefMatcher.group(1) + "q=" +q + hrefMatcher.group(2) + DELIMITER + title);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }


        @Override
        protected void onPostExecute(ArrayList<String> data) {
            super.onPostExecute(data);

            setData();
            streetBar.setVisibility(View.GONE);
            swapper.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.VISIBLE);
        }
    }

    private void setData() {
        if(data != null && data.size() > 0){
            StreetCardAdapter adapter = new StreetCardAdapter(data);
            adapter.setListener(position -> {
                // TODO: 11.03.2018 Listener
            });
            recycler.setAdapter(adapter);

            String from = data.get(0).split(DELIMITER)[1];
            String to = data.get(data.size()-1).split(DELIMITER)[1];

            fromSide.setText(from);
            toSide.setText(to);

            swap.setOnClickListener(view -> {
                if(q == 0){
                    fromSide.setText(to);
                    toSide.setText(from);
                    q = 1;
                }else{
                    fromSide.setText(from);
                    toSide.setText(to);
                    q = 0;
                }

                Collections.reverse(data);
                adapter.notifyDataSetChanged();
            });
        }

    }



}
