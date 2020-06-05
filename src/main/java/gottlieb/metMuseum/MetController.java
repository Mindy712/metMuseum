package gottlieb.metMuseum;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MetController {

    private MetService service;
    private JComboBox<MetFeed.DepartmentsList.Departments> departmentsListBox;
    private JLabel objectImage;
    private JLabel objectName;
    private JLabel objectTitle;
    private JLabel objectArtist;
    private List<Integer> objectIDs;
    private int currObj = 0;

    public MetController(MetService service,
                                   JComboBox<MetFeed.DepartmentsList.Departments> departmentsList,
                                    JLabel objectImage,
                                    JLabel objectName,
                                    JLabel objectTitle,
                                    JLabel objectArtist)
    {
        this.service = service;
        this.departmentsListBox = departmentsList;
        this.objectImage = objectImage;
        this.objectName = objectName;
        this.objectTitle = objectTitle;
        this.objectArtist = objectArtist;
    }

    public void requestDepartmentData() {
        service.getDepartments().enqueue(new Callback<MetFeed.DepartmentsList>() {
            @Override
            public void onResponse(Call<MetFeed.DepartmentsList> call, Response<MetFeed.DepartmentsList> response) {
                fillComboBox(response);
            }

            @Override
            public void onFailure(Call<MetFeed.DepartmentsList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void requestObjects(int deptId) {
        service.getObjectsList(deptId).enqueue(new Callback<MetFeed.ObjectsList>(){
            @Override
            public void onResponse(Call<MetFeed.ObjectsList> call, Response<MetFeed.ObjectsList> response) {
                getObjectData(response);
            }

            @Override
            public void onFailure(Call<MetFeed.ObjectsList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public int getDepartmentId(MetFeed.DepartmentsList.Departments dept) {
        return dept.departmentId;
    }

    public void fillComboBox(Response<MetFeed.DepartmentsList> response) {
        MetFeed.DepartmentsList departments = response.body();
        for (MetFeed.DepartmentsList.Departments dept : departments.departmentsList) {
//            ListItems listItems = new ListItems(dept.departmentId, dept.displayName);
            departmentsListBox.addItem(dept);
        }
    }

    public void getObjectData(Response<MetFeed.ObjectsList> response) {
        objectIDs = response.body().objectIDs;
        Integer objectId = objectIDs.get(currObj);
        objectImage.setIcon(null);
        objectImage.setText("Loading...");
        objectName.setText("");
        objectTitle.setText("");
        objectArtist.setText("");
        requestSingleObject(objectId);
    }

    public void requestSingleObject(int objectId) {
        service.getObject(objectId).enqueue(new Callback<MetFeed.Objects>() {
            @Override
            public void onResponse(Call<MetFeed.Objects> call, Response<MetFeed.Objects> response) {
                setObjectData(response);
            }

            @Override
            public void onFailure(Call<MetFeed.Objects> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void setObjectData(Response<MetFeed.Objects> response) {
        MetFeed.Objects object = setObjectLabels(response);
        setImage(object);
    }

    public MetFeed.Objects setObjectLabels(Response<MetFeed.Objects> response) {
        MetFeed.Objects object = response.body();
        objectName.setText("Name: " + object.objectName);
        objectTitle.setText("Description: " + object.title);
        String artistName = object.artistDisplayName;
        if (artistName.equals("")){
            objectArtist.setText("Artist: Unknown");
        }
        else {
            objectArtist.setText("Artist: " + artistName);
        }
        return object;
    }

    public void setImage(MetFeed.Objects object) {
        try {
            BufferedImage img = ImageIO.read(new URL(object.primaryImage));
            Image resizedImg = resize(img, 325, 325);
            objectImage.setIcon(new ImageIcon(resizedImg));
            objectImage.setText("");
        } catch (MalformedURLException e) {
            objectImage.setIcon(null);
            objectImage.setText("No image data available");
        } catch (IOException e) {
            objectImage.setIcon(null);
            objectImage.setText("No image data available");
        }
    }

    public int resetCurrObj() {
        return currObj = 0;
    }

    public int getNext() {
        if (currObj == (objectIDs.size() - 1)) {
            return currObj = 0;
        }
        else {
            return ++currObj;
        }
    }

    public int getPrev() {
        if (currObj == (0)) {
            return currObj = objectIDs.size() - 1;
        }
        else {
            return --currObj;
        }
    }

    public Image resize(Image img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
}