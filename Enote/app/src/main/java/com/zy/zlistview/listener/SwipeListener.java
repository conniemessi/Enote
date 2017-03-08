package com.zy.zlistview.listener;

import com.zy.zlistview.view.ZSwipeItem;

public interface SwipeListener {

	public void onStartOpen(ZSwipeItem layout);

	public void onOpen(ZSwipeItem layout);

	public void onStartClose(ZSwipeItem layout);

	public void onClose(ZSwipeItem layout);

	public void onUpdate(ZSwipeItem layout, int leftOffset, int topOffset);

	public void onHandRelease(ZSwipeItem layout, float xvel, float yvel);

}
