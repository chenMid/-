package wakoo.config;


import lombok.Getter;
import wakoo.service.FaAdminCode;

@Getter
public enum MsgUtils implements FaAdminCode {


    SUCCESS(1000,"请求成功"),
    FAILED(1001,"请求失败"),
    VALIDATE_ERROR(1002,"参数校验失败"),
    RESPONSE_PACK_ERROR(1003,"response返回包装失败"),
    CONTACT_ADMINISTRATOR(500,"联系管理员"),
    TOKEN_FAILURE(500,"token失效");

    private int code;
    private String msg;

    //构造函数
    MsgUtils(int code, String msg){
        this.code=code;
        this.msg=msg;
    }

}
