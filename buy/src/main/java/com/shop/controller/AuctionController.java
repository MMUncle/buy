package com.shop.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.pojo.Auction;
import com.shop.pojo.Auctionrecord;
import com.shop.pojo.User;
import com.shop.service.AuctionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Controller
public class AuctionController {
    private final  static Integer PAGE_SIZE = 5;
    @Resource
    AuctionService auctionService;

    @RequestMapping(value = "/index")
    public String index(Model model ,@ModelAttribute("condition")Auction auction,
                        @RequestParam(name = "pageNum",required = false,defaultValue = "1") int pageNum){
        PageHelper.startPage(pageNum,PAGE_SIZE);
        List<Auction> auctions = auctionService.findAllAction();
        PageInfo page = new PageInfo(auctions);
        model.addAttribute("auctions",auctions);
        model.addAttribute("page",page);
        return "index";
    }

    @RequestMapping(value = "/queryAllAuctions")
    public String conditionsQuery(@ModelAttribute("condition")Auction auction, Model model,
                                  @RequestParam(name = "pageNum",required = false,defaultValue = "1") int pageNum){
        PageHelper.startPage(pageNum,PAGE_SIZE);
        List<Auction> auctions = auctionService.findActionsExample(auction);
        PageInfo page = new PageInfo(auctions);
        model.addAttribute("auctions",auctions);
        model.addAttribute("page",page);
        return "index";
    }

    @RequestMapping(value = "/auctionDetails/{auctionid}")
    public String auctionDetails(@PathVariable Integer auctionid,Model model){
        System.out.println("auctionid:"+auctionid);
        Auction auctionDetails = auctionService.getAuctionDetails(auctionid);
        System.out.println("auctionDetails=========================="+auctionDetails);
        model.addAttribute("auctionDetail",auctionDetails);
        return "auctionDetail";
    }
    @RequestMapping(value = "/saveAuctionRecord")
    public String saveAuctionRecord(HttpSession session, BigDecimal auctionprice, Integer auctionid) throws Exception {
        Auctionrecord auctionrecord = new Auctionrecord();
        auctionrecord.setAuctionid(auctionid);
        auctionrecord.setAuctiontime(new Date());
        auctionrecord.setAuctionprice(auctionprice);
        User user = (User) session.getAttribute("user");
        auctionrecord.setUserid(user.getUserid());
        System.out.println(auctionrecord);
        auctionService.bidding(auctionrecord);
        return "redirect:/auctionDetails/"+auctionid;
    }
}
