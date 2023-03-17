//package com.cloudofgoods.notification.configuration.security;
//
//import com.cloudofgoods.notification.util.Utility;
//import org.springframework.web.servlet.HandlerInterceptor;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//public class BasicAuthenticationInterceptor implements HandlerInterceptor {
//
//    private static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";
//    private static final String EMPTY_SPACE_STR = " ";
//    private static final String ERROR_MSG = "The URL you are trying to reach Un-Authorized.";
//
//    public String clientSecret;
//
//    BasicAuthenticationInterceptor(String secret) {
//        this.clientSecret = secret;
//    }
//
//    @Override
//    public boolean preHandle(HttpServletRequest request,
//                             HttpServletResponse response,
//                             Object handler) throws Exception {
//        String authHeader = request.getHeader(Utility.AUTHORIZATION_HEADER);
//        String getClientSecret = authHeader.split(EMPTY_SPACE_STR)[1];
//        if (clientSecret.equals(getClientSecret)) {
//            return true;
//        } else {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setCharacterEncoding(Utility.ENCODING_FORMAT_UTF_8);
//            response.setContentType(CONTENT_TYPE_TEXT_PLAIN);
//            response.getOutputStream().println(ERROR_MSG);
//            return false;
//        }
//    }
//}
