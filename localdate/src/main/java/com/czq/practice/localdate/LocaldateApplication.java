package com.czq.practice.localdate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

@SpringBootApplication
public class LocaldateApplication {


    /***
     * ┌───┐   ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
     * │Esc│   │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│  ┌┐    ┌┐    ┌┐
     * └───┘   └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘  └┘    └┘    └┘
     * ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐ ┌───┬───┬───┐ ┌───┬───┬───┬───┐
     * │~ `│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp │ │Ins│Hom│PUp│ │N L│ / │ * │ - │
     * ├───┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤ ├───┼───┼───┤ ├───┼───┼───┼───┤
     * │ Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ │ │Del│End│PDn│ │ 7 │ 8 │ 9 │   │
     * ├─────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤ └───┴───┴───┘ ├───┼───┼───┤ + │
     * │ Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │               │ 4 │ 5 │ 6 │   │
     * ├──────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤     ┌───┐     ├───┼───┼───┼───┤
     * │ Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │     │ ↑ │     │ 1 │ 2 │ 3 │   │
     * ├─────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤ ┌───┼───┼───┐ ├───┴───┼───┤ E││
     * │ Ctrl│    │Alt │         Space         │ Alt│    │    │Ctrl│ │ ← │ ↓ │ → │ │   0   │ . │←─┘│
     * └─────┴────┴────┴───────────────────────┴────┴────┴────┴────┘ └───┴───┴───┘ └───────┴───┴───┘
     */


    public static void main(String[] args) {
        SpringApplication.run(LocaldateApplication.class, args);

        System.out.println("Java8提供了一系列的关于日期和时间API的更新，在JDK中引入了基于JSR310：Date and Time API规范的Joda Time API。新的API提供了大量关于日期和时间的优秀的功能");

//        init();
//        operate();
        format();
    }

    /**
     * 初始化
     */
    private static void init() {
        LocalDate d = LocalDate.now();
        LocalDate d2 = LocalDate.of(2019, 2, 23);

        LocalTime t = LocalTime.now();
        LocalTime t2 = LocalTime.of(12, 3, 333);

        LocalDateTime dt = LocalDateTime.now();
        LocalDateTime dt2 = LocalDateTime.of(d, t);
    }

    /**
     * 加减操作
     */
    private static void operate() {
        //从代码中可以看到，这些plus()和minus()方法，是不会改变原date和time的实例的，返回的是新的实例
        LocalDate d = LocalDate.now();
        LocalDate d2 = d.plusDays(1);
        LocalDate d3 = d.minusDays(2);
        System.out.println("d = " + d);
        System.out.println("d2 = " + d2);
        System.out.println("d3 = " + d3);
//        d.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 格式化
     */
    private static void format() {
        LocalDateTime ldt = LocalDateTime.now();

        System.out.println(ldt.format(DateTimeFormatter.ISO_DATE));
        System.out.println(ldt.format(DateTimeFormatter.ISO_DATE_TIME));
        System.out.println(ldt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println(ldt.format(DateTimeFormatter.BASIC_ISO_DATE));

        System.out.println(ldt.format(DateTimeFormatter.ofPattern("d-M-y")));

        LocalDate ld = LocalDate.parse("2018-09-26");
        LocalDateTime ldt2 = LocalDateTime.parse("2018-09-26T22:24:33");
    }

}
