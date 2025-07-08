/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.koneksi;
import java.sql.*;
/**
 *
 * @author King Topik
 */
public class koneksi {
    private Connection koneksi;
    public Connection connect(){
    try{
     Class.forName("com.mysql.jdbc.Driver");
     System.out.println("berhasil konek");
    }
    catch(ClassNotFoundException ex){
        System.out.println("gagal konek"+ex);
    }
    String url ="jdbc:mysql://localhost/db_bestpropertyy";
    try{
        koneksi= DriverManager.getConnection(url, "root","");
        System.out.println("berhasil koneksi database");   
    }
    catch(SQLException ex){
    System.out.println("gagal koneksi database"+ex);
    }
    return koneksi;
    }
}
