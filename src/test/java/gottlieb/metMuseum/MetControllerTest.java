package gottlieb.metMuseum;

import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        verify(call).enqueue(any());
    }

    @Test
    public void fillComboBox() {
        MetService service = mock(MetService.class);
        JComboBox comboBox = mock(JComboBox.class);
        JLabel label = mock(JLabel.class);
        Response<MetFeed.DepartmentsList> response = mock(Response.class);

        MetController controller = new MetController(service, comboBox, label, label, label, label);

        MetFeed.DepartmentsList deptList = new MetFeed.DepartmentsList();
        MetFeed.DepartmentsList.Departments depts = new MetFeed.DepartmentsList.Departments();
        depts.departmentId = 1;
        depts.displayName = "department";
        ArrayList<MetFeed.DepartmentsList.Departments> list = new ArrayList<>();
        list.add(depts);
        deptList.departmentsList = list;

        doReturn(deptList).when(response).body();

        //when
        controller.fillComboBox(response);

        //then
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

        MetFeed.Objects object = new MetFeed.Objects();
        object.artistDisplayName = "John Smith";
        object.objectName = "Hat";
        object.title = "Hat";

        doReturn(object).when(response).body();

        //when
        controller.setObjectLabels(response);

        //then
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

        MetFeed.Objects object = new MetFeed.Objects();
        object.primaryImage = "";

        doReturn(object).when(response).body();

        //when
        controller.setImage(object);

        //then
        verify(label).setText("No image data available");
    }
}

