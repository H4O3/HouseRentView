/**
 * HouseDao接口用于与数据库中的house_rent_by_district表进行交互
 * 主要负责查询不同区域的房源信息
 */
package com.haina.rent.dao;

import com.haina.rent.model.*;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 定义一个仓库组件HouseDao，用于处理与房源相关的数据库操作
 */
@Repository
public interface HouseDao {

    @Select("select * from house_rent_by_district")
    List<District> selectDistrict();

    @Select("select * from house_rent_by_room order by room asc")
    List<Room> selectRoom();

    @Select("select * from house_rent_by_bathroom order by bathroom asc")
    List<Bathroom> selectBathroom();

    @Select("select * from house_rent_by_floor")
    List<Floor> selectFloor();

    @Select("select * from house_rent_by_layout")
    List<Layout> selectLayout();

    @Select("select * from house_rent_by_living order by living asc")
    List<Living> selectLiving();

    @Select("select * from house_rent_by_orientation")
    List<Orientation> selectOrientation();

    @Select("select * from house_rent_by_area_rent")
    List<AreaRent> selectAreaRent();

    @Select("select * from house_rent_by_area_rent_dis")
    List<AreaRentDis> selectAreaRentDis();

}

