package gottlieb.metMuseum;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Creates an instance of the MetService Interface, so it can be used by MetController.
public class MetServiceFactory {
    public static MetService getInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://collectionapi.metmuseum.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MetService service = retrofit.create((MetService.class));
        return service;
    }

}
