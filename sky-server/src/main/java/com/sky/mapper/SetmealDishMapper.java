package com.sky.mapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 通过菜品id来查询套餐id
     *
     * @param ids ids
     * @return {@link List}<{@link Long}>
     */
    List<Long> getSetmealIdsByDishIds(List<Long> ids);
}
