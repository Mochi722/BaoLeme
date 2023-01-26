package com.gooood.GooooCoin.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Builder
@AllArgsConstructor
public class ResponseVO {
    private boolean success;
    private String message;
    private Object content;
}
