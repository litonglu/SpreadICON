package com.wangdao.our.spread_2.slide_widget.widget_m;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

import com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenuListView;
import com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenuView;

/**
 * 
 * @author baoyz
 * @date 2014-8-24
 * 
 */

public class SwipeMenuAdapter implements WrapperListAdapter, SwipeMenuView.OnSwipeItemClickListener {

    private ListAdapter mAdapter;
    private Context mContext;
    private SwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener;

    public SwipeMenuAdapter(Context context, ListAdapter adapter) {
        mAdapter = adapter;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mAdapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return mAdapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return mAdapter.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenuLayout layout = null;
        if (convertView == null) {
            View contentView = mAdapter.getView(position, convertView, parent);
            com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenu menu = new com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenu(mContext);
            menu.setViewType(getItemViewType(position));
            createMenu(menu);
            SwipeMenuView menuView = new SwipeMenuView(menu,
                    (SwipeMenuListView) parent);
            menuView.setOnSwipeItemClickListener(this);
            SwipeMenuListView listView = (SwipeMenuListView) parent;
            layout = new com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenuLayout(contentView, menuView,
                    listView.getCloseInterpolator(),
                    listView.getOpenInterpolator());
            layout.setPosition(position);
        } else {
            layout = (com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenuLayout) convertView;
            layout.closeMenu();
            layout.setPosition(position);
            View view = mAdapter.getView(position, layout.getContentView(),
                    parent);
        }
        if (mAdapter instanceof com.wangdao.our.spread_2.slide_widget.widget_m.BaseSwipListAdapter) {
            boolean swipEnable = (((com.wangdao.our.spread_2.slide_widget.widget_m.BaseSwipListAdapter) mAdapter).getSwipEnableByPosition(position));
            layout.setSwipEnable(swipEnable);
        }
        return layout;
    }

    public void createMenu(com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenu menu) {
        // Test Code
        com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenuItem item = new SwipeMenuItem(mContext);
        item.setTitle("Item 1");
        item.setBackground(new ColorDrawable(Color.GRAY));
        item.setWidth(300);
        menu.addMenuItem(item);

        item = new com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenuItem(mContext);
        item.setTitle("Item 2");
        item.setBackground(new ColorDrawable(Color.RED));
        item.setWidth(300);
        menu.addMenuItem(item);
    }

    @Override
    public void onItemClick(SwipeMenuView view, com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenu menu, int index) {
        if (onMenuItemClickListener != null) {
            onMenuItemClickListener.onMenuItemClick(view.getPosition(), menu,
                    index);
        }
    }

    public void setOnSwipeItemClickListener(
            SwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mAdapter.unregisterDataSetObserver(observer);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return mAdapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        return mAdapter.isEnabled(position);
    }

    @Override
    public boolean hasStableIds() {
        return mAdapter.hasStableIds();
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return mAdapter.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    @Override
    public ListAdapter getWrappedAdapter() {
        return mAdapter;
    }

}
