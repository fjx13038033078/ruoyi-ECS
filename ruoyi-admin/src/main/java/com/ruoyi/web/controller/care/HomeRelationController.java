package com.ruoyi.web.controller.care;

import com.ruoyi.care.domain.HomeRelation;
import com.ruoyi.care.service.HomeRelationService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/4/16 16:07
 */
@RestController
@RequestMapping("/care/relation")
@RequiredArgsConstructor
public class HomeRelationController extends BaseController {
    private final HomeRelationService homeRelationService;

    @GetMapping("/listAll")
    public TableDataInfo listAll() {
        List<HomeRelation> list = homeRelationService.getAllHomeRelations();
        return getDataTable(list);
    }

    @GetMapping("/detail")
    public AjaxResult getById(@RequestParam Long relationId) {
        return success(homeRelationService.getHomeRelationById(relationId));
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody HomeRelation relation) {
        return toAjax(homeRelationService.addHomeRelation(relation));
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody HomeRelation relation) {
        return toAjax(homeRelationService.updateHomeRelation(relation));
    }

    @GetMapping("/delete")
    public AjaxResult delete(@RequestParam Long relationId) {
        return toAjax(homeRelationService.deleteHomeRelation(relationId));
    }
}
