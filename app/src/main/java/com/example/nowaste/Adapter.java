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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Nece
public class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {
    Context context;
    ArrayList<AlimentoItem> listAlimenti;

    public Adapter(Context context, ArrayList<AlimentoItem> listAlimenti) {
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
        AlimentoItem item = listAlimenti.get(position);
        holder.nomeAlimento.setText(item.getNomeAlimento());
        holder.dataScadenza.setText(item.getDate());
        holder.quantity.setText(Integer.toString(item.getQuantity()));
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
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Query q = ref.child("Alimenti").child(user.getUid()); // TODO la query non è completa devo recuperarmi l'id dell'alimento ma non so come
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot d : snapshot.getChildren()){
                                d.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        }

    }
    /*
    per la ricerca
    public void setFilteredList(ArrayList<AlimentoItem> filteredList){
        this.listAlimenti = filteredList;
        notifyDataSetChanged();
    }*/
}
