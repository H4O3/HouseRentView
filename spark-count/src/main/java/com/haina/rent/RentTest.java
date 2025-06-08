package com.haina.rent;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.*;

/**
 * RentTest类用于处理和分析租赁数据
 * 该类读取CSV文件中的租赁数据，进行数据清洗和过滤，以去除重复的条目和不符合条件的数据
 */
public class RentTest {
    /**
     * 主函数执行数据处理和分析操作
     *
     * @param args 命令行参数，未使用
     */
    public static void main(String[] args) {
        // 创建SparkSession，用于读取和处理数据
        SparkSession spark = SparkSession.builder().master("local[*]").appName("RentTest").getOrCreate();
        // 读取CSV文件，包含表头和推断数据类型
        Dataset<Row> df = spark.read().option("header", true).option("inferSchema", true).csv("rent.csv");
        // 去除重复行
        Dataset<Row> rowDataset = df.dropDuplicates();
        // 过滤数据：住房面积大于0,小于1000,租金大于0,小于10000
        Dataset<Row> dfFilter = rowDataset.filter(col("area").gt(0).and(col("area").lt(1000)).and(col("rent").gt(0).and(col("rent").lt(100000))));
        // 删除地址为空的记录，并将centre字段中的空值替换为"未知"
        Dataset<Row> dfNo = dfFilter.na().drop(new String[]{"address"}).na().fill("未知", new String[]{"centre"});
        // 打印数据条数
        //System.out.println("数据条数："+dfNo.count());
        // 打印数据结构
        dfNo.printSchema();
        // 显示前二十条数据
        dfNo.show();
        //1.区域对租金的影响
        dfNo.groupBy("district").agg(avg("rent").alias("avg_rent")).show();
        //2.地区对租金的影响
        dfNo.groupBy("district", "address").agg(avg("rent").alias("avg_rent")).show();
        //3.区域,地区对租金的影响
        dfNo.groupBy("district", "address", "orientation").agg(avg("rent").alias("avg_rent")).show();
        //4.每平方米多少钱
        //添加新列,列中租金/面积
        dfNo.withColumn("area_rent", round(col("rent").divide("area"), 2)).show();
        //5.面积段的统计分析
        dfNo.withColumn("rent_range",
                        when(col("area").lt(300), "小于300平")
                                .when(col("area")
                                        .between(300, 500), "300-500")
                                .otherwise("大于500平"))
                .groupBy("rent_range").agg(avg(col("rent")).alias("avg_rent"))
                .orderBy(desc("avg_rent"))
                .show();
        //6.楼层的一个数据统计
        dfNo.groupBy("floor").count().orderBy("floor").show();
    }
}
