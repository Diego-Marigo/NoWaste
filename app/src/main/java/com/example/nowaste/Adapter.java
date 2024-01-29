package com.example.nowaste;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {
    Context context;
    ArrayList<Alimenti> listAlimenti;

    public Adapter(Context context, ArrayList<Alimenti> listAlimenti) {
        this.context = context;
        this.listAlimenti = listAlimenti;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.food_list_item, parent, false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Alimenti item = listAlimenti.get(position);
        holder.nomeAlimento.setText(item.nomeAlimento);
        //holder.dataScadenza.setText(item.dataScadenza.toString());
        //holder.quantity.setText(Integer.valueOf(item.quantity));
    }

    @Override
    public int getItemCount() {
        return listAlimenti.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView nomeAlimento;
        TextView quantity;
        TextView dataScadenza;
        Button edit, delete;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            nomeAlimento = itemView.findViewById(R.id.input_name);
            quantity = itemView.findViewById(R.id.input_quantity);
            dataScadenza = itemView.findViewById(R.id.input_date);
            edit = itemView.findViewById(R.id.btn_edit);
            delete = itemView.findViewById(R.id.btn_delete);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO mostrare popup per modificare alimento
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO
                }
            });
        }

    }
}
