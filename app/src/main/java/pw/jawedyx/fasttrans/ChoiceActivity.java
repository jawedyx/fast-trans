package pw.jawedyx.fasttrans;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;

public class ChoiceActivity extends AppCompatActivity {

    public static final String BUS = "bus";
    public static final String TRAIN = "train";
    public static final String TROLL = "troll";
    public static final String CHOICE_TAG = "transport";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        CardView busCard = findViewById(R.id.bus_card);
        CardView trainCard = findViewById(R.id.train_card);
        CardView trollCard = findViewById(R.id.troll_card);

        busCard.setOnClickListener( v-> choice(BUS));
        trainCard.setOnClickListener( v -> choice(TRAIN));
        trollCard.setOnClickListener( v -> choice(TROLL));
    }

    private void choice(String bus) {
        Intent cIntent = new Intent(getApplicationContext(), RoutesActivity.class);

        switch (bus){
            case BUS:
                cIntent.putExtra(CHOICE_TAG, BUS);
                break;
            case TRAIN:
                cIntent.putExtra(CHOICE_TAG, TRAIN);
                break;
            case TROLL:
                cIntent.putExtra(CHOICE_TAG, TROLL);
                break;
        }

        startActivity(cIntent);
    }
}
