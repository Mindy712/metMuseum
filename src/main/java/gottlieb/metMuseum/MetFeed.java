package gottlieb.metMuseum;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MetFeed {

    //Class of list of Departments in the Met, and the Departments class that populates it
    static class DepartmentsList {
        //List of departments in the Met
        @SerializedName("departments")
        ArrayList<Departments> departmentsList;

        //Departments objects that populate departmentsList
        static class Departments {
            int departmentId;
            String displayName;

            @Override
            public String toString() {
                return displayName;
            }
        }
    }

    //List of objects in a particular department in the Met
    public static class ObjectsList
    {
        ArrayList<Integer> objectIDs;
    }

    //Data about a specific object from the Met.
    public static class Objects
    {
        String objectName;
        String title;
        String primaryImage;
        String artistDisplayName;
    }
}
