package love.cookbook.FirstPage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawView extends View {
	Paint paint = new Paint();
	int startY,stopY,startX;

    public DrawView(Context context,int startY,int stopY,int startX) {
        super(context);
        this.startY=startY;
        this.stopY=stopY;
        this.startX=startX;
        System.out.println("Inside cons");
    }

    @Override
    public void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	paint.setColor(Color.WHITE);
    	paint.setStrokeWidth(3);
    	System.out.println(startX+" "+startY+" "+stopY);
        canvas.drawLine(startX, startY, startX, stopY, paint);
    }

}
