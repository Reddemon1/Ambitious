package com.example.ambitious;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class reqAdapter extends RecyclerView.Adapter<reqAdapter.MyViewHolder> {
    Context context;
    ArrayList<tbrequest> list;
    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public reqAdapter(Context context, ArrayList<tbrequest> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemrequest,parent,false);
        return new MyViewHolder(v , mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        tbrequest tbrequest = list.get(position);
        holder.reqNama.setText(tbrequest.getName());
        holder.reqEmail.setText(tbrequest.getEmail());
    }



    private void getResources() {

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView reqNama , reqEmail;
        CardView reqCard;

        public MyViewHolder(@NonNull View itemView , OnItemClickListener listener ) {
            super(itemView);
            reqCard = itemView.findViewById(R.id.reqCard);
            reqNama = itemView.findViewById(R.id.reqNama);
            reqEmail = itemView.findViewById(R.id.reqEmail);

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
