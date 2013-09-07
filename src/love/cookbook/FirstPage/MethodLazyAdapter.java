package love.cookbook.FirstPage;

import java.util.*;
import android.app.*;
import android.content.*;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.SparseBooleanArray;
import android.view.*;
import android.widget.*;


public class MethodLazyAdapter extends BaseAdapter {

    Activity activity;
    
    private String[] preparationSteps;
    private String[] timeToPrepare;
    private String[] arrowValue;
    private int [] noOfStepsFlag;
    String recipeID;
    ListView list;
    Intent intent;
    Context context;
    
    int minutes;
    int noOfSteps;
    private HashMap<Integer, Boolean> mIsChecked = new HashMap<Integer, Boolean>();
    private ArrayList<Boolean> itemChecked = new ArrayList<Boolean>();
    private SparseBooleanArray checked = new SparseBooleanArray();


    private static LayoutInflater inflater=null; 
    MediaPlayer player;
    String vibratorService;
    Vibrator vibrator;
    
    ViewHolder holder;
    
    public MethodLazyAdapter(){
    	
    }
    
    
    public MethodLazyAdapter(Activity a,String[] preparationSteps,String [] timeToPrepare,String [] arrowValue,int noOfSteps,ListView list) {
        this.activity = a;
        this.preparationSteps = preparationSteps;
        this.timeToPrepare= timeToPrepare;
        this.arrowValue=arrowValue;
        
        this.list=list;
        this.noOfSteps = noOfSteps;
        
        noOfStepsFlag = new int[noOfSteps];
        for(int i=0;i<noOfSteps;i++){
        	noOfStepsFlag[i]=0;
        	itemChecked.add(i, false);
        }
        	
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    
    public View getView(final int position, View convertView, ViewGroup parent) {
    	//View vi=convertView;
        if(convertView==null){
        	//vi = inflater.inflate(R.layout.method_item, null);
        	convertView = inflater.inflate(R.layout.method_item, null);
        	holder = new ViewHolder();
        	holder.textView = (TextView)convertView.findViewById(R.id.textView1);
        	holder.textView2 = (TextView)convertView.findViewById(R.id.textView2);
        	holder.image1 = (ImageView)convertView.findViewById(R.id.imageView1);
        	holder.image2 = (ImageView)convertView.findViewById(R.id.imageView2);
        	convertView.setTag(holder);
        	
        }
        else
        	holder = (ViewHolder)convertView.getTag();
        
        
        holder.textView.setText(preparationSteps[position]);
        
        if(!timeToPrepare[position].equals("NA"))
        	holder.image1.setImageResource(R.drawable.lock);
        else{
        	holder.image1.setImageResource(0);
        }

        if(checked.get(position, false)){
        	holder.image2.setImageResource(R.drawable.select_right);
            if(!timeToPrepare[position].equals("NA"))
            	holder.image1.setImageResource(R.drawable.select_right);
	      }
	      else{
	    	  holder.image2.setImageResource(0);  
	      }
		  
        return convertView;
  	
        /*
        holder.checkedTextView.setText(preparationSteps[position]);
                        
        if(!timeToPrepare[position].equals("NA"))
        	holder.image.setImageResource(R.drawable.lock);
        else
        	holder.image.setImageResource(0);
        
        
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        
               
	    holder.checkedTextView.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v)
	        {
	            ((CheckedTextView) v).toggle();
	            
	            if(holder.checkedTextView.isChecked())
	            	itemChecked.set(position, true);
	            else if(!holder.checkedTextView.isChecked())
	            	itemChecked.set(position,false);
	            
	            if(!timeToPrepare[position].equals("NA")  && holder.checkedTextView.isChecked() && noOfStepsFlag[position]==0){
	            	noOfStepsFlag[position]=1;
	            	playAudio();
	            	 new CountDownTimer(Integer.parseInt(timeToPrepare[position])*60*1000, 500) { //the timer runs for 30 seconds in this case
	   	            // ProgressDialog progressDialog = ProgressDialog.show(activity, "Timer Started", null);
	   	            
	            	     public void onTick(long leftTimeInMilliseconds) {
	            	    	 long seconds = leftTimeInMilliseconds / 1000;
	            	    	 holder.textView.setText(seconds/60+":"+seconds%60);
	            	    	 //alert.setMessage(seconds / 60 + "mins and " + seconds % 60+" seconds remaining");
            	    	 	// progressDialog.setMessage(seconds / 60 + "mins and " + seconds % 60+" seconds remaining");    
   	    	 
	            	     }

	            	     public void onFinish() {
	            	    	// progressDialog.dismiss();
	            	    	 holder.textView.setText("Done");
	            	    	 alert.setTitle("Done!!");
	            	    	 alert.setMessage(arrowValue[position]);
	            	    	 alert.setIcon(R.drawable.savai_upma);
	            	    	 playAudio();
	            	    	 vibratorService = Context.VIBRATOR_SERVICE;
	            	    	 vibrator = (Vibrator)activity.getSystemService(vibratorService);
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
	        }

			private void playAudio() {
				// TODO Auto-generated method stub
				player = MediaPlayer.create(activity, R.raw.facebook_ringtone_pop);
            	player.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer player) {
						// TODO Auto-generated method stub
						player.release();							
					}
				});
            	player.start();
			}
	    });
	    
	    /*
	     * The checkedTextView click state is saved in array itemChecked when the row is clicked and by default its set 
	     * to false.
	     * Outside onClick the checkTextView row will be checked or unchecked from the 
	     * value stored in array itemChecked.
	     
	    holder.checkedTextView.setChecked(itemChecked.get(position));
       */
        
    }
    
    public void setChecked(SparseBooleanArray ch){
        checked = ch;
       notifyDataSetChanged();
    }

    static class ViewHolder{
        TextView textView;
        TextView textView2;
        ImageView image1;
        ImageView image2;
        //CheckedTextView checkedTextView;
    }  
    
    public int getCount() {
        return preparationSteps.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

}
