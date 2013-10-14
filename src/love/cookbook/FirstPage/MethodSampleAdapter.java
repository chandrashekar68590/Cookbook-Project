package love.cookbook.FirstPage;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;

public class MethodSampleAdapter extends BaseAdapter {

	 private static LayoutInflater inflater=null;
	 Activity activity,activity1;
	 ViewHolder holder;
    private String[] preparationSteps;
    private String[] timeToPrepare;
    private String[] arrowValue;
    private int [] noOfStepsFlag;
    int noOfSteps;
    public Toast t[];
    public AlertDialog.Builder alert;
    MediaPlayer player;
    String vibratorService;
    Vibrator vibrator;
    Context context;
    SampleMethodActivity sample;
    
    public final int count =0;
    public CountDownTimer myTimer[] = new CountDownTimer[30];
    private ArrayList<Boolean> itemChecked = new ArrayList<Boolean>();
    private SparseBooleanArray checked = new SparseBooleanArray();
    private SparseBooleanArray checked1 = new SparseBooleanArray();
	 
	 public MethodSampleAdapter(SampleMethodActivity a,String[] preparationSteps,String [] timeToPrepare,String [] arrowValue,int noOfSteps,Context c) {
		// TODO Auto-generated constructor stub
		 this.activity = a;
		 this.activity1 = activity;
		 this.context = context;
        this.preparationSteps = preparationSteps;
        this.timeToPrepare= timeToPrepare;
        this.arrowValue=arrowValue;
        this.noOfSteps = noOfSteps;
    
        
        noOfStepsFlag = new int[noOfSteps];

        for(int i=0;i<noOfSteps;i++){
        	noOfStepsFlag[i]=0;
        	itemChecked.add(i, false);
        }
        
		 inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	 
	}

	public MethodSampleAdapter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	 public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
		 if(convertView==null){
	        	convertView = inflater.inflate(R.layout.sample_method_item, null);
	        	sample = new SampleMethodActivity();
	            alert = new AlertDialog.Builder(activity1);
	        	holder = new ViewHolder();
	        	holder.b = (Button)convertView.findViewById(R.id.button1);
	        	holder.imageView = (ImageView)convertView.findViewById(R.id.imageView2);
	        	holder.timerButton = (Button)convertView.findViewById(R.id.timerButton);
	        	holder.textView1 = (TextView)convertView.findViewById(R.id.textView1);
	        	convertView.setTag(holder);
	        	
		 }
		 else
	        holder = (ViewHolder)convertView.getTag();
		 
	 
		 holder.b.setText(preparationSteps[position]);
		 
		 if(!timeToPrepare[position].equals("NA")){
	        	holder.timerButton.setBackgroundResource(R.drawable.timer);
	        	holder.textView1.setText(timeToPrepare[position]);
		 }
		 
        else{
        	holder.timerButton.setBackgroundResource(0);
        	holder.textView1.setText(null);
        }
	        

		 /*
		 holder.b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				System.out.println("Position:"+position);
				 boolean stat = checked.get(position, false);
	                checked.put(position, !stat);
	                setChecked(checked);
	                
	                
				if(checked1.get(position, false)){
					System.out.println("Position:"+position+" "+checked1.get(position, false));
		        	holder.imageView.setImageResource(R.drawable.select_right);
				}
		        else{
		        	System.out.println("Position:"+position+" "+checked1.get(position, false));
		        	holder.imageView.setImageResource(0);  
		        }
	     						
			}
		});
		 */
		 
		 holder.timerButton.setOnClickListener(new OnClickListener() {
			 
			boolean flag = false;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				final int count = position+1;
				
				//inflater = activity.getLayoutInflater();
            	//View layout = inflater.inflate(R.layout.toast_custom_layout, (ViewGroup) v.findViewById(R.id.toast_layout_root));
            	//final TextView textView = (TextView)layout.findViewById(R.id.textView1);
            	//textView.setTextColor(Color.BLACK);
            	//textView.setTypeface(Typeface.DEFAULT_BOLD);
				
				//t[position] = new Toast(context);
            	//t[position].setGravity(Gravity.BOTTOM, 0, 0);
            	//t[position].setView(layout);
            	
            	if(!timeToPrepare[position].equals("NA") && !flag){
            		//textView.setText("Step "+count+":Timer Started");
    				//t[position].show();
    				
	            	myTimer[position] = new CountDownTimer(Integer.parseInt(ARRAY.timeToPrepare[position])*60*1000, 500) { //the timer runs for 30 seconds in this case
	            		
	   	            
	            	     public void onTick(long leftTimeInMilliseconds) {
	            	    	 flag = true;
	            	    	 long seconds = leftTimeInMilliseconds / 1000;
	            	    	// System.out.println("tick of position: "+position);
            					//textView.setText("Step "+count+":"+seconds / 60 + " mins and " + seconds % 60+" seconds remaining");
            	    	 
	            	    	// System.out.println("Step "+count+":"+seconds / 60 + " mins and " + seconds % 60+" seconds remaining");
            					if((seconds % 60 == 60||seconds % 60 == 50||seconds % 60 == 40||seconds % 60 == 30||seconds % 60 == 20||seconds % 60 == 10|seconds % 60 == 00) && !timeToPrepare[position].equals("NA")){
            						//t[position].show();
            						holder.textView1.setText(seconds / 60+":"+seconds % 60);
            						System.out.println("Step "+count+":"+seconds / 60 + " mins and " + seconds % 60+" seconds remaining");
            					}
            					else if (timeToPrepare[position].equals("NA"))
            						holder.textView1.setText(null);
            					
	            	    	// textView2.setText(seconds/60+":"+seconds%60);
	            	    	 //alert.setMessage(seconds / 60 + "mins and " + seconds % 60+" seconds remaining");
            	    	 	// progressDialog.setMessage(seconds / 60 + "mins and " + seconds % 60+" seconds remaining");  

	            	     }

	            	     public void onFinish() {
	            	    	// progressDialog.dismiss();
	            	    	 flag = false;
	            	    	 //list.setEnabled(true);
	            	    	 //t[position].cancel();
	            	    	 alert.setTitle("Done!!");
	            	    	 alert.setMessage(ARRAY.arrowValue[position]);
	            	    	// alert.setIcon(R.drawable.savai_upma);
	            	    	 //playAudio();
	            	    	 vibratorService = Context.VIBRATOR_SERVICE;
	            	    	 vibrator = (Vibrator)activity.getApplicationContext().getSystemService(vibratorService);
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
	                	if(myTimer[position]!= null){
	                		myTimer[position].cancel();
	                		//textView.setText("Step "+count+": Timer Stopped!!");
	                		//t[position].show();
	                	}
      			}
            	
				
			}
		});
		 
			return convertView;
		}
	 
	    static class ViewHolder{
	        Button b,timerButton;
	        ImageView imageView;
	        TextView textView1;
	    }  
	    
	    public void setChecked(SparseBooleanArray ch){
	        checked1 = ch;
	       notifyDataSetChanged();
	    }
	    
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return preparationSteps.length;
		}
	
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}
	
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}	

}
