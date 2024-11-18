package com.ensa.gestiondescomptes.api;

import com.ensa.gestiondescomptes.models.Compte;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface CompteApi {
    @GET("banque/comptes")
    Call<List<Compte>> getAllComptes(@Header("Accept") String acceptType);

    // Dans CompteApi.java
    @POST("banque/comptes")
    Call<Compte> createCompte(
            @Body Compte compte,
            @Header("Content-Type") String contentType,  // Format des données envoyées
            @Header("Accept") String acceptType          // Format des données reçues
    );
    @PUT("banque/comptes/{id}")
    Call<Compte> updateCompte(
            @Path("id") Long id,
            @Body Compte compte,
            @Header("Content-Type") String contentType,
            @Header("Accept") String acceptType
    );

    @DELETE("banque/comptes/{id}")
    Call<Void> deleteCompte(
            @Path("id") Long id,
            @Header("Accept") String acceptType
    );
}