package com.example.ddd.interfaces.iam.controller;

import com.example.ddd.application.iam.command.LoginCommand;
import com.example.ddd.application.iam.dto.CurrentUserDTO;
import com.example.ddd.application.iam.dto.LoginResultDTO;
import com.example.ddd.application.iam.service.AccessControlAppService;
import com.example.ddd.application.iam.service.LoginAppService;
import com.example.ddd.interfaces.iam.request.LoginRequest;
import com.example.ddd.interfaces.iam.security.SecurityContextHolder;
import com.example.ddd.interfaces.shared.response.ApiResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    private final LoginAppService loginAppService;
    private final AccessControlAppService accessControlAppService;

    public AuthController(LoginAppService loginAppService, AccessControlAppService accessControlAppService) {
        this.loginAppService = loginAppService;
        this.accessControlAppService = accessControlAppService;
    }

    /** 用户登录：用用户名+密码换取访问 Token。免鉴权入口。 */
    @PostMapping("/login")
    public ApiResponse<LoginResultDTO> login(@Valid @RequestBody LoginRequest request) {
        LoginCommand command = new LoginCommand(request.getUsername(), request.getPassword());
        return ApiResponse.success(loginAppService.login(command));
    }

    /** 用户登出：使当前请求携带的 Token 失效。 */
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        String header = request.getHeader(HEADER);
        if (header != null && header.startsWith(PREFIX)) {
            loginAppService.logout(header.substring(PREFIX.length()).trim());
        }
        return ApiResponse.success(null);
    }

    /** 查看当前登录用户：返回身份信息及其拥有的全部角色与权限。 */
    @GetMapping("/me")
    public ApiResponse<CurrentUserDTO> currentUser() {
        return ApiResponse.success(accessControlAppService.loadCurrentUser(SecurityContextHolder.getCurrentUserId()));
    }
}
