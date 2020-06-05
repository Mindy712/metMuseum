package gottlieb.metMuseum;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MetService {
    @GET("public/collection/v1/departments")
    Call<MetFeed.DepartmentsList> getDepartments();

    @GET("public/collection/v1/objects?")
    Call<MetFeed.ObjectsList> getObjectsList(@Query("departmentIds") int departmentId);

    @GET("public/collection/v1/objects/{objectId}")
    Call<MetFeed.Objects> getObject(@Path("objectId") int objectId);
}
