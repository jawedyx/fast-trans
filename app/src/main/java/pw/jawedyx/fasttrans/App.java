package pw.jawedyx.fasttrans;

import android.app.Application;

import okhttp3.OkHttpClient;


public class App extends Application {
    private static OkHttpClient client;
    private static int choiceId;


    public static int getChoiceId() {
        return choiceId;
    }

    public static void setChoiceId(int choiceId) {
        App.choiceId = choiceId;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        client = new OkHttpClient();
        choiceId = 0;
    }

    public static OkHttpClient getClient() {
        return client;
    }

}
