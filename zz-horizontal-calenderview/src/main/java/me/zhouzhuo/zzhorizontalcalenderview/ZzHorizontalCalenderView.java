package me.zhouzhuo.zzhorizontalcalenderview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.zhouzhuo.zzhorizontalcalenderview.adapter.DayAdapter;
import me.zhouzhuo.zzhorizontalcalenderview.bean.DateItem;
import me.zhouzhuo.zzhorizontalcalenderview.utils.DateUtil;

/**
 * Created by zz on 2016/9/14.
 */
public class ZzHorizontalCalenderView extends FrameLayout {

    private static final String TAG = "Zz";

    private View rootView;
    private RecyclerView rv;
    private LinearLayout llYear;
    private LinearLayout llMonth;
    private TextView tvYear;
    private TextView tvMonth;
    private TextView tvYearUnit;
    private TextView tvMonthUnit;

    private DayAdapter adapter;

    private int curYear;

    private int curMonth;

    private int curDay;

    private int curWeek;

    private int selectedYear;

    private int selectedMonth;

    private int selectedDay;

    private int selectedWeek;

    private int firstWeek;

    private int curMonthLastDay;

    private OnDaySelectedListener daySelectedListener;

    private OnYearMonthClickListener onYearMonthClickListener;

    private boolean showPickDialog;
    private int unitColor;
    private int yearTextColor;
    private int monthTextColor;

    private int dayTextColorSelected;
    private int dayTextColorNormal;
    private int daySelectionColor;
    private int weekTextColor;

    private int pressShapeSelectorId;
    private int todayPointColor;

    public ZzHorizontalCalenderView(Context context) {
        super(context);
        init(context, null);
    }

    public ZzHorizontalCalenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ZzHorizontalCalenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        initAttrs(context, attrs);

        rootView = LayoutInflater.from(context).inflate(R.layout.zz_horizontal_calender_view, null);

        rv = (RecyclerView) rootView.findViewById(R.id.rv);
        llYear = (LinearLayout) rootView.findViewById(R.id.ll_year);
        llMonth = (LinearLayout) rootView.findViewById(R.id.ll_month);
        tvYear = (TextView) rootView.findViewById(R.id.tv_year);
        tvMonth = (TextView) rootView.findViewById(R.id.tv_month);
        tvYearUnit = (TextView) rootView.findViewById(R.id.tv_year_unit);
        tvMonthUnit = (TextView) rootView.findViewById(R.id.tv_month_unit);

        initData(context);

        initEvent();

        addView(rootView);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZzHorizontalCalenderView);
        showPickDialog = a.getBoolean(R.styleable.ZzHorizontalCalenderView_zhc_show_pick_dialog, true);
        unitColor = a.getColor(R.styleable.ZzHorizontalCalenderView_zhc_unit_color, 0xff3498DB);
        yearTextColor = a.getColor(R.styleable.ZzHorizontalCalenderView_zhc_year_text_color, 0xff000000);
        monthTextColor = a.getColor(R.styleable.ZzHorizontalCalenderView_zhc_month_text_color, 0xff000000);
        dayTextColorSelected = a.getColor(R.styleable.ZzHorizontalCalenderView_zhc_day_selected_text_color, 0xffffffff);
        dayTextColorNormal = a.getColor(R.styleable.ZzHorizontalCalenderView_zhc_day_unselected_text_color, 0xff000000);
        weekTextColor = a.getColor(R.styleable.ZzHorizontalCalenderView_zhc_week_text_color, 0xff9e9e9e);
        daySelectionColor = a.getColor(R.styleable.ZzHorizontalCalenderView_zhc_selection_color, 0xff3498DB);
        pressShapeSelectorId = a.getResourceId(R.styleable.ZzHorizontalCalenderView_zhc_press_shape_selector, R.drawable.month_year_bg_selector);
        todayPointColor = a.getColor(R.styleable.ZzHorizontalCalenderView_zhc_today_point_color, 0xffed9f24);
        a.recycle();
    }

    private void initData(Context context) {
        curYear = DateUtil.getCurrentYear();
        curMonth = DateUtil.getCurrentMonth();
        curDay = DateUtil.getCurrentDay();
        curWeek = DateUtil.getCurrentWeek();
        selectedYear = curYear;
        selectedMonth = curMonth;
        selectedDay = curDay;
        selectedWeek = curWeek;

        tvYear.setText(curYear + "");
        tvMonth.setText(curMonth + "");

        List<DateItem> items = getItems();
        adapter = new DayAdapter(context, items, dayTextColorSelected, dayTextColorNormal, daySelectionColor, weekTextColor, pressShapeSelectorId, todayPointColor);
        adapter.setItemClick(new DayAdapter.OnItemClick() {
            @Override
            public void onItemClick(int year, int month, int day, int week) {
                setSelectedDate(year, month, day, week);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(manager);
        rv.setHasFixedSize(true);
        rv.offsetChildrenHorizontal(3);
        rv.setAdapter(adapter);
        rv.scrollToPosition(curDay - 4);
    }

    private List<DateItem> getItems() {
        List<DateItem> items = new ArrayList<DateItem>();
        firstWeek = getFirstDayOfWeek(selectedYear, selectedMonth);
        int tempWeek = firstWeek;

        int lastMonthDay = getLastDayOfMonth(selectedYear, selectedMonth) - firstWeek;
        curMonthLastDay = DateUtil.calDayOfMonth(selectedYear, selectedMonth);
        int nextMonthDay = 1;

        DateItem item = null;
        for (int i = 0; i < 42; i++) {
            item = new DateItem();
            if (i < firstWeek) {
                lastMonthDay++;
                item.setIsLastMonth(true);
                item.setDay(lastMonthDay);
            } else {
                item.setIsLastMonth(false);
                int d = i - firstWeek + 1;
                if (d <= curMonthLastDay) {
                    item.setIsNextMonth(false);
                    if (d == selectedDay) {
                        item.setSelected(true);
                    }
                    item.setDay(i - firstWeek + 1);
                    item.setWeek(tempWeek % 7);
                    item.setYear(selectedYear);
                    item.setMonth(selectedMonth);
                    if (selectedYear == curYear && selectedMonth == curMonth && d == curDay) {
                        item.setCurrent(true);
                    }
                    items.add(item);
                    tempWeek++;
                } else {
                    item.setIsNextMonth(true);
                    item.setDay(nextMonthDay);
                    nextMonthDay++;
                }
            }
        }
        return items;
    }

    public void setUnitColorResId(int colorResId) {
        this.unitColor = getContext().getResources().getColor(colorResId);
        setUnitColor(this.unitColor);
    }

    public void setUnitColor(int colorValue) {
        if (tvYearUnit != null)
            tvYearUnit.setTextColor(colorValue);
        if (tvMonthUnit != null)
            tvMonthUnit.setTextColor(colorValue);
    }

    public void setYearTextColorResId(int yearTextColorResId) {
        this.yearTextColor = getContext().getResources().getColor(yearTextColorResId);
        setYearTextColor(yearTextColor);
    }

    public void setTodayPointColorResId(int todayPointColorResId) {
        this.todayPointColor = getContext().getResources().getColor(todayPointColorResId);
        setTodayPointColor(todayPointColor);
    }
    public void setTodayPointColor(int todayPointColor) {
        adapter.setTodayPointColor(todayPointColor);
    }

    public void setYearTextColor(int yearTextColor) {
        if (tvYear != null)
            tvYear.setTextColor(yearTextColor);
    }


    public void setMonthTextColorResId(int monthTextColorResId) {
        this.monthTextColor = getContext().getResources().getColor(monthTextColorResId);
        setMonthTextColor(monthTextColor);
    }

    public void setMonthTextColor(int monthTextColor) {
        if (tvMonth != null)
            tvMonth.setTextColor(monthTextColor);
    }

    public void setWeekTextColorResId(int weekTextColorResId) {
        this.weekTextColor = getContext().getResources().getColor(weekTextColorResId);
        setWeekTextColor(weekTextColor);
    }

    public void setWeekTextColor(int weekTextColor) {
        adapter.setWeekTextColor(weekTextColor);
    }

    public void setDaySelectionColorResId(int daySelectionColorResId) {
        this.daySelectionColor = getContext().getResources().getColor(daySelectionColorResId);
        setDaySelectionColor(daySelectionColor);
    }

    public void setDayTextColorSelectedResId(int dayTextColorSelectedRedId) {
        this.dayTextColorSelected = getContext().getResources().getColor(dayTextColorSelectedRedId);
        setDayTextColorSelected(dayTextColorSelected);
    }

    public void setDayTextColorSelected(int dayTextColorSelected) {
        adapter.setDayTextColorSelected(dayTextColorSelected);
    }

    public void setDayTextColorNormalResId(int dayTextColorNormalRedId) {
        this.dayTextColorNormal = getContext().getResources().getColor(dayTextColorNormalRedId);
        setDayTextColorNormal(dayTextColorNormal);
    }

    public void setDayTextColorNormal(int dayTextColorNormal) {
        adapter.setDayTextColorNormal(dayTextColorNormal);
    }

    public void setDaySelectionColor(int daySelectionColor) {
        adapter.setDaySelectionColor(daySelectionColor);
    }

    private void initEvent() {
        llYear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showPickDialog) {
                    LinearLayout ll = new LinearLayout(getContext());

                    final NumberPicker yearPicker = new NumberPicker(getContext());
                    yearPicker.setMinValue(2012);
                    yearPicker.setMaxValue(DateUtil.getCurrentYear());
                    yearPicker.setValue(selectedYear);

                    final NumberPicker monthPicker = new NumberPicker(getContext());
                    monthPicker.setMinValue(1);
                    monthPicker.setMaxValue(12);
                    monthPicker.setValue(selectedMonth);

                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    ll.setGravity(Gravity.CENTER);
                    ll.addView(yearPicker);
                    ll.addView(monthPicker);

                    new AlertDialog.Builder(getContext()).setView(ll).setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int year = yearPicker.getValue();
                            int month = monthPicker.getValue();
                            setSelectedYearAndMonth(year, month);
                        }
                    }).setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).show();
                }
                if (onYearMonthClickListener != null) {
                    onYearMonthClickListener.onYearClick(selectedYear, selectedMonth);
                }
            }
        });

        llMonth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showPickDialog) {
                    LinearLayout ll = new LinearLayout(getContext());

                    final NumberPicker yearPicker = new NumberPicker(getContext());
                    yearPicker.setMinValue(2012);
                    yearPicker.setMaxValue(DateUtil.getCurrentYear());
                    yearPicker.setValue(selectedYear);
                    final NumberPicker monthPicker = new NumberPicker(getContext());
                    monthPicker.setMinValue(1);
                    monthPicker.setMaxValue(12);
                    monthPicker.setValue(selectedMonth);

                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    ll.setGravity(Gravity.CENTER);
                    ll.addView(yearPicker);
                    ll.addView(monthPicker);

                    monthPicker.requestFocus();

                    new AlertDialog.Builder(getContext()).setView(ll).setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int year = yearPicker.getValue();
                            int month = monthPicker.getValue();
                            setSelectedYearAndMonth(year, month);
                        }
                    }).setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).show();
                }
                if (onYearMonthClickListener != null) {
                    onYearMonthClickListener.onMonthClick(selectedYear, selectedMonth);
                }
            }
        });
    }

    public boolean isShowPickDialog() {
        return showPickDialog;
    }

    public void setShowPickDialog(boolean showPickDialog) {
        this.showPickDialog = showPickDialog;
    }

    public int getFirstDayOfWeek(int y, int m) {
        return DateUtil.calWeek(y, m, 1);
    }

    public int getLastDayOfMonth(int y, int m) {
        //TODO FIXME
        if (m == 1) {
            return DateUtil.calDayOfMonth(y - 1, 12);
        } else {
            return DateUtil.calDayOfMonth(y, m - 1);
        }
    }

    public interface OnYearMonthClickListener {
        void onYearClick(int selectedYear, int selectedMonth);

        void onMonthClick(int selectedYear, int selectedMonth);
    }

    public interface OnDaySelectedListener {
        void onSelected(boolean hasChanged, int year, int month, int day, int week);
    }

    public String setSelectedDate() {
        return String.format(selectedYear + "-" + selectedMonth + "-" + selectedDay);
    }

    public int getSelectedYear() {
        return selectedYear;
    }

    public int getSelectedMonth() {
        return selectedMonth;
    }

    public int getSelectedDay() {
        return selectedDay;
    }

    public int getSelectedWeek() {
        return selectedWeek;
    }

    public void setSelectedDate(int year, int month, int day) {
        int lastYear = selectedYear;
        int lastMonth = selectedMonth;
        int lastDay = selectedDay;
        this.selectedYear = year;
        this.selectedMonth = month;
        this.selectedDay = day;
        this.selectedWeek = DateUtil.calWeek(selectedYear, selectedMonth, selectedDay);
        int totalDayOfMonth = DateUtil.calDayOfMonth(selectedYear, selectedMonth);
        if (daySelectedListener != null && totalDayOfMonth >= selectedDay) {
            daySelectedListener.onSelected(lastYear != year || lastMonth != month || lastDay != day,
                    year, month, day, selectedWeek);
        }
        tvYear.setText(year + "");
        tvMonth.setText(month + "");
        adapter.setDatas(getItems());
        adapter.notifyDataSetChanged();
        rv.scrollToPosition(selectedDay - 4);
        rv.smoothScrollToPosition(selectedDay + 2);
    }

    private void setSelectedDate(int year, int month, int day, int week) {
        int lastYear = selectedYear;
        int lastMonth = selectedMonth;
        int lastDay = selectedDay;
        this.selectedYear = year;
        this.selectedMonth = month;
        this.selectedDay = day;
        this.selectedWeek = week;
        tvYear.setText(year + "");
        tvMonth.setText(month + "");
        if (daySelectedListener != null) {
            daySelectedListener.onSelected(lastYear != year || lastMonth != month || lastDay != day,
                    year, month, day, selectedWeek);
        }
        adapter.setDatas(getItems());
        adapter.notifyDataSetChanged();
        rv.scrollToPosition(selectedDay - 4);
        rv.smoothScrollToPosition(selectedDay + 2);
    }

    public void setPressShapeSelectorId(int pressShapeSelectorId) {
        this.pressShapeSelectorId = pressShapeSelectorId;
        if (llYear != null)
            llYear.setBackgroundResource(pressShapeSelectorId);
        if (llMonth != null) {
            llMonth.setBackgroundResource(pressShapeSelectorId);
        }
        adapter.setPressShapeSelectorId(pressShapeSelectorId);
    }

    public void setSelectedYear(int year) {
        int lastYear = selectedYear;
        this.selectedYear = year;
        this.selectedWeek = DateUtil.calWeek(year, selectedMonth, selectedDay);
        tvYear.setText(year + "");
        int totalDayOfMonth = DateUtil.calDayOfMonth(selectedYear, selectedMonth);
        if (daySelectedListener != null) {
            daySelectedListener.onSelected(lastYear != year, year, selectedMonth, selectedDay, selectedWeek);
        }
        adapter.setDatas(getItems());
        adapter.notifyDataSetChanged();
        rv.scrollToPosition(selectedDay - 4);
        rv.smoothScrollToPosition(selectedDay + 2);
    }

    public void setSelectedYearAndMonth(int year, int month) {
        int lastYear = selectedYear;
        int lastMonth = selectedMonth;
        this.selectedYear = year;
        this.selectedMonth = month;
        this.selectedWeek = DateUtil.calWeek(selectedYear, selectedMonth, selectedDay);
        int totalDayOfMonth = DateUtil.calDayOfMonth(selectedYear, selectedMonth);
        if (daySelectedListener != null && totalDayOfMonth >= selectedDay) {
            daySelectedListener.onSelected(lastYear != year || lastMonth != month,
                    year, month, selectedDay, selectedWeek);
        }
        tvYear.setText(year + "");
        tvMonth.setText(month + "");
        adapter.setDatas(getItems());
        adapter.notifyDataSetChanged();
        rv.scrollToPosition(selectedDay - 4);
        rv.smoothScrollToPosition(selectedDay + 2);
    }

    public void setSelectedMonth(int month) {
        int lastMonth = this.selectedMonth;
        this.selectedMonth = month;
        tvMonth.setText(month + "");
        this.selectedWeek = DateUtil.calWeek(selectedYear, month, selectedDay);
        int totalDayOfMonth = DateUtil.calDayOfMonth(selectedYear, selectedMonth);

        if (daySelectedListener != null && totalDayOfMonth >= selectedDay) {
            daySelectedListener.onSelected(lastMonth != month, selectedYear, month, selectedDay, selectedWeek);
        }
        adapter.setDatas(getItems());
        adapter.notifyDataSetChanged();
        rv.scrollToPosition(selectedDay - 4);
        rv.smoothScrollToPosition(selectedDay + 2);
    }

    public String getSelectedDate() {
        return selectedYear + "-" + selectedMonth + "-" + selectedDay;
    }

    public String getSelectedDate(String gapStr) {
        return selectedYear + gapStr + selectedMonth + gapStr + selectedDay;
    }

    public void setOnDaySelectedListener(OnDaySelectedListener daySelectedListener) {
        this.daySelectedListener = daySelectedListener;
    }

    public void setOnYearMonthClickListener(OnYearMonthClickListener onYearMonthClickListener) {
        this.onYearMonthClickListener = onYearMonthClickListener;
    }
}
