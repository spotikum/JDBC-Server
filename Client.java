import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Scanner;

public class Client {
    static String usernaame,nama,alamat,no_hp,id_user;
    static boolean login = false;

    public static void main(String[] args) {

        int port = 5555;
        boolean running = true;
        boolean kondisi = false;
        String jawab;
        String inp = "";
        Scanner scan = new Scanner(System.in);

        try {
            // membuat koneksi dengan socket

            Scanner sc = new Scanner(System.in);
            Socket socket = new Socket("127.0.0.1", port);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

            while (running) {
                if(!login){
                    MenuAuth(dis,dos);
                }else{
                    MenuUtama(dis,dos);
                }

                System.out.println("\nApakah anda ingin lanjut?");
                System.out.print("Jawab y/n : ");

                jawab = scan.nextLine();

                // Mengecek input User
                if( jawab.equalsIgnoreCase("y") ){
                    running = true;
                    kondisi = false;
                }else{
                    running = false;
                    kondisi = false;
                }
            }
        }catch (Exception e) {
            System.out.println("Anda Keluar dari Aplikasi");
        }
    }

    public static void MenuAuth(DataInputStream dis, DataOutputStream dos) throws IOException{
        String pilihan;
        Scanner sc = new Scanner(System.in);

        System.out.print("\t\t----Sistem Informasi----");
        System.out.print("\n\t\t----Penjualan Buku----");

        System.out.print("\n\n1. Register");
        System.out.print("\n2. Login");
        System.out.print("\n0. Keluar");
        System.out.print("\nMasukkan pilihan anda: ");
        pilihan = sc.nextLine();

        switch(pilihan){
            case "1":
                MenuRegsister(dis,dos);
                break;
            case "2":
                MenuLogin(dis,dos);
                break;
            case "0":
                break;
            default:
                System.out.println("Pilihan Tidak Tersedia");
        }
    }

    public static void MenuLogin(DataInputStream dis, DataOutputStream dos) throws IOException{
        Scanner sc = new Scanner(System.in);
        String username,password,send,back;
        String out = "login gagal, username atau password yang dimasukkan salah";
        String kondisi = new String("");
        kondisi = out;

        while(kondisi.equals("login gagal, username atau password yang dimasukkan salah") ){
            System.out.print("\t\t  ----Sistem Informasi----");
            System.out.print("\n\t\t----Penjualan Buku----");
            System.out.print("\n\t\t  ---Menu Login---");
            System.out.print("\n\nMasukkan username: ");
            username = sc.nextLine();
            System.out.print("Masukkan Password: ");
            password = sc.nextLine();

            send = "1" + " " + username + " " + password;
            // Mengirim nilai ke server
            dos.writeUTF(send);

            out = dis.readUTF();
            kondisi = out;
            if(kondisi.equals("login gagal, username atau password yang dimasukkan salah")){
                System.out.println(out);
                back = sc.nextLine();
            }else {
                System.out.print("login Berhasil. Klik enter untuk lanjut ....");
                System.in.read();

            }
        }

        StringTokenizer st = new StringTokenizer(out);
        id_user = st.nextToken();
        login = true;
        MenuUtama(dis,dos);
    }

    public static void MenuRegsister(DataInputStream dis, DataOutputStream dos) throws IOException{
        String nama,username,alamat,password,no_tlp,ulangi_password,send,out,back;
        Scanner sc = new Scanner(System.in);
        String kondisi = new String("Register gagal, Username yang dimasukkan sudah terdaftar");

        while(kondisi.equals("Register gagal, Username yang dimasukkan sudah terdaftar")){
            dos.writeUTF("2");
            System.out.print("\t\t  ----Sistem Informasi----");
            System.out.print("\n\t\t----Penjualan Buku----");
            System.out.print("\n\t\t  ---Menu Register---");
            System.out.print("\n\nMasukkan nama: ");
            nama = sc.nextLine();
            dos.writeUTF(nama);
            System.out.print("Masukkan username: ");
            username = sc.nextLine();
            dos.writeUTF(username);
            System.out.print("Masukkan alamat: ");
            alamat = sc.nextLine();
            dos.writeUTF(alamat);
            System.out.print("Masukkan nomor telepon: ");
            no_tlp = sc.nextLine();
            dos.writeUTF(no_tlp);
            System.out.print("Masukkan Password: ");
            password = sc.nextLine();
            dos.writeUTF(password);
            System.out.print("Ulangi Password: ");
            ulangi_password = sc.nextLine();
            dos.writeUTF(ulangi_password);

            if (username.isEmpty()) {
                System.out.print("\nUsername Tidak Boleh kosong");
                System.out.print("\n\nKlik enter untuk lanjut...");
                System.in.read();
                continue;
            }else if (username.length() < 6 ){
                System.out.print("\nUsername terlalu pendek. Masukkan username hingga 6 Karakter");
                System.out.print("\n\nKlik enter untuk lanjut...");
                System.in.read();
                continue;
            }else if (password.isEmpty()){
                System.out.print("\nPassword Tidak boleh kosong");
                System.out.print("\n\nKlik enter untuk lanjut...");
                System.in.read();
                continue;
            }else if (password.length() < 8) {
                System.out.print("\nPassword terlalu pendek. Masukkan password hingga 8 Karakter");
                System.out.print("\n\nKlik enter untuk lanjut...");
                System.in.read();
                continue;
            } else if (!password.equals(ulangi_password)) {
                System.out.print("\nPassword yang anda masukkan berbeda. Mohon dicoba kembali.");
                System.out.print("\n\nKlik enter untuk lanjut...");
                System.in.read();
                continue;
            }else if (no_tlp.length() > 13){
                System.out.print("\nNo Tlp Maksimal 13 Karakter");
                System.out.print("\n\nKlik enter untuk lanjut...");
                System.in.read();
                continue;


        }
            //Menunggu Output dari Server
            out = dis.readUTF();
            kondisi = out;

            System.out.println(out);
            back = sc.nextLine();
        }
        MenuLogin(dis,dos);
    }

    public static void MenuUtama(DataInputStream dis, DataOutputStream dos) throws IOException{
        String inp;
        Scanner sc = new Scanner(System.in);

        System.out.print("\t\t----Sistem Informasi----");
        System.out.print("\n\t\t----Penjualan Buku----");
        System.out.print("\n\t\t---Menu Utama---");
        System.out.print("\n\nMasukkan pilihan Layanan :");
        System.out.print("\n1. Melihat Barang yang tersedia");
        System.out.print("\n2. Pemesanan");
        System.out.print("\n3. Melihat Daftar Pesanan");
        System.out.print("\n0. Keluar");
        System.out.print("\nMasukkan pilihan anda: ");
        inp = sc.nextLine();

        switch(inp){
            case "1":
                DaftarBarang(dis,dos);
                break;
            case "2":
                PemesananBarang(dis,dos);
                break;
            case "3":
                LihatPesanan(dis,dos);
                break;
            case "0":
                break;
            default:
                System.out.println("Pilihan Tidak Tersedia");

        }
    }


    public static void DaftarBarang(DataInputStream dis, DataOutputStream dos) throws IOException{
        String hasil;
        System.out.print("\t\t|---Sistem Informasi---|");
        System.out.print("\n\t\t|---Penjualan Buku---|");
        System.out.print("\n\t\t|--Daftar barang--|\n\n\n");
        //System.out.print("\t|No.|\t\t Nama Barang \t\t\t|\tDeskripsi Barang \t\t\t| Brand \t|\t type \t|\t warna \t|\t Harga |\n");
        dos.writeUTF("11");
        hasil = dis.readUTF();
        System.out.println(hasil);
    }
    public static void PemesananBarang(DataInputStream dis, DataOutputStream dos) throws IOException{
        String hasil,barang,test;
        Scanner sc = new Scanner(System.in);

        System.out.print("\t\t|---Sistem Informasi---|");
        System.out.print("\n\t\t|---Penjualan Buku---|");
        System.out.print("\n\t\t|--Pembelian Barang--|\n\n");
        dos.writeUTF("11");
        hasil = dis.readUTF();
        System.out.println(hasil);
        System.out.print("\nMasukkan no Barang yang akan dibeli : ");
        barang = sc.nextLine();
        dos.writeUTF("12 "+barang);

        dos.writeUTF(barang);
        dos.writeUTF(""+id_user);
        hasil = dis.readUTF();
        System.out.println(hasil);
        test = sc.nextLine();
    }

    public static void LihatPesanan(DataInputStream dis, DataOutputStream dos) throws IOException{
        String hasil;
        System.out.print("\t\t----Sistem Informasi----");
        System.out.print("\n\t\t----Penjualan Buku----");
        System.out.print("\n\t\t---Lihat pesanan---\n\n");
        System.out.print("\n\t\t---Daftar Pembelian Anda---\n");
        dos.writeUTF("13");
        dos.writeUTF(""+id_user);
        hasil = dis.readUTF();
        System.out.println(hasil);
    }
}
