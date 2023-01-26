package com.gooood.GooooCoin.service.userService.bl;

import com.gooood.GooooCoin.VO.ResponseVO;
import org.springframework.web.bind.annotation.RequestParam;


// 合并
public interface UserCollectionService {
    ResponseVO collect(long id, long recipeId, String recipeName);

    ResponseVO getCollections(long id);

    ResponseVO deleteCollection(long id, long entity);
}
