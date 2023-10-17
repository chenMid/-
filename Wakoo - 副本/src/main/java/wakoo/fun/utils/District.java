package wakoo.fun.utils;

import lombok.Data;

import java.io.Serializable;


/**
 * @author JCccc
 * 地域表实体 （省市区）
 */
@Data
public class District implements Serializable {

    /**
     * 省级编码
     */
    public static final String PROVINCE_CODE = "1";
    /**
     * 地址编码
     */
    private String code;
    /**
     * 地址名称
     */
    private String name;
    /**
     * 地址父级编码
     */
    private String parentCode;
    /**
     * 当前地区对应的全称
     */
    private String fullName;

}