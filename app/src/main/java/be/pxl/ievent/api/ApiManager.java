package be.pxl.ievent.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import be.pxl.ievent.models.apiResponses.GeocodeResponseWrap;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Emitter;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by jessevandenberghe on 03/10/2017.
 */

public class ApiManager {
    private static final String TAG = ApiManager.class.getSimpleName();

    private static final String ENDPOINT = "https://maps.googleapis.com/maps/api/";
    private static final String APIKEY = "AIzaSyBOTwBpygFkMEn2Z5CK_CUiV-GeQGierLo";
    public static final OkHttpClient CLIENT  = getClient();

    private static final Gson gson = new GsonBuilder().create();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(ENDPOINT)
            .client(CLIENT)
            .build();

    private static OkHttpClient getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(1);

        return new OkHttpClient()
                .newBuilder()
                .addInterceptor(interceptor)
                .dispatcher(dispatcher)
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public static final ApiManagerService apiManager = retrofit.create(ApiManagerService.class);

    public static Observable<GeocodeResponseWrap> getGeocode(final String searchQuery){
        return Observable.create(new Observable.OnSubscribe<GeocodeResponseWrap>() {
            @Override
            public void call(Subscriber<? super GeocodeResponseWrap> subscriber) {
                if(!subscriber.isUnsubscribed()){
                    try {
                        Response<GeocodeResponseWrap> response = apiManager.getLocationCoordinates(searchQuery, APIKEY).execute();
                        if(response.isSuccessful()){
                            subscriber.onNext(response.body());
                            subscriber.onCompleted();
                        }
                        else{
                            handleError(subscriber,null,null,response);
                        }
                    }
                    catch (Exception ex){
                        handleError(subscriber,null,ex,null);
                    }
                }
            }
        });
    }

    private static void handleError(Subscriber subscriber, Emitter emitter, Exception ex, Response response){
        ex.printStackTrace();
        if (subscriber != null) {
            subscriber.onError(ex);
        }
        if (emitter != null){
            emitter.onError(new Exception("Whoops, something went wrong"));
        }
    }
}
