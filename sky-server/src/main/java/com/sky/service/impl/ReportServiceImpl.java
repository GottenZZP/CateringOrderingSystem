package com.sky.service.impl;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.models.auth.In;
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
        // 创建一个日期列表，包含从开始日期的前一天到结束日期的所有日期
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin.minusDays(1)); // 添加开始日期的前一天
        dateList.add(begin); // 添加开始日期
        while (begin.isBefore(end)) { // 当开始日期在结束日期之前时
            begin = begin.plusDays(1); // 将开始日期向后推一天
            dateList.add(begin); // 将新的开始日期添加到日期列表中
        }

        // 创建两个列表，一个用于存储每天的总用户数，一个用于存储每天的新用户数
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();

        // 遍历日期列表，对于每一个日期，获取该日期的总用户数，并添加到总用户列表中
        for (LocalDate date : dateList) {
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX); // 获取该日期的最后一刻

            Map map = new HashMap();
            map.put("end", endTime); // 将该日期的最后一刻作为键值对的值放入Map中，键为"end"
            Integer totalUser = orderMapper.countByMap(map); // 调用orderMapper.countByMap(map)方法，获取该日期的总用户数
            totalUser = totalUser == null ? 0 : totalUser; // 如果获取到的总用户数为null，则将其设置为0.0
            totalUserList.add(totalUser); // 将总用户数添加到总用户列表中
        }

        // 遍历总用户列表，计算每一天的新用户数，即每一天的总用户数减去前一天的总用户数，并将结果添加到新用户列表中
        for (int i = 1; i < totalUserList.size(); i++) {
            newUserList.add(totalUserList.get(i) - totalUserList.get(i - 1));
        }

        // 使用UserReportVO.builder()构建一个UserReportVO对象，并设置其dateList、totalUserList和newUserList属性，然后返回这个对象
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList.subList(1, newUserList.size() + 1), ","))
                .totalUserList(StringUtils.join(totalUserList.subList(1, newUserList.size() + 1), ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }

    /**
     * 获取订单统计信息
     *
     * @param begin 开始
     * @param end   结束
     * @return {@link UserReportVO}
     */
    @Override
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {
        // 创建一个日期列表
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin); // 添加开始日期
        while (begin.isBefore(end)) { // 当开始日期在结束日期之前时
            begin = begin.plusDays(1); // 将开始日期向后推一天
            dateList.add(begin); // 将新的开始日期添加到日期列表中
        }

        // 创建一个订单数列表
        ArrayList<Integer> orderCountList = new ArrayList<>();
        // 创建一个有效订单数列表
        ArrayList<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            // 获取该日期的订单数
            Integer orderCount = getOrderCount(beginTime, endTime, null);
            // 获取该日期的有效订单数
            Integer validOrderCount = getOrderCount(beginTime, endTime, Orders.COMPLETED);

            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);
        }
        // 计算总订单数和有效订单数
        Integer totalCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer validCount = validOrderCountList.stream().reduce(Integer::sum).get();
        Double orderCompletionRate = totalCount != 0.0 ? validCount / totalCount : 0.0;

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalCount)
                .validOrderCount(validCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    // 获取订单数
    private Integer getOrderCount(LocalDateTime beginTime, LocalDateTime endTime, Integer status) {
        Map map = new HashMap();
        map.put("begin", beginTime);
        map.put("end", endTime);
        map.put("status", status);

        return orderMapper.countByMap(map);
    }
}
