package com.haina.rent;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

import static org.apache.spark.sql.functions.*;

public class JDBCRentTest {
    /**
     * 主函数：租金数据分析及存储程序入口
     * 功能说明：
     * 1. 创建Spark会话进行数据处理
     * 2. 对原始租房数据进行清洗和转换
     * 3. 分析不同行政区的平均租金
     * 4. 将结果持久化到MySQL数据库
     */
    public static void main(String[] args) {
        // 初始化Spark环境配置（本地模式，使用所有可用核心）
        SparkSession spark = SparkSession.builder().master("local[*]").appName("RentTest").getOrCreate();

        // 读取原始租房数据（自动解析表头和数据格式）
        Dataset<Row> df = spark.read().option("header", true).option("inferSchema", true).csv("rent.csv");

        // 数据预处理：去除完全重复的数据记录
        Dataset<Row> rowDataset = df.dropDuplicates();

        // 数据有效性过滤（排除异常面积和租金数据）
        Dataset<Row> dfFilter = rowDataset.filter(
                col("area")
                        .gt(0)
                        .and(col("area").lt(1000))
                        .and(col("rent").gt(0).and(col("rent").lt(100000)))
        );

        // 数据完整性处理：移除关键字段缺失的记录并填充默认值
        Dataset<Row> dfNo = dfFilter.na().drop(new String[]{"address"})
                .na().fill("未知", new String[]{"centre"});

        // 配置MySQL数据库连接参数
        String url = "jdbc:mysql://localhost:3306/atguigudb";
        Properties properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "123456");
        properties.put("driver", "com.mysql.cj.jdbc.Driver");

        // 1.按行政区统计平均租金
        Dataset<Row> district = dfNo.groupBy("district")
                // 聚合计算并格式化数值（保留两位小数）
                .agg(round(avg("rent"), 2)
                        .alias("avg_rent"));
        // 数据持久化：将统计结果写入MySQL指定表（覆盖模式）
        district.write().mode("overwrite").jdbc(url, "house_rent_by_district", properties);

        // 2.按卫生间个数统计平均租金
        Dataset<Row> bathroom = dfNo.groupBy("bathroom")
                // 聚合计算并格式化数值（保留两位小数）
                .agg(round(avg("rent"), 2).alias("avg_rent"));
        // 数据持久化：将统计结果写入MySQL指定表（覆盖模式）
        bathroom.write().mode("overwrite").jdbc(url, "house_rent_by_bathroom", properties);

        // 3.按楼层统计平均租金
        Dataset<Row> floor = dfNo.withColumn("floor_count",
                        when(col("floor").lt(10), "低层")
                                .when(col("floor")
                                        .between(10, 30), "中层")
                                .otherwise("高层"))
                .groupBy("floor_count")
                // 聚合计算并格式化数值（保留两位小数）
                .agg(round(avg("rent"), 2).alias("avg_rent"));
        // 数据持久化：将统计结果写入MySQL指定表（覆盖模式）
        floor.write().mode("overwrite").jdbc(url, "house_rent_by_floor", properties);

        //4.按照户型和租金的关系
        Dataset<Row> layout = dfNo.withColumn("layout",
                        concat_ws("-", col("room"),
                                col("living"), col("bathroom")))
                .groupBy("layout")
                .agg(round(avg("rent"), 2).alias("avg_rent"),
                        count("*").alias("count"));
        // 数据持久化：将统计结果写入MySQL指定表（覆盖模式）
        layout.write().mode("overwrite").jdbc(url, "house_rent_by_layout", properties);

        //5.客厅的个数对于租金的影响
        Dataset<Row> living = dfNo.groupBy("living")
                .agg(round(avg("rent"), 2).alias("avg_rent")
                        , count("*").alias("count"));
        living.write().mode("overwrite").jdbc(url, "house_rent_by_living", properties);

        //6.朝向的个数对于租金的影响
        Dataset<Row> orientation = dfNo.groupBy("orientation")
                .agg(round(avg("rent"), 2).alias("avg_rent")
                        , count("*").alias("count"));
        orientation.write().mode("overwrite").jdbc(url, "house_rent_by_orientation", properties);

        //7.卧室的个数对于租金的影响
        Dataset<Row> room = dfNo.groupBy("room")
                .agg(round(avg("rent"), 2).alias("avg_rent")
                        , count("*").alias("count"));
        room.write().mode("overwrite").jdbc(url, "house_rent_by_room", properties);

        //8.每平方米多少钱
        Dataset<Row> area_rent = dfNo.withColumn("area_rent",
                round(col("rent").divide(col("area")), 2));
        //指定往表中添加列的个数
        area_rent.select("district", "address", "area", "area_rent")
                .write()
                .mode("overwrite")
                .jdbc(url, "house_rent_by_area_rent", properties);

        //9.面积区间的统计
        Dataset<Row> area_rent_dis = dfNo.withColumn("area_rent_dis",
                        when(col("area").lt(300), "小于300平")
                                .when(col("area").between(300, 500), "300-500平")
                                .otherwise("大于500平"))
                .groupBy("area_rent_dis")
                .agg(round(avg("rent"), 2).alias("avg_rent")
                        , count("*").alias("count"));
        area_rent_dis.write().mode("overwrite").jdbc(url, "house_rent_by_area_rent_dis", properties);

        //10.户型的房源的统计
        Dataset<Row> area_dis = dfNo.withColumn("area_dis",
                        when(col("area").lt(300), "小户型小于300平")
                                .when(col("area").between(300, 500), "中户型300-500平")
                                .otherwise("大户型大于500平"))
                .groupBy("area_dis")
                .agg(round(avg("rent"), 2).alias("avg_rent")
                        , count("*").alias("count"));
        area_dis.write().mode("overwrite").jdbc(url, "house_rent_by_area_dis", properties);
    }
}
