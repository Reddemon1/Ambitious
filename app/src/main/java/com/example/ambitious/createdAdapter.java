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

public class createdAdapter extends RecyclerView.Adapter<createdAdapter.MyViewHolder> {
    Context context;
    ArrayList<tblomba> list;
    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public createdAdapter(Context context, ArrayList<tblomba> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.created,parent,false);
        return new MyViewHolder(v , mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        tblomba tblomba = list.get(position);
        holder.createdName.setText(tblomba.getName());
        holder.createdEnd.setText(tblomba.getEndDate());
        holder.createdOpen.setText(tblomba.getOpenDate());
        holder.createdParticipant.setText(tblomba.getNoParticipant());
        Picasso.with(context).load(tblomba.getImage()).into(holder.createdImage);
    }



    private void getResources() {

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView createdOpen , createdEnd,createdParticipant,createdName;
        ImageView createdImage;
        CardView createdCard;

        public MyViewHolder(@NonNull View itemView , OnItemClickListener listener ) {
            super(itemView);
            createdOpen = itemView.findViewById(R.id.createdOpen);
            createdEnd = itemView.findViewById(R.id.createdEnd);
            createdImage = itemView.findViewById(R.id.createdImage);
            createdName = itemView.findViewById(R.id.createdName);
            createdParticipant = itemView.findViewById(R.id.createdParticipant);
            createdCard = itemView.findViewById(R.id.createdCard);

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
