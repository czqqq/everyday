package com.czq.practice;

import org.apache.spark.Partitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.*;
import java.util.regex.Pattern;

public class GroupByKey {


    private static final Pattern SPACE = Pattern.compile(" ");

    public static void main(String args[]) {
//        groupByKey1();
//        groupByKey2();
//        main3();
//        mapParWithIndex();
//        sortBy();
        reduce();
    }

    private static void groupByKey1() {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("Spark_GroupByKey_Sample");
        sparkConf.setMaster("local");

        JavaSparkContext context = new JavaSparkContext(sparkConf);

        List<Integer> data = Arrays.asList(1, 1, 2, 2, 1);
        JavaRDD<Integer> distData = context.parallelize(data);

        JavaPairRDD<Integer, Integer> firstRDD = distData.mapToPair(new PairFunction<Integer, Integer, Integer>() {
            @Override
            public Tuple2<Integer, Integer> call(Integer integer) throws Exception {
                return new Tuple2(integer, integer * integer);
            }
        });

        JavaPairRDD<Integer, Iterable<Integer>> secondRDD = firstRDD.groupByKey();

        List<Tuple2<Integer, String>> reslist = secondRDD.map(new Function<Tuple2<Integer, Iterable<Integer>>, Tuple2<Integer, String>>() {
            @Override
            public Tuple2<Integer, String> call(Tuple2<Integer, Iterable<Integer>> integerIterableTuple2) throws Exception {
                int key = integerIterableTuple2._1();
                StringBuffer sb = new StringBuffer();
                Iterable<Integer> iter = integerIterableTuple2._2();
                for (Integer integer : iter) {
                    sb.append(integer).append(" ");
                }
                return new Tuple2(key, sb.toString().trim());
            }
        }).collect();


        for (Tuple2<Integer, String> str : reslist) {
            System.out.println(str._1() + "\t" + str._2());
        }
        context.stop();
    }


    /**
     * maptopair的使用
     */
    private static void groupByKey2() {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("Spark_GroupByKey_Sample");
        sparkConf.setMaster("local");

        JavaSparkContext context = new JavaSparkContext(sparkConf);
        List<Integer> data = Arrays.asList(1, 2, 4, 3, 5, 6, 7);
        JavaRDD<Integer> javaRDD = context.parallelize(data);
        //转为k，v格式
        JavaPairRDD<Integer, Integer> javaPairRDD = javaRDD.mapToPair(new PairFunction<Integer, Integer, Integer>() {
            @Override
            public Tuple2<Integer, Integer> call(Integer integer) throws Exception {
                return new Tuple2<Integer, Integer>(integer, 1);
            }
        });

        JavaPairRDD<Integer, Iterable<Integer>> groupByKeyRDD = javaPairRDD.groupByKey(2);
        System.out.println(groupByKeyRDD.collect());

        //自定义partition
        JavaPairRDD<Integer, Iterable<Integer>> groupByKeyRDD3 = javaPairRDD.groupByKey(new Partitioner() {
            //partition各数
            @Override
            public int numPartitions() {
                return 10;
            }

            //partition方式
            @Override
            public int getPartition(Object o) {
                return (o.toString()).hashCode() % numPartitions();
            }
        });
        System.out.println(groupByKeyRDD3.collect());
    }

    private static void main3() {
        List<Integer> data = Arrays.asList(1, 2, 4, 3, 5, 6, 7);
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("Spark_GroupByKey_Sample");
        sparkConf.setMaster("local");

        JavaSparkContext context = new JavaSparkContext(sparkConf);
        //RDD有两个分区
        JavaRDD<Integer> javaRDD = context.parallelize(data, 2);
        //计算每个分区的合计
        JavaRDD<Integer> mapPartitionsRDD = javaRDD.mapPartitions((FlatMapFunction<Iterator<Integer>, Integer>) integerIterator -> {
            int isum = 0;
            while (integerIterator.hasNext())
                isum += integerIterator.next();
            LinkedList<Integer> linkedList = new LinkedList<Integer>();
            linkedList.add(isum);
            return linkedList.iterator();
        });


        /*
        非lambda写法
        JavaRDD<Integer> mapPartitionsRDD = javaRDD.mapPartitions(new FlatMapFunction<Iterator<Integer>, Integer>() {
            @Override
            public Iterable<Integer> call(Iterator<Integer> integerIterator) throws Exception {
                int isum = 0;
                while (integerIterator.hasNext())
                    isum += integerIterator.next();
                LinkedList<Integer> linkedList = new LinkedList<Integer>();
                linkedList.add(isum);
                return linkedList.iterator();
            }
        });*/


        System.out.println("mapPartitionsRDD~~~~~~~~~~~~~~~~~~~~~~" + mapPartitionsRDD.collect());
        //输出结果 mapPartitionsRDD~~~~~~~~~~~~~~~~~~~~~~[7, 21]
    }


    private static void mapParWithIndex() {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("mapPartitionsWithIndex");
        sparkConf.setMaster("local");
        JavaSparkContext context = new JavaSparkContext(sparkConf);
        List<Integer> data = Arrays.asList(1, 2, 4, 3, 5, 6, 7);
        //RDD有两个分区
        JavaRDD<Integer> javaRDD = context.parallelize(data, 2);
        //分区index、元素值、元素编号输出
        JavaRDD<String> mapPartitionsWithIndexRDD = javaRDD.mapPartitionsWithIndex((Function2<Integer, Iterator<Integer>, Iterator<String>>) (v1, v2) -> {
            LinkedList<String> linkedList = new LinkedList<String>();
            int i = 0;
            while (v2.hasNext())
                linkedList.add(Integer.toString(v1) + "|" + v2.next().toString() + Integer.toString(i++));
            return linkedList.iterator();
        }, false);

   /*
    非lambda写法
    JavaRDD<String> mapPartitionsWithIndexRDD = javaRDD.mapPartitionsWithIndex(new Function2<Integer, Iterator<Integer>, Iterator<String>>() {
            @Override
            public Iterator<String> call(Integer v1, Iterator<Integer> v2) throws Exception {
                LinkedList<String> linkedList = new LinkedList<String>();
                int i = 0;
                while (v2.hasNext())
                    linkedList.add(Integer.toString(v1) + "|" + v2.next().toString() + Integer.toString(i++));
                return linkedList.iterator();
            }
        }, false);
*/

        System.out.println("mapPartitionsWithIndexRDD~~~~~~~~~~~~~~~~~~~~~~" + mapPartitionsWithIndexRDD.collect());
        // 输出结果：mapPartitionsWithIndexRDD~~~~~~~~~~~~~~~~~~~~~~[0|10, 0|21, 0|42, 1|30, 1|51, 1|62, 1|73]
    }

    private static void sortBy() {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("mapPartitionsWithIndex");
        sparkConf.setMaster("local");
        JavaSparkContext context = new JavaSparkContext(sparkConf);
        List<Integer> data = Arrays.asList(5, 1, 1, 4, 4, 2, 2);
        JavaRDD<Integer> javaRDD = context.parallelize(data, 3);
        final Random random = new Random(100);
        //对RDD进行转换，每个元素有两部分组成
        JavaRDD<String> javaRDD1 = javaRDD.map(new Function<Integer, String>() {
            @Override
            public String call(Integer v1) throws Exception {
                return v1.toString() + "_" + random.nextInt(100);
            }
        });
        System.out.println(javaRDD1.collect());
        //按RDD中每个元素的第二部分进行排序
        JavaRDD<String> resultRDD = javaRDD1.sortBy(new Function<String, Object>() {
            @Override
            public Object call(String v1) throws Exception {
                return v1.split("_")[1];
            }
        },false,3);
        System.out.println("result--------------" + resultRDD.collect());
        //输出结果 result--------------[2_74, 1_50, 4_50, 2_50, 5_15, 1_15, 4_15]
    }


    private static void reduce() {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("reduce");
        sparkConf.setMaster("local");

        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        List<Integer> data = Arrays.asList(5, 1, 1, 4, 4, 2, 2);

        JavaRDD<Integer> javaRDD = javaSparkContext.parallelize(data,3);

        Integer reduceRDD = javaRDD.reduce(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + reduceRDD);
        //输出结果 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~19
    }
}
