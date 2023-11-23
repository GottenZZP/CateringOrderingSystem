package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 新增菜品和对应的口味
     *
     * @param dishDTO 菜品dto
     */
    public void saveWithFlavor(DishDTO dishDTO);

    /**
     * 页面查询
     *
     * @param dishPageQueryDTO 菜品页查询数据传输对象
     * @return {@link PageResult}
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品批量删除
     *
     * @param ids ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 通过id查询菜品和口味
     *
     * @param id id
     * @return {@link DishVO}
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 修改菜品信息
     *
     * @param dishDTO 菜品数据传输对象
     */
    void updateWithFlavor(DishDTO dishDTO);
}
