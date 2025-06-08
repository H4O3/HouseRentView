/**
 * HouseDao接口用于与数据库中的house_rent_by_district表进行交互
 * 主要负责查询不同区域的房源信息
 */
package com.haina.rent.dao;

import com.haina.rent.model.Area;
import com.haina.rent.model.AreaRent;
import com.haina.rent.model.District;
import com.haina.rent.model.Room;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 定义一个仓库组件HouseDao，用于处理与房源相关的数据库操作
 */
@Repository
public interface HouseDao {

    /**
     * 查询所有区域的房源信息
     *
     * @return 包含所有区域房源信息的列表
     */
    @Select("select * from house_rent_by_district")
    List<District> selectDistrict();

    @Select("select * from house_rent_by_room order by room asc")
    List<Room> selectRoom();

    @Select("select * from house_rent_by_area_dis")
    List<Area> selectArea();

    @Select("select * from house_rent_by_area_rent")
    List<AreaRent> selectAreaRent();
}

