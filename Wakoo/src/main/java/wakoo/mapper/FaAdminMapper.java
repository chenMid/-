package wakoo.mapper;

import jdk.nashorn.internal.parser.Token;
import wakoo.pojo.FaAdmin;

import java.util.ArrayList;
import java.util.List;


public interface FaAdminMapper {
    List<FaAdmin> faAdmin(String account);
    Boolean UpdToken(String Token,Integer id);
}
