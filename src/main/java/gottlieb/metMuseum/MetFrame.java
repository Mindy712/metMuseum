package gottlieb.metMuseum;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MetFrame extends JFrame {

    //JFrame elements passed to MetController
    private JComboBox<MetFeed.DepartmentsList.Departments> departmentsList = new JComboBox<>();
    private JLabel objectImage = new JLabel("");
    private JLabel objectName = new JLabel("");
    private JLabel objectTitle = new JLabel("");
    private JLabel objectArtist = new JLabel("");

    public MetFrame() throws IOException {
        //set up MetFrame
        setSize(1000, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Met Museum");
        setLayout(new BorderLayout());

        //Add panel for Departments comboBox
        JPanel departmentsPanel = new JPanel();
        add(departmentsPanel, BorderLayout.WEST);

        //Add departmentsLabel and the departments ComboBox
        JLabel departmentsLabel = new JLabel("Choose a department:");
        departmentsPanel.add(departmentsLabel);
        departmentsPanel.add(departmentsList);

        //Add panel for the Objects to display
        JPanel objectsPanel = new JPanel();
        objectsPanel.setLayout(new BoxLayout(objectsPanel, BoxLayout.PAGE_AXIS));
        add(objectsPanel, BorderLayout.CENTER);

        //Add object Labels and set position
        objectsPanel.add(objectImage);
        objectsPanel.add(objectName);
        objectsPanel.add(objectTitle);
        objectsPanel.add(objectArtist);
        objectImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        objectName.setAlignmentX(Component.CENTER_ALIGNMENT);
        objectTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        objectArtist.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Add panel for the arrows
        JPanel arrowsPanel = new JPanel();
        add(arrowsPanel, BorderLayout.SOUTH);

        //Set backArrow button as Unicode backArrow and add button
        int backArrow = 11013;
        JButton backButton = new JButton();
        backButton.setText(Character.toString((char)backArrow));
        arrowsPanel.add(backButton);

        //Set nextArrow button as Unicode nextArrow and add button
        int nextArrow = 11157;
        JButton nextButton = new JButton();
        nextButton.setText(Character.toString((char)nextArrow));
        arrowsPanel.add(nextButton);

        //Fill comboBox by instantiating a MetController and then calling MetController.requestDepartmentData
        MetService service = MetServiceFactory.getInstance();
        MetController controller = new MetController(service, departmentsList, objectImage, objectName, objectTitle, objectArtist);
        controller.requestDepartmentData();

        /*Lambda expression called when a department is chosen.
        Sets departmentID with ID of department that was chosen.
        Gets the objects in the department by calling MetController.requestObjects(departmentID).*/
        departmentsList.addActionListener(actionEvent -> {
            MetFeed.DepartmentsList.Departments dept = (MetFeed.DepartmentsList.Departments) departmentsList.getSelectedItem();
            int departmentID = dept.departmentId;
            controller.requestObjects(departmentID);
        });

        /*Lamda expression called when backButton is clicked.
        Decrements currObj and gets back the previous objectID, with MetController.getPrev().
        Gets the previous object by calling requestSingleObject(currObj).*/
        backButton.addActionListener(e -> {
            int currObj = controller.getPrev();
            controller.requestSingleObject(currObj);
        });

        /*Lamda expression called when nextButton is clicked.
        Increments currObj and gets back the nex objectID, with MetController.getPrev().
        Gets the next object by calling requestSingleObject(currObj).*/
        nextButton.addActionListener(e -> {
            int currObj = controller.getNext();
            controller.requestSingleObject(currObj);
        });
    }

    public static void main(String[] args) throws IOException {

        new MetFrame().setVisible(true);
    }
}
