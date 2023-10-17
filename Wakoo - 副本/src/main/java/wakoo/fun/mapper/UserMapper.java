package wakoo.fun.mapper;

import wakoo.fun.dto.User;

public interface UserMapper {
    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    User getUsr(String username);
}
