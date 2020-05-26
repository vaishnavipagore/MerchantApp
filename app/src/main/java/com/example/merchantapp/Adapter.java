package com.example.merchantapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {




    private List<ModelClass> modelClassList;

    public Adapter(List<ModelClass> modelClassesList){
        this.modelClassList = modelClassesList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = modelClassList.get(position).getTitle();
        String tid = modelClassList.get(position).getTid();
        String rrn = modelClassList.get(position).getRrn();
        String amount = modelClassList.get(position).getAmount();
        holder.setData(title,tid,rrn,amount);


    }

    @Override
    public int getItemCount() {
        return modelClassList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{



        private TextView title;
        private TextView tid;
        private TextView rrn;
        private CardView cardView;
        private TextView amount;


        public ViewHolder(@NonNull final View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.textView2);
            tid = itemView.findViewById(R.id.mytid);
            rrn = itemView.findViewById(R.id.myrrn);
            amount = itemView.findViewById(R.id.amount);
            cardView = itemView.findViewById(R.id.card_view2);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Intent i = new Intent(Adapter.this,PerTransaction.class);
                   Integer i = getLayoutPosition();

                   Log.d("Position is ",i.toString());


                    History transactionHistory = new History();
                   // transactionHistory.myFunc(i);


                }


            });


        }
        private void setData(String titleText,String tid1,String rrn1,String amount1){

            title.setText(titleText);
            tid.setText(tid1);
            rrn.setText(rrn1);
            amount.setText(amount1);


        }
    }
}
