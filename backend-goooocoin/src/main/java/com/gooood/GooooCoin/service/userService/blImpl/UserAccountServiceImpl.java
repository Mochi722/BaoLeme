package com.gooood.GooooCoin.service.userService.blImpl;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.gooood.GooooCoin.PO.User;
import com.gooood.GooooCoin.VO.ResponseVO;
import com.gooood.GooooCoin.VO.UserVO;
import com.gooood.GooooCoin.constant.OssConstant;
import com.gooood.GooooCoin.data.mysqlData.UserMapper;
import com.gooood.GooooCoin.service.userService.bl.UserAccountService;
import com.gooood.GooooCoin.util.OSSUtil;
import com.gooood.GooooCoin.util.TransformUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final UserMapper userMapper;

    @Autowired
    public UserAccountServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    private String getMethodName() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        return e.getMethodName();
    }


    @Override
    public ResponseVO login(String phone, String password) {
        try {
            User user = userMapper.getUserByPhone(phone);
            if (null == user) return ResponseVO.builder().message("用户名不存在").success(false).build();
            if (!user.getPassword().equals(password)) {
                return ResponseVO.builder().message("密码错误").success(false).build();
            }
            UserVO userVO = UserVO.builder()
                    .id(user.getId())
                    .userType(user.getUserType())
                    .username(user.getUsername())
                    .phoneNumber(user.getPhoneNumber())
                    .avatarAddress(user.getAvatarAddress())
                    .flavor(TransformUtil.string2List(user.getFlavor()))
                    .method(TransformUtil.string2List(user.getMethod()))
                    .type(TransformUtil.string2List(user.getType()))
                    .build();
            System.out.println("avatarAddress"+userVO.getAvatarAddress());
            return ResponseVO.builder().message("登陆成功").success(true).content(userVO).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }

    @Override
    public ResponseVO register(String username, String password, String phone) {
        try {
            User user = userMapper.getUserByPhone(phone);
            if (null != user) return ResponseVO.builder().message("手机号码已被使用").success(false).build();
            userMapper.register(username,password, phone);
            return ResponseVO.builder().message("注册成功").success(true).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }

    @Override
    public ResponseVO modifyFavor(long id, String flavor, String method, String type) {
        try {
            userMapper.modifyFavor(id, flavor, method, type);
            return ResponseVO.builder().success(true).message("修改成功").build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }

    @Override
    public ResponseVO modifyPassword(long id, String originPassword, String newPassword) {
        try {
            User user = userMapper.getUserById(id);
            String origin = DigestUtil.md5Hex(originPassword);
            if (origin.equals(user.getPassword())) {
                userMapper.modifyPassword(id, DigestUtil.md5Hex(newPassword));
                return ResponseVO.builder().success(true).message("修改密码成功").build();
            } else return ResponseVO.builder().success(false).message("原密码错误，修改失败").build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.builder().success(false).message("Failed in :" + getMethodName()).build();
        }
    }

    @Override
    public ResponseVO modifyAvatar(Long userId, MultipartFile multipartFile){
        /* 上传到OSS，并返回文件地址 */
        String imgURL = OSSUtil.uploadObject2OSS(OSSUtil.getOSSClient(),TransformUtil.transferToFile(multipartFile), OssConstant.OSS_BUCKET_IM,"avatar");
        Map data = new HashMap();
        data.put("path", imgURL);
        System.out.println("imgURL "+imgURL);
        if (imgURL != null) {
            userMapper.modifyAvatar(userId,imgURL);
            return ResponseVO.builder().success(true).message("上传成功").content(data).build();
        } else {
            return ResponseVO.builder().success(false).message("上传失败").build();
        }
    }
}
