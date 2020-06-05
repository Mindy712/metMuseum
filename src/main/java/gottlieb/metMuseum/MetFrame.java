package gottlieb.metMuseum;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MetFrame extends JFrame {

    JComboBox<MetFeed.DepartmentsList.Departments> departmentsList = new JComboBox<>();
    JLabel objectImage = new JLabel("");
    JLabel objectName = new JLabel("");
    JLabel objectTitle = new JLabel("");
    JLabel objectArtist = new JLabel("");
    int departmentID;

    public MetFrame() throws IOException {
        setSize(1000, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Met Museum");
        setLayout(new BorderLayout());

        JPanel departmentsPanel = new JPanel();
        add(departmentsPanel, BorderLayout.WEST);

        JLabel departmentsLabel = new JLabel("Choose a department:");
        departmentsPanel.add(departmentsLabel);
        departmentsPanel.add(departmentsList);

        JPanel objectsPanel = new JPanel();
        objectsPanel.setLayout(new BoxLayout(objectsPanel, BoxLayout.PAGE_AXIS));
        add(objectsPanel, BorderLayout.CENTER);

        objectsPanel.add(objectImage);
        objectsPanel.add(objectName);
        objectsPanel.add(objectTitle);
        objectsPanel.add(objectArtist);
        objectImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        objectName.setAlignmentX(Component.CENTER_ALIGNMENT);
        objectTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        objectArtist.setAlignmentX(Component.CENTER_ALIGNMENT);

        MetService service = MetServiceFactory.getInstance();
        MetController controller = new MetController(service, departmentsList, objectImage, objectName, objectTitle, objectArtist);
        controller.requestDepartmentData();

        JPanel arrowsPanel = new JPanel();
        add(arrowsPanel, BorderLayout.SOUTH);

        Image backArrow = ImageIO.read(new File("Images/BackArrow.png"));
        Image resizedBackArrow = controller.resize(backArrow, 75, 75);
        JButton backButton = new JButton();
        backButton.setIcon(new ImageIcon(resizedBackArrow));
        arrowsPanel.add(backButton);

        Image nextArrow = ImageIO.read(new File("Images/NextArrow.png"));
        Image resizedNextArrow = controller.resize(nextArrow, 75, 75);
        JButton nextButton = new JButton();
        nextButton.setIcon(new ImageIcon(resizedNextArrow));
        arrowsPanel.add(nextButton);

        departmentsList.addActionListener(actionEvent -> {
            departmentID = controller.getDepartmentId((MetFeed.DepartmentsList.Departments) departmentsList.getSelectedItem());
            controller.resetCurrObj();
            controller.requestObjects(departmentID);
        });

        backButton.addActionListener(e -> {
            controller.getPrev();
            controller.requestObjects(departmentID);
        });

        nextButton.addActionListener(e -> {
            controller.getNext();
            controller.requestObjects(departmentID);
        });
    }

    public static void main(String[] args) throws IOException {

        new MetFrame().setVisible(true);
    }
}
