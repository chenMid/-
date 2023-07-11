package wakoo.fun.Vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import wakoo.fun.utils.MsgUtils;
import wakoo.fun.service.FaAdminCode;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class MsgVo {
    private  int code;
    private String msg;
    private Object data;

    //手动设置返回vo
    public MsgVo(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    //默认返回成功的状态码,参数为数据对象
    public MsgVo(Object data) {
        this.code = MsgUtils.SUCCESS.getCode();
        this.msg =  MsgUtils.SUCCESS.getMsg();
        this.data = data;
    }
    //返回指定状态码,数据对象
    public MsgVo(FaAdminCode faAdminCode, Object data) {
        this.code = faAdminCode.getCode();
        this.msg = faAdminCode.getMsg();
        this.data = data;
    }
    //只返回状态码
    public MsgVo(FaAdminCode faAdminCode) {
        this.code = faAdminCode.getCode();
        this.msg = faAdminCode.getMsg();
        this.data = null;
    }
}
