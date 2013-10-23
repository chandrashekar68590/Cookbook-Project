package love.cookbook.FirstPage;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.*;
import android.graphics.*;
import android.media.MediaPlayer;
import android.os.*;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class SampleMethodActivity extends SherlockActivity {
	Button b,b1,b2;
	Button button[],timerButton[],clickButton[],clickButton1[],ingredientButton;
	TextView textView[],lineTextView[],arrowTextView[];
	ImageView image[],timerImage[];
	public int width,height,bottom;
	 RelativeLayout ll;
	 Intent intent;
	 public int noOfSteps,i;
	 public int deviceHeight,deviceWidth;
	 public SparseBooleanArray checked = new SparseBooleanArray();
	 RelativeLayout.LayoutParams r6[],r7[],r8[],r9[];
	 
	 boolean timerClicked[];
	 public SparseBooleanArray checked1 = new SparseBooleanArray();
	 View view;
	 boolean flag = false;
	 
    public static LayoutInflater inflater=null;     
    public CountDownTimer myTimer[] = new CountDownTimer[30];
    public Toast t[];
    public final int count =0;
    public AlertDialog.Builder alert;
    MediaPlayer player;
    String vibratorService;
    Vibrator vibrator;

    public void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_method2);
        
        final Intent intent = getIntent();
        ARRAY.preparationSteps = intent.getStringArrayExtra("STEPS");
        ARRAY.timeToPrepare = intent.getStringArrayExtra("TIMETOPREPARE"); 
        ARRAY.arrowValue = intent.getStringArrayExtra("ARROW");
        noOfSteps = intent.getIntExtra("NUMBEROFSTEPS", 0);

        alert = new AlertDialog.Builder(this);
        Context context = getApplicationContext();
        
        //To get Device Height and width at runtime.
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        deviceHeight = displaymetrics.heightPixels;
        deviceWidth = displaymetrics.widthPixels;
        
        
        ll = (RelativeLayout) findViewById(R.id.my_linear_layout);
        
        RelativeLayout.LayoutParams r1[] = new RelativeLayout.LayoutParams[noOfSteps];
        RelativeLayout.LayoutParams r2[] = new RelativeLayout.LayoutParams[noOfSteps];
        RelativeLayout.LayoutParams r3[] = new RelativeLayout.LayoutParams[noOfSteps];
        RelativeLayout.LayoutParams r4[] = new RelativeLayout.LayoutParams[noOfSteps];
        RelativeLayout.LayoutParams r5 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        r6 = new RelativeLayout.LayoutParams[noOfSteps];
        r7 = new RelativeLayout.LayoutParams[noOfSteps];
        r8 = new RelativeLayout.LayoutParams[noOfSteps];
        RelativeLayout.LayoutParams r9 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams r10 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        
        button = new Button[noOfSteps];
        image = new ImageView[noOfSteps];
        timerButton = new Button[noOfSteps];
        clickButton = new Button[noOfSteps];
        clickButton1 = new Button[noOfSteps];
        textView = new TextView[noOfSteps];
        lineTextView = new TextView[noOfSteps];
        arrowTextView = new TextView[noOfSteps];
        
        t = new Toast[ARRAY.preparationSteps.length];
             
        timerClicked = new boolean[ARRAY.preparationSteps.length];
        
        for(i=0;i<noOfSteps;i++){
        	timerClicked[i]=false;
        	timerButton[i] = new Button(this);
        }

        
        ingredientButton = (Button)findViewById(R.id.ingredientButton);
        ingredientButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
                
        for(i=0;i<noOfSteps;i++){        	
        	if(i==0){
        		button[i] = new Button(this);
	        	r1[i] = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	
	        	button[i].setText(ARRAY.preparationSteps[i]);
	        	button[i].setId(i+150);
	        	button[i].setBackgroundResource(R.drawable.method_background);
	        	if(ARRAY.preparationSteps[i].length()<50){
            		r1[i].width=(int) (deviceWidth/1.44);
            		r1[i].height=(int) (deviceWidth/4.363);
            	}
            	else if(ARRAY.preparationSteps[i].length()>50 && ARRAY.preparationSteps[i].length()<100){
	        		r1[i].width=(int) (deviceWidth/1.44);
	        		r1[i].height=(int) (deviceWidth/3.692)+10;
            	}
	        	else if(ARRAY.preparationSteps[i].length()>100 && ARRAY.preparationSteps[i].length()<150){
	        		r1[i].width=(int) (deviceWidth/1.028);
	        		//r1[i].height=185;
	        	}
	        	else{
	        		r1[i].width=(int) (deviceWidth/1.028);
	        	}
	        	
	        	button[i].setTextColor(Color.BLACK);
	        	r1[i].topMargin=10;
	        	r1[i].addRule(RelativeLayout.CENTER_HORIZONTAL,-1);
	        	//r1[i].addRule(RelativeLayout.BELOW,topTextView.getId());
	        	button[i].setLayoutParams(r1[i]);
	        	ll.addView(button[i]);
	        		        
        	}
        	else{        		 
        		button[i] = new Button(this);
            	r1[i] = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            	button[i].setText(ARRAY.preparationSteps[i]);
            	button[i].setId(i+150);
            	button[i].setBackgroundResource(R.drawable.method_background);
            	
            	if(ARRAY.preparationSteps[i].length()<50){
            		r1[i].width=(int) (deviceWidth/1.44);
            		r1[i].height=(int) (deviceWidth/4.363);
            	}
            	else if(ARRAY.preparationSteps[i].length()>50 && ARRAY.preparationSteps[i].length()<100){
	        		r1[i].width=(int) (deviceWidth/1.44);
	        		r1[i].height=(int) (deviceWidth/3.692)+10;
            	}
	        	else if(ARRAY.preparationSteps[i].length()>100 && ARRAY.preparationSteps[i].length()<150){
	        		r1[i].width=(int) (deviceWidth/1.028);
	        		//r1[i].height=185;
	        	}
	        	else{
	        		r1[i].width=(int) (deviceWidth/1.028);
	        	}
            	
	        	button[i].setTextColor(Color.BLACK);
           	
            	r1[i].addRule(RelativeLayout.CENTER_HORIZONTAL,-1);
            	r1[i].addRule(RelativeLayout.BELOW,arrowTextView[i-1].getId());
            	r1[i].topMargin=-10;
            	button[i].setLayoutParams(r1[i]);
            	ll.addView(button[i]);
        	}
        	        	
        	if(i<noOfSteps-1){
        		
        		lineTextView[i] = new TextView(this);
        		lineTextView[i].setBackgroundResource(android.R.color.white);
        		lineTextView[i].setText("");
        		lineTextView[i].setWidth(3);
        		//dummyTextView[i].setHeight((int) Utility.convertDpToPixel(1,this));
        		if(ARRAY.arrowValue[i].equals("NA"))
        			lineTextView[i].setHeight(100);
        		else
        			lineTextView[i].setHeight(200);
        		
        		r2[i] = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        		r2[i].addRule(RelativeLayout.BELOW,button[i].getId());
                r2[i].addRule(RelativeLayout.CENTER_HORIZONTAL,button[i].getId());
                r2[i].topMargin=-10;
                lineTextView[i].setId(i+300);
                lineTextView[i].setLayoutParams(r2[i]);
                ll.addView(lineTextView[i]);
                
                arrowTextView[i] = new TextView(this);
                arrowTextView[i].setBackgroundResource(android.R.drawable.arrow_down_float);
                arrowTextView[i].setId(i+500);
                
                r7[i] = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                r7[i].addRule(RelativeLayout.BELOW,lineTextView[i].getId());
                r7[i].addRule(RelativeLayout.CENTER_HORIZONTAL,lineTextView[i].getId());
                r7[i].topMargin=-10;
                arrowTextView[i].setLayoutParams(r7[i]);
                ll.addView(arrowTextView[i]);
                      		
                textView[i] = new TextView(this);
        		r3[i] = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        		if(ARRAY.arrowValue[i].equals("NA"))
        			textView[i].setText(null);
        		else
        			textView[i].setText(ARRAY.arrowValue[i]);
        		textView[i].setTextColor(Color.WHITE);
        		r3[i].topMargin = 75;
        		r3[i].leftMargin = 10;
        		r3[i].addRule(RelativeLayout.BELOW,button[i].getId());
        		r3[i].addRule(RelativeLayout.RIGHT_OF,lineTextView[i].getId());
        		textView[i].setLayoutParams(r3[i]);
        		ll.addView(textView[i]);
        	        		
        		//timerButton[i] = new Button(this);
        		r4[i] = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        		r4[i].rightMargin = r1[i].width/4;
        		r4[i].topMargin = (int) -(deviceWidth/32.722);
        		if(ARRAY.timeToPrepare[i].equals("NA"))
        			timerButton[i].setBackgroundResource(0);
        		else{
        			timerButton[i].setBackgroundResource(R.drawable.timer);
            		timerButton[i].setText(ARRAY.timeToPrepare[i]);
            		timerButton[i].setTextColor(Color.BLACK);
            		timerButton[i].setTextSize(15);
        		}
        		r4[i].addRule(RelativeLayout.BELOW,button[i].getId());
        		r4[i].addRule(RelativeLayout.LEFT_OF,lineTextView[i].getId());
        		r4[i].width=(int) (deviceWidth/4.8);
        		r4[i].height=(int) (deviceWidth/4.556);
        		timerButton[i].setLayoutParams(r4[i]);
        		timerButton[i].setId(i+151515);
        		ll.addView(timerButton[i]);
        		
        	}
        }

    	
        for(i=0;i<noOfSteps;i++){
        	button[i].setOnClickListener(new StepsButtonClickListener(i));
        }
        
        for(i=0 ;i<noOfSteps;i++){
        	if(ARRAY.timeToPrepare[i].equals("NA"));
        	else
        		timerButton[i].setOnClickListener(new ButtonClickListener(i,context));
        }
    
    }
    
    public void setChecked(SparseBooleanArray ch){
        checked = ch;
    }

    
    class StepsButtonClickListener extends Object implements android.view.View.OnClickListener{

    	int position;
    	public StepsButtonClickListener(int i){
    		this.position=i;
    	}
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			boolean stat = checked.get(position, false);
            checked.put(position, !stat);
            setChecked(checked);
            
            if(checked.get(position, false)){
            	if(clickButton1[position]!=null)
            		ll.removeView(clickButton1[position]);
            	clickButton[position] = new Button(getApplicationContext());
            	clickButton[position].setBackgroundResource(R.drawable.select_right);
	        	clickButton[position].setWidth(50);
	        	clickButton[position].setHeight(50);
	        	r6[position] = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        	
	        	r6[position].addRule(RelativeLayout.ALIGN_BOTTOM,button[position].getId());
            	r6[position].addRule(RelativeLayout.ALIGN_RIGHT,button[position].getId());
            	clickButton[position].setLayoutParams(r6[position]);
            	ll.addView(clickButton[position]);
            	button[position].setTextColor(Color.RED);
            }
            else{
            	ll.removeView(clickButton[position]);
            	clickButton1[position] = new Button(getApplicationContext());
            	clickButton1[position].setBackgroundResource(0);
	        	r7[position] = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	            r7[position].addRule(RelativeLayout.ALIGN_BOTTOM,button[position].getId());
	        	r7[position].addRule(RelativeLayout.ALIGN_RIGHT,button[position].getId());
	        	clickButton[position].setLayoutParams(r7[position]);
	        	ll.addView(clickButton1[position]);
	        	button[position].setTextColor(Color.BLACK);
            }
		}
    	
    }
    
    class ButtonClickListener extends Object implements android.view.View.OnClickListener{

    	int position;
    	
    	Context context;
    	public ButtonClickListener(int i,Context c) {
			// TODO Auto-generated constructor stub
    		position = i;
    		this.context = c;
    		
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			final int count = position+1;
			
			inflater = getLayoutInflater();
        	View layout = inflater.inflate(R.layout.toast_custom_layout, (ViewGroup) findViewById(R.id.toast_layout_root));
        	final TextView textView = (TextView)layout.findViewById(R.id.textView1);
        	textView.setTextColor(Color.BLACK);
        	textView.setTypeface(Typeface.DEFAULT_BOLD);
        	
        	timerButton[position].setTextColor(Color.RED);
        	t[position] = new Toast(context);
        	t[position].setGravity(Gravity.BOTTOM, 0, 0);
        	t[position].setView(layout);
        	
        	if(!ARRAY.timeToPrepare[position].equals("NA") && !flag){
        		textView.setText("Step "+count+":Timer Started");
				t[position].show();
				
            	myTimer[position] = new CountDownTimer(Integer.parseInt(ARRAY.timeToPrepare[position])*60*1000, 500) { //the timer runs for 30 seconds in this case
            		
   	            
            	     public void onTick(long leftTimeInMilliseconds) {
            	    	 flag = true;
            	    	 long seconds = leftTimeInMilliseconds / 1000;
        					textView.setText("Step "+count+":"+seconds / 60 + " mins and " + seconds % 60+" seconds remaining");
        					if(seconds % 60 == 60||seconds % 60 == 50||seconds % 60 == 40||seconds % 60 == 30||seconds % 60 == 20||seconds % 60 == 10|seconds % 60 == 00)
        						t[position].show();
        					

            	     }

            	     public void onFinish() {
            	    	 flag = false;
            	    	 t[position].cancel();
            	    	 alert.setTitle("Done!!");
            	    	 alert.setMessage(ARRAY.arrowValue[position]);
            	    	 //playAudio();
            	    	 vibratorService = Context.VIBRATOR_SERVICE;
            	    	 vibrator = (Vibrator)getApplicationContext().getSystemService(vibratorService);
            	    	 vibrator.vibrate(1000); //vibrate for 1sec.
            	    	 
            	    	 alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                // Some code
                             }

                          });
            	    	 alert.show();
            	     }
            	  }.start();
        	}
        	 else if(!ARRAY.timeToPrepare[position].equals("NA") && flag){
             	flag = false;
             	timerButton[position].setTextColor(Color.BLACK);
             	if(myTimer[position]!= null){
             		myTimer[position].cancel();
             		textView.setText("Step "+count+": Timer Stopped!!");
             		t[position].show();
             	}
 			}						
		}
    	
    }

	 protected void onDestroy(){
	    	super.onDestroy();
	    	flag=false;
	        System.gc();
	 }
	 
	 
	 @Override   
	 public void onBackPressed(){
		 super.onBackPressed(); 
		 for(int i=0;i<myTimer.length;i++){
			 if(myTimer[i]!=null){
				   myTimer[i].cancel();
				   t[i].cancel();
			 }
		 }
		 flag=false;
		 
	 }
	   public boolean onOptionsItemSelected(MenuItem item) {
		   switch (item.getItemId()) {
		   
		   case android.R.id.home:
			   for(int i=0;i<myTimer.length;i++){
					 if(myTimer[i]!=null){
						   myTimer[i].cancel();
						   t[i].cancel();
					 }
				 }
			   flag=false;
			   finish();
		   default:
	           return super.onOptionsItemSelected(item);
		   }
		   
	   }
}
