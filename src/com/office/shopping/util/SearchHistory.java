package com.office.shopping.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索历史工具类
 * 负责搜索历史的添加、加载、保存和管理
 */
public class SearchHistory {
    /**
     * 历史记录文件路径
     */
    private static final String HISTORY_FILE = "search_history.dat";
    /**
     * 最大历史记录数量
     */
    private static final int MAX_HISTORY = 30;
    /**
     * 默认显示的历史记录数量
     */
    private static final int DEFAULT_DISPLAY = 5;
    
    /**
     * 添加搜索历史记录
     * @param keyword 搜索关键词
     */
    public static void addHistory(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return;
        
        List<String> history = loadHistory();
        
        // 如果关键词已存在，移除旧的记录
        history.remove(keyword);
        
        // 将新关键词添加到最前面
        history.add(0, keyword);
        
        // 保持历史记录不超过最大数量
        if (history.size() > MAX_HISTORY) {
            history = history.subList(0, MAX_HISTORY);
        }
        
        saveHistory(history);
    }
    
    /**
     * 加载搜索历史记录
     * @return 历史记录列表
     */
    public static List<String> loadHistory() {
        File file = new File(HISTORY_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * 保存搜索历史记录
     * @param history 历史记录列表
     */
    private static void saveHistory(List<String> history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HISTORY_FILE))) {
            oos.writeObject(history);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取默认显示的历史记录（最多5条）
     * @return 默认显示的历史记录
     */
    public static List<String> getDefaultHistory() {
        List<String> history = loadHistory();
        if (history.size() <= DEFAULT_DISPLAY) {
            return history;
        }
        return history.subList(0, DEFAULT_DISPLAY);
    }
    
    /**
     * 获取所有历史记录（最多30条）
     * @return 所有历史记录
     */
    public static List<String> getAllHistory() {
        return loadHistory();
    }
    
    /**
     * 清空搜索历史记录
     */
    public static void clearHistory() {
        File file = new File(HISTORY_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
