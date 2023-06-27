package wakoo.service;

import org.springframework.stereotype.Service;
import wakoo.pojo.FaAdmin;

import java.util.List;

public interface FaAdminService {
    /*查询所有并且查询指定账号*/
    List<FaAdmin> faAdmin(String account);
    /*更新Token*/
    Boolean UpdToken(String Token,Integer id);
}
