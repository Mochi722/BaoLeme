package com.gooood.GooooCoin;

import com.gooood.GooooCoin.PO.Entity;
import com.gooood.GooooCoin.VO.IntelligenceSearchVO;
import com.gooood.GooooCoin.VO.ResponseVO;
import com.gooood.GooooCoin.VO.UserVO;
import com.gooood.GooooCoin.controller.graphController.AdminGraphController;
import com.gooood.GooooCoin.controller.graphController.GraphController;
import com.gooood.GooooCoin.controller.graphController.IntelligenceController;
import com.gooood.GooooCoin.controller.userController.UserAccountController;
import com.gooood.GooooCoin.controller.userController.UserCollectionController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@EnableAutoConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class KGIntergrateTests {
    @Autowired
    private GraphController graphController;
    @Autowired
    private AdminGraphController adminGraphController;
    @Autowired
    private IntelligenceController intelligenceController;
    @Autowired
    private UserAccountController userAccountController;
    @Autowired
    private UserCollectionController userCollectionController;


    @Test
    public void totalTest() {
        ResponseVO res;
        res=userAccountController.register(UserVO.builder().phoneNumber("12345678906").password("12345").username("testUser").build());
        Assert.assertTrue(res.isSuccess());
        System.out.println();
        res=userAccountController.login(UserVO.builder().phoneNumber("12345678906").password("12345").build(),new MockHttpServletResponse());
        UserVO user= ((UserVO) res.getContent());
        Assert.assertTrue(res.isSuccess());
        System.out.println("register login");

        res=intelligenceController.intelligenceSearch(IntelligenceSearchVO.builder().key("菜").label("Recipe").build());
        Assert.assertTrue(res.isSuccess());
        System.out.println("search");

        Entity recipe=((List<Entity>) res.getContent()).get(0);
        long recipeId= recipe.getId();
        int heat1=Integer.parseInt (recipe.getProperties().get("heat").toString());
        String recipeName=recipe.getProperties().get("name").toString();
        System.out.println(String.format("id:%d,recipe:%d,name:%s",user.getId(),recipeId,recipeName)+"\n\n\n\n\n");
        res=userCollectionController.collect(user.getId(),recipeId,recipeName);
        Assert.assertTrue(res.isSuccess());
        System.out.println("collect");

        res=intelligenceController.intelligenceSearch(IntelligenceSearchVO.builder().key(recipeName).label("Recipe").build());
        Assert.assertTrue(res.isSuccess());
        System.out.println("search");

        recipe=((List<Entity>) res.getContent()).get(0);
        int heat2=Integer.parseInt (recipe.getProperties().get("heat").toString());
        Assert.assertEquals(heat1+1,heat2);
        System.out.println("collect heat++");
    }
}
