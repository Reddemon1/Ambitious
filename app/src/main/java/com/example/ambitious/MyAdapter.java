package com.example.ambitious;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<tblomba> list;
    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public MyAdapter(Context context, ArrayList<tblomba> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v , mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        tblomba tblomba = list.get(position);
        holder.namaLomba.setText(tblomba.getName());
        holder.noParticipant.setText(tblomba.getNoParticipant());
        Picasso.with(context).load(tblomba.getImage()).into(holder.gambarLomba);
    }



    private void getResources() {

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView namaLomba , noParticipant;
        ImageView gambarLomba;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView , OnItemClickListener listener ) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardItem);
            namaLomba = itemView.findViewById(R.id.namaLomba);
            noParticipant = itemView.findViewById(R.id.noParticipant);
            gambarLomba = itemView.findViewById(R.id.gambarLomba);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

    }

}
