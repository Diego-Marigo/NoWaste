package com.example.nowaste;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolder> {
    private List<ListaItem> listaItems;
    private Context context;

    public ListaAdapter(List<ListaItem> listaItems, Context context) {
        this.listaItems = listaItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListaItem item = listaItems.get(position);
        holder.textViewNomeLista.setText(item.getNomeLista());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CustomListView.class);
            intent.putExtra("listId", item.getUserId()); // Passa l'ID della lista
            intent.putExtra("nomeLista", item.getNomeLista());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNomeLista;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewNomeLista = itemView.findViewById(R.id.list_of_list_item);
        }
    }
}
