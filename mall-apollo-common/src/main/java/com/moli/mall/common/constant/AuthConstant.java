package com.moli.mall.common.constant;

/**
 * @author moli
 * @time 2024-03-29 16:52:15
 * @description 认证常量
 */
public interface AuthConstant {

    /**
     * token 访问有效时间
     */
    int ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24; // 24 小时

    /**
     * token 刷新有效时间
     */
    int REFRESH_TOKEN_VALIDITY_SECONDS = AuthConstant.ACCESS_TOKEN_VALIDITY_SECONDS * 7; // 一周

    /**
     * JWT存储权限前缀
     */
    String AUTHORITY_PREFIX = "ROLE_";

    /**
     * JWT存储权限属性
     */
    String AUTHORITY_CLAIM_NAME = "authorities";

    /**
     * 认证信息Http请求头
     */
    String JWT_TOKEN_HEADER = "Authorization";

    /**
     * client_id 参数名
     */
    String CLIENT_ID_PARAM = "client_id";

    /**
     * client_secret 参数名
     */
    String CLIENT_SECRET_PARAM = "client_secret";

    /**
     * grant_type 参数名
     */
    String GRANT_TYPE_PARAM = "grant_type";

    /**
     * grant_type 密码类型
     */
    String PASSWORD_GRANT_TYPE = "password";

    /**
     * grant_type 刷新token类型
     */
    String REFRESH_TOKEN_GRANT_TYPE = "refresh_token";

    /**
     * 用户名参数
     */
    String USERNAME_PARAM = "username";

    /**
     * 密码参数
     */
    String PASSWORD_PARAM = "password";

    /**
     * 刷新token参数
     */
    String REFRESH_TOKEN = "refresh_token";

    /**
     * 后台管理client_id
     */
    String ADMIN_CLIENT_ID = "mall-apollo-admin-app";

    /**
     * 前台管理client_id
     */
    String PORTAL_CLIENT_ID = "mall-apollo-portal-app";

    /**
     * client_secret 密钥
     */
    String CLIENT_SECRETE_KEY = "mall-apollo-pass";

    /**
     * JWT令牌前缀
     */
    String JWT_TOKEN_PREFIX = "Bearer ";

    /**
     * 用户信息Http请求头
     */
    String USER_TOKEN_HEADER = "user";

    /**
     * Redis缓存权限规则key
     */
    String RESOURCE_ROLES_MAP_KEY = "auth:resourceRolesMap";

    /**
     * 后台管理接口路径匹配
     */
    String ADMIN_URL_PATTERN = "/mall-admin/**";
}
