package com.haina.rent.controller;

import com.haina.rent.dao.HouseDao;
import com.haina.rent.model.Area;
import com.haina.rent.model.AreaRent;
import com.haina.rent.model.District;
import com.haina.rent.model.Room;
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
    @RequestMapping("/districtView")
    public @ResponseBody List<District> districtsView() {
        return houseDao.selectDistrict();
    }

    @RequestMapping("/roomView")
    public @ResponseBody List<Room> roomView() {
        return houseDao.selectRoom();
    }

    @RequestMapping("/areaView")
    public @ResponseBody List<Area> areaView() {
        return houseDao.selectArea();
    }

    @RequestMapping("/areaRentView")
    public @ResponseBody List<AreaRent> areaRentView() {
        return houseDao.selectAreaRent();
    }

}
