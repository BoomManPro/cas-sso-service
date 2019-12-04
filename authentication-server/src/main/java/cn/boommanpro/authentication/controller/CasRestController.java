package cn.boommanpro.authentication.controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangqimeng
 * @date 2019/12/4 14:03
 */
@Slf4j
@RestController
@RequestMapping("api/cas-rest")
public class CasRestController {

    @PostMapping("/validateLogin")
    public Object validateLogin(@RequestHeader HttpHeaders httpHeaders) {
        log.info("==============开始验证服务================");
        CasUser casUser = new CasUser();
        try {
            UserTemp userCas = this.obtainUserFormHeader(httpHeaders);
            if (userCas == null) {
                return new ResponseEntity<CasUser>(HttpStatus.NOT_FOUND);
            } else {
//                UserInfo dbUser = userService.getUser(userCas.username);
//                if (dbUser != null) {
//                    if (!dbUser.getPassword().equals(userCas.password)) {
//                        log.info("您输入的密码不匹配,请检查！");
//                        return new ResponseEntity<CasUser>(HttpStatus.BAD_REQUEST); //密码不匹配
//                    } else if (dbUser.isLock() == true) {
//                        log.info("用户已被锁定,请联系管理员！");
//                        return new ResponseEntity<CasUser>(HttpStatus.LOCKED); //锁定
//                    }
//                } else {
//                    return new ResponseEntity<CasUser>(HttpStatus.NOT_FOUND); //不存在
//                }
//                casUser.setUsername(dbUser.getUsername());

                casUser.setUsername(userCas.username);

            }
        } catch (UnsupportedEncodingException e) {
            return new ResponseEntity<CasUser>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            log.info("用户不存在！");
            return new ResponseEntity<CasUser>(HttpStatus.NOT_FOUND);
        }
        log.info("[{}] login is ok", casUser.getUsername());
        return casUser;
    }

    /**
     * 将授权加密的信息返回
     * 根据请求头获取用户名及密码
     *
     * @param httpHeaders
     * @return
     * @throws UnsupportedEncodingException
     */
    private UserTemp obtainUserFormHeader(HttpHeaders httpHeaders) throws UnsupportedEncodingException {
        /**
         *  根据官方文档，当请求过来时，会通过把用户信息放在请求头authorization中，并且通过Basic认证方式加密
         * This allows the CAS server to reach to a remote REST endpoint via a POST for verification of credentials.
         * Credentials are passed via an Authorization header whose value is Basic XYZ where XYZ is a Base64 encoded version of the credentials.
         */
        //将得到 Basic Base64(用户名:密码)
        String authorization = httpHeaders.getFirst("authorization");
        if (StringUtils.isEmpty(authorization)) {
            return null;
        }

        String baseCredentials = authorization.split(" ")[1];
        //用户名:密码
        String usernamePassword = new String(Base64Utils.decodeFromString(baseCredentials), StandardCharsets.UTF_8);
        String credentials[] = usernamePassword.split(":");
        return new UserTemp(credentials[0], credentials[1]);
    }

    /**
     * 解析请求过来的用户
     */
    private class UserTemp {

        private String username;

        private String password;

        public UserTemp(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    /**
     * 用于传递给CAS服务器验证数据
     */
    public class CasUser {

        @JsonProperty("id")
        private String username;

        @JsonProperty("@class")
        private String clazz = "org.apereo.cas.authentication.principal.SimplePrincipal";

        @JsonProperty("attributes")
        private Map<String, Object> attributes = new HashMap<>();

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getClazz() {
            return clazz;
        }

        public void setClazz(String clazz) {
            this.clazz = clazz;
        }

        public Map<String, Object> getAttributes() {
            return attributes;
        }

        public void setAttributes(Map<String, Object> attributes) {
            this.attributes = attributes;
        }
    }
}
