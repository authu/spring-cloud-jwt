package io.authu.springcloudjwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by MrTT (jiang.taojie@foxmail.com)
 * 2018/9/26.
 */
@Slf4j
public class JwtAuthServer {

    @Resource
    private JwtProperties properties;

    public Jws<Claims> parse(String token) {
        String prefix = properties.getPrefix();
        if (StringUtils.isEmpty(token)) {
            log.warn("Token is empty!");
            throw new UnsupportedJwtException("Token is empty!");
        }
        if (!token.startsWith(prefix)) {
            log.warn("Don't have prefix {}!", prefix);
            throw new UnsupportedJwtException("Unsupported jwt token!");
        }
        token = token.substring(properties.getPrefix().length());
        return Jwts.parser().setSigningKey(properties.getSecret()).parseClaimsJws(token);
    }

    public String getToken(ServerHttpRequest request) {
        List<String> list = request.getHeaders().get(properties.getHeader());
        if (list == null || list.size() != 1) {
            log.warn("Token is empty!");
            throw new UnsupportedJwtException("Token is empty!");
        }
        return list.get(0);
    }

    public String getToken(HttpServletRequest request) {
        String header = request.getHeader(properties.getHeader());
        if (StringUtils.isEmpty(header)) {
            log.warn("Token is empty!");
            throw new UnsupportedJwtException("Token is empty!");
        }
        return header;
    }

}
