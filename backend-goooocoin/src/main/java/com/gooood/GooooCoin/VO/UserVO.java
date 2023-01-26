package com.gooood.GooooCoin.VO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserVO {
    private Long id;
    private Integer userType;
    private String password;
    private String newPassword;
    private String username;
    private String phoneNumber;
    private String avatarAddress;
    private List<String> flavor;
    private List<String> method;
    private List<String> type;
}
