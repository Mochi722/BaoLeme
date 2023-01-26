package com.gooood.GooooCoin.controller.userController;

import com.gooood.GooooCoin.VO.ResponseVO;
import com.gooood.GooooCoin.VO.UserVO;
import com.gooood.GooooCoin.service.userService.bl.UserAccountService;
import com.gooood.GooooCoin.util.TransformUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserAccountController {
    private final UserAccountService userAccountService;

    @Autowired
    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping("/")
    public String hello(){
        return "hello";
    }

    @PostMapping("/login")
    public ResponseVO login(@RequestBody UserVO userVO, HttpServletResponse response) {
        ResponseVO res= userAccountService.login(userVO.getPhoneNumber(), userVO.getPassword());
        if(res.isSuccess()){
            System.out.println(res.getContent().toString());
            Cookie cookie=new Cookie("goooocoinUserType",((UserVO)res.getContent()).getUserType().toString());
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
        }
        return res;
    }

    @PostMapping("/register")
    public ResponseVO register(@RequestBody UserVO userVO) {
        System.out.println(userVO);
        return userAccountService.register(userVO.getUsername(), userVO.getPassword(),userVO.getPhoneNumber());
    }

    @PostMapping("/modifyFavor")
    public ResponseVO modifyFavor(@RequestBody UserVO userVO){
        return userAccountService.modifyFavor(userVO.getId(), TransformUtil.list2String(userVO.getFlavor()),TransformUtil.list2String(userVO.getMethod()),TransformUtil.list2String(userVO.getType()));
    }

    @PostMapping("/modifyPassword")
    public ResponseVO modifyPassword(@RequestBody UserVO userVO){
        return userAccountService.modifyPassword(userVO.getId(),userVO.getPassword(), userVO.getNewPassword());
    }

    @PostMapping("/modifyAvatar")
    public ResponseVO upload(@RequestParam Long userId,
                         @RequestParam("file") MultipartFile file) throws IOException {
        return userAccountService.modifyAvatar(userId,file);
    }
}
