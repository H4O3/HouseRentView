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
public class ViewController {

    /**
     * 房源数据访问对象，用于执行与数据库相关的操作
     */
    @Resource
    private HouseDao houseDao;

    //1.按行政区统计平均租金
    @RequestMapping("/districtView")
    public @ResponseBody List<District> districtsView() {
        return houseDao.selectDistrict();
    }

    //2.按卫生间个数统计平均租金
    @RequestMapping("/bathroomView")
    public @ResponseBody List<Bathroom> bathroomView() {
        return houseDao.selectBathroom();
    }

    // 3.按楼层统计平均租金
    @RequestMapping("/floorView")
    public @ResponseBody List<Floor> floorView() {
        return houseDao.selectFloor();
    }

    //4.按照户型和租金的关系
    @RequestMapping("/layoutView")
    public @ResponseBody List<Layout> layoutView() {
        return houseDao.selectLayout();
    }

    //5.客厅的个数对于租金的影响
    @RequestMapping("/livingView")
    public @ResponseBody List<Living> livingView() {
        return houseDao.selectLiving();
    }

    //6.朝向的个数对于租金的影响
    @RequestMapping("/orientationView")
    public @ResponseBody List<Orientation> orientationView() {
        return houseDao.selectOrientation();
    }

    //7.卧室的个数对于租金的影响
    @RequestMapping("/roomView")
    public @ResponseBody List<Room> roomView() {
        return houseDao.selectRoom();
    }

    //8.每平方米多少钱
    @RequestMapping("/areaRentView")
    public @ResponseBody List<AreaRent> areaView() {
        return houseDao.selectAreaRent();
    }

    //9.面积区间的统计
    @RequestMapping("/areaRentDisView")
    public @ResponseBody List<AreaRentDis> areaRentDisView() {
        return houseDao.selectAreaRentDis();
    }

}
