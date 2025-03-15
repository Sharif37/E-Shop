package com.sharif.eshop.utils;

import com.sharif.eshop.security.jwt.AuthTokenFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {

    @Value("${app.useSecureCookie}")
    private boolean useSecureCookie ;

    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken, long maxAge) {
        if(response == null){
            throw new IllegalArgumentException("response cannot be null");
        }
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge((int) maxAge/1000);
        refreshTokenCookie.setSecure(useSecureCookie);
        String sameSite = useSecureCookie ? "None" : "Lax";
        setResponseHeader(response,refreshTokenCookie,sameSite);

    }

    private void setResponseHeader(HttpServletResponse response, Cookie refreshTokenCookie, String sameSite) {
        StringBuilder cookieHeader = new StringBuilder();
        cookieHeader.append(refreshTokenCookie.getName()).append("=")
                .append(refreshTokenCookie.getValue())
                .append("; HttpOnly; Path=")
                .append(refreshTokenCookie.getPath())
                .append("; Max-Age=").append(refreshTokenCookie.getMaxAge())
                .append(useSecureCookie ? "; Secure" : "")
                .append("; SameSite=").append(sameSite);

        response.setHeader("Set-Cookie", cookieHeader.toString());
    }


    public String getRefreshTokenFromCookie(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if(cookies != null){

            for(Cookie cookie : cookies){
                System.out.println("Name of Cookie found: "+cookie.getName());
                if(cookie.getName().equals("refreshToken")){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void logCookieNames(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                System.out.println("Cookie Name: "+cookie.getName() +", " + "Cookie Value: "+cookie.getValue());
            }
        }
    }


}
