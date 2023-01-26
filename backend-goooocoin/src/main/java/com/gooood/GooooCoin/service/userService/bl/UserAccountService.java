package com.gooood.GooooCoin.service.userService.bl;

import com.gooood.GooooCoin.VO.ResponseVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface UserAccountService {
    ResponseVO login(String phone, String password);

    ResponseVO register(String phone, String username, String password);

    ResponseVO modifyFavor(long id, String flavor, String method, String type);

    ResponseVO modifyPassword(long id,String originPassword,String newPassword);

    ResponseVO modifyAvatar(Long userId, MultipartFile file);

}
