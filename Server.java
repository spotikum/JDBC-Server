import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) throws IOException {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } 	catch(Exception e){
            e.printStackTrace();
        }
        int port = 5555;

        ServerSocket ss = new ServerSocket(port);
        System.out.println("Server ON (Port: "+port+")");

        while(true){

            Socket socket = null;

            try {
                //menerima socket dari client
                socket = ss.accept();
                System.out.println("\nClient has been connected : " + socket );

                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                //membuat thread baru
                System.out.println("New thread for client : " + socket );
                Thread th = new ClientHandler(socket, dis, dos);
                th.start();

            } catch (Exception e) {
                socket.close();
                e.printStackTrace();
            }
        }


    }

}
