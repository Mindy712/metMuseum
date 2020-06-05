package gottlieb.metMuseum;

import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MetControllerTest {
    @Test
    public void requestDepartmentData() {
        //given
        MetService service = mock(MetService.class);
        JComboBox comboBox = mock(JComboBox.class);
        JLabel label = mock(JLabel.class);
        Call<MetFeed> call = mock(Call.class);
        doReturn(call).when(service).getDepartments();
        MetController controller = new MetController(service, comboBox, label, label, label, label);

        //when
        controller.requestDepartmentData();

        //then
        //a call was made
        verify(call).enqueue(any());
    }

    @Test
    public void requestObjects() {
        //given
        MetService service = mock(MetService.class);
        JComboBox comboBox = mock(JComboBox.class);
        JLabel label = mock(JLabel.class);
        Call<MetFeed.ObjectsList> call = mock(Call.class);
        doReturn(call).when(service).getObjectsList(1);
        MetController controller = new MetController(service, comboBox, label, label, label, label);

        //when
        controller.requestObjects(1);

        //then
        //a call was made
        verify(call).enqueue(any());
    }

    @Test
    public void getSingleObject() {
        //given
        MetService service = mock(MetService.class);
        JComboBox comboBox = mock(JComboBox.class);
        JLabel label = mock(JLabel.class);
        Call<MetFeed.Objects> call = mock(Call.class);
        doReturn(call).when(service).getObject(1);
        MetController controller = new MetController(service, comboBox, label, label, label, label);

        //when
        controller.requestSingleObject(1);

        //then
        //a call was made
        verify(call).enqueue(any());
    }

    @Test
    public void fillComboBox() {
        MetService service = mock(MetService.class);
        JComboBox comboBox = mock(JComboBox.class);
        JLabel label = mock(JLabel.class);
        Response<MetFeed.DepartmentsList> response = mock(Response.class);

        MetController controller = new MetController(service, comboBox, label, label, label, label);

        //Make the list and Departments objects
        MetFeed.DepartmentsList deptList = new MetFeed.DepartmentsList();
        MetFeed.DepartmentsList.Departments depts = new MetFeed.DepartmentsList.Departments();

        //fill list and departments object
        depts.departmentId = 1;
        depts.displayName = "department";
        ArrayList<MetFeed.DepartmentsList.Departments> list = new ArrayList<>();
        list.add(depts);
        deptList.departmentsList = list;

        //return the created list when response.body() is called
        doReturn(deptList).when(response).body();

        //when
        controller.fillComboBox(response);

        //then
        //comboBox adds the given item from the list
        verify(comboBox).addItem(deptList.departmentsList.get(0));
    }

//    @Test
//    public void getObjectData() {
//
//    }

    @Test
    public void setObjectLabels() {
        //given
        MetService service = mock(MetService.class);
        JComboBox comboBox = mock(JComboBox.class);
        JLabel label = mock(JLabel.class);
        Response<MetFeed.Objects> response = mock(Response.class);

        MetController controller = new MetController(service, comboBox, label, label, label, label);

        //Create and fill an objects object
        MetFeed.Objects object = new MetFeed.Objects();
        object.artistDisplayName = "John Smith";
        object.objectName = "Hat";
        object.title = "Hat";

        //return the created object when response.body() is called
        doReturn(object).when(response).body();

        //when
        controller.setObjectLabels(response);

        //then
        //ensure the labels are properly set
        verify(label).setText("Name: " + object.objectName);
        verify(label).setText("Description: " + object.title);
        verify(label).setText("Artist: " + object.artistDisplayName);
    }

    @Test
    public void setImage() throws IOException {
        //given
        MetService service = mock(MetService.class);
        JComboBox comboBox = mock(JComboBox.class);
        JLabel label = mock(JLabel.class);
        Response<MetFeed.Objects> response = mock(Response.class);

        MetController controller = new MetController(service, comboBox, label, label, label, label);

        //create an Objects object and add no image to it
        MetFeed.Objects object = new MetFeed.Objects();
        object.primaryImage = "";

        //return the created object when response.body() is called
        doReturn(object).when(response).body();

        //when
        controller.setImage(object);

        //then
        //ensure the label is properly set
        verify(label).setText("No image data available");
    }
}

