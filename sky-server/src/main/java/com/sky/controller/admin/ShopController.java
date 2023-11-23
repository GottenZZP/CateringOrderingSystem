package com.sky.controller.admin;

import com.sky.constant.RedisConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端商店控制层
 *
 * @author GottenZZP
 * @date 2023/11/23
 */
@RestController("adminShopController")
@Slf4j
@Api(tags = "店铺相关接口")
@RequestMapping("/admin/shop")
public class ShopController {

    /**
     * redis模板
     */
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置状态
     *
     * @param status 状态
     * @return {@link Result}
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺营业状态")
    public Result setStatus(@PathVariable Integer status) {
        log.info("设置店铺营业状态：{}", status == 1 ? "营业中" : "打样中");
        redisTemplate.opsForValue().set("SHOP_STATUS", status);
        return Result.success();
    }

    /**
     * 获取状态
     *
     * @return {@link Result}<{@link Integer}>
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(RedisConstant.KEY);
        log.info("获取店铺营业状态: {}", status == 1 ? "营业中" : "打样中");
        return Result.success(status);
    }
}
