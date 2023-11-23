package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 菜品风味映射器
 *
 * @author GottenZZP
 * @date 2023/11/20
 */
@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入
     *
     * @param flavors 风味
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据id删除菜品品味
     *
     * @param id id
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    void deleteBatch(List<Long> dishIds);

    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
