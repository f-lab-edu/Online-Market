package com.example.onlinemarket.domain.user.service;

import com.example.onlinemarket.common.exception.UnauthorizedException;
import com.example.onlinemarket.domain.user.constants.SessionKey;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

    private final HttpSession session;

    @Override
    public void login(long id) {

        session.setAttribute(SessionKey.LOGGED_IN_USER, id);
    }

    @Override
    public void logout() {
        session.removeAttribute(SessionKey.LOGGED_IN_USER);
    }

    @Override
    public long getLoginUserId() {
        Object sessionId = session.getAttribute(SessionKey.LOGGED_IN_USER);
        if (sessionId == null) {
            throw new UnauthorizedException();
        }
        return (int) sessionId;
    }
}
