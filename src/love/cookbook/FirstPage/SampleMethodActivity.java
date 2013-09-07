package love.cookbook.FirstPage;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;

public class SampleMethodActivity extends Activity {
	Button b;

    public void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_method);
        
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.my_layout);

        b = new Button(this);
        b.setText("This is a sample text to check the width and height of a button");
        b.setX(50);
        b.setY(50);
        b.setWidth(500);
        rl.addView(b);
        
        System.out.println("The y is: "+b.getY());
        System.out.println("The height is "+ b.getBottom());
      //  b.setLayoutParams(new RelativeLayout.LayoutParams(50, 50));
       // b.layout(100, 50, 100, 50);
       // RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(null);
        //params.leftMargin = 50;
        //params.topMargin = 60;
        //rl.addView(b);
    }

}
