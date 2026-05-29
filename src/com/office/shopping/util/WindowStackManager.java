package com.office.shopping.util;

import javax.swing.*;
import java.util.Stack;

/**
 * 窗口栈管理器
 * 使用栈来管理窗口，焦点始终在栈顶的窗口上
 */
public class WindowStackManager {
    
    private static WindowStackManager instance;
    private Stack<JDialog> windowStack;
    
    private WindowStackManager() {
        windowStack = new Stack<>();
    }
    
    /**
     * 获取窗口栈管理器的单例实例
     * @return WindowStackManager实例
     */
    public static WindowStackManager getInstance() {
        if (instance == null) {
            instance = new WindowStackManager();
        }
        return instance;
    }
    
    /**
     * 将窗口压入栈顶
     * @param dialog 要压入的对话框
     */
    public void push(JDialog dialog) {
        // 如果栈不为空，隐藏当前栈顶窗口
        if (!windowStack.isEmpty()) {
            JDialog topWindow = windowStack.peek();
            topWindow.setVisible(false);
        }
        
        // 将新窗口压入栈
        windowStack.push(dialog);
        
        // 显示新窗口并设置焦点
        dialog.setVisible(true);
        dialog.requestFocus();
        
        System.out.println("窗口已压入栈顶: " + dialog.getTitle());
    }
    
    /**
     * 弹出栈顶窗口
     * @return 弹出的窗口，如果栈为空则返回null
     */
    public JDialog pop() {
        if (windowStack.isEmpty()) {
            return null;
        }
        
        JDialog poppedWindow = windowStack.pop();
        poppedWindow.dispose();
        
        System.out.println("窗口已弹出栈: " + poppedWindow.getTitle());
        
        // 如果栈不为空，显示新的栈顶窗口并设置焦点
        if (!windowStack.isEmpty()) {
            JDialog newTopWindow = windowStack.peek();
            newTopWindow.setVisible(true);
            newTopWindow.requestFocus();
            System.out.println("焦点已切换到新栈顶窗口: " + newTopWindow.getTitle());
        }
        
        return poppedWindow;
    }
    
    /**
     * 获取当前栈顶窗口
     * @return 栈顶窗口，如果栈为空则返回null
     */
    public JDialog peek() {
        if (windowStack.isEmpty()) {
            return null;
        }
        return windowStack.peek();
    }
    
    /**
     * 检查栈是否为空
     * @return 如果栈为空返回true，否则返回false
     */
    public boolean isEmpty() {
        return windowStack.isEmpty();
    }
    
    /**
     * 获取栈的大小
     * @return 栈中窗口的数量
     */
    public int size() {
        return windowStack.size();
    }
    
    /**
     * 清空整个栈
     */
    public void clear() {
        while (!windowStack.isEmpty()) {
            JDialog dialog = windowStack.pop();
            dialog.dispose();
        }
        System.out.println("窗口栈已清空");
    }
}
