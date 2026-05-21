package com.office.shopping.util;

import com.office.shopping.model.Address;
import com.office.shopping.service.AddressService;

/**
 * 示例地址添加工具类
 * <p>
 * 负责添加示例地址数据到数据库
 * 用于测试地址管理功能
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class AddSampleAddresses {
    public static void main(String[] args) {
        AddressService service = new AddressService();

        // 为买家1添加地址
        Address addr1 = new Address(0, 5, "张三", "13800138001", "广东省", "广州市", "天河区", "珠江新城CBD中心大厦1801室", true);
        service.addAddress(addr1);

        Address addr2 = new Address(0, 5, "李四", "13900139002", "广东省", "深圳市", "南山区", "科技园南区深南大道9999号", false);
        service.addAddress(addr2);

        System.out.println("示例地址添加成功!");
    }
}