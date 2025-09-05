package com.example.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // ComponentScan
@RequestMapping("/api") // URL과의 연결
@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true") // CORS -> 외부에서 접근 허용
public class CookieController {

    // 단순 쿠키
    @GetMapping("/set-cookie")
    public ResponseEntity<String> setCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("simpleCookie", "basic-value");
        cookie.setPath("/");
        cookie.setHttpOnly(false); // JS 접근 가능 (보안 취약)
        response.addCookie(cookie);
        return ResponseEntity.ok("일반 쿠키 설정 완료");
    }

    // 보안 쿠키
    @GetMapping("/set-secure-cookie")
    public ResponseEntity<String> setSecureCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("secureCookie", "safe-value")
                .httpOnly(true)      // JS 접근 차단 → XSS 방어
                .secure(true)        // HTTPS 필요 (단, localhost/127.0.0.1은 최신 브라우저에서 허용)
                .sameSite("None")    // 크로스 도메인 요청 허용
                .path("/")
                .maxAge(60 * 60)     // 1시간
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok("보안 쿠키 설정 완료");
    }

    @GetMapping("/get-cookie")
    public ResponseEntity<String> getCookie(
            @CookieValue(value = "secureCookie", required = false) String value) {
        return ResponseEntity.ok("쿠키 값: " + value);
    }

    @GetMapping("/get-cookie2")
    public ResponseEntity<String> getCookie2(
            @CookieValue(value = "simpleCookie", required = false) String value) {
        return ResponseEntity.ok("쿠키 값: " + value);
    }
}