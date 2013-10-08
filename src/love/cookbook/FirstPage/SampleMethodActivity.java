package love.cookbook.FirstPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class SampleMethodActivity extends Activity {
	Button b,b1;
	public int width,height,bottom;
	 LinearLayout ll;
	 MethodSampleAdapter adapter;
	 Intent intent;
	 public int noOfSteps;

    public void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_method);
                
        final ListView list = (ListView)findViewById(R.id.listView1);
        
        final Intent intent = getIntent();
        ARRAY.preparationSteps = intent.getStringArrayExtra("STEPS");
        ARRAY.timeToPrepare = intent.getStringArrayExtra("TIMETOPREPARE"); 
        ARRAY.arrowValue = intent.getStringArrayExtra("ARROW");
        noOfSteps = intent.getIntExtra("NUMBEROFSTEPS", 0);
        
        
        adapter=new MethodSampleAdapter(this,ARRAY.preparationSteps,ARRAY.timeToPrepare,ARRAY.arrowValue,noOfSteps);
        list.setAdapter(adapter);
        
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				System.out.println("Item Clicked");
				
			}
		});
        
        /*
        ll = (LinearLayout) findViewById(R.id.my_linear_layout);
        
       
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        	params.setMargins(0, 12, 0, 40);

        b = new Button(this);
        b.setText("This is a sample text");
       // b.setX(50);
        //b.setY(50);
        //b.setWidth(500);
        b.setLayoutParams(params);
        b.setGravity(Gravity.CENTER);
        ll.addView(b);
   	
              
        b1 = new Button(this);
        b1.setText("This is a sample text to chck the width and height of a button1 and need to check how long it gets stretched and to check the width");
        b1.setLayoutParams(params);
        b1.setGravity(Gravity.CENTER);
        ll.addView(b1);
        
   
        
        //System.out.println("The y is: "+b.getY());
        //System.out.println("in oncreate Width: "+b.getDrawingCache().getWidth()+" height: "+b.getDrawingCache().getHeight());

      //  b.setLayoutParams(new RelativeLayout.LayoutParams(50, 50));
       // b.layout(100, 50, 100, 50);
       // RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(null);
        //params.leftMargin = 50;
        //params.topMargin = 60;
        //rl.addView(b);
         * 
         * 
         */
    }

    /*
	@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
      
        
        // Call here getWidth() and getHeight()
        width = b.getWidth();
        height = b.getHeight();
        bottom = b.getBottom();
        
        DrawView dv = new DrawView(this,width/2,bottom);
        ll.addView(dv);
        System.out.println("Width: "+width+" height: "+height);
        
     }
     */
}
