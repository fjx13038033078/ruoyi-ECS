package com.ruoyi.care.service;

import com.ruoyi.care.domain.HomeRelation;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/4/16 16:01
 */
public interface HomeRelationService {
    List<HomeRelation> getAllHomeRelations();

    HomeRelation getHomeRelationById(Long relationId);

    List<HomeRelation> getHomeRelationByDependentsId(Long dependentsId);

    boolean addHomeRelation(HomeRelation homeRelation);

    boolean updateHomeRelation(HomeRelation homeRelation);

    boolean deleteHomeRelation(Long relationId);
}
