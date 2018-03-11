package pw.jawedyx.fasttrans;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;

public class ChoiceActivity extends AppCompatActivity {

    public static final int BUS_ID = 1;
    public static final int TROLL_ID = 2;
    public static final int TRAIN_ID = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        CardView busCard = findViewById(R.id.bus_card);
        CardView trainCard = findViewById(R.id.train_card);
        CardView trollCard = findViewById(R.id.troll_card);

        busCard.setOnClickListener( v-> choice(BUS_ID));
        trainCard.setOnClickListener( v -> choice(TRAIN_ID));
        trollCard.setOnClickListener( v -> choice(TROLL_ID));
    }

    private void choice(int bus) {
        Intent cIntent = new Intent(getApplicationContext(), RoutesActivity.class);

        switch (bus){
            case BUS_ID:
                App.setChoiceId(BUS_ID);
                break;
            case TRAIN_ID:
                App.setChoiceId(TRAIN_ID);
                break;
            case TROLL_ID:
                App.setChoiceId(TROLL_ID);
                break;
        }

        startActivity(cIntent);
    }
}
