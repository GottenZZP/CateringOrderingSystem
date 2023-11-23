package com.sky.controller.user;

import com.sky.constant.RedisConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 用户端商店控制层
 *
 * @author GottenZZP
 * @date 2023/11/23
 */
@RestController("userShopController")
@Slf4j
@Api(tags = "店铺相关接口")
@RequestMapping("/user/shop")
public class ShopController {

    /**
     * redis模板
     */
    @Autowired
    private RedisTemplate redisTemplate;

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
