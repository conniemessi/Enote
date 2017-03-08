package com.dscientia.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author zhy http://blog.csdn.net/lmj623565791/article/details/39761281
 */
public class ClipImageBorderView extends View {
	public static final String TAG = "ClipImageBorderView";
	/**
	 * 屏幕宽度
	 */
	private int screenWidth;
	/**
	 * 屏幕高度
	 */
	private int screenHeight;
	/**
	 * 水平方向与View的边距
	 */
	private int mHorizontalPadding;
	/**
	 * 绘制的内圆的半径
	 */
	private int innerRadius;
	/**
	 * 绘制的外环宽度
	 */
	private int ringWidth;

	private Paint mPaint;

	public ClipImageBorderView(Context context) {
		this(context, null);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

/*		mBorderWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
						.getDisplayMetrics());*/
		mPaint = new Paint();
		innerRadius  = dip2px(context, 150);
		ringWidth = 300;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 计算圆形区域的半径

		// 绘制内边框
		mPaint.setStyle(Style.STROKE);
		mPaint.setColor(Color.WHITE);
		mPaint.setStrokeWidth(2);
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, innerRadius, mPaint);

		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(ringWidth);
		mPaint.setAlpha(180);
		mPaint.setAntiAlias(true);
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, innerRadius + 1
				+ ringWidth / 2, mPaint);
	}

	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;
	}
	
	public void setScreenWidth(int ScreenWidth) {
		this.screenWidth = ScreenWidth;
	}
	
	public void setScreenHeight(int ScreenHeight) {
		this.screenHeight = ScreenHeight;
	}
	
	/**
	 *  根据手机的分辨率从 dp 的单位 转成为 px(像素)  
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

}
