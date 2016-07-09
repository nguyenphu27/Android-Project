package com.ducphu.note.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

public class DrawLineEdit extends EditText{

	private Rect mRect;
	private Paint mPaint;
	
	public DrawLineEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mRect = new Rect();
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setColor(Color.YELLOW);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub		
		Rect r = mRect;
		Paint paint = mPaint;
		
		int height = getHeight();
		int line_height = getLineHeight();
		
		int count = height / line_height;
		if(getLineCount() > count){
			count = getLineCount();
		}

		int baseline = getLineBounds(0, r);
		
		for(int i = 0;i<count;i++){
			canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
			baseline += getLineHeight();
		}
		super.onDraw(canvas);
	}

}
