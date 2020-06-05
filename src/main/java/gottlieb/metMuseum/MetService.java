package gottlieb.metMuseum;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MetService {
    //Calls list of Departments
    @GET("public/collection/v1/departments")
    Call<MetFeed.DepartmentsList> getDepartments();

    //Calls list of objectIds in a given department
    @GET("public/collection/v1/objects?")
    Call<MetFeed.ObjectsList> getObjectsList(@Query("departmentIds") int departmentId);

    //Calls a given object
    @GET("public/collection/v1/objects/{objectId}")
    Call<MetFeed.Objects> getObject(@Path("objectId") int objectId);
}
