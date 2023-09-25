package picasso.server.api.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import picasso.server.api.user.service.UserService;
import picasso.server.api.user.vo.request.LoginRequestDto;
import picasso.server.api.user.vo.request.SignUpRequestDto;
import picasso.server.domain.domains.user.entity.User;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 로그인 폼으로 이동
     *
     * @return
     */
//    @GetMapping("/login")
//    public String showLoginForm(HttpSession session) {
//        // TODO :  페이지 들어올때 자동로그인 설정 된 경우 Cookie 받아서 처리하는 로직 추가하기
//        return "auth/login";
//    }

    @GetMapping("/login")
    public String showLoginForm(HttpSession session, HttpServletRequest request) {
        //쿠키를 확인하여 자동 로그인 처리
        Cookie[] cookies = request.getCookies();

        String userId = null;
        String email = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    userId = cookie.getValue();
                } else if (cookie.getName().equals("email")) {
                    email = cookie.getValue();
                }
            }
        }
            if (userId != null && email != null) {
                return "redirect:/";
            } else {
                return "auth/login";
                // 자동 로그인이 실패한 경우 로그인 폼 페이지를 보여줍니다.
            }

        }


    /*
     * 로그인 기능 구현
     *
     * @param requestDto
     * @param session
     * @return
     * @throws JsonProcessingException
     */

    @PostMapping("/login")
    public String handleLogin(LoginRequestDto requestDto, HttpSession session, HttpServletResponse response) {
        session.removeAttribute("loginUser");
        Optional<User> findResult = userService.login(requestDto);
        findResult.ifPresent(user -> {
            setSessionLoginUser(session, user);
            // 자동 로그인 쿠키 생성
            if (requestDto.isRememberMe()) {
                Cookie cookie = new Cookie("userId", user.getId().toString());
                cookie.setMaxAge(60 * 60 * 24 * 365);
                response.addCookie(cookie);
            }
        });
        if (findResult.isEmpty())
            return "redirect:/auth/login";
        return "redirect:/";
    }


    @GetMapping("/logout")
    public String showLogoutForm(HttpSession session) {
        // 로그아웃 폼 페이지를 보여줍니다.
        return "auth/logout";
    }

    @PostMapping("/logout")
    public String handleLogout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        // 세션 삭제
        session.removeAttribute("loginUser");
        // 로그아웃 후 로그인 페이지로 리다이렉트
        return "redirect:/";
    }


    /**
     * 회원가입 폼으로 이동
     *
     * @return
     */
    @GetMapping("/signup")
    public String showSignUpForm() {
        return "auth/signup-form";
    }

    /**
     * 회원가입 처리 로직
     *
     * @param requestDto
     * @return
     */

    @PostMapping("/signup")
    public String signUp(SignUpRequestDto requestDto, HttpSession session) {
        setSessionLoginUser(session, userService.signUp(requestDto));
        return "redirect:/";
    }


    private void setSessionLoginUser(HttpSession session, User user) {
        session.setAttribute("loginUser", user);
    }
}
