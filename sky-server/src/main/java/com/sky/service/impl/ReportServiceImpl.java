package com.sky.service.impl;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报告服务impl
 *
 * @author GottenZZP
 * @date 2023/11/20
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 获得营业额统计
     *
     * @param begin 开始
     * @param end   结束
     * @return {@link TurnoverReportVO}
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        // 生成日期列表, 从开始日期到结束日期
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (begin.isBefore(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 生成营业额列表
        ArrayList<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.subByMap(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }


        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 获取用户统计信息
     *
     * @param begin 开始
     * @param end   结束
     * @return {@link UserReportVO}
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        // 生成日期列表, 从开始日期到结束日期
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin.minusDays(1));
        dateList.add(begin);
        while (begin.isBefore(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 生成总用户列表和每日新用户列表
        ArrayList<Double> totalUserList = new ArrayList<>();
        ArrayList<Double> newUserList = new ArrayList<>();

        // 生成总用户列表
        for (LocalDate date : dateList) {
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("end", endTime);
            Double totalUser = orderMapper.countByMap(map);
            totalUser = totalUser == null ? 0.0 : totalUser;
            totalUserList.add(totalUser);

        }

        for (int i = 1; i < totalUserList.size(); i++) {
            newUserList.add(totalUserList.get(i) - totalUserList.get(i - 1));
        }


        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList.subList(1, newUserList.size() + 1), ","))
                .totalUserList(StringUtils.join(totalUserList.subList(1, newUserList.size() + 1), ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }
}
