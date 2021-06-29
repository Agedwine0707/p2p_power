package com.dlpower.p2p.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dlpower.p2p.model.loan.BidInfo;
import com.dlpower.p2p.model.loan.LoanInfo;
import com.dlpower.p2p.model.vo.PaginationVo;
import com.dlpower.p2p.service.BidInfoService;
import com.dlpower.p2p.service.LoanInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description:投资产品详情页控制器
 *
 * @author chenlanjiang
 * @date 2021/6/28
 */
@Controller
public class LoanInfoController {

    @Reference(interfaceClass = LoanInfoService.class, version = "1.0.0", check = false, timeout = 15000)
    private LoanInfoService loanInfoService;

    @Reference(interfaceClass = BidInfoService.class, version = "1.0.0", check = false, timeout = 15000)
    private BidInfoService bidInfoService;

    @RequestMapping("loan/loan")
    public String toLoanList(@RequestParam(required = false) Integer ptype,
                             @RequestParam(defaultValue = "1") Integer currentPage,
                             Model model) {

        Map<String, Object> paramMap = new HashMap<>();
        // 不是必须的
        paramMap.put("productType", ptype);
        paramMap.put("currentPage", currentPage);
        int pageSize = 9;
        paramMap.put("pageSize", pageSize);
        // 把查询结果封装到分页模板中
        PaginationVo<LoanInfo> paginationVo = loanInfoService.queryLoanInfoByPage(paramMap);

        // 将list集合数据传递给页面
        model.addAttribute("list", paginationVo.getList());
        int totalPage  = paginationVo.getTotal().intValue() / pageSize;
        int mod = paginationVo.getTotal().intValue() % pageSize;
        if (mod > 0) {
            totalPage = totalPage + 1;
        }
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("currentPage",currentPage);

        if (ObjectUtils.allNotNull(ptype)) {
            model.addAttribute("productType",ptype);
        }
        // TODO 投资排行榜
        return "loan";
    }

    @RequestMapping("loan/loanInfo")
    public String toLoanInfo(Model model, Integer id) {
        // 根据id查询投资产品信息
        LoanInfo loanInfo = loanInfoService.queryLoanInfoById(id);
        model.addAttribute("loanInfo", loanInfo);

        // 查询当前产品最近的10条投资记录
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("loanId", id);
        paramMap.put("currentPage", 0);
        paramMap.put("pageSize", 10);
        List<BidInfo> bidInfoList = bidInfoService.queryRecentlyBidInfoByLoanId(paramMap);

        model.addAttribute("bidInfoList", bidInfoList);

        // TODO 立即投资

        return "loanInfo";

    }
}
