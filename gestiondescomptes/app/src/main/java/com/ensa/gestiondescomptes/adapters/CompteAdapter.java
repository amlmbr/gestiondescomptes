package com.ensa.gestiondescomptes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ensa.gestiondescomptes.R;
import com.ensa.gestiondescomptes.models.Compte;
import java.util.ArrayList;
import java.util.List;

public class CompteAdapter extends RecyclerView.Adapter<CompteAdapter.CompteViewHolder> {
    private List<Compte> comptes = new ArrayList<>();
    private OnCompteClickListener listener;

    public interface OnCompteClickListener {
        void onEditClick(Compte compte);
        void onDeleteClick(Compte compte);
    }

    public void setOnCompteClickListener(OnCompteClickListener listener) {
        this.listener = listener;
    }

    public void setComptes(List<Compte> comptes) {
        this.comptes = comptes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CompteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_compte, parent, false);
        return new CompteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompteViewHolder holder, int position) {
        Compte compte = comptes.get(position);
        holder.bind(compte);
    }

    @Override
    public int getItemCount() {
        return comptes.size();
    }

    class CompteViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSolde;
        private final TextView tvType;
        private final TextView tvDate;
        private final Button btnEdit;
        private final Button btnDelete;

        CompteViewHolder(View itemView) {
            super(itemView);
            tvSolde = itemView.findViewById(R.id.tvSolde);
            tvType = itemView.findViewById(R.id.tvType);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        void bind(final Compte compte) {
            tvSolde.setText(String.format("Solde: %.2f", compte.getSolde()));
            tvType.setText(String.format("Type: %s", compte.getType()));
            tvDate.setText(String.format("Date: %s", compte.getDateCreation()));

            btnEdit.setOnClickListener(v -> {
                if (listener != null) listener.onEditClick(compte);
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) listener.onDeleteClick(compte);
            });
        }
    }
}
