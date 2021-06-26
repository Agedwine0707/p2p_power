package com.dlpower.p2p.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dlpower.p2p.cons.Constants;
import com.dlpower.p2p.service.BidInfoService;
import com.dlpower.p2p.service.LoanInfoService;
import com.dlpower.p2p.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author chenlanjiang
 * @date 2021/6/26
 */
@Controller
public class IndexController {
    @Reference(interfaceClass = LoanInfoService.class, version = "1.0.0", check = false, timeout = 15000)
    private LoanInfoService loaninfoService;

    @Reference(interfaceClass = UserService.class, version = "1.0.0", check = false, timeout = 15000)
    private UserService userService;

    @Reference(interfaceClass = BidInfoService.class, version = "1.0.0", check = false, timeout = 15000)
    private BidInfoService bidInfoService;


    @RequestMapping("/index")
    public String toIndex(Model model) {


        // 获取平台平均历史年化收益率
        Double historyAvgRate = loaninfoService.queryHistoryAvgRate();
        model.addAttribute(Constants.HISTORY_AVG_RATE, historyAvgRate);


        // 获取平台用户数
        Long allUserCount = userService.queryAllUserCount();
        model.addAttribute(Constants.ALL_USER_COUNT, allUserCount);

        // 获取平台累计成交金额
        Double investmentAmountSum = bidInfoService.getInvestmentAmountSum();
        model.addAttribute(Constants.INVEST_MONEY_SUM, investmentAmountSum);


        // 获取新手宝产品

        // 获取优选产品

        // 获取散步类型产品


        return "index";
    }

}
