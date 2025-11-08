package com.ecom.config;

import com.ecom.util.ActiveUserStore;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SessionListener implements HttpSessionListener {

    private final ActiveUserStore activeUserStore;

    public SessionListener(ActiveUserStore activeUserStore) {
        this.activeUserStore = activeUserStore;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // nothing to do when created
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // when session ends, remove user
        SecurityContext context = (SecurityContext) se.getSession()
                .getAttribute("SPRING_SECURITY_CONTEXT");
        if (context != null) {
            Authentication auth = context.getAuthentication();
            if (auth != null) {
                String username = auth.getName();
                activeUserStore.removeUser(username);
            }
        }
    }
}
