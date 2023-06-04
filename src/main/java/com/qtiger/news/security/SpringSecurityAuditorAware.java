package com.qtiger.news.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Optional<Authentication> authentication = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication);
        if(authentication.isEmpty()) {
            return Optional.of("");
        }

        return authentication.filter(a -> a.isAuthenticated() && !a.getPrincipal().equals("anonymousUser"))
                .map(Authentication::getPrincipal)
                .map(OidcUser.class::cast)
                .map(OidcUser::getSubject);
    }
}
