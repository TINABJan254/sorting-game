/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sortinggame.server.util;

import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author ADMIN
 */
public class ConfigLoader {

    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties = null;

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Khong tim thay file cau hinh: " + CONFIG_FILE);
            }
            properties = new Properties();
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Loi load cau hinh db: " + e.getMessage(), e);
        }
    }

    public static String getHost() {
        return properties.getProperty("db.host");
    }

    public static String getPort() {
        return properties.getProperty("db.port");
    }

    public static String getUsername() {
        return properties.getProperty("db.username");
    }

    public static String getPassword() {
        return properties.getProperty("db.password");
    }

    public static String getDbName() {
        return properties.getProperty("db.dbname");
    }

    public static String getDriver() {
        return properties.getProperty("db.driver");
    }

    public static String getUrl() {
        //jdbc:mysql://host:port/dbname
        return String.format("jdbc:mysql://%s:%s/%s", getHost(), getPort(), getDbName());
    }
}
