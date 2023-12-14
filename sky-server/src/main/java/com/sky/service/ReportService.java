package com.sky.service;


import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {


    /**
     * 获得营业额统计
     *
     * @param begin 开始
     * @param end   结束
     * @return {@link TurnoverReportVO}
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 获取用户统计信息
     *
     * @param begin 开始
     * @param end   结束
     * @return {@link UserReportVO}
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 获取订单统计信息
     *
     * @param begin 开始
     * @param end   结束
     * @return {@link UserReportVO}
     */
    OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end);
}
