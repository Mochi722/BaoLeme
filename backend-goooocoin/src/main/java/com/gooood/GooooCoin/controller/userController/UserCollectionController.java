package com.gooood.GooooCoin.controller.userController;

import com.gooood.GooooCoin.VO.ResponseVO;
import com.gooood.GooooCoin.service.userService.bl.UserCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserCollectionController {
    private final UserCollectionService userCollectionService;

    @Autowired
    public UserCollectionController(UserCollectionService userCollectionService) {
        this.userCollectionService = userCollectionService;
    }


    @PostMapping("/collect")
    public ResponseVO collect(@RequestParam("id") long id, @RequestParam("entity") long en_id, @RequestParam("name") String name) {
        return userCollectionService.collect(id, en_id, name);
    }


    @GetMapping("/getCollection")
    public ResponseVO getCollection(@RequestParam long id) {
        return userCollectionService.getCollections(id);
    }

    @PostMapping("/deleteCollection")
    public ResponseVO deleteCollection(@RequestParam long id, @RequestParam long entity) {
        return userCollectionService.deleteCollection(id, entity);
    }

}
