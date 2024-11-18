package com.ensa.gestiondescomptes.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ensa.gestiondescomptes.models.Compte;
import com.ensa.gestiondescomptes.repository.CompteRepository;

import java.util.List;

public class CompteViewModel extends ViewModel {
    private final CompteRepository repository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> format = new MutableLiveData<>("application/json"); // Format par défaut

    private static final String ACCEPT_JSON = "application/json";
    private static final String ACCEPT_XML = "application/xml";

    public CompteViewModel() {
        repository = new CompteRepository();
        loadComptes();
    }

    // Fetch the list of comptes
    public LiveData<List<Compte>> getComptes() {
        return repository.getComptes();
    }

    public LiveData<String> getFormat() {
        return format;
    }

    // Setter for format
    public void setFormat(String format) {
        this.format.setValue(format);
        // Recharger les comptes avec le nouveau format
        loadComptes();
    }

    // Fetch error messages
    public LiveData<String> getError() {
        return repository.getError();
    }

    // Check loading state
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    // Method to load comptes from the repository
    public void loadComptes() {
        isLoading.setValue(true);

        // Vérification du format et appel de la méthode correspondante dans le repository
        String acceptType = format.getValue().equals(ACCEPT_JSON) ? ACCEPT_JSON : ACCEPT_XML;

        // Appeler le repository avec le bon format
        repository.loadComptes(acceptType);

        isLoading.setValue(false);
    }

    // Method to add a new compte
    public void addCompte(Compte compte) {
        isLoading.setValue(true);
        repository.addCompte(compte);
        isLoading.setValue(false);
    }

    // Method to update an existing compte
    public void updateCompte(Compte compte) {
        isLoading.setValue(true);
        repository.updateCompte(compte);
        isLoading.setValue(false);
    }

    // Method to delete a compte
    public void deleteCompte(Long id) {
        isLoading.setValue(true);
        repository.deleteCompte(id);
        isLoading.setValue(false);
    }
}
