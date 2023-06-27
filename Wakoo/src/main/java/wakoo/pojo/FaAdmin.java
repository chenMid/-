package wakoo.pojo;

public class FaAdmin {
    private Integer id;
    private String userName,nickName,password,salt,avatar,email,campusId,mobile,loginIp,token,status;
    private int loginFailure;
    private Long loginTime,createTime,updateTime,deleteTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLoginFailure() {
        return loginFailure;
    }

    public void setLoginFailure(int loginFailure) {
        this.loginFailure = loginFailure;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Long deleteTime) {
        this.deleteTime = deleteTime;
    }

    public FaAdmin(String userName, String nickName, String password, String salt, String avatar, String email, String campusId, String mobile, String loginIp, String token, String status, int loginFailure, Long loginTime, Long createTime, Long updateTime, Long deleteTime) {
        this.userName = userName;
        this.nickName = nickName;
        this.password = password;
        this.salt = salt;
        this.avatar = avatar;
        this.email = email;
        this.campusId = campusId;
        this.mobile = mobile;
        this.loginIp = loginIp;
        this.token = token;
        this.status = status;
        this.loginFailure = loginFailure;
        this.loginTime = loginTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.deleteTime = deleteTime;
    }

    public FaAdmin(Integer id, String userName, String nickName, String password, String salt, String avatar, String email, String campusId, String mobile, String loginIp, String token, String status, int loginFailure, Long loginTime, Long createTime, Long updateTime, Long deleteTime) {
        this.id = id;
        this.userName = userName;
        this.nickName = nickName;
        this.password = password;
        this.salt = salt;
        this.avatar = avatar;
        this.email = email;
        this.campusId = campusId;
        this.mobile = mobile;
        this.loginIp = loginIp;
        this.token = token;
        this.status = status;
        this.loginFailure = loginFailure;
        this.loginTime = loginTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.deleteTime = deleteTime;
    }

    public FaAdmin() {
    }
}
