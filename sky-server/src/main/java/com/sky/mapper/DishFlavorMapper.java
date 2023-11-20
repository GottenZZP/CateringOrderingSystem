package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

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
}
