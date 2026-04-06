import java.io.*;
import java.net.*;

public class FileServer {

    static final int PORT = 5000;
    static final String STORAGE_DIR = "server_files";

    public static void main(String[] args) {
        new File(STORAGE_DIR).mkdir();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("File Server started...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");

                new Thread(new ClientHandler(clientSocket)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {

    private Socket socket;

    ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        ) {
            while (true) {
                String command = dis.readUTF();

                if (command.equals("UPLOAD")) {
                    receiveFile(dis, dos);
                } else if (command.equals("DOWNLOAD")) {
                    sendFile(dis, dos);
                } else if (command.equals("EXIT")) {
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("Client disconnected");
        }
    }

    private void receiveFile(DataInputStream dis, DataOutputStream dos) throws IOException {
        String fileName = dis.readUTF();
        long fileSize = dis.readLong();

        File file = new File(FileServer.STORAGE_DIR + "/" + fileName);
        FileOutputStream fos = new FileOutputStream(file);

        byte[] buffer = new byte[4096];
        int read;
        long remaining = fileSize;

        while ((read = dis.read(buffer, 0, (int)Math.min(buffer.length, remaining))) > 0) {
            fos.write(buffer, 0, read);
            remaining -= read;
        }

        fos.close();
        dos.writeUTF("File uploaded successfully");
        System.out.println("Received: " + fileName);
    }

    private void sendFile(DataInputStream dis, DataOutputStream dos) throws IOException {
        String fileName = dis.readUTF();
        File file = new File(FileServer.STORAGE_DIR + "/" + fileName);

        if (!file.exists()) {
            dos.writeUTF("NOT_FOUND");
            return;
        }

        dos.writeUTF("FOUND");
        dos.writeLong(file.length());

        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        int read;

        while ((read = fis.read(buffer)) > 0) {
            dos.write(buffer, 0, read);
        }

        fis.close();
        System.out.println("Sent: " + fileName);
    }
}