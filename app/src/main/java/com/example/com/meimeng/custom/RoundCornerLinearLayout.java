package com.example.com.meimeng.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class RoundCornerLinearLayout extends LinearLayout {

	private Path clipPath;
	private float rx=10.0f;
	private float ry=10.0f;
	public RoundCornerLinearLayout(Context context) {
		super(context);
		clipPath = new Path();
	}

	public RoundCornerLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		clipPath = new Path();
	}

	public RoundCornerLinearLayout(Context context, AttributeSet attrs,
								   int defStyle) {
		super(context, attrs, defStyle);
		clipPath = new Path();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		int w = this.getWidth();
		int h = this.getHeight();
		clipPath.addRoundRect(new RectF(0, 0, w, h), rx, ry,
				Path.Direction.CW);
		
		canvas.clipPath(clipPath);
		super.onDraw(canvas);
	}
	
	public void setAngie(float rx,float ry) {
		this.rx=rx;
		this.ry=ry;
		postInvalidate();
	}
}