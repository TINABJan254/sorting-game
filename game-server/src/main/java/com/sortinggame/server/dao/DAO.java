/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sortinggame.server.dao;

import com.sortinggame.server.util.ConfigLoader;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author ADMIN
 */
public class DAO {
    protected Connection connection;

    public DAO() {
        try {
            Class.forName(ConfigLoader.getDriver());
            connection = DriverManager.getConnection(
                ConfigLoader.getUrl(),
                ConfigLoader.getUsername(),
                ConfigLoader.getPassword()
            );
            System.out.println("Ket noi db thanh cong");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Khong the ket noi toi db: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        DAO d = new DAO();
        
    }
    
}
