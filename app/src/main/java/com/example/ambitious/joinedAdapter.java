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

public class joinedAdapter extends RecyclerView.Adapter<joinedAdapter.MyViewHolder> {
    Context context;
    ArrayList<tbparticipant> list;
    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public joinedAdapter(Context context, ArrayList<tbparticipant> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.joined,parent,false);
        return new MyViewHolder(v , mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        tbparticipant tbparticipant = list.get(position);
        holder.joinedName.setText(tbparticipant.getName());
        holder.joinedEmail.setText(tbparticipant.getEmail());
    }



    private void getResources() {

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView joinedEmail,joinedName;
        CardView joinedCard;

        public MyViewHolder(@NonNull View itemView , OnItemClickListener listener ) {
            super(itemView);
            joinedName = itemView.findViewById(R.id.joinedName);
            joinedEmail = itemView.findViewById(R.id.joinedEmail);
            joinedCard = itemView.findViewById(R.id.joinedCard);

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
