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
import java.util.ArrayList;

public class MetController {

    //Constructor objects
    private MetService service;
    private JComboBox<MetFeed.DepartmentsList.Departments> departmentsListBox;
    private JLabel objectImage;
    private JLabel objectName;
    private JLabel objectTitle;
    private JLabel objectArtist;

    /*ArrayList of objects in a given department. Set by getObjectData().
    Used by getNext and getPrev to iterate through objects.*/
    private ArrayList<Integer> objectIDs;

    /*Counter of what index object from objectIDs the Frame is showing.
    Reset to 0 when a new department is called.
    Incremented by 1 when next arrow is clicked.
    Decremented by 1 when back arrow is clicked.*/
    private int currObj = 0;

    //Construct MetController with service and all the JFrame elements it will use
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

    //API call to get the departments list
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

    /*Method called on Response of the requestDepartmentData API call.
    Takes the list of Departments objects from the response and puts them in a MetFeed.Departments object.
    Loops through the departmentsList and adds each department to the ComboBox.
    Resets currObj to 0. Called when a new department is chosen, so the objects displayed begin from the first object.*/
    public void fillComboBox(Response<MetFeed.DepartmentsList> response) {
        MetFeed.DepartmentsList departments = response.body();
        for (MetFeed.DepartmentsList.Departments dept : departments.departmentsList) {
            departmentsListBox.addItem(dept);
        }
    }

    //API call to get the list of objects in a given department
    public void requestObjects(int deptId) {
        service.getObjectsList(deptId).enqueue(new Callback<MetFeed.ObjectsList>(){
            @Override
            public void onResponse(Call<MetFeed.ObjectsList> call, Response<MetFeed.ObjectsList> response) {
                currObj = 0;
                getObjectData(response);
            }

            @Override
            public void onFailure(Call<MetFeed.ObjectsList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /*Method called on Response of the requestObjects API call.
    Take the list of objectIDs from the response and sets the ArrayList objectIDs as that.
    Sets objectID as the ID that is in the currObj index of the list.
    Calls requestSingleObject API call with ObjectID.*/
    public void getObjectData(Response<MetFeed.ObjectsList> response) {
        objectIDs = response.body().objectIDs;
        int objectId = objectIDs.get(currObj);
        requestSingleObject(objectId);
    }

    //API call to get a given object. Called by getObjectData
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

    /*Resets all JLabels to empty.
    Takes the data about the object from the response and assigns it to MetFeed.Objects object
    Sets the JLabels with the appropriate text from the API.
    Calls the setImage(object) method.*/
    public void setObjectData(Response<MetFeed.Objects> response) {
        System.out.println(currObj);
        System.out.println(objectIDs);
        objectImage.setIcon(null);
        objectImage.setText("Loading...");
        objectName.setText("");
        objectTitle.setText("");
        objectArtist.setText("");
        MetFeed.Objects object = response.body();
        setLabel(objectName, object.objectName, "Name");
        setLabel(objectTitle, object.title, "Title");
        setLabel(objectArtist, object.artistDisplayName, "Artist");
        setImage(object);
    }

    /*Uses object returned by setObjectsLabels(response)
    Tries to get the image from the URL as a BufferedImage.
    Resizes the image.
    Sets the label to display that icon and no text.
    If that fails (because there is no image), label is set as No Image Data Available, with no Icon.*/
    public void setImage(MetFeed.Objects object) {
        try {
            BufferedImage img = ImageIO.read(new URL(object.primaryImage));
            Image resizedImg = img.getScaledInstance(325, 325, Image.SCALE_SMOOTH);
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

    /*Called when nextArrow is clicked.
    If the currObj is the last in the list, it goes back to 0.
    Otherwise it increments currObj by 1.
    It returns the value for "currObj index" object from objectIDs.*/
    public int getNext() {
        if (currObj == (objectIDs.size() - 1)) {
            return objectIDs.get(currObj = 0);
        }
        else {
            return objectIDs.get(++currObj);
        }
    }

    /*Called when backArrow is clicked.
    If the currObj is 0, it goes to the last.
    Otherwise it decrements currObj by 1.
    It returns the value for "currObj index" object from objectIDs.*/
    public int getPrev() {
        if (currObj == (0)) {
            return objectIDs.get(currObj = objectIDs.size() - 1);
        }
        else {
            return objectIDs.get(--currObj);
        }
    }

    /*Sets JLabels with data from the API.
    Called by setObjectData()
    Accounts for the possibility of there being no data provided by the API*/
    public void setLabel(JLabel label, String data, String value) {
        if(data.equals("")) {
            label.setText(value + ": Unknown");
        }
        else {
            label.setText(value + ": " + data);
        }
    }

    //setter for ArrayList objectIDs. Used to test methods getNext() and getPrev()
    protected void setObjectIDs(ArrayList objectIDs) {
        this.objectIDs = objectIDs;
    }

}