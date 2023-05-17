//package com.qtiger.news.security;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
//import org.springframework.security.web.util.UrlUtils;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.UriComponents;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.net.URI;
//import java.nio.charset.StandardCharsets;
//
//@Component
//public class CognitoLogoutHandler extends SimpleUrlLogoutSuccessHandler {
//
//    @Value("${spring.security.oauth2.client.registration.cognito.client-id}")
//    private String clientId;
//
//    @Value("${cognito.logoutUrl}")
//    private String logoutUrl;
//
//    @Override
//    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        UriComponents baseUrl = UriComponentsBuilder
//                .fromHttpUrl(UrlUtils.buildFullRequestUrl(request))
//                .replacePath(request.getContextPath())
//                .replaceQuery(null)
//                .fragment(null)
//                .build();
//
//        return UriComponentsBuilder
//                .fromUri(URI.create(logoutUrl))
//                .queryParam("client_id", clientId)
//                .queryParam("logout_uri", baseUrl)
//                .encode(StandardCharsets.UTF_8)
//                .build()
//                .toUriString();
//    }
//}
