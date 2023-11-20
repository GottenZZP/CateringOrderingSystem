package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 公用控制器
 *
 * @author GottenZZP
 * @date 2023/11/20
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    /**
     * 阿里云工具类
     */
    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 上传文件至阿里云OSS
     *
     * @param file 文件
     * @return {@link Result}<{@link String}>
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传: {}", file);

        // 获取文件后缀名
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        //随机生成UUID并拼接后缀作为新名字
        String name = UUID.randomUUID().toString() + suffix;

        // 上传至阿里云OSS
        try {

            String filePath = aliOssUtil.upload(file.getBytes(), name);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败: {}", e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
