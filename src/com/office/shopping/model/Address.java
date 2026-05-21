package com.office.shopping.model; // 声明包名

/**
 * 收货地址模型类
 * <p>
 * 用于表示用户的收货地址信息，包含收件人姓名、联系方式、省市地区和详细地址
 * 支持设置默认地址标识
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class Address { // 定义Address类
    /**
     * 地址ID，自增主键
     */
    private int id; // 地址ID字段

    /**
     * 用户ID，外键关联用户表
     */
    private int userId; // 用户ID字段

    /**
     * 收件人姓名
     */
    private String receiverName; // 收件人姓名字段

    /**
     * 联系电话
     */
    private String phone; // 联系电话字段

    /**
     * 省份
     */
    private String province; // 省份字段

    /**
     * 城市
     */
    private String city; // 城市字段

    /**
     * 区县
     */
    private String district; // 区县字段

    /**
     * 详细地址
     */
    private String detailAddress; // 详细地址字段

    /**
     * 是否为默认地址
     */
    private boolean isDefault; // 是否为默认地址字段

    /**
     * 默认构造方法
     */
    public Address() {
    } // 默认无参构造方法

    /**
     * 完整构造方法
     *
     * @param id            地址ID
     * @param userId        用户ID
     * @param receiverName  收件人姓名
     * @param phone         联系电话
     * @param province      省份
     * @param city          城市
     * @param district      区县
     * @param detailAddress 详细地址
     * @param isDefault     是否为默认地址
     */
    public Address(int id, int userId, String receiverName, String phone, String province, String city, String district,
            String detailAddress, boolean isDefault) { // 带参数构造方法
        this.id = id; // 设置地址ID
        this.userId = userId; // 设置用户ID
        this.receiverName = receiverName; // 设置收件人姓名
        this.phone = phone; // 设置联系电话
        this.province = province; // 设置省份
        this.city = city; // 设置城市
        this.district = district; // 设置区县
        this.detailAddress = detailAddress; // 设置详细地址
        this.isDefault = isDefault; // 设置是否为默认地址
    }

    /**
     * 获取地址ID
     *
     * @return 地址ID
     */
    public int getId() { // 获取地址ID方法
        return id; // 返回地址ID
    }

    /**
     * 设置地址ID
     *
     * @param id 地址ID
     */
    public void setId(int id) { // 设置地址ID方法
        this.id = id; // 设置地址ID
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    public int getUserId() { // 获取用户ID方法
        return userId; // 返回用户ID
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(int userId) { // 设置用户ID方法
        this.userId = userId; // 设置用户ID
    }

    /**
     * 获取收件人姓名
     *
     * @return 收件人姓名
     */
    public String getReceiverName() { // 获取收件人姓名方法
        return receiverName; // 返回收件人姓名
    }

    /**
     * 设置收件人姓名
     *
     * @param receiverName 收件人姓名
     */
    public void setReceiverName(String receiverName) { // 设置收件人姓名方法
        this.receiverName = receiverName; // 设置收件人姓名
    }

    /**
     * 获取联系电话
     *
     * @return 联系电话
     */
    public String getPhone() { // 获取联系电话方法
        return phone; // 返回联系电话
    }

    /**
     * 设置联系电话
     *
     * @param phone 联系电话
     */
    public void setPhone(String phone) { // 设置联系电话方法
        this.phone = phone; // 设置联系电话
    }

    /**
     * 获取省份
     *
     * @return 省份
     */
    public String getProvince() { // 获取省份方法
        return province; // 返回省份
    }

    /**
     * 设置省份
     *
     * @param province 省份
     */
    public void setProvince(String province) { // 设置省份方法
        this.province = province; // 设置省份
    }

    /**
     * 获取城市
     *
     * @return 城市
     */
    public String getCity() { // 获取城市方法
        return city; // 返回城市
    }

    /**
     * 设置城市
     *
     * @param city 城市
     */
    public void setCity(String city) { // 设置城市方法
        this.city = city; // 设置城市
    }

    /**
     * 获取区县
     *
     * @return 区县
     */
    public String getDistrict() { // 获取区县方法
        return district; // 返回区县
    }

    /**
     * 设置区县
     *
     * @param district 区县
     */
    public void setDistrict(String district) { // 设置区县方法
        this.district = district; // 设置区县
    }

    /**
     * 获取详细地址
     *
     * @return 详细地址
     */
    public String getDetailAddress() { // 获取详细地址方法
        return detailAddress; // 返回详细地址
    }

    /**
     * 设置详细地址
     *
     * @param detailAddress 详细地址
     */
    public void setDetailAddress(String detailAddress) { // 设置详细地址方法
        this.detailAddress = detailAddress; // 设置详细地址
    }

    /**
     * 获取是否为默认地址
     *
     * @return 是否为默认地址
     */
    public boolean isDefault() { // 获取是否为默认地址方法
        return isDefault; // 返回是否为默认地址
    }

    /**
     * 设置是否为默认地址
     *
     * @param isDefault 是否为默认地址
     */
    public void setDefault(boolean isDefault) { // 设置是否为默认地址方法
        this.isDefault = isDefault; // 设置是否为默认地址
    }

    /**
     * 获取完整地址（省市区详细地址）
     *
     * @return 完整地址字符串
     */
    public String getFullAddress() { // 获取完整地址方法
        return province + city + district + detailAddress; // 返回省市区详细地址拼接结果
    }

    /**
     * 返回完整的地址信息，包含收件人姓名和电话
     *
     * @return 带联系方式的完整地址
     */
    public String getFullAddressWithContact() { // 获取带联系方式的完整地址方法
        return receiverName + " " + phone + " " + province + city + district + detailAddress; // 返回带姓名和电话的完整地址
    }

    /**
     * 用于在地址选择对话框中显示地址信息
     *
     * @return 地址显示字符串
     */
    @Override
    public String toString() { // 重写toString方法
        return receiverName + " " + phone + " - " + province + city + district + detailAddress; // 返回地址显示字符串
    }
}