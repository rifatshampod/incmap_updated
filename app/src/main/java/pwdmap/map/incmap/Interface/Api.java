package pwdmap.map.incmap.Interface;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pwdmap.map.incmap.Models.AddFeedbackModel;
import pwdmap.map.incmap.Models.AddLocationModel;
import pwdmap.map.incmap.Models.AddRatingModel;
import pwdmap.map.incmap.Models.NearestLocationModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {


    /*Signup*/
   /* @FormUrlEncoded
    @POST("signup")
    Call<SignUpModel> create_user(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password,
            @Field("device_id") String device_id,
            @Field("device_type") String device_type,
            @Field("user_type") String user_type,
            @Field("mac_address") String mac_address,
            @Field("phone") String phone);




    *//*Login*//*
    @FormUrlEncoded
    @POST("patientLogin")
    Call<LoginModel> login_user(
            @Field("email") String email,
            @Field("password") String password);


*/

    //*Signup*//*
    @FormUrlEncoded
    @POST("addRating")
    Call<AddRatingModel> add_Rating(
            @Field("post_id") String post_id,
            @Field("rating") String rating);


    //*Add Location*//*
    @Multipart
    @POST("addPlace")
    Call<AddLocationModel> add_location(
            @Part("name") RequestBody name,
            @Part("lat") RequestBody lat,
            @Part("lon") RequestBody lon,
            @Part("description") RequestBody description,
            @Part("service") RequestBody service,
            @Part("timing") RequestBody timing,
            @Part MultipartBody.Part file
    );

        //*Nearest Locations*//*
    @FormUrlEncoded
    @POST("nearst_location")
    Call<NearestLocationModel> nearest_locations(
            @Field("latitude") double latitude,
            @Field("longitude") double longitude);

    //*Rating*//*
    @FormUrlEncoded
    @POST("addFeedBack")
    Call<AddFeedbackModel> addFeedback(
            @Field("location_id") String post_id,
            @Field("description") String description,
            @Field("descriptionss") String descriptionss,
            @Field("descriptionsss") String descriptionsss);


}
