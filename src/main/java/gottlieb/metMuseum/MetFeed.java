package gottlieb.metMuseum;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MetFeed {

    //List of departments in the Met
    static class DepartmentsList {
        @SerializedName("departments")
        ArrayList<Departments> departmentsList;

        static class Departments {
            int departmentId;
            String displayName;

            @Override
            public String toString() {
                return displayName;
            }
        }
    }

    //List of objects in a particular department
    public static class ObjectsList
    {
        ArrayList<Integer> objectIDs;
    }

    //Specific object data
    public static class Objects
    {
        String objectName;
        String title;
        String primaryImage;
        String artistDisplayName;
    }
}
