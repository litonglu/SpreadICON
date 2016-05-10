package com.wangdao.our.spread_2.slide_widget.widget_m;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * 
 * @author baoyz
 * @date 2014-8-23
 * 
 */
public class SwipeMenuView extends LinearLayout implements OnClickListener {

	private com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenuListView mListView;
	private com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenuLayout mLayout;
	private com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenu mMenu;
	private OnSwipeItemClickListener onItemClickListener;
	private int position;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public SwipeMenuView(com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenu menu, com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenuListView listView) {
		super(menu.getContext());
		mListView = listView;
		mMenu = menu;
		List<com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenuItem> items = menu.getMenuItems();
		int id = 0;
		for (com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenuItem item : items) {
			addItem(item, id++);
		}
	}

	private void addItem(com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenuItem item, int id) {
		LayoutParams params = new LayoutParams(item.getWidth(),
				LayoutParams.MATCH_PARENT);
		LinearLayout parent = new LinearLayout(getContext());
		parent.setId(id);
		parent.setGravity(Gravity.CENTER);
		parent.setOrientation(LinearLayout.VERTICAL);
		parent.setLayoutParams(params);
		parent.setBackgroundDrawable(item.getBackground());
		parent.setOnClickListener(this);
		addView(parent);

		if (item.getIcon() != null) {
			parent.addView(createIcon(item));
		}
		if (!TextUtils.isEmpty(item.getTitle())) {
			parent.addView(createTitle(item));
		}

	}

	private ImageView createIcon(com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenuItem item) {
		ImageView iv = new ImageView(getContext());
		iv.setImageDrawable(item.getIcon());
		return iv;
	}

	private TextView createTitle(com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenuItem item) {
		TextView tv = new TextView(getContext());
		tv.setText(item.getTitle());
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(item.getTitleSize());
		tv.setTextColor(item.getTitleColor());
		return tv;
	}

	@Override
	public void onClick(View v) {
		if (onItemClickListener != null && mLayout.isOpen()) {
			onItemClickListener.onItemClick(this, mMenu, v.getId());
		}
	}

	public OnSwipeItemClickListener getOnSwipeItemClickListener() {
		return onItemClickListener;
	}

	public void setOnSwipeItemClickListener(OnSwipeItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void setLayout(com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenuLayout mLayout) {
		this.mLayout = mLayout;
	}

	public static interface OnSwipeItemClickListener {
		void onItemClick(SwipeMenuView view, com.wangdao.our.spread_2.slide_widget.widget_m.SwipeMenu menu, int index);
	}
}
