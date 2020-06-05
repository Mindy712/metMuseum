package gottlieb.metMuseum;

import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class MetServiceTest {

    @Test
    public void getDepartments() throws IOException {
        //given
        MetService service = new MetServiceFactory().getInstance();

        //when
        Response<MetFeed.DepartmentsList> response = service.getDepartments().execute();

        //then
        assertTrue(response.toString(), response.isSuccessful());
        MetFeed.DepartmentsList departmentsFeed = response.body();
        assertNotNull(departmentsFeed);

        List<MetFeed.DepartmentsList.Departments> departmentsList = departmentsFeed.departmentsList;
        assertFalse(departmentsList.isEmpty());

        final MetFeed.DepartmentsList.Departments department1 = departmentsList.get(0);
        assertNotNull(department1.displayName);
        assertNotNull(department1.departmentId);
    }

    @Test
    public void getObjectsList() throws IOException {
        //given
        MetService service = new MetServiceFactory().getInstance();

        //when
        Response<MetFeed.ObjectsList> response = service.getObjectsList(1).execute();

        //then
        assertTrue(response.toString(), response.isSuccessful());
        MetFeed.ObjectsList objectsListFeed = response.body();
        assertNotNull(objectsListFeed);

        assertNotNull(objectsListFeed.total);
        assertFalse(objectsListFeed.objectIDs.isEmpty());
    }

    @Test
    public void getObjects() throws IOException {
        //given
        MetService service = new MetServiceFactory().getInstance();

        //when
        Response<MetFeed.Objects> response = service.getObject(1).execute();

        //then
        assertTrue(response.toString(), response.isSuccessful());
        MetFeed.Objects objectFeed = response.body();
        assertNotNull(objectFeed);

        assertNotNull(objectFeed.objectName);
        assertNotNull(objectFeed.primaryImage);
    }
}