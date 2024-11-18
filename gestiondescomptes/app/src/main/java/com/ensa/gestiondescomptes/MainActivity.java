package com.ensa.gestiondescomptes;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ensa.gestiondescomptes.adapters.CompteAdapter;
import com.ensa.gestiondescomptes.models.Compte;
import com.ensa.gestiondescomptes.viewmodels.CompteViewModel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private CompteViewModel viewModel;
    private CompteAdapter adapter;
    private static final String[] ACCOUNT_TYPES = {"EPARGNE", "COURANT"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViewModel();
        setupRecyclerView();
        setupFormatSpinner();
        setupFab();
        observeViewModel();
    }

    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(CompteViewModel.class);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CompteAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnCompteClickListener(new CompteAdapter.OnCompteClickListener() {
            @Override
            public void onEditClick(Compte compte) {
                showEditCompteDialog(compte);
            }

            @Override
            public void onDeleteClick(Compte compte) {
                showDeleteConfirmationDialog(compte);
            }
        });
    }

    private void setupFormatSpinner() {
        Spinner formatSpinner = findViewById(R.id.formatSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                Arrays.asList("application/json", "application/xml"));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        formatSpinner.setAdapter(spinnerAdapter);

        formatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String format = parent.getItemAtPosition(position).toString();
                viewModel.setFormat(format);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupFab() {
        findViewById(R.id.fabAddCompte).setOnClickListener(v -> showAddCompteDialog());
    }

    private void observeViewModel() {
        viewModel.getComptes().observe(this, comptes -> adapter.setComptes(comptes));
        viewModel.getError().observe(this, error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show());
    }

    private void showAddCompteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_compte, null);

        EditText etSolde = view.findViewById(R.id.etSolde);
        Spinner spinnerType = view.findViewById(R.id.spinnerType);

        // Configuration du Spinner
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                ACCOUNT_TYPES
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        builder.setView(view)
                .setTitle("Ajouter un compte")
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    try {
                        Compte compte = new Compte();
                        compte.setSolde(Double.parseDouble(etSolde.getText().toString()));
                        compte.setType(spinnerType.getSelectedItem().toString());
                        compte.setDateCreation(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .format(new Date()));
                        viewModel.addCompte(compte);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void showEditCompteDialog(Compte compte) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_compte, null);

        EditText etSolde = view.findViewById(R.id.etSolde);
        Spinner spinnerType = view.findViewById(R.id.spinnerType);

        // Configuration du Spinner
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                ACCOUNT_TYPES
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        // Définir les valeurs actuelles
        etSolde.setText(String.valueOf(compte.getSolde()));
        // Sélectionner le type de compte actuel dans le Spinner
        spinnerType.setSelection(
                Arrays.asList(ACCOUNT_TYPES).indexOf(compte.getType())
        );

        builder.setView(view)
                .setTitle("Modifier le compte")
                .setPositiveButton("Modifier", (dialog, which) -> {
                    try {
                        compte.setSolde(Double.parseDouble(etSolde.getText().toString()));
                        compte.setType(spinnerType.getSelectedItem().toString());
                        viewModel.updateCompte(compte);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void showDeleteConfirmationDialog(Compte compte) {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer le compte")
                .setMessage("Êtes-vous sûr de vouloir supprimer ce compte ?")
                .setPositiveButton("Oui", (dialog, which) -> viewModel.deleteCompte(compte.getId())) // Pass ID instead of the whole compte
                .setNegativeButton("Non", null)
                .show();
    }

}