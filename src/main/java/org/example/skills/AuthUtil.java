package org.example.skills;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public class AuthUtil {

    private static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();
    }

    public static Long getUserId() {
        Object v = getRequest().getAttribute("userId");
        if (v == null) throw new RuntimeException("UserId not found in request");
        return (Long) v;
    }

    public static Long getCompanyId() {
        Object v = getRequest().getAttribute("companyId");
        if (v == null) throw new RuntimeException("CompanyId not found in request");
        return (Long) v;
    }
}
