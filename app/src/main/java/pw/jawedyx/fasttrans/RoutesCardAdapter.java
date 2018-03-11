package pw.jawedyx.fasttrans;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RoutesCardAdapter extends RecyclerView.Adapter<RoutesCardAdapter.ViewHolder>{
    private ArrayList<String> routes;
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


    public RoutesCardAdapter(){ }

    public RoutesCardAdapter(ArrayList<String> routes){
        this.routes = routes;
    }

    @Override
    public RoutesCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.route_card, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(RoutesCardAdapter.ViewHolder holder, final int position) {

        CardView cardView = holder.cardView;
        TextView side, num, details;
        String data = routes.get(position);
        String[] sData = data.split(RoutesActivity.DELIMITER);

        num = cardView.findViewById(R.id.num);
        side = cardView.findViewById(R.id.side);
        details = cardView.findViewById(R.id.details);

        cardView.setOnClickListener(v -> {
            if(listener != null) {
                listener.onClick(position);
            }
        });

        num.setText(sData[0].trim());
        side.setText(sData[1].trim());
        details.setText(sData[2].trim());

        cardView.setOnClickListener(v -> {
            if(listener != null) {
                listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(routes != null) return routes.size();
        return 0;
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

}
