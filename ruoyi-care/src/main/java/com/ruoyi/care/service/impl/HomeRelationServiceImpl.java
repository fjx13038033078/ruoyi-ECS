package com.ruoyi.care.service.impl;

import com.ruoyi.care.domain.HomeRelation;
import com.ruoyi.care.mapper.HomeRelationMapper;
import com.ruoyi.care.service.HomeRelationService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.ruoyi.common.utils.PageUtils.startPage;

/**
 * @Author 范佳兴
 * @date 2025/4/16 16:01
 */
@Service
@RequiredArgsConstructor
public class HomeRelationServiceImpl implements HomeRelationService {
    private final HomeRelationMapper homeRelationMapper;
    private final ISysRoleService roleService;
    private final ISysUserService userService;

    @Override
    public List<HomeRelation> getAllHomeRelations() {
        Long userId = SecurityUtils.getUserId();
        String role = roleService.selectStringRoleByUserId(userId);

        if ("admin".equals(role)) {
            startPage();
            List<HomeRelation> list = homeRelationMapper.getAllHomeRelations();
            fillUserName(list);
            return list;
        } else if ("home".equals(role)){
            startPage();
            List<HomeRelation> list = homeRelationMapper.getHomeRelationByDependentsId(userId);
            fillUserName(list);
            return list;
        } else {
            startPage();
            List<HomeRelation> list = homeRelationMapper.getHomeRelationByUserId(userId);
            fillUserName(list);
            return list;
        }
    }

    @Override
    public HomeRelation getHomeRelationById(Long relationId) {
        return homeRelationMapper.getHomeRelationById(relationId);
    }

    @Override
    public List<HomeRelation> getHomeRelationByDependentsId(Long dependentsId) {
        return homeRelationMapper.getHomeRelationByDependentsId(dependentsId);
    }

    @Override
    public boolean addHomeRelation(HomeRelation homeRelation) {
        Long userId = SecurityUtils.getUserId();
        String role = roleService.selectStringRoleByUserId(userId);

        if (!"admin".equals(role)) {
            homeRelation.setHomeId(userId);
        }

        return homeRelationMapper.addHomeRelation(homeRelation) > 0;
    }

    @Override
    public boolean updateHomeRelation(HomeRelation homeRelation) {
        return homeRelationMapper.updateHomeRelation(homeRelation) > 0;
    }

    @Override
    public boolean deleteHomeRelation(Long relationId) {
        return homeRelationMapper.deleteHomeRelation(relationId) > 0;
    }

    private void fillUserName(List<HomeRelation> list) {
        for (HomeRelation relation : list) {
            Long id = relation.getUserId();
            if (id != null) {
                SysUser user = userService.selectUserById(id);
                relation.setUserName(user.getNickName());
            }
            Long dependentsId = relation.getDependentsId();
            if (dependentsId != null) {
                SysUser user = userService.selectUserById(dependentsId);
                relation.setDependentsName(user.getNickName());
            }
        }
    }
}
