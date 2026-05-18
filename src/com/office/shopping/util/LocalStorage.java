package com.office.shopping.util;

import com.office.shopping.model.User;

import java.io.*;

/**
 * 本地存储工具类
 * 负责用户登录信息的本地存储和读取
 */
public class LocalStorage {
    /**
     * 存储文件路径
     */
    private static final String STORAGE_FILE = "user_login.dat";
    
    /**
     * 保存用户登录信息到本地文件
     * @param user 用户对象，包含登录信息
     */
    public static void saveUserLogin(User user) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STORAGE_FILE))) {
            oos.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 从本地文件读取用户登录信息
     * @return 用户对象，如果没有保存的登录信息则返回null
     */
    public static User loadUserLogin() {
        File file = new File(STORAGE_FILE);
        if (!file.exists()) {
            return null;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STORAGE_FILE))) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 清除本地保存的用户登录信息
     */
    public static void clearUserLogin() {
        File file = new File(STORAGE_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}