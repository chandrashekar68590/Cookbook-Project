package love.cookbook.FirstPage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;

public class MethodSampleAdapter extends BaseAdapter {

	 private static LayoutInflater inflater=null;
	 Activity activity;
	 ViewHolder holder;
    private String[] preparationSteps;
    private String[] timeToPrepare;
    private String[] arrowValue;
    int noOfSteps;
	 
	 public MethodSampleAdapter(SampleMethodActivity a,String[] preparationSteps,String [] timeToPrepare,String [] arrowValue,int noOfSteps) {
		// TODO Auto-generated constructor stub
		 this.activity = a;
		 
        this.preparationSteps = preparationSteps;
        this.timeToPrepare= timeToPrepare;
        this.arrowValue=arrowValue;
        this.noOfSteps = noOfSteps;
        
		 inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	 
	}

	@Override
	 public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
		 if(convertView==null){
	        	convertView = inflater.inflate(R.layout.sample_method_item, null);
	        	holder = new ViewHolder();
	        	holder.b = (Button)convertView.findViewById(R.id.button1);
	        	holder.imageView = (ImageView)convertView.findViewById(R.id.imageView2);
	        	holder.timerImageView = (ImageView)convertView.findViewById(R.id.timerImageView);
	        	convertView.setTag(holder);
		 }
		 else
	        holder = (ViewHolder)convertView.getTag();
		 
	 
		 holder.b.setText(preparationSteps[position]);
		 
		 if(!timeToPrepare[position].equals("NA")){
	        	holder.timerImageView.setImageResource(R.drawable.timer);
	        	System.out.println(position);
		 }
	        else{
	        	holder.timerImageView.setImageResource(0);
	        	System.out.println("Else: "+position);
	        }

		 holder.b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("Button Clicked: "+position);
				holder.imageView.setImageResource(R.drawable.select_right);
				System.out.println("Button Clicked after image set: "+position);
				
			}
		});
		 
			return convertView;
		}
	 
	    static class ViewHolder{
	        Button b;
	        ImageView imageView,timerImageView;
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
