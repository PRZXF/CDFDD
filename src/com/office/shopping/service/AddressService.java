package com.office.shopping.service; // 声明包名

import com.office.shopping.dao.AddressDAO; // 导入地址数据访问对象
import com.office.shopping.model.Address; // 导入地址模型类

import java.util.List; // 导入列表接口

/**
 * 收货地址服务类
 * <p>
 * 负责收货地址相关的业务逻辑，包括添加、更新、删除地址和获取用户地址列表
 * 提供默认地址管理功能
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class AddressService { // 定义地址服务类
    /**
     * 地址数据访问对象
     */
    private AddressDAO addressDAO; // 地址DAO对象

    /**
     * 构造方法
     */
    public AddressService() { // 默认构造方法
        this.addressDAO = new AddressDAO(); // 初始化地址DAO对象
    }

    /**
     * 添加收货地址
     *
     * @param address 地址对象，包含地址信息
     */
    public void addAddress(Address address) { // 添加地址方法
        addressDAO.addAddress(address); // 调用DAO添加地址
    }

    /**
     * 更新收货地址
     *
     * @param address 地址对象，包含更新后的地址信息
     */
    public void updateAddress(Address address) { // 更新地址方法
        addressDAO.updateAddress(address); // 调用DAO更新地址
    }

    /**
     * 删除收货地址
     *
     * @param id 地址ID
     */
    public void deleteAddress(int id) { // 删除地址方法
        addressDAO.deleteAddress(id); // 调用DAO删除地址
    }

    /**
     * 更新地址的收件人和电话
     *
     * @param addressId    地址ID
     * @param receiverName 收件人姓名
     * @param phone        联系电话
     */
    public void updateAddressContact(int addressId, String receiverName, String phone) { // 更新联系方式方法
        addressDAO.updateAddressContact(addressId, receiverName, phone); // 调用DAO更新联系方式
    }

    /**
     * 获取用户所有地址
     *
     * @param userId 用户ID
     * @return 地址列表
     */
    public List<Address> getAddressesByUserId(int userId) { // 获取用户地址列表方法
        return addressDAO.getAddressesByUserId(userId); // 调用DAO获取地址列表
    }

    /**
     * 获取默认地址
     *
     * @param userId 用户ID
     * @return 默认地址对象，不存在返回null
     */
    public Address getDefaultAddress(int userId) { // 获取默认地址方法
        return addressDAO.getDefaultAddress(userId); // 调用DAO获取默认地址
    }

    /**
     * 检查用户是否存在地址
     *
     * @param userId 用户ID
     * @return true表示存在地址，false表示不存在
     */
    public boolean hasAddress(int userId) { // 检查是否存在地址方法
        return addressDAO.hasAddress(userId); // 调用DAO检查地址存在性
    }

    /**
     * 设置默认地址
     *
     * @param userId    用户ID
     * @param addressId 要设为默认的地址ID
     */
    public void setDefaultAddress(int userId, int addressId) { // 设置默认地址方法
        addressDAO.setDefaultAddress(userId, addressId); // 调用DAO设置默认地址
    }
}