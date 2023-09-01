import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import org.json.JSONException;
import org.json.JSONObject;

public class FileOrganizerLogic {
    private static JSONObject config;
    public static boolean exitStatus = true;

    public static boolean getExitStatus() {
        return exitStatus;
    }

    private static void loadConfig() {
        try {
            String configPath = "config.json"; // Adjust path if needed
            String configContent = Files.readString(Paths.get(configPath));
            config = new JSONObject(configContent);
        }

        catch (IOException | JSONException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void organizeFiles(File selectedDirectory) {

        loadConfig();

        String sourceDirectory = selectedDirectory.getAbsolutePath();

        try {
            Files.walkFileTree(Paths.get(sourceDirectory), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String fileName = file.getFileName().toString();
                    String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

                    String destinationFolder = determineDestinationFolder(extension);

                    if (destinationFolder != null) {
                        createDestinationFolder(sourceDirectory, destinationFolder);
                        Path destinationPath = Paths.get(sourceDirectory, destinationFolder, fileName);
                        Files.move(file, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Moved: " + fileName + " to " + destinationFolder);
                    }

                    else {
                        exitStatus = false;
                        System.out.println("No suitable destination folder found for: " + fileName);
                    }

                    return FileVisitResult.CONTINUE;
                }
            });
        }
        
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String determineDestinationFolder(String extension) {
        if (config.has(extension.toLowerCase())) {
            return config.getString(extension.toLowerCase());
        } else {
            return "miscellany"; // Default value if extension is not found
        }
    }


    private static void createDestinationFolder(String sourceDirectory, String folderName) {
        File destinationFolder = new File(sourceDirectory, folderName);
        if (!destinationFolder.exists()) {
            boolean folderCreated = destinationFolder.mkdirs();
            if (folderCreated) {
                System.out.println("Created destination folder: " + folderName);
            }
            
            else {
                System.out.println("Failed to create destination folder: " + folderName);
            }
        }
    }

    
}