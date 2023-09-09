import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;

public class SimpleHttpsClient
{
    public static void main(String[] args) throws Exception
    {
        String serverUrl = "https://localhost:8080"; // Replace with your server URL
        String truststoreFileName = "truststore.jks"; // Replace with your truststore file name
        String downloadFileName = "file.txt"; // File to download

        String currentDirectory = System.getProperty("user.dir");
        String truststorePath = currentDirectory + File.separator + truststoreFileName;
        String downloadFilePath = currentDirectory + File.separator + "download" + File.separator + downloadFileName;

        // Configure SSL/TLS
        System.setProperty("javax.net.ssl.trustStore", truststorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", "password");

        // Create Swing frame for file selection
        JFrame frame = new JFrame("File Selection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton fileSelectButton = new JButton("Select File");
        frame.add(fileSelectButton);

        fileSelectButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION)
                {
                    File selectedFile = fileChooser.getSelectedFile();
                    String selectedFilePath = selectedFile.getAbsolutePath();

                    // File upload
                    boolean uploadSuccess = uploadFile(serverUrl + "/upload", selectedFilePath);

                    if (uploadSuccess)
                    {
                        System.out.println("File uploaded successfully.");
                    }
                    else
                    {
                        System.out.println("File upload failed.");
                    }
                }
            }
        });

        frame.pack();
        frame.setVisible(true);

        // File download
        boolean downloadSuccess = downloadFile(serverUrl + "/download", downloadFilePath);

        if (downloadSuccess)
        {
            System.out.println("File downloaded successfully.");
        }
        else
        {
            System.out.println("File download failed.");
        }
    }

    static boolean uploadFile(String serverUrl, String filePath)
    {
        // Implement your file upload logic here
        try
        {
            URL url = new URL(serverUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            File fileToUpload = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(fileToUpload);
            OutputStream outputStream = conn.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1)
            {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            fileInputStream.close();

            int responseCode = conn.getResponseCode();
            return responseCode == 200; // Check if the upload was successful
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    static boolean downloadFile(String serverUrl, String filePath)
    {
        // Implement your file download logic here
        try
        {
            URL url = new URL(serverUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream inputStream = conn.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(filePath);

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1)
            {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            int responseCode = conn.getResponseCode();
            return responseCode == 200; // Check if the download was successful
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
