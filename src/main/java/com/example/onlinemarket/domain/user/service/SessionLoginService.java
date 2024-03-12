package com.example.onlinemarket.domain.user.service;

import com.example.onlinemarket.domain.user.constants.SessionKey;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

    private final HttpSession session;

    @Override
    public void login(String email) {
        session.setAttribute(SessionKey.LOGGED_IN_USER, email);
    }

    @Override
    public void logout() {
        session.removeAttribute(SessionKey.LOGGED_IN_USER);
    }

    @Override
    public Optional<String> getLoginUserEmail() {
        return Optional.ofNullable((String) session.getAttribute(SessionKey.LOGGED_IN_USER));
    }
}
