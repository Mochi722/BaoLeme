package com.gooood.GooooCoin.PO;

import lombok.Data;

@Data
public class User {
    private Long id;
    private Integer userType;
    private String password;
    private String username;
    private String phoneNumber;
    private String flavor;
    private String method;
    private String type;
    private String avatarAddress;

}
