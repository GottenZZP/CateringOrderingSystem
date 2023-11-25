package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品控制器
 *
 * @author GottenZZP
 * @date 2023/11/20
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     *
     * @param dishDTO 菜品dto
     * @return {@link Result}
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品: {}", dishDTO);
        dishService.saveWithFlavor(dishDTO);

        // 删除redis中的缓存
        String key = "dish_" + dishDTO.getCategoryId();
        cleanRedisCache(key);

        return Result.success();
    }

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO 菜品分页查询数据传输对象
     * @return {@link Result}<{@link PageResult}>
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询: {}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 菜品批量删除
     *
     * @param ids ids
     * @return {@link Result}
     */
    @DeleteMapping
    @ApiOperation("菜品批量删除")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("菜品批量删除");
        dishService.deleteBatch(ids);

        // 将所有菜品缓存删除
        cleanRedisCache("dish_*");
        return Result.success();
    }

    /**
     * 按id查询菜品
     *
     * @param id id
     * @return {@link Result}<{@link DishVO}>
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品, {}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 更新菜品
     *
     * @param dishDTO 菜品数据传输对象
     * @return {@link Result}
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品, {}", dishDTO);
        dishService.updateWithFlavor(dishDTO);

        // 将所有菜品缓存删除
        cleanRedisCache("dish_*");
        return Result.success();
    }

    /**
     * 按类别id获取菜品
     *
     * @param categoryId 类别id
     * @return {@link Result}<{@link List}<{@link DishVO}>>
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId) {
        log.info("根据分类id查询菜品, {}", categoryId);
        List<Dish> dishList = dishService.list(categoryId);
        return Result.success(dishList);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用菜品")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        dishService.startOrStop(status, id);
        cleanRedisCache("dish_*");
        return Result.success();
    }

    /**
     * 清除redis缓存
     *
     * @param pattern 图案
     */
    private void cleanRedisCache(String pattern) {
        // 删除redis中的缓存
        redisTemplate.keys(pattern).forEach(key -> redisTemplate.delete(key));
    }
}
