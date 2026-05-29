package com.office.shopping.controller;

import com.office.shopping.model.Address;
import com.office.shopping.model.User;
import com.office.shopping.service.AddressService;
import com.office.shopping.util.ButtonStyle;
import com.office.shopping.util.ChinaProvinces;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 门店地址管理对话框
 * <p>
 * 用于卖家管理发货门店地址信息
 * 支持省市输入搜索和自动补全，提供直观的地址管理界面
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class StoreAddressManageDialog extends JDialog {

    private User currentUser;
    private AddressService addressService = new AddressService();
    private Address selectedAddress = null;
    private boolean confirmed = false;

    // 组件
    private JPanel addressListPanel;
    private JPanel formPanel;
    private JComboBox<String> provinceComboBox;
    private JComboBox<String> cityComboBox;
    private JTextField nameField;
    private JTextField districtField;
    private JTextArea detailArea;
    private JCheckBox defaultCheckBox;
    private JButton saveButton;
    private JButton updateButton;
    private JButton cancelButton;
    private JButton addNewButton;

    private int editingAddressId = 0;

    /**
     * 构造方法
     */
    public StoreAddressManageDialog(JFrame parent, User user) {
        super(parent, "发货门店信息", true);
        this.currentUser = user;

        setSize(600, 500);
        setResizable(false);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 左侧：地址列表
        JPanel leftPanel = createAddressListPanel();
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // 右侧：地址编辑表单
        JPanel rightPanel = createAddressFormPanel();
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // 底部按钮
        JPanel buttonPanel = createBottomButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        loadAddressList();
        loadDefaultAddress();
    }

    /**
     * 创建地址列表面板
     */
    private JPanel createAddressListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(250, 0));
        panel.setBorder(BorderFactory.createTitledBorder("已有发货门店"));

        addressListPanel = new JPanel();
        addressListPanel.setLayout(new BoxLayout(addressListPanel, BoxLayout.Y_AXIS));
        addressListPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(addressListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 新增发货门店按钮
        addNewButton = new JButton("+ 新增发货门店");
        addNewButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        addNewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addNewButton.setBackground(new Color(70, 130, 180));
        addNewButton.setForeground(Color.WHITE);
        addNewButton.setBorder(null);
        addNewButton.setFocusPainted(false);
        addNewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addNewButton.setPreferredSize(new Dimension(200, 30));
        addNewButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addNewButton.setBackground(new Color(50, 110, 160));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                addNewButton.setBackground(new Color(70, 130, 180));
            }
        });
        addNewButton.addActionListener(e -> clearForm());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(addNewButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * 创建地址表单面板
     */
    private JPanel createAddressFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("发货门店信息"));
        panel.setBackground(Color.WHITE);

        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.weightx = 1.0;

        // 发货门店名
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("发货门店名:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(20);
        nameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        formPanel.add(nameField, gbc);

        // 省份（可编辑的下拉框）
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("省份:"), gbc);

        gbc.gridx = 1;
        provinceComboBox = new JComboBox<>(ChinaProvinces.PROVINCES);
        provinceComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        provinceComboBox.setEditable(true);
        provinceComboBox.setPreferredSize(new Dimension(200, 25));
        setupAutoCompleteComboBox(provinceComboBox, ChinaProvinces.PROVINCES);
        formPanel.add(provinceComboBox, gbc);

        // 城市（可编辑的下拉框）
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("城市:"), gbc);

        gbc.gridx = 1;
        cityComboBox = new JComboBox<>();
        cityComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        cityComboBox.setEditable(true);
        cityComboBox.setPreferredSize(new Dimension(200, 25));
        formPanel.add(cityComboBox, gbc);

        // 初始化城市列表
        int selectedProvinceIndex = provinceComboBox.getSelectedIndex();
        String[] cities = ChinaProvinces.getCitiesByProvince(selectedProvinceIndex);
        updateCityComboBox(cities);

        // 为城市下拉框设置自动补全
        setupCityAutoComplete();

        // 省份选择事件监听
        provinceComboBox.addActionListener(e -> {
            String selectedProvince = (String) provinceComboBox.getSelectedItem();
            if (selectedProvince != null && !selectedProvince.isEmpty()) {
                int provinceIndex = findProvinceIndex(selectedProvince);
                String[] cityList = ChinaProvinces.getCitiesByProvince(provinceIndex);
                if (cityList != null && cityList.length > 0) {
                    updateCityComboBox(cityList);
                }
            }
        });

        // 区县
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("区县:"), gbc);

        gbc.gridx = 1;
        districtField = new JTextField(20);
        districtField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        formPanel.add(districtField, gbc);

        // 详细地址
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("详细地址:"), gbc);

        gbc.gridx = 1;
        detailArea = new JTextArea(3, 20);
        detailArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        detailArea.setLineWrap(true);
        formPanel.add(new JScrollPane(detailArea), gbc);

        // 默认门店
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        defaultCheckBox = new JCheckBox("设为默认发货门店");
        defaultCheckBox.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        defaultCheckBox.setBackground(Color.WHITE);
        defaultCheckBox.addActionListener(e -> handleDefaultChange());
        formPanel.add(defaultCheckBox, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        // 保存/更新按钮
        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        formButtonPanel.setBackground(Color.WHITE);
        formButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        saveButton = new JButton("保存门店信息");
        ButtonStyle.setPrimaryStyle(saveButton);
        saveButton.addActionListener(e -> saveAddress());

        updateButton = new JButton("更新门店信息");
        ButtonStyle.setSuccessStyle(updateButton);
        updateButton.setVisible(false);
        updateButton.addActionListener(e -> updateAddress());

        cancelButton = new JButton("取消");
        ButtonStyle.setDefaultStyle(cancelButton);
        cancelButton.addActionListener(e -> clearForm());

        formButtonPanel.add(saveButton);
        formButtonPanel.add(updateButton);
        formButtonPanel.add(cancelButton);
        panel.add(formButtonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // 省份列表缓存
    private String[] provinceItems;
    // 城市列表缓存
    private String[] cityItems = new String[0];

    /**
     * 设置可编辑下拉框的自动补全功能
     */
    private void setupAutoCompleteComboBox(JComboBox<String> comboBox, String[] items) {
        this.provinceItems = items;
        JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent();

        editor.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    if (!comboBox.isPopupVisible()) {
                        comboBox.showPopup();
                    }
                });
            }
        });

        editor.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                SwingUtilities.invokeLater(() -> {
                    filterComboBoxItems(comboBox, provinceItems);
                });
            }
        });

        comboBox.addActionListener(e -> {
            if (e.getActionCommand().equals("comboBoxChanged")) {
                String selected = (String) comboBox.getSelectedItem();
                if (selected != null) {
                    editor.setText(selected);
                }
            }
        });
    }

    /**
     * 过滤下拉框选项
     */
    private void filterComboBoxItems(JComboBox<String> comboBox, String[] items) {
        JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent();
        String input = editor.getText();

        if (input == null || input.isEmpty()) {
            resetComboBoxItems(comboBox, items);
            return;
        }

        java.util.List<String> filteredItems = new java.util.ArrayList<>();
        for (String item : items) {
            if (item.contains(input)) {
                filteredItems.add(item);
            }
        }

        if (!filteredItems.isEmpty()) {
            updateComboBoxWithItems(comboBox, filteredItems);
        }
    }

    /**
     * 重置下拉框为所有选项
     */
    private void resetComboBoxItems(JComboBox<String> comboBox, String[] items) {
        comboBox.removeAllItems();
        for (String item : items) {
            comboBox.addItem(item);
        }
    }

    /**
     * 更新下拉框选项
     */
    private void updateComboBoxWithItems(JComboBox<String> comboBox, java.util.List<String> items) {
        comboBox.removeAllItems();
        for (String item : items) {
            comboBox.addItem(item);
        }
    }

    /**
     * 设置城市下拉框的自动补全
     */
    private void setupCityAutoComplete() {
        JTextField editor = (JTextField) cityComboBox.getEditor().getEditorComponent();

        editor.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    if (!cityComboBox.isPopupVisible()) {
                        cityComboBox.showPopup();
                    }
                });
            }
        });

        editor.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                SwingUtilities.invokeLater(() -> {
                    filterCityItems();
                });
            }
        });

        cityComboBox.addActionListener(e -> {
            if (e.getActionCommand().equals("comboBoxChanged")) {
                String selected = (String) cityComboBox.getSelectedItem();
                if (selected != null) {
                    editor.setText(selected);
                }
            }
        });
    }

    /**
     * 过滤城市下拉框选项
     */
    private void filterCityItems() {
        JTextField editor = (JTextField) cityComboBox.getEditor().getEditorComponent();
        String input = editor.getText();

        if (input == null || input.isEmpty()) {
            resetComboBoxItems(cityComboBox, cityItems);
            return;
        }

        java.util.List<String> filteredItems = new java.util.ArrayList<>();
        for (String city : cityItems) {
            if (city.contains(input)) {
                filteredItems.add(city);
            }
        }

        if (!filteredItems.isEmpty()) {
            updateComboBoxWithItems(cityComboBox, filteredItems);
        }
    }

    /**
     * 更新城市下拉框
     */
    private void updateCityComboBox(String[] cities) {
        this.cityItems = cities;
        cityComboBox.removeAllItems();
        for (String city : cities) {
            cityComboBox.addItem(city);
        }
    }

    /**
     * 根据省份名称查找索引
     */
    private int findProvinceIndex(String provinceName) {
        for (int i = 0; i < ChinaProvinces.PROVINCES.length; i++) {
            if (ChinaProvinces.PROVINCES[i].equals(provinceName)) {
                return i;
            }
        }
        for (int i = 0; i < ChinaProvinces.PROVINCES.length; i++) {
            if (ChinaProvinces.PROVINCES[i].contains(provinceName)
                    || provinceName.contains(ChinaProvinces.PROVINCES[i])) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 创建底部按钮面板
     */
    private JPanel createBottomButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JButton confirmButton = new JButton("确认选择");
        ButtonStyle.setPrimaryStyle(confirmButton);
        confirmButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        confirmButton.setPreferredSize(new Dimension(120, 35));
        confirmButton.addActionListener(e -> {
            if (selectedAddress == null) {
                JOptionPane.showMessageDialog(this, "请先选择或添加一个发货门店");
                return;
            }
            confirmed = true;
            dispose();
        });

        JButton closeButton = new JButton("关闭");
        ButtonStyle.setDefaultStyle(closeButton);
        closeButton.setPreferredSize(new Dimension(100, 35));
        closeButton.addActionListener(e -> {
            selectedAddress = null;
            confirmed = false;
            dispose();
        });

        panel.add(confirmButton);
        panel.add(closeButton);

        return panel;
    }

    /**
     * 加载地址列表
     */
    private void loadAddressList() {
        addressListPanel.removeAll();

        List<Address> addresses = addressService.getAddressesByUserId(currentUser.getId());

        if (addresses.isEmpty()) {
            JLabel emptyLabel = new JLabel("暂无发货门店，请添加");
            emptyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            addressListPanel.add(emptyLabel);
        } else {
            for (Address address : addresses) {
                addressListPanel.add(createAddressCard(address));
                addressListPanel.add(Box.createVerticalStrut(5));
            }
        }

        addressListPanel.revalidate();
        addressListPanel.repaint();
    }

    /**
     * 创建地址卡片
     */
    private JPanel createAddressCard(Address address) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        StringBuilder sb = new StringBuilder();
        sb.append("<html><b>").append(address.getReceiverName()).append("</b><br>");
        sb.append(address.getProvince()).append(address.getCity()).append(address.getDistrict()).append("<br>");
        sb.append(address.getDetailAddress());
        if (address.isDefault()) {
            sb.append(" <font color='red'>[默认]</font>");
        }
        sb.append("</html>");

        JLabel infoLabel = new JLabel(sb.toString());
        infoLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        card.add(infoLabel, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectAddress(address);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(240, 248, 255));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    /**
     * 加载默认地址到表单
     */
    private void loadDefaultAddress() {
        Address defaultAddress = addressService.getDefaultAddress(currentUser.getId());

        if (defaultAddress != null) {
            selectAddress(defaultAddress);
        } else {
            clearForm();
        }
    }

    /**
     * 选择地址
     */
    private void selectAddress(Address address) {
        this.selectedAddress = address;
        this.editingAddressId = address.getId();

        nameField.setText(address.getReceiverName());
        districtField.setText(address.getDistrict());
        detailArea.setText(address.getDetailAddress());
        defaultCheckBox.setSelected(address.isDefault());

        provinceComboBox.setSelectedItem(address.getProvince());

        SwingUtilities.invokeLater(() -> {
            cityComboBox.setSelectedItem(address.getCity());
        });

        saveButton.setVisible(false);
        updateButton.setVisible(true);
    }

    /**
     * 清空表单（新增模式）
     */
    private void clearForm() {
        this.editingAddressId = 0;
        this.selectedAddress = null;

        nameField.setText("");
        districtField.setText("");
        detailArea.setText("");
        defaultCheckBox.setSelected(false);

        provinceComboBox.setSelectedIndex(0);

        saveButton.setVisible(true);
        updateButton.setVisible(false);
    }

    /**
     * 保存地址
     */
    private void saveAddress() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入发货门店名");
            return;
        }
        if (detailArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入详细地址");
            return;
        }

        String province = (String) provinceComboBox.getSelectedItem();
        if (province == null || province.isEmpty()) {
            province = (String) provinceComboBox.getEditor().getItem();
        }
        if (findProvinceIndex(province) == 0 && !province.equals(ChinaProvinces.PROVINCES[0])) {
            JOptionPane.showMessageDialog(this, "请选择有效的省份");
            return;
        }

        String city = (String) cityComboBox.getSelectedItem();
        if (city == null || city.isEmpty()) {
            city = (String) cityComboBox.getEditor().getItem();
        }

        Address address = new Address();
        address.setUserId(currentUser.getId());
        address.setReceiverName(nameField.getText().trim());
        address.setProvince(province);
        address.setCity(city);
        address.setDistrict(districtField.getText().trim());
        address.setDetailAddress(detailArea.getText().trim());
        address.setDefault(defaultCheckBox.isSelected());

        if (editingAddressId > 0) {
            address.setId(editingAddressId);
            addressService.updateAddress(address);
            JOptionPane.showMessageDialog(this, "门店信息更新成功");
        } else {
            addressService.addAddress(address);
            JOptionPane.showMessageDialog(this, "门店信息添加成功");
        }

        loadAddressList();
        clearForm();
    }

    /**
     * 更新地址
     */
    private void updateAddress() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入发货门店名");
            return;
        }
        if (detailArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入详细地址");
            return;
        }

        String province = (String) provinceComboBox.getSelectedItem();
        if (province == null || province.isEmpty()) {
            province = (String) provinceComboBox.getEditor().getItem();
        }
        if (findProvinceIndex(province) == 0 && !province.equals(ChinaProvinces.PROVINCES[0])) {
            JOptionPane.showMessageDialog(this, "请选择有效的省份");
            return;
        }

        String city = (String) cityComboBox.getSelectedItem();
        if (city == null || city.isEmpty()) {
            city = (String) cityComboBox.getEditor().getItem();
        }

        Address address = new Address();
        address.setId(editingAddressId);
        address.setUserId(currentUser.getId());
        address.setReceiverName(nameField.getText().trim());
        address.setProvince(province);
        address.setCity(city);
        address.setDistrict(districtField.getText().trim());
        address.setDetailAddress(detailArea.getText().trim());
        address.setDefault(defaultCheckBox.isSelected());

        addressService.updateAddress(address);
        JOptionPane.showMessageDialog(this, "门店信息更新成功");

        loadAddressList();
        clearForm();
    }

    /**
     * 获取选中的地址
     */
    public Address getSelectedAddress() {
        return selectedAddress;
    }

    /**
     * 是否确认选择
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * 处理默认地址改变事件
     */
    private void handleDefaultChange() {
        if (editingAddressId > 0 && defaultCheckBox.isSelected()) {
            addressService.setDefaultAddress(currentUser.getId(), editingAddressId);
            loadAddressList();

            if (selectedAddress != null && selectedAddress.getId() == editingAddressId) {
                selectedAddress.setDefault(true);
            }

            JOptionPane.showMessageDialog(this, "已设为默认发货门店");
        }
    }
}