package com.pearl.figgodriver.Network
import com.pearl.FiggoPartner.Model.RegisterNumber
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap


interface RegisterApiService {
    @POST("/register")
    fun registerNum(@QueryMap params: Map<String, String>):Call<RegisterNumber>
}