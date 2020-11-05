package com.icebartech.core.utils;

import com.icebartech.core.vo.Capital;

import java.text.DecimalFormat;

/**
 * @author xiaoxiong
 * @date 2018/11/13 17:10
 */
public class AverageCapitalUtils {


    /**
     * 计算等额本息还款
     *
     * @param principal 贷款总额
     * @param months    贷款期限
     * @param rate      贷款利率
     * @return
     */
    public static Capital calculateEqualPrincipalAndInterest(Double principal, Integer months, Double rate) {
        Capital capital = new Capital();
        if (null == principal || null == months || null == rate
                || principal < 0 || months < 0 || rate < 0) {
            return capital;
        }
        DecimalFormat FORMAT = new DecimalFormat("#.##");

        //月利率
        double monthRate = rate / (100 * 12);
        //每月还款金额
        double preLoan = (principal * monthRate * Math.pow((1 + monthRate), months)) / (Math.pow((1 + monthRate), months) - 1);
        //还款总额
        double totalMoney = preLoan * months;
        //还款总利息
        double interest = totalMoney - principal;
        //还款总额
        capital.setTotalMoney(FORMAT.format(totalMoney));
        //贷款总额
        capital.setPrincipal(FORMAT.format(principal));
        //还款总利息
        capital.setInterest(FORMAT.format(interest));
        //每月还款金额
        capital.setPreLoan(FORMAT.format(preLoan));
        //还款期限
        capital.setMonths(String.valueOf(months));
        return capital;
    }

    /**
     * 计算等额本金还款
     * 等额本金是指一种贷款的还款方式，是在还款期内把贷款数总额等分，每月偿还同等数额的本金和剩余贷款在该月所产生的利息，这样由于每月的还款
     * 本金额固定，
     * 而利息越来越少，借款人起初还款压力较大，但是随时间的推移每月还款数也越来越少。
     *
     * @param principal 贷款总额
     * @param months    贷款期限
     * @param rate      贷款利率
     * @return
     */
    public static Capital calculateEqualPrincipal(Double principal, Integer months, Double rate) {
        Capital capital = new Capital();
        if (null == principal || null == months || null == rate
                || principal < 0 || months < 0 || rate < 0) {
            return capital;
        }

        DecimalFormat FORMAT = new DecimalFormat("#.##");

        //月利率
        double monthRate = rate / (100 * 12);
        //每月还款本金
        double prePrincipal = principal / months;
        //第一个月还款金额
        double firstMonth = prePrincipal + principal * monthRate;
        //每月利息递减
        double decreaseMonth = prePrincipal * monthRate;
        // 还款总利息
        double interest = (months + 1) * principal * monthRate / 2;
        //还款总额
        double totalMoney = principal + interest;
        //还款总额
        capital.setTotalMoney(FORMAT.format(totalMoney));
        //贷款总额
        capital.setPrincipal(FORMAT.format(principal));
        //还款总利息
        capital.setInterest(FORMAT.format(interest));
        //首月还款金额
        capital.setFirstMonth(FORMAT.format(firstMonth));
        //每月递减利息
        capital.setDecreaseMonth(FORMAT.format(decreaseMonth));
        //还款期限
        capital.setMonths(String.valueOf(months));
        return capital;
    }

}
