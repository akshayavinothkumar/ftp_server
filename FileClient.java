import java.io.*;
import java.net.*;
import java.util.Scanner;

public class FileClient {

    static final String SERVER_IP = "localhost";
    static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            Scanner sc = new Scanner(System.in);
        ) {

            while (true) {
                System.out.println("\n1. Upload File");
                System.out.println("2. Download File");
                System.out.println("3. Exit");

                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 1) {
                    System.out.print("Enter file path: ");
                    String path = sc.nextLine();

                    File file = new File(path);

                    if (!file.exists()) {
                        System.out.println("File not found");
                        continue;
                    }

                    dos.writeUTF("UPLOAD");
                    dos.writeUTF(file.getName());
                    dos.writeLong(file.length());

                    FileInputStream fis = new FileInputStream(file);
                    byte[] buffer = new byte[4096];
                    int read;

                    while ((read = fis.read(buffer)) > 0) {
                        dos.write(buffer, 0, read);
                    }

                    fis.close();

                    String response = dis.readUTF();
                    System.out.println("Server: " + response);
                }

                else if (choice == 2) {
                    System.out.print("Enter file name: ");
                    String fileName = sc.nextLine();

                    dos.writeUTF("DOWNLOAD");
                    dos.writeUTF(fileName);

                    String status = dis.readUTF();

                    if (status.equals("NOT_FOUND")) {
                        System.out.println("File not found on server");
                        continue;
                    }

                    long fileSize = dis.readLong();
                    FileOutputStream fos = new FileOutputStream("downloaded_" + fileName);

                    byte[] buffer = new byte[4096];
                    int read;
                    long remaining = fileSize;

                    while ((read = dis.read(buffer, 0, (int)Math.min(buffer.length, remaining))) > 0) {
                        fos.write(buffer, 0, read);
                        remaining -= read;
                    }

                    fos.close();
                    System.out.println("File downloaded successfully");
                }

                else {
                    dos.writeUTF("EXIT");
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}