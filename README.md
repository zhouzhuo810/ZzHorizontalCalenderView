# ZzHorizontalCalenderView
A horizontal and scrollable CalenderView.

Github地址：[https://github.com/zhouzhuo810/ZzHorizontalCalenderView](https://github.com/zhouzhuo810/ZzHorizontalCalenderView)


Gradle:

```
compile 'me.zhouzhuo.zzhorizontalcalenderview:zz-horizontal-calenderview:1.0.1'
```

Maven:

```xml
<dependency>
  <groupId>me.zhouzhuo.zzhorizontalcalenderview</groupId>
  <artifactId>zz-horizontal-calenderview</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```

##功能简介：
1.支持年、月、日、星期的文字颜色配置；
2.支持日的选中颜色配置；
3.支持年月对话框选择；
4.支持年和月点击回调；
5.支持年月日星期点击回调；
6.支持选中自动滑动到中间；
7.支持今天小圆点标注；
8.支持今天小圆点颜色配置；
9.所有配置均支持xml属性配置或java代码动态设置；
10.支持选择年份时最小年份配置；

效果图如下：

![demo](https://github.com/zhouzhuo810/ZzHorizontalCalenderView/blob/master/zzhorizontalcalenderview.gif)


**属性**：


```xml
    <declare-styleable name="ZzHorizontalCalenderView">
        <attr name="zhc_show_pick_dialog" format="boolean" />
        <attr name="zhc_min_year" format="integer" />
        <attr name="zhc_unit_color" format="color|reference" />
        <attr name="zhc_selection_color" format="color|reference" />
        <attr name="zhc_press_shape_selector" format="reference" />
        <attr name="zhc_year_text_color" format="color|reference" />
        <attr name="zhc_month_text_color" format="color|reference" />
        <attr name="zhc_week_text_color" format="color|reference" />
        <attr name="zhc_day_selected_text_color" format="color|reference" />
        <attr name="zhc_day_unselected_text_color" format="color|reference" />
        <attr name="zhc_today_point_color" format="color|reference" />
    </declare-styleable>
```


##属性说明：

| 属性名称| 属性作用| 属性类型 |
| ----| ---- |---- |
| zhc_show_pick_dialog| 点击年或月是否弹出日期选择对话框 | boolean |
| zhc_min_year| 最小年份 | integer |
| zhc_unit_color| 年月单位的颜色 | color |
| zhc_selection_color| 日的选中颜色 |color|
| zhc_press_shape_selector| 年或月或日点击效果选择器 | selector |
| zhc_year_text_color| 年的颜色 |color|
| zhc_month_text_color| 月的颜色 |color|
| zhc_week_text_color| 星期的颜色|color|
| zhc_day_selected_text_color| 日选中时文字颜色|color|
| zhc_day_unselected_text_color| 日未选中时文字颜色 |color|
| zhc_today_point_color| 今天的下表点的颜色 |color|


##用法简介：

###① xml 代码

```xml
    <me.zhouzhuo.zzhorizontalcalenderview.ZzHorizontalCalenderView
        android:id="@+id/zz_horizontal_calender_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:zhc_day_selected_text_color="@android:color/white"/>

```

###②java 代码

```   
 final ZzHorizontalCalenderView view = (ZzHorizontalCalenderView) findViewById(R.id.zz_horizontal_calender_view);
    //日点击监听
    view.setOnDaySelectedListener(new ZzHorizontalCalenderView.OnDaySelectedListener() {
        @Override
        public void onSelected(boolean hasChanged, int year, int month, int day, int week) {
            tvResult.setText("日期是否有变化:" + hasChanged + ",\n\n日期:" + year + "-" + month + "-" + day + ",\n\n星期:" + week);
        }
    });

    //动态设置各种属性值：
    view.setShowPickDialog(true);
    view.setUnitColorResId(android.R.color.holo_green_dark);
    view.setDayTextColorSelectedResId(android.R.color.holo_blue_bright);
    view.setDayTextColorNormalResId(android.R.color.holo_red_dark);
    view.setDaySelectionColorResId(android.R.color.holo_orange_dark);
    view.setTodayPointColor(Color.YELLOW);
    view.setMonthTextColor(Color.RED);
    view.setYearTextColor(Color.BLUE);

    //年月点击监听
    view.setOnYearMonthClickListener(new ZzHorizontalCalenderView.OnYearMonthClickListener() {
        @Override
        public void onYearClick(int selectedYear, int selectedMonth) {
            tvResult.setText("点击了年:("+selectedYear+","+selectedMonth+")");
        }

        @Override
        public void onMonthClick(int selectedYear, int selectedMonth) {
            tvResult.setText("点击了月 :("+selectedYear+","+selectedMonth+")");
        }
    });
```
