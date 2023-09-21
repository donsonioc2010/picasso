package picasso.server.api.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import picasso.server.api.user.service.UserService;
import picasso.server.domain.domains.member.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class AuthController {

  {
    System.out.println("LoginController 생성됨!");
  }

  @Autowired
  UserService userService;

  @GetMapping("form")
  public void form(@CookieValue(required = false) String email, Model model) {
    model.addAttribute("email", email);
  }

  @PostMapping("login")
  public String login(
          String email,
          String password,
          String saveEmail,
          HttpSession session,
          Model model,
          HttpServletResponse response) throws Exception {

    if (saveEmail != null) {
      Cookie cookie = new Cookie("email", email);
      response.addCookie(cookie);
    } else {
      Cookie cookie = new Cookie("email", "no");
      cookie.setMaxAge(0);
      response.addCookie(cookie);
    }

    Optional<User> loginUser = userService.findUserByEmailAndPassword(email, password);
    if (loginUser.isEmpty()) {
      model.addAttribute("refresh", "2;url=form");
      throw new Exception("회원 정보가 일치하지 않습니다.");
    }

    session.setAttribute("loginUser", loginUser);
    return "redirect:/";
  }

  @GetMapping("logout")
  public String logout(HttpSession session) throws Exception {
    session.invalidate();
    return "redirect:/";
  }
  
  
  // TODO: 테스트용 Controller 입니다. 추후 삭제 혹은 변경이 필요합니다.
  @ResponseBody
  @PostMapping("/session-info")
  public Map<String, Long> paymentSessionInfoRtn(HttpSession session) {
    User temp = (User)session.getAttribute("loginUser");
    return new HashMap<String, Long>(){{
      put("userId", 1L);
    }};
  }
}