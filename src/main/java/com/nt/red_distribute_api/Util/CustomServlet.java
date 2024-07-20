package com.nt.red_distribute_api.Util;

import jakarta.servlet.http.HttpServletRequest;

public class CustomServlet {
    public static String getClientIpAddress(HttpServletRequest request) {
    String xForwardedForHeader = request.getHeader("X-Forwarded-For");
    if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
        // The X-Forwarded-For header can contain a comma-separated list of IP addresses
        // The first one in the list is the original client IP address
        return xForwardedForHeader.split(",")[0].trim();
    }
    return request.getRemoteAddr();
}
}
