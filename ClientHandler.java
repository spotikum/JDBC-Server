import javax.swing.text.TabableView;
import javax.swing.text.TableView;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.sql.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

class ClientHandler extends Thread {
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket socket;

    // Menyiapkan paramter JDBC untuk koneksi ke datbase
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/db_penjualan";
    static final String USER = "root";
    static final String PASS = "";

    // Menyiapkan objek untuk database
    static Connection conn;
    static Statement stmt;
    static ResultSet rs;
    static String hasil;

    public ClientHandler(Socket socket, DataInputStream dis, DataOutputStream dos) {
        this.socket = socket;
        this.dis = dis;
        this.dos = dos;
    }

    public void connectDB(String sql, int pilihan) {
        // Koneksi dengan database
        try {
            // Koneksi Driver JDBC
            Class.forName(JDBC_DRIVER);

            // Koneksi Database
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            // Query Untuk database
            if (pilihan == 1) {
                rs = stmt.executeQuery(sql);
                hasil = menampilkan("select");
            } else if (pilihan == 2) {
                rs = stmt.executeQuery(sql);
                hasil = menampilkan("login");
            } else if (pilihan == 3) {
                rs = stmt.executeQuery(sql);
                hasil = menampilkan("validasi");
            } else if (pilihan == 4) {
                stmt.execute(sql);
            } else if (pilihan == 12) {
                rs = stmt.executeQuery(sql);
                hasil = menampilkan("pembelian");
            } else if (pilihan == 13) {
                rs = stmt.executeQuery(sql);
                hasil = menampilkan("tampilkan");
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void run(){
        final String secretKey = "netprog";
        while(true){
            try {
                // wait for input
                String input = dis.readUTF();
                if(input.equals("0")){
                    break;
                }

                String sql;
                StringTokenizer st = new StringTokenizer(input);

                int pilihan = Integer.parseInt(st.nextToken());
                String dataMasukan = " ";
                System.out.println("Input :"+ input);
                if(pilihan == 1){
                    String username = st.nextToken();
                    String password = st.nextToken();
                    password = AES.encrypt(password, secretKey);

                    sql = "select * from tb_user where username = '"+username+"' and password = '"+password+"'";
                    connectDB(sql,2);

                    if(hasil == ""){
                        hasil = "login gagal, username atau password yang dimasukkan salah";
                    }
//                    else
//                        hasil = "login berhasil";
                    dos.writeUTF(hasil);
                }else if(pilihan == 2){
                    System.out.println("pilihan 2");
                    String nama = dis.readUTF();
                    String username = dis.readUTF();
                    String alamat = dis.readUTF();
                    String no_hp = dis.readUTF();
                    String password = dis.readUTF();
                    String ulangi_password =dis.readUTF();
                    System.out.println("nama: " + nama);
                    System.out.println("username: " + username);
                    System.out.println("alamat: " + alamat);
                    System.out.println("no hp: " + no_hp);
                    System.out.println("password: " + password);
                    password = AES.encrypt(password, secretKey);
                    ulangi_password = AES.encrypt(password, secretKey);

                    sql = "select * from tb_user where username = '"+username+"'";
                    connectDB(sql,3);

                    if(hasil == ""){
                        sql = "insert into tb_user (nama,username,alamat,no_hp,password) values ('"+nama+"','"+username+"','"+alamat+"','"+no_hp+"','"+password+"')";
                        connectDB(sql,4);
                        hasil = "User berhasil didaftarkan. Klik enter untuk lanjut ....";
                    }else{
                        hasil = "Username yang dimasukkan sudah terdaftar, klik enter untuk lanjut ....";
                    }
                    dos.writeUTF(hasil);
                }else if(pilihan == 11) {
//                    sql = "SELECT DISTINCT nama_jenis FROM tb_jenis\n";
//                    sql = "SET @no=0\n"+
//                    "SELECT @no:=@no+1 AS nomor, nama_jenis FROM tb_jenis\n" +
//                            "GROUP BY nama_jenis";
                    sql ="SELECT id_kategori, judul_buku, deskripsi, penulis, kategori, penerbit, harga FROM tb_kategori\n" +
                    "inner join tb_buku on tb_kategori.`id_buku` = tb_buku.`id_buku`\n";
                    connectDB(sql,1 );

                    System.out.println("Melihat Barang yang tersedia");
                    // send the result back to the client.
                    dos.writeUTF(hasil);
                }else if(pilihan == 12) {
//                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");dtf.format(now)+
//                    LocalDate now = LocalDate.now();
//                    LocalDate date = LocalDate.now();

                    try {
                        int barang = Integer.parseInt(dis.readUTF());
                        int id_user = Integer.parseInt(dis.readUTF());
                        sql = "insert into tb_pembelian (id_kategori,id_user) values(" +barang+","+id_user+")";
                        connectDB(sql, 4);
                        hasil = "Barang berhasil Dipesan, Klik enter untuk lanjut ....";
                        dos.writeUTF(hasil);
                    } catch (Exception e) {
                        e.printStackTrace();
                        hasil = "Pilihan tidak tersedia";
                        dos.writeUTF(hasil);
                    }
                }else if(pilihan == 13){
                    int id_user = Integer.parseInt(dis.readUTF());
                    //int id = Integer.parseInt(inputUser.get(1));

                    sql = "select id_pembelian, judul_buku, deskripsi, kategori, penulis, penerbit, tanggal_terbit, harga from tb_pembelian\n" +
                           "inner join tb_kategori on tb_pembelian.`id_kategori` = tb_kategori.`id_kategori`\n"+
                            "inner join tb_buku on tb_kategori.`id_buku` = tb_buku.`id_buku`\n"+
                            "where id_user = "+id_user;

//                    sql = "select nama, alamat, no_hp, nama_barang, deskripsi, nama_jenis, type, warna, harga from tb_pembelian\n" +
//                            "inner join tb_user on tb_pembelian.`id_user` = tb_user.`id_user`\n" +
//                            "inner join tb_jenis on tb_pembelian.`id_jenis` = tb_jenis.`id_jenis`\n" +
//                            "inner join tb_barang on tb_pembelian.`id_barang` = tb_barang.`id_barang`\n" +
//                            "where id_user = "+id_user;
                    connectDB(sql,13);

                    dos.writeUTF(hasil);
                }
            } catch(Exception e){
                System.out.print("Klien : "+this.socket+" terputus");
                e.printStackTrace();
                break;
            }
        }
    }

    private void connectDB() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private String menampilkan(String select) {
        String hasil;
        hasil = "";
        int count = 1;
        try{
            while(rs.next()){
                if(select == "select") {

                    //hasil = hasil +"\t|"+ rs.getInt("id") + "\t|\t" +rs.getString("nama_barang") + "\t\t\t\t|\t" + rs.getString("deskripsi")+ "\t\t\t|" + rs.getString("brand")+ "\t|\t"+ rs.getString("type")+ "\t|\t" + rs.getString("harga") + " \n";
                     // hasil = hasil + rs.getString("nama_jenis")+"\n";

                    hasil = hasil + rs.getInt("id_kategori") + ".\t Judul Buku            : " + rs.getString("judul_buku");
                    hasil = hasil + "\n\t Deskripsi Buku        : " + rs.getString("deskripsi");
                    hasil = hasil + "\n\t Kategori              : " + rs.getString("kategori");
                    hasil = hasil + "\n\t Penulis               : " + rs.getString("penulis");
                    hasil = hasil + "\n\t Penerbit              : " + rs.getString("penerbit");
                    hasil = hasil + "\n\t Harga Barang          : Rp." + rs.getInt("harga")+ "\n\n";
                }else if (select == "login"){
                    hasil = rs.getInt("id_user") + " " + rs.getString("nama") +" " + rs.getString("username") + " " + rs.getString("no_hp");
                }else if (select == "validasi"){
                    hasil = ""+rs.getInt("id_user");
                }else if (select == "pembelian"){
//                    hasil = hasil + "\t"+rs.getInt("id_pembelian")+". "+rs.getString("tanggal")+rs.getInt("harga")+"\n";
                    hasil = hasil + rs.getInt("id_kategori") + ".\t Judul Buku            : " + rs.getString("judul_buku");
                    hasil = hasil + "\n\t Deskripsi Buku        : " + rs.getString("deskripsi");
                    hasil = hasil + "\n\t Kategori              : " + rs.getString("kategori");
                    hasil = hasil + "\n\t Penulis               : " + rs.getString("penulis");
                    hasil = hasil + "\n\t Penerbit              : " + rs.getString("penerbit");
                    hasil = hasil + "\n\t Harga Buku            : Rp. " + rs.getInt("harga")+ "\n\n";
                }else if (select == "tampilkan"){
                    hasil = hasil + count + ".\t No. Pembelian   : "+ rs.getInt("id_pembelian");
                    hasil = hasil + " \n\t Judul Buku            :"+ rs.getString("judul_buku");
                    hasil = hasil + " \n\t Deskripsi Buku        :"+ rs.getString("deskripsi");
                    hasil = hasil + " \n\t Kategori              :" + rs.getString("kategori");
                    hasil = hasil + " \n\t Penulis               :" + rs.getString("penulis");
                    hasil = hasil + " \n\t Penerbit              :" + rs.getString("penerbit");
                    hasil = hasil + " \n\t Tanggal Terbit        :" + rs.getString("tanggal_terbit");
                    hasil = hasil + " \n\t Harga Barang          :" + rs.getInt("harga")+"\n\n";
                }
            }
        }catch (Exception e){
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, e);
        }
        return hasil;
    }
}