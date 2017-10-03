package be.pxl.ievent.api;

import be.pxl.ievent.models.apiResponses.GeocodeResponseWrap;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jessevandenberghe on 03/10/2017.
 */

public interface ApiManagerService {
    @GET("geocode/json")
    Call<GeocodeResponseWrap> getLocationCoordinates(@Query("address") String searchQuery, @Query("key") String apiKey);
}
