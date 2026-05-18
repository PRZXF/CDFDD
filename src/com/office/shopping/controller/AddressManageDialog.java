package com.office.shopping.controller; // 声明包名

import com.office.shopping.model.Address; // 导入地址模型类
import com.office.shopping.model.User; // 导入用户模型类
import com.office.shopping.service.AddressService; // 导入地址服务类
import com.office.shopping.util.ButtonStyle; // 导入按钮样式工具类
import com.office.shopping.util.ChinaProvinces; // 导入中国省份数据工具类

import javax.swing.*; // 导入Swing组件包
import javax.swing.event.DocumentEvent; // 导入文档事件类
import javax.swing.event.DocumentListener; // 导入文档监听器接口
import java.awt.*; // 导入AWT组件包
import java.util.List; // 导入List接口

/**
 * 地址管理对话框
 * <p>
 * 合并地址选择、新增和编辑功能于一体的统一对话框
 * 支持省市输入搜索和自动补全，提供直观的地址管理界面
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class AddressManageDialog extends JDialog { // 定义地址管理对话框类，继承自JDialog

    private User currentUser; // 当前用户对象
    private AddressService addressService = new AddressService(); // 创建地址服务实例
    private Address selectedAddress = null; // 选中的地址对象
    private boolean confirmed = false; // 是否确认选择标志

    // 组件
    private JPanel addressListPanel; // 地址列表面板
    private JPanel formPanel; // 地址表单面板
    private JComboBox<String> provinceComboBox; // 省份下拉框
    private JComboBox<String> cityComboBox; // 城市下拉框
    private JTextField nameField; // 收货人输入框
    private JTextField phoneField; // 联系电话输入框
    private JTextField districtField; // 区县输入框
    private JTextArea detailArea; // 详细地址输入区
    private JCheckBox defaultCheckBox; // 默认地址复选框
    private JButton saveButton; // 保存按钮（新增）
    private JButton updateButton; // 更新按钮（更新）
    private JButton cancelButton; // 取消按钮
    private JButton addNewButton; // 新增按钮

    // 当前编辑的地址ID（0表示新增）
    private int editingAddressId = 0; // 编辑地址ID，0表示新增

    /**
     * 构造方法
     */
    public AddressManageDialog(JFrame parent, User user) { // 带参数构造方法
        super(parent, "收货信息", true); // 调用父类构造方法，设置标题和模态
        this.currentUser = user; // 设置当前用户

        setSize(600, 500); // 设置对话框大小
        setResizable(false); // 设置不可调整大小
        setLocationRelativeTo(parent); // 设置相对于父窗口居中
        setLayout(new BorderLayout()); // 设置边框布局

        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout()); // 创建主面板，使用边框布局
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 设置边距

        // 左侧：地址列表
        JPanel leftPanel = createAddressListPanel(); // 创建地址列表面板
        mainPanel.add(leftPanel, BorderLayout.WEST); // 添加到主面板西部

        // 右侧：地址编辑表单
        JPanel rightPanel = createAddressFormPanel(); // 创建地址表单面板
        mainPanel.add(rightPanel, BorderLayout.CENTER); // 添加到主面板中央

        add(mainPanel, BorderLayout.CENTER); // 将主面板添加到对话框中央

        // 底部按钮
        JPanel buttonPanel = createBottomButtonPanel(); // 创建底部按钮面板
        add(buttonPanel, BorderLayout.SOUTH); // 添加到对话框南部

        // 加载地址列表
        loadAddressList(); // 调用加载地址列表方法

        // 初始化时尝试加载默认地址到表单
        loadDefaultAddress(); // 加载默认地址
    }

    /**
     * 创建地址列表面板
     */
    private JPanel createAddressListPanel() { // 创建地址列表面板方法
        JPanel panel = new JPanel(new BorderLayout()); // 创建面板，使用边框布局
        panel.setPreferredSize(new Dimension(250, 0)); // 设置首选宽度
        panel.setBorder(BorderFactory.createTitledBorder("已有收货信息")); // 设置标题边框

        // 地址列表
        addressListPanel = new JPanel(); // 创建地址列表容器面板
        addressListPanel.setLayout(new BoxLayout(addressListPanel, BoxLayout.Y_AXIS)); // 设置垂直布局
        addressListPanel.setBackground(Color.WHITE); // 设置白色背景

        JScrollPane scrollPane = new JScrollPane(addressListPanel); // 创建滚动面板
        scrollPane.setBorder(null); // 移除边框
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // 设置滚动速度
        panel.add(scrollPane, BorderLayout.CENTER); // 添加到面板中央

        // 新增收货信息按钮
        addNewButton = new JButton("+ 新增收货信息"); // 创建新增按钮
        addNewButton.setFont(new Font("微软雅黑", Font.PLAIN, 12)); // 设置字体
        addNewButton.setAlignmentX(Component.CENTER_ALIGNMENT); // 居中对齐
        addNewButton.setBackground(new Color(70, 130, 180)); // 设置蓝色背景
        addNewButton.setForeground(Color.WHITE); // 设置白色文字
        addNewButton.setBorder(null); // 移除边框
        addNewButton.setFocusPainted(false); // 移除焦点框
        addNewButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 设置手型光标
        addNewButton.setPreferredSize(new Dimension(200, 30)); // 设置按钮大小
        addNewButton.addMouseListener(new java.awt.event.MouseAdapter() { // 添加鼠标事件
            public void mouseEntered(java.awt.event.MouseEvent evt) { // 鼠标进入
                addNewButton.setBackground(new Color(50, 110, 160)); // 加深颜色
            }

            public void mouseExited(java.awt.event.MouseEvent evt) { // 鼠标退出
                addNewButton.setBackground(new Color(70, 130, 180)); // 恢复颜色
            }
        });
        addNewButton.addActionListener(e -> clearForm()); // 点击清空表单

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // 创建按钮面板
        buttonPanel.setBackground(Color.WHITE); // 设置白色背景
        buttonPanel.add(addNewButton); // 添加按钮
        panel.add(buttonPanel, BorderLayout.SOUTH); // 添加到面板南部

        return panel; // 返回面板
    }

    /**
     * 创建地址表单面板
     */
    private JPanel createAddressFormPanel() { // 创建地址表单面板方法
        JPanel panel = new JPanel(new BorderLayout()); // 创建面板，使用边框布局
        panel.setBorder(BorderFactory.createTitledBorder("收货信息")); // 设置标题边框
        panel.setBackground(Color.WHITE); // 设置白色背景

        formPanel = new JPanel(new GridBagLayout()); // 创建表单面板，使用网格包布局
        formPanel.setBackground(Color.WHITE); // 设置白色背景
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // 设置边距

        GridBagConstraints gbc = new GridBagConstraints(); // 创建网格约束对象
        gbc.fill = GridBagConstraints.HORIZONTAL; // 设置水平填充
        gbc.insets = new Insets(8, 5, 8, 5); // 设置组件间距
        gbc.weightx = 1.0; // 设置水平权重

        // 收货人
        gbc.gridx = 0; // 设置网格列索引
        gbc.gridy = 0; // 设置网格行索引
        formPanel.add(new JLabel("收货人:"), gbc); // 添加标签到表单

        gbc.gridx = 1; // 设置网格列索引
        nameField = new JTextField(20); // 创建收货人输入框
        nameField.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        formPanel.add(nameField, gbc); // 添加输入框到表单

        // 联系电话
        gbc.gridx = 0; // 设置网格列索引
        gbc.gridy = 1; // 设置网格行索引
        formPanel.add(new JLabel("联系电话:"), gbc); // 添加标签到表单

        gbc.gridx = 1; // 设置网格列索引
        phoneField = new JTextField(20); // 创建联系电话输入框
        phoneField.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        formPanel.add(phoneField, gbc); // 添加输入框到表单

        // 省份（可编辑的下拉框）
        gbc.gridx = 0; // 设置网格列索引
        gbc.gridy = 2; // 设置网格行索引
        formPanel.add(new JLabel("省份:"), gbc); // 添加标签到表单

        gbc.gridx = 1; // 设置网格列索引
        provinceComboBox = new JComboBox<>(ChinaProvinces.PROVINCES); // 创建省份下拉框
        provinceComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        provinceComboBox.setEditable(true); // 设置可编辑
        provinceComboBox.setPreferredSize(new Dimension(200, 25)); // 设置首选大小
        setupAutoCompleteComboBox(provinceComboBox, ChinaProvinces.PROVINCES); // 设置自动补全
        formPanel.add(provinceComboBox, gbc); // 添加下拉框到表单

        // 城市（可编辑的下拉框）
        gbc.gridx = 0; // 设置网格列索引
        gbc.gridy = 3; // 设置网格行索引
        formPanel.add(new JLabel("城市:"), gbc); // 添加标签到表单

        gbc.gridx = 1; // 设置网格列索引
        cityComboBox = new JComboBox<>(); // 创建城市下拉框
        cityComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        cityComboBox.setEditable(true); // 设置可编辑
        cityComboBox.setPreferredSize(new Dimension(200, 25)); // 设置首选大小
        formPanel.add(cityComboBox, gbc); // 添加下拉框到表单

        // 初始化城市列表
        int selectedProvinceIndex = provinceComboBox.getSelectedIndex(); // 获取选中省份索引
        String[] cities = ChinaProvinces.getCitiesByProvince(selectedProvinceIndex); // 获取对应城市列表
        updateCityComboBox(cities); // 更新城市下拉框

        // 为城市下拉框设置自动补全（只设置一次）
        setupCityAutoComplete(); // 设置城市自动补全

        // 省份选择事件监听
        provinceComboBox.addActionListener(e -> { // 添加省份选择事件监听器
            String selectedProvince = (String) provinceComboBox.getSelectedItem(); // 获取选中省份
            if (selectedProvince != null && !selectedProvince.isEmpty()) { // 判断省份非空
                int provinceIndex = findProvinceIndex(selectedProvince); // 查找省份索引
                String[] cityList = ChinaProvinces.getCitiesByProvince(provinceIndex); // 获取城市列表
                if (cityList != null && cityList.length > 0) { // 判断城市列表有效
                    updateCityComboBox(cityList); // 更新城市下拉框
                }
            }
        });

        // 区县
        gbc.gridx = 0; // 设置网格列索引
        gbc.gridy = 4; // 设置网格行索引
        formPanel.add(new JLabel("区县:"), gbc); // 添加标签到表单

        gbc.gridx = 1; // 设置网格列索引
        districtField = new JTextField(20); // 创建区县输入框
        districtField.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        formPanel.add(districtField, gbc); // 添加输入框到表单

        // 详细地址
        gbc.gridx = 0; // 设置网格列索引
        gbc.gridy = 5; // 设置网格行索引
        gbc.anchor = GridBagConstraints.NORTHWEST; // 设置锚点为西北
        formPanel.add(new JLabel("详细地址:"), gbc); // 添加标签到表单

        gbc.gridx = 1; // 设置网格列索引
        detailArea = new JTextArea(3, 20); // 创建详细地址输入区
        detailArea.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
        detailArea.setLineWrap(true); // 设置自动换行
        formPanel.add(new JScrollPane(detailArea), gbc); // 添加滚动面板到表单

        // 默认地址
        gbc.gridx = 0; // 设置网格列索引
        gbc.gridy = 6; // 设置网格行索引
        gbc.gridwidth = 2; // 设置跨两列
        gbc.anchor = GridBagConstraints.CENTER; // 设置居中对齐
        defaultCheckBox = new JCheckBox("设为默认收货信息"); // 创建默认地址复选框
        defaultCheckBox.setFont(new Font("微软雅黑", Font.PLAIN, 13)); // 设置字体
        defaultCheckBox.setBackground(Color.WHITE); // 设置白色背景
        defaultCheckBox.addActionListener(e -> handleDefaultChange()); // 添加默认地址改变事件
        formPanel.add(defaultCheckBox, gbc); // 添加复选框到表单

        panel.add(formPanel, BorderLayout.CENTER); // 将表单面板添加到面板中央

        // 保存/更新按钮
        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // 创建按钮面板
        formButtonPanel.setBackground(Color.WHITE); // 设置白色背景
        formButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // 设置底部边距

        saveButton = new JButton("保存收货信息"); // 创建保存按钮（新增）
        ButtonStyle.setPrimaryStyle(saveButton); // 设置主样式
        saveButton.addActionListener(e -> saveAddress()); // 添加点击事件

        updateButton = new JButton("更新收货信息"); // 创建更新按钮（更新）
        ButtonStyle.setSuccessStyle(updateButton); // 设置成功样式
        updateButton.setVisible(false); // 默认隐藏
        updateButton.addActionListener(e -> updateAddress()); // 添加点击事件

        cancelButton = new JButton("取消"); // 创建取消按钮
        ButtonStyle.setDefaultStyle(cancelButton); // 设置默认样式
        cancelButton.addActionListener(e -> clearForm()); // 添加点击事件

        formButtonPanel.add(saveButton); // 添加保存按钮
        formButtonPanel.add(updateButton); // 添加更新按钮
        formButtonPanel.add(cancelButton); // 添加取消按钮
        panel.add(formButtonPanel, BorderLayout.SOUTH); // 添加按钮面板到南部

        return panel; // 返回面板
    }

    // 省份列表缓存
    private String[] provinceItems; // 省份列表缓存数组
    // 城市列表缓存
    private String[] cityItems = new String[0]; // 城市列表缓存数组

    /**
     * 设置可编辑下拉框的自动补全功能（点击显示下拉框，实时过滤）
     */
    private void setupAutoCompleteComboBox(JComboBox<String> comboBox, String[] items) { // 设置自动补全方法
        this.provinceItems = items; // 保存省份列表
        JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent(); // 获取编辑器组件

        // 点击输入框时显示下拉列表
        editor.addFocusListener(new java.awt.event.FocusAdapter() { // 添加焦点监听器
            @Override
            public void focusGained(java.awt.event.FocusEvent e) { // 获得焦点时
                SwingUtilities.invokeLater(() -> { // 在EDT中执行
                    if (!comboBox.isPopupVisible()) { // 如果下拉框未显示
                        comboBox.showPopup(); // 显示下拉框
                    }
                });
            }
        });

        // 实时根据输入过滤（只过滤，不自动选中）
        editor.addKeyListener(new java.awt.event.KeyAdapter() { // 添加键盘监听器
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) { // 按键释放时
                SwingUtilities.invokeLater(() -> { // 在EDT中执行
                    filterComboBoxItems(comboBox, provinceItems); // 过滤下拉框选项
                });
            }
        });

        // 监听下拉框选择事件，只有点击选项时才填入输入框
        comboBox.addActionListener(e -> { // 添加下拉框选择事件
            if (e.getActionCommand().equals("comboBoxChanged")) { // 判断事件类型
                String selected = (String) comboBox.getSelectedItem(); // 获取选中项
                if (selected != null) { // 判断非空
                    editor.setText(selected); // 设置编辑器文本
                }
            }
        });
    }

    /**
     * 过滤下拉框选项
     */
    private void filterComboBoxItems(JComboBox<String> comboBox, String[] items) { // 过滤下拉框选项方法
        JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent(); // 获取编辑器组件
        String input = editor.getText(); // 获取输入文本

        if (input == null || input.isEmpty()) { // 判断输入为空
            // 输入为空，恢复所有选项
            resetComboBoxItems(comboBox, items); // 重置下拉框选项
            return; // 返回
        }

        // 过滤匹配的选项
        java.util.List<String> filteredItems = new java.util.ArrayList<>(); // 创建过滤列表
        for (String item : items) { // 遍历所有选项
            if (item.contains(input)) { // 判断是否包含输入文本
                filteredItems.add(item); // 添加到过滤列表
            }
        }

        if (!filteredItems.isEmpty()) { // 判断过滤列表非空
            updateComboBoxWithItems(comboBox, filteredItems); // 更新下拉框选项
        }
    }

    /**
     * 重置下拉框为所有选项
     */
    private void resetComboBoxItems(JComboBox<String> comboBox, String[] items) { // 重置下拉框方法
        comboBox.removeAllItems(); // 移除所有选项
        for (String item : items) { // 遍历所有选项
            comboBox.addItem(item); // 添加选项
        }
    }

    /**
     * 更新下拉框选项
     */
    private void updateComboBoxWithItems(JComboBox<String> comboBox, java.util.List<String> items) { // 更新下拉框方法
        comboBox.removeAllItems(); // 移除所有选项
        for (String item : items) { // 遍历列表
            comboBox.addItem(item); // 添加选项
        }
    }

    /**
     * 设置城市下拉框的自动补全（点击显示下拉框，实时过滤）
     */
    private void setupCityAutoComplete() { // 设置城市自动补全方法
        JTextField editor = (JTextField) cityComboBox.getEditor().getEditorComponent(); // 获取编辑器组件

        // 点击输入框时显示下拉列表
        editor.addFocusListener(new java.awt.event.FocusAdapter() { // 添加焦点监听器
            @Override
            public void focusGained(java.awt.event.FocusEvent e) { // 获得焦点时
                SwingUtilities.invokeLater(() -> { // 在EDT中执行
                    if (!cityComboBox.isPopupVisible()) { // 如果下拉框未显示
                        cityComboBox.showPopup(); // 显示下拉框
                    }
                });
            }
        });

        // 实时根据输入过滤（只过滤，不自动选中）
        editor.addKeyListener(new java.awt.event.KeyAdapter() { // 添加键盘监听器
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) { // 按键释放时
                SwingUtilities.invokeLater(() -> { // 在EDT中执行
                    filterCityItems(); // 过滤城市选项
                });
            }
        });

        // 监听下拉框选择事件，只有点击选项时才填入输入框
        cityComboBox.addActionListener(e -> { // 添加下拉框选择事件
            if (e.getActionCommand().equals("comboBoxChanged")) { // 判断事件类型
                String selected = (String) cityComboBox.getSelectedItem(); // 获取选中项
                if (selected != null) { // 判断非空
                    editor.setText(selected); // 设置编辑器文本
                }
            }
        });
    }

    /**
     * 过滤城市下拉框选项
     */
    private void filterCityItems() { // 过滤城市选项方法
        JTextField editor = (JTextField) cityComboBox.getEditor().getEditorComponent(); // 获取编辑器组件
        String input = editor.getText(); // 获取输入文本

        if (input == null || input.isEmpty()) { // 判断输入为空
            // 输入为空，恢复所有城市
            resetComboBoxItems(cityComboBox, cityItems); // 重置城市下拉框
            return; // 返回
        }

        // 过滤匹配的城市
        java.util.List<String> filteredItems = new java.util.ArrayList<>(); // 创建过滤列表
        for (String city : cityItems) { // 遍历城市列表
            if (city.contains(input)) { // 判断是否包含输入文本
                filteredItems.add(city); // 添加到过滤列表
            }
        }

        if (!filteredItems.isEmpty()) { // 判断过滤列表非空
            updateComboBoxWithItems(cityComboBox, filteredItems); // 更新城市下拉框
        }
    }

    /**
     * 更新城市下拉框
     */
    private void updateCityComboBox(String[] cities) { // 更新城市下拉框方法
        this.cityItems = cities; // 保存城市列表
        cityComboBox.removeAllItems(); // 移除所有选项
        for (String city : cities) { // 遍历城市列表
            cityComboBox.addItem(city); // 添加城市选项
        }
    }

    /**
     * 根据省份名称查找索引
     */
    private int findProvinceIndex(String provinceName) { // 查找省份索引方法
        for (int i = 0; i < ChinaProvinces.PROVINCES.length; i++) { // 遍历省份数组
            if (ChinaProvinces.PROVINCES[i].equals(provinceName)) { // 精确匹配
                return i; // 返回索引
            }
        }
        // 如果完全匹配不到，尝试模糊匹配
        for (int i = 0; i < ChinaProvinces.PROVINCES.length; i++) { // 再次遍历
            if (ChinaProvinces.PROVINCES[i].contains(provinceName)
                    || provinceName.contains(ChinaProvinces.PROVINCES[i])) { // 模糊匹配
                return i; // 返回索引
            }
        }
        return 0; // 默认返回第一个
    }

    /**
     * 创建底部按钮面板
     */
    private JPanel createBottomButtonPanel() { // 创建底部按钮面板方法
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // 创建面板
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // 设置边距

        JButton confirmButton = new JButton("确认选择"); // 创建确认按钮
        ButtonStyle.setPrimaryStyle(confirmButton); // 设置主样式
        confirmButton.setFont(new Font("微软雅黑", Font.BOLD, 14)); // 设置字体
        confirmButton.setPreferredSize(new Dimension(120, 35)); // 设置首选大小
        confirmButton.addActionListener(e -> { // 添加点击事件
            if (selectedAddress == null) { // 判断未选择地址
                JOptionPane.showMessageDialog(this, "请先选择或添加一个收货信息"); // 显示提示
                return; // 返回
            }
            confirmed = true; // 设置确认标志
            dispose(); // 关闭对话框
        });

        JButton closeButton = new JButton("关闭"); // 创建关闭按钮
        ButtonStyle.setDefaultStyle(closeButton); // 设置默认样式
        closeButton.setPreferredSize(new Dimension(100, 35)); // 设置首选大小
        closeButton.addActionListener(e -> { // 添加点击事件
            selectedAddress = null; // 清空选中地址
            confirmed = false; // 设置未确认
            dispose(); // 关闭对话框
        });

        panel.add(confirmButton); // 添加确认按钮
        panel.add(closeButton); // 添加关闭按钮

        return panel; // 返回面板
    }

    /**
     * 加载地址列表
     */
    private void loadAddressList() { // 加载地址列表方法
        addressListPanel.removeAll(); // 清空地址列表

        List<Address> addresses = addressService.getAddressesByUserId(currentUser.getId()); // 获取用户地址列表

        if (addresses.isEmpty()) { // 判断地址列表为空
            JLabel emptyLabel = new JLabel("暂无收货信息，请添加"); // 创建空提示标签
            emptyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体
            emptyLabel.setForeground(Color.GRAY); // 设置灰色字体
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置水平居中
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // 设置边距
            addressListPanel.add(emptyLabel); // 添加空提示标签
        } else { // 地址列表不为空
            for (Address address : addresses) { // 遍历地址列表
                addressListPanel.add(createAddressCard(address)); // 创建地址卡片
                addressListPanel.add(Box.createVerticalStrut(5)); // 添加垂直间距
            }
        }

        addressListPanel.revalidate(); // 重新验证布局
        addressListPanel.repaint(); // 重新绘制
    }

    /**
     * 创建地址卡片
     */
    private JPanel createAddressCard(Address address) { // 创建地址卡片方法
        JPanel card = new JPanel(new BorderLayout()); // 创建卡片面板
        card.setBackground(Color.WHITE); // 设置白色背景
        card.setBorder(BorderFactory.createCompoundBorder( // 设置复合边框
                BorderFactory.createLineBorder(new Color(220, 220, 220)), // 外边框
                BorderFactory.createEmptyBorder(10, 10, 10, 10))); // 内边距
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); // 设置最大高度
        card.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 设置手型光标

        // 地址信息
        StringBuilder sb = new StringBuilder(); // 创建字符串构建器
        sb.append("<html><b>").append(address.getReceiverName()).append(" ").append(address.getPhone())
                .append("</b><br>"); // 添加收货人姓名和电话
        sb.append(address.getProvince()).append(address.getCity()).append(address.getDistrict()).append("<br>"); // 添加省市区县
        sb.append(address.getDetailAddress()); // 添加详细地址
        if (address.isDefault()) { // 判断是否默认地址
            sb.append(" <font color='red'>[默认]</font>"); // 添加默认标记
        }
        sb.append("</html>"); // 结束HTML标签

        JLabel infoLabel = new JLabel(sb.toString()); // 创建信息标签
        infoLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12)); // 设置字体
        card.add(infoLabel, BorderLayout.CENTER); // 添加到卡片中央

        // 点击事件
        card.addMouseListener(new java.awt.event.MouseAdapter() { // 添加鼠标监听器
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) { // 鼠标点击时
                selectAddress(address); // 选择地址
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) { // 鼠标进入时
                card.setBackground(new Color(240, 248, 255)); // 改变背景色
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) { // 鼠标离开时
                card.setBackground(Color.WHITE); // 恢复白色背景
            }
        });

        return card; // 返回卡片面板
    }

    /**
     * 加载默认地址到表单
     */
    private void loadDefaultAddress() { // 加载默认地址方法
        // 查询用户的默认地址
        Address defaultAddress = addressService.getDefaultAddress(currentUser.getId()); // 获取默认地址

        if (defaultAddress != null) { // 存在默认地址
            selectAddress(defaultAddress); // 加载到表单
        } else { // 不存在默认地址
            clearForm(); // 清空表单
        }
    }

    /**
     * 选择地址
     */
    private void selectAddress(Address address) { // 选择地址方法
        this.selectedAddress = address; // 设置选中地址
        this.editingAddressId = address.getId(); // 设置编辑地址ID

        // 填充表单
        nameField.setText(address.getReceiverName()); // 设置收货人
        phoneField.setText(address.getPhone()); // 设置联系电话
        districtField.setText(address.getDistrict()); // 设置区县
        detailArea.setText(address.getDetailAddress()); // 设置详细地址
        defaultCheckBox.setSelected(address.isDefault()); // 设置默认标记

        // 设置省份和城市
        provinceComboBox.setSelectedItem(address.getProvince()); // 设置省份

        // 延迟更新城市，确保省份选择事件触发
        SwingUtilities.invokeLater(() -> { // 在EDT中延迟执行
            cityComboBox.setSelectedItem(address.getCity()); // 设置城市
        });

        // 显示更新按钮，隐藏保存按钮
        saveButton.setVisible(false); // 隐藏保存按钮
        updateButton.setVisible(true); // 显示更新按钮
    }

    /**
     * 清空表单（新增模式）
     */
    private void clearForm() { // 清空表单方法
        this.editingAddressId = 0; // 重置编辑ID为0（新增模式）
        this.selectedAddress = null; // 清空选中地址

        nameField.setText(""); // 清空收货人
        phoneField.setText(""); // 清空联系电话
        districtField.setText(""); // 清空区县
        detailArea.setText(""); // 清空详细地址
        defaultCheckBox.setSelected(false); // 取消默认勾选

        provinceComboBox.setSelectedIndex(0); // 重置省份选择

        // 显示保存按钮，隐藏更新按钮
        saveButton.setVisible(true); // 显示保存按钮
        updateButton.setVisible(false); // 隐藏更新按钮
    }

    /**
     * 保存地址
     */
    private void saveAddress() { // 保存地址方法
        // 验证输入
        if (nameField.getText().trim().isEmpty()) { // 判断收货人为空
            JOptionPane.showMessageDialog(this, "请输入收货人"); // 显示提示
            return; // 返回
        }
        if (phoneField.getText().trim().isEmpty()) { // 判断联系电话为空
            JOptionPane.showMessageDialog(this, "请输入联系电话"); // 显示提示
            return; // 返回
        }
        if (detailArea.getText().trim().isEmpty()) { // 判断详细地址为空
            JOptionPane.showMessageDialog(this, "请输入详细地址"); // 显示提示
            return; // 返回
        }

        // 获取省份（处理用户输入的文本）
        String province = (String) provinceComboBox.getSelectedItem(); // 获取选中省份
        if (province == null || province.isEmpty()) { // 判断为空
            province = (String) provinceComboBox.getEditor().getItem(); // 从编辑器获取
        }
        // 确保省份有效
        if (findProvinceIndex(province) == 0 && !province.equals(ChinaProvinces.PROVINCES[0])) { // 验证省份有效性
            JOptionPane.showMessageDialog(this, "请选择有效的省份"); // 显示提示
            return; // 返回
        }

        // 获取城市（处理用户输入的文本）
        String city = (String) cityComboBox.getSelectedItem(); // 获取选中城市
        if (city == null || city.isEmpty()) { // 判断为空
            city = (String) cityComboBox.getEditor().getItem(); // 从编辑器获取
        }

        Address address = new Address(); // 创建地址对象
        address.setUserId(currentUser.getId()); // 设置用户ID
        address.setReceiverName(nameField.getText().trim()); // 设置收货人
        address.setPhone(phoneField.getText().trim()); // 设置联系电话
        address.setProvince(province); // 设置省份
        address.setCity(city); // 设置城市
        address.setDistrict(districtField.getText().trim()); // 设置区县
        address.setDetailAddress(detailArea.getText().trim()); // 设置详细地址
        address.setDefault(defaultCheckBox.isSelected()); // 设置默认标记

        if (editingAddressId > 0) { // 判断为更新模式
            // 更新地址
            address.setId(editingAddressId); // 设置地址ID
            addressService.updateAddress(address); // 调用更新方法
            JOptionPane.showMessageDialog(this, "收货信息更新成功"); // 显示成功提示
        } else { // 判断为新增模式
            // 新增地址
            addressService.addAddress(address); // 调用新增方法
            JOptionPane.showMessageDialog(this, "收货信息添加成功"); // 显示成功提示
        }

        // 重新加载地址列表
        loadAddressList(); // 刷新地址列表

        // 清空表单
        clearForm(); // 重置表单
    }

    /**
     * 更新地址
     */
    private void updateAddress() { // 更新地址方法
        // 验证输入
        if (nameField.getText().trim().isEmpty()) { // 判断收货人为空
            JOptionPane.showMessageDialog(this, "请输入收货人"); // 显示提示
            return; // 返回
        }
        if (phoneField.getText().trim().isEmpty()) { // 判断联系电话为空
            JOptionPane.showMessageDialog(this, "请输入联系电话"); // 显示提示
            return; // 返回
        }
        if (detailArea.getText().trim().isEmpty()) { // 判断详细地址为空
            JOptionPane.showMessageDialog(this, "请输入详细地址"); // 显示提示
            return; // 返回
        }

        // 获取省份（处理用户输入的文本）
        String province = (String) provinceComboBox.getSelectedItem(); // 获取选中省份
        if (province == null || province.isEmpty()) { // 判断为空
            province = (String) provinceComboBox.getEditor().getItem(); // 从编辑器获取
        }
        // 确保省份有效
        if (findProvinceIndex(province) == 0 && !province.equals(ChinaProvinces.PROVINCES[0])) { // 验证省份有效性
            JOptionPane.showMessageDialog(this, "请选择有效的省份"); // 显示提示
            return; // 返回
        }

        // 获取城市（处理用户输入的文本）
        String city = (String) cityComboBox.getSelectedItem(); // 获取选中城市
        if (city == null || city.isEmpty()) { // 判断为空
            city = (String) cityComboBox.getEditor().getItem(); // 从编辑器获取
        }

        Address address = new Address(); // 创建地址对象
        address.setId(editingAddressId); // 设置地址ID
        address.setUserId(currentUser.getId()); // 设置用户ID
        address.setReceiverName(nameField.getText().trim()); // 设置收货人
        address.setPhone(phoneField.getText().trim()); // 设置联系电话
        address.setProvince(province); // 设置省份
        address.setCity(city); // 设置城市
        address.setDistrict(districtField.getText().trim()); // 设置区县
        address.setDetailAddress(detailArea.getText().trim()); // 设置详细地址
        address.setDefault(defaultCheckBox.isSelected()); // 设置默认标记

        // 更新地址
        addressService.updateAddress(address); // 调用更新方法
        JOptionPane.showMessageDialog(this, "收货信息更新成功"); // 显示成功提示

        // 重新加载地址列表
        loadAddressList(); // 刷新地址列表

        // 清空表单
        clearForm(); // 重置表单
    }

    /**
     * 获取选中的地址
     */
    public Address getSelectedAddress() { // 获取选中地址方法
        return selectedAddress; // 返回选中地址
    }

    /**
     * 是否确认选择
     */
    public boolean isConfirmed() { // 判断是否确认方法
        return confirmed; // 返回确认标志
    }

    /**
     * 处理默认地址改变事件
     */
    private void handleDefaultChange() { // 默认地址改变处理方法
        if (editingAddressId > 0 && defaultCheckBox.isSelected()) { // 正在编辑且勾选了默认
            // 将当前地址设为默认，同时取消其他地址的默认标记
            addressService.setDefaultAddress(currentUser.getId(), editingAddressId); // 设置默认地址

            // 刷新地址列表，更新显示
            loadAddressList(); // 重新加载地址列表

            // 如果当前选中的地址被修改了默认状态，需要重新加载到表单
            if (selectedAddress != null && selectedAddress.getId() == editingAddressId) {
                selectedAddress.setDefault(true); // 更新选中地址的默认状态
            }

            JOptionPane.showMessageDialog(this, "已设为默认收货信息"); // 提示用户
        }
    }
}