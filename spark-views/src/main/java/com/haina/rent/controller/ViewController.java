package com.haina.rent.controller;

import com.haina.rent.dao.HouseDao;
import com.haina.rent.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 控制器类，用于处理与视图相关的请求
 */
@Controller
//@RequestMapping("rent")
public class ViewController {

    /**
     * 房源数据访问对象，用于执行与数据库相关的操作
     */
    @Resource
    private HouseDao houseDao;

    /**
     * 处理districtView请求，返回区域列表
     *
     * @return 区域列表
     */

    //不同区域的价格
    @RequestMapping("/districtView")
    public @ResponseBody List<District> districtsView() {
        return houseDao.selectDistrict();
    }

    //房间个数影响价格
    @RequestMapping("/roomView")
    public @ResponseBody List<Room> roomView() {
        return houseDao.selectRoom();
    }

    //面积影响价格
    @RequestMapping("/areaView")
    public @ResponseBody List<Area> areaView() {
        return houseDao.selectArea();
    }

    //不同区域的每平方米租金
    @RequestMapping("/areaRentView")
    public @ResponseBody List<AreaRent> areaRentView() {
        return houseDao.selectAreaRent();
    }

    //卧室个数影响价格
    @RequestMapping("/bathroomView")
    public @ResponseBody List<Bathroom> bathroomView() {
        return houseDao.selectBathroom();
    }

}
