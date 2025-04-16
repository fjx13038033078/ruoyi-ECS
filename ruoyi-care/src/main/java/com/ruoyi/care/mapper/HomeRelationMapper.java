package com.ruoyi.care.mapper;

import com.ruoyi.care.domain.HomeRelation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/4/16 15:58
 */
@Mapper
public interface HomeRelationMapper {
    /**
     * 获取所有家属关系
     * @return 家属关系列表
     */
    List<HomeRelation> getAllHomeRelations();

    /**
     * 根据ID获取家属关系
     * @param homeId 家属关系ID
     * @return 家属关系详情
     */
    HomeRelation getHomeRelationById(Long homeId);

    List<HomeRelation> getHomeRelationByUserId(Long userId);

    List<HomeRelation> getHomeRelationByDependentsId(Long dependentsId);

    /**
     * 添加家属关系
     * @param homeRelation 家属关系对象
     * @return 影响行数
     */
    int addHomeRelation(HomeRelation homeRelation);

    /**
     * 更新家属关系
     * @param homeRelation 家属关系对象
     * @return 影响行数
     */
    int updateHomeRelation(HomeRelation homeRelation);

    /**
     * 删除家属关系
     * @param homeId 家属关系ID
     * @return 影响行数
     */
    int deleteHomeRelation(Long homeId);
}
