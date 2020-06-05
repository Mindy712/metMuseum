package gottlieb.metMuseum;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MetFeed {

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

    public static class ObjectsList
    {
        int total;
        List<Integer> objectIDs;
    }

    public static class Objects
    {
        String objectName;
        String title;
        String primaryImage;
        String artistDisplayName;
    }
}
