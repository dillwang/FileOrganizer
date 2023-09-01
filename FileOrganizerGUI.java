import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileOrganizerGUI {
    private static JTextField directoryField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("File Organizer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel();

            directoryField = new JTextField(20);
            JButton browseButton = new JButton("Browse");
            JButton startButton = new JButton("Start File Organizer");

            browseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int result = fileChooser.showOpenDialog(frame);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedDirectory = fileChooser.getSelectedFile();
                        directoryField.setText(selectedDirectory.getAbsolutePath());
                    }
                }
            });

            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String sourceDirectoryPath = directoryField.getText();
                    File selectedDirectory = new File(sourceDirectoryPath);

                    if (selectedDirectory.exists() && selectedDirectory.isDirectory()) {
                        FileOrganizerLogic.organizeFiles(selectedDirectory);
                        if (FileOrganizerLogic.getExitStatus() == true) {
                            JOptionPane.showMessageDialog(frame, "Files Organized Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } 
                    else {
                        JOptionPane.showMessageDialog(frame, "Please select a valid source directory.");
                    }
                }
            });

            panel.add(directoryField);
            panel.add(browseButton);
            panel.add(startButton);

            frame.add(panel);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
