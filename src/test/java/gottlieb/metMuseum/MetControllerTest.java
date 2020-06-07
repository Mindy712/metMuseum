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

    /*expect NullPointerException when requestSingleObject(objectId) is called by the test.
    If test passes, it means there was a NullPointerException, i.e. requestSingleObject(objectId)
    was called as expected.*/
    @Test (expected = NullPointerException.class)
    public void getObjectData() {
        //given
        MetService service = mock(MetService.class);
        JComboBox comboBox = mock(JComboBox.class);
        JLabel label = mock(JLabel.class);
        Response<MetFeed.ObjectsList> response = mock(Response.class);

        MetController controller = new MetController(service, comboBox, label, label, label, label);

        //Create and set objectIDs List
        MetFeed.ObjectsList objects = new MetFeed.ObjectsList();
        ArrayList objectIDs = new ArrayList();
        objectIDs.add(1);
        objects.objectIDs = objectIDs;

        //return the created list when response.body() is called
        doReturn(objects).when(response).body();

        //when
        controller.getObjectData(response);

        //then
        verify(controller).requestSingleObject(1);
    }

    @Test
    public void requestSingleObject() {
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
    public void setObjectData() {
        //given
        MetService service = mock(MetService.class);
        JComboBox comboBox = mock(JComboBox.class);
        JLabel label = mock(JLabel.class);
        Response<MetFeed.Objects> response = mock(Response.class);

        MetController mockController = mock(MetController.class);
        MetController controller = new MetController(service, comboBox, label, label, label, label);

        //Create and fill an objects object
        MetFeed.Objects object = new MetFeed.Objects();
        object.artistDisplayName = "John Smith";
        object.objectName = "Hat";
        object.title = "Hat";
        object.primaryImage = "";

        //return the created object when response.body() is called
        doReturn(object).when(response).body();

        //when
        controller.setObjectData(response);

        //then
        //ensure 8 labels are set
        verify(label, times(8)).setText(any());
    }

    @Test
    public void setImage() throws IOException {
        //given
        MetService service = mock(MetService.class);
        JComboBox comboBox = mock(JComboBox.class);
        JLabel label = mock(JLabel.class);
        Response<MetFeed.Objects> response = mock(Response.class);

        MetController controller = new MetController(service, comboBox, label, label, label, label);

        //create an Objects object and add valid image to it
        MetFeed.Objects object = new MetFeed.Objects();
        object.primaryImage = "Images/Loading.png";

        //return the created object when response.body() is called
        doReturn(object).when(response).body();

        //when
        controller.setImage(object);

        //then
        //ensure the label is properly set with an image
        verify(label).setIcon(any());
    }

    @Test
    public void setImageNoImage() throws IOException {
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
        //ensure the label is properly set if there is no image
        verify(label).setText("No image data available");
    }

    @Test
    public void getNext() {
        //given
        MetService service = mock(MetService.class);
        JComboBox comboBox = mock(JComboBox.class);
        JLabel label = mock(JLabel.class);

        MetController controller = new MetController(service, comboBox, label, label, label, label);

        //make ArrayList to put in controller.objectIDs
        ArrayList<Integer> objectIDs = new ArrayList<>();
        objectIDs.add(0);
        objectIDs.add(1);
        objectIDs.add(2);
        controller.setObjectIDs(objectIDs);

        //when
        int time1 = controller.getNext();
        int time2 = controller.getNext();
        int time3 = controller.getNext();

        //then
        //after once, index is 1
        assertTrue(time1 == 1);

        //after twice, index is 2
        assertTrue(time2 == 2);

        //after three times, index goes back to 0 because list is only 3 ints long
        assertTrue(time3 == 0);
    }

    @Test
    public void getPrev() {
        //given
        MetService service = mock(MetService.class);
        JComboBox comboBox = mock(JComboBox.class);
        JLabel label = mock(JLabel.class);

        MetController controller = new MetController(service, comboBox, label, label, label, label);

        //make ArrayList to put in controller.objectIDs
        ArrayList<Integer> objectIDs = new ArrayList<>();
        objectIDs.add(0);
        objectIDs.add(1);
        objectIDs.add(2);
        controller.setObjectIDs(objectIDs);

        //when
        int time1 = controller.getPrev();
        int time2 = controller.getPrev();
        int time3 = controller.getPrev();

        //then
        //after once, index is 2, because it goes back to the end of the list
        assertTrue(time1 == 2);

        //after twice, index is 1
        assertTrue(time2 == 1);

        //after three times, index is 0
        assertTrue(time3 == 0);
    }

    @Test
    public void setLabel() {
        //given
        MetService service = mock(MetService.class);
        JComboBox comboBox = mock(JComboBox.class);
        JLabel label = mock(JLabel.class);

        MetController controller = new MetController(service, comboBox, label, label, label, label);

        JLabel name = new JLabel("");
        String data = "Mindy";
        String value = "Name";

        //when
        controller.setLabel(name, data, value);

        //then
        assertTrue(name.getText().equals("Name: Mindy"));
    }

    @Test
    public void setLabelNoData() {
        //given
        MetService service = mock(MetService.class);
        JComboBox comboBox = mock(JComboBox.class);
        JLabel label = mock(JLabel.class);

        MetController controller = new MetController(service, comboBox, label, label, label, label);

        JLabel name = new JLabel("");
        String data = "";
        String value = "Name";

        //when
        controller.setLabel(name, data, value);

        //then
        assertTrue(name.getText().equals("Name: Unknown"));
    }
}

