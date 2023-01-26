package com.gooood.GooooCoin.controller.graphController;

import com.gooood.GooooCoin.VO.IntelligenceSearchVO;
import com.gooood.GooooCoin.VO.ResponseVO;
import com.gooood.GooooCoin.service.graphService.bl.IntelligenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/graph")
public class IntelligenceController {
    private final IntelligenceService intelligenceService;

    @Autowired
    public IntelligenceController(IntelligenceService intelligenceService) {
        this.intelligenceService = intelligenceService;
    }


    /**
     * 数据格式：
     * {
     *     key:"搜索关键字",可以是null
     *     label:"Recipe/Ingredients"或者null
     *     props:（可以是null）{
     *         （如果是Recipe）
     *         "口味":["1","2"],
     *         "工艺":["1","2"],
     *         "类型":["1","2"],
     *         （如果是Ingredients）
     *         "类别":["1","2"],
     *     }
     * }
     * @param VO
     * @return
     */
    @PostMapping("/intelligence_search")
    public ResponseVO intelligenceSearch(@RequestBody IntelligenceSearchVO VO){

        return intelligenceService.search(VO.getKey(),VO.getLabel(),VO.getProps());
    }

    @PostMapping("/cook_what")
    public ResponseVO cookWhat(@RequestBody IntelligenceSearchVO VO){
        return intelligenceService.cookWhat(VO.getNames(),VO.getProps());
    }

    @GetMapping("/query")
    public ResponseVO query(@RequestParam(value = "question") String question) throws Exception {
        return intelligenceService.answer(question);
    }

}
