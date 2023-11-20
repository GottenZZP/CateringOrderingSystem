package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜品服务impl
 *
 * @author GottenZZP
 * @date 2023/11/20
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    /**
     * 菜品映射器
     */
    @Autowired
    private DishMapper dishMapper;
    /**
     * 菜品味道映射器
     */
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 存储菜品味道
     *
     * @param dishDTO 菜品dto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 插入菜品
        dishMapper.insert(dish);
        // 获取上一步insert语句生成的主键值
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));
            // 向口味表插入多条数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }
}
