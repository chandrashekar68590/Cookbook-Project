package love.cookbook.FirstPage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawView extends View {
	Paint paint = new Paint();
	int x,y;

    public DrawView(Context context,int x,int y) {
        super(context);
        this.x=x;
        this.y=y;
    }

    @Override
    public void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	paint.setColor(Color.WHITE);
    	paint.setStrokeWidth(3);
    	System.out.println("X: "+x);
        canvas.drawLine(x, y+200, x, y+40, paint);
    }

}
