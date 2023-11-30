package com.example.authjwt.controller;


import com.example.authjwt.model.AuthRequest;
import com.example.authjwt.model.UserInfo;
import com.example.authjwt.service.JwtService;
import com.example.authjwt.service.UserInfoService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }



     
//    @GetMapping("/success")
//    public String success(@RequestHeader(name = "Authorization", required = false) String authorizationHeader) {
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            // Extract the token from the Authorization header
//            String token = authorizationHeader.substring(7);
//
//            // You can now use the token as needed, for example, by validating it or extracting information.
//            // For simplicity, let's just return the token in the response headers.
//            return "Login Success. Token: " + token;
//        } else {
//            return "Login Success. No token provided.";
//        }
//    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @GetMapping("/success")
     public String success(Authentication authentication1) {

        AuthRequest authRequest;

         authentication1 = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication1.getName();
        

         return "Login Success";
     }

}
