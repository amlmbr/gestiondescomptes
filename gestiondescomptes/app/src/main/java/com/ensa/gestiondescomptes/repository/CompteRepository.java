package com.ensa.gestiondescomptes.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ensa.gestiondescomptes.api.CompteApi;
import com.ensa.gestiondescomptes.models.Compte;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CompteRepository {
    private static final String BASE_URL = "http://10.0.2.2:8080";
    private static final String ACCEPT_TYPE = "application/json";
    private static final String CONTENT_TYPE = "application/json";
    private final CompteApi compteApi;
    private final MutableLiveData<List<Compte>> comptesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public CompteRepository() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        compteApi = retrofit.create(CompteApi.class);
    }

    public LiveData<List<Compte>> getComptes() {
        return comptesLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }
    public void loadComptes() {
        // Default to "application/json" if no argument is passed
        loadComptes(ACCEPT_TYPE);
    }
    public void loadComptes(String acceptType) {
        compteApi.getAllComptes(acceptType).enqueue(new Callback<List<Compte>>() {
            @Override
            public void onResponse(Call<List<Compte>> call, Response<List<Compte>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    comptesLiveData.setValue(response.body());
                } else {
                    errorLiveData.setValue("Erreur lors du chargement des comptes: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Compte>> call, Throwable t) {
                errorLiveData.setValue("Erreur réseau : " + t.getMessage());
            }
        });
    }

    public void addCompte(Compte compte) {
        compteApi.createCompte(compte, CONTENT_TYPE, ACCEPT_TYPE).enqueue(new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful()) {
                    loadComptes(ACCEPT_TYPE); // Pass acceptType here
                } else {
                    errorLiveData.setValue("Erreur lors de l'ajout du compte");
                }
            }

            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                errorLiveData.setValue("Erreur réseau : " + t.getMessage());
            }
        });
    }

    public void updateCompte(Compte compte) {
        compteApi.updateCompte(compte.getId(), compte, CONTENT_TYPE, ACCEPT_TYPE).enqueue(new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful()) {
                    loadComptes(ACCEPT_TYPE); // Pass acceptType here
                } else {
                    errorLiveData.setValue("Erreur lors de la mise à jour du compte");
                }
            }

            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                errorLiveData.setValue("Erreur réseau : " + t.getMessage());
            }
        });
    }

    public void deleteCompte(Long id) {
        compteApi.deleteCompte(id, ACCEPT_TYPE).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    loadComptes(ACCEPT_TYPE); // Pass acceptType here
                } else {
                    errorLiveData.setValue("Erreur lors de la suppression du compte");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorLiveData.setValue("Erreur réseau : " + t.getMessage());
            }
        });
    }

}