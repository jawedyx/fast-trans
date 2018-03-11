package pw.jawedyx.fasttrans;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class StreetCardAdapter extends RecyclerView.Adapter<StreetCardAdapter.ViewHolder>{
    private ArrayList streets;
    private Listener listener;


    public interface Listener{
        void onClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }


    public StreetCardAdapter(){ }

    public StreetCardAdapter(ArrayList streets){
        this.streets = streets;
    }

    @Override
    public StreetCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.street_card, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(StreetCardAdapter.ViewHolder holder, final int position) {

        CardView cardView = holder.cardView;
        TextView streetName, details;
        String data = streets.get(position).toString();
        String[] sData = data.split(RoutesActivity.DELIMITER);

        streetName = cardView.findViewById(R.id.street_name);
        details = cardView.findViewById(R.id.street_details);

        cardView.setOnClickListener(v -> {
            if(listener != null) {
                listener.onClick(position);
            }
        });

        streetName.setText(sData[1].trim());
        details.setText(sData[0].trim());

        cardView.setOnClickListener(v -> {
            if(listener != null) {
                listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(streets != null) return streets.size();
        return 0;
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

}
