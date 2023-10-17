package wakoo.fun.service;

public interface PersonalCenterService {
    /**
     * 更新头像
     * @param id
     * @param avatarPath
     * @return
     */
    Boolean avatar(Integer id,String avatarPath);
}
