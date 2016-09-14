package me.zhouzhuo.zzhorizontalcalenderview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.zhouzhuo.zzhorizontalcalenderview.R;
import me.zhouzhuo.zzhorizontalcalenderview.bean.DateItem;
import me.zhouzhuo.zzhorizontalcalenderview.utils.ViewUtil;


/**
 * Created by user999 on 2016/6/7.
 */
public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

    private List<DateItem> datas;
    private Context context;
    private int dayTextColorSelected;
    private int dayTextColorNormal;
    private int daySelectionColor;
    private int weekTextColor;
    private OnItemClick itemClick;
    private int pressShapeSelectorId;
    private int todayPointColor;

    public DayAdapter(Context context, List<DateItem> datas, int dayTextColorSelected, int dayTextColorNormal, int daySelectionColor, int weekTextColor, int pressShapeSelectorId, int todayPointColor) {
        this.datas = datas;
        this.context = context;
        this.dayTextColorSelected = dayTextColorSelected;
        this.dayTextColorNormal = dayTextColorNormal;
        this.daySelectionColor = daySelectionColor;
        this.weekTextColor = weekTextColor;
        this.pressShapeSelectorId = pressShapeSelectorId;
        this.todayPointColor = todayPointColor;
    }

    public void setDatas(List<DateItem> datas) {
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.list_item_day, null);
        ViewUtil.scaleContentView((ViewGroup) view.findViewById(R.id.list_item_root_layout));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        int week = datas.get(position).getWeek();
        switch (week) {
            case 0:
                holder.tvWeek.setText("SUN");
                break;
            case 1:
                holder.tvWeek.setText("MON");
                break;
            case 2:
                holder.tvWeek.setText("TUE");
                break;
            case 3:
                holder.tvWeek.setText("WED");
                break;
            case 4:
                holder.tvWeek.setText("THU");
                break;
            case 5:
                holder.tvWeek.setText("FRI");
                break;
            case 6:
                holder.tvWeek.setText("SAT");
                break;
        }
        GradientDrawable p = (GradientDrawable) holder.tvDay.getBackground();
        if (p != null) {
            if (datas.get(position).isSelected()) {
                p.setColor(daySelectionColor);
            } else {
                p.setColor(0x00000000);
            }
        }
        GradientDrawable t = (GradientDrawable) holder.tvPoint.getBackground();
        if (t != null) {
            if (datas.get(position).isCurrent()) {
                t.setColor(todayPointColor);
            } else {
                t.setColor(0x00000000);
            }
        }

        holder.rootView.setBackgroundResource(pressShapeSelectorId);
        holder.tvWeek.setTextColor(weekTextColor);
        holder.tvDay.setText(datas.get(position).getDay() + "");
        holder.tvDay.setTextColor(datas.get(position).isSelected() ? dayTextColorSelected
                : dayTextColorNormal);
//        holder.tvDay.setBackgroundResource(datas.get(position).isSelected() ? R.drawable.day_selected_shape : R.drawable.day_selected_shape_normal);
        holder.tvPoint.setVisibility(datas.get(position).isCurrent() ? View.VISIBLE : View.INVISIBLE);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick != null) {
                    itemClick.onItemClick(datas.get(position).getYear(), datas.get(position).getMonth(), datas.get(position).getDay(), datas.get(position).getWeek());
                }
            }
        });
    }

    public void setPressShapeSelectorId(int pressShapeSelectorId) {
        this.pressShapeSelectorId = pressShapeSelectorId;
        notifyDataSetChanged();
    }

    public void setDayTextColorSelected(int dayTextColorSelected) {
        this.dayTextColorSelected = dayTextColorSelected;
        notifyDataSetChanged();
    }

    public void setDayTextColorNormal(int dayTextColorNormal) {
        this.dayTextColorNormal = dayTextColorNormal;
        notifyDataSetChanged();
    }

    public void setDaySelectionColor(int daySelectionColor) {
        this.daySelectionColor = daySelectionColor;
        notifyDataSetChanged();
    }

    public void setWeekTextColor(int weekTextColor) {
        this.weekTextColor = weekTextColor;
        notifyDataSetChanged();
    }

    public void setTodayPointColor(int todayPointColor) {
        this.todayPointColor = todayPointColor;
        notifyDataSetChanged();
    }

    public void setItemClick(OnItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public interface OnItemClick {
        void onItemClick(int year, int month, int day, int week);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public TextView tvWeek;
        public TextView tvDay;
        public View tvPoint;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.list_item_root_layout);
            tvWeek = (TextView) itemView.findViewById(R.id.item_week);
            tvDay = (TextView) itemView.findViewById(R.id.item_day);
            tvPoint = itemView.findViewById(R.id.item_point);
        }
    }
}
