package love.cookbook.FirstPage;

import android.app.Activity;
import android.content.Context;
import android.view.*;
import android.widget.*;

	public class IngredienceLazyAdapter extends BaseAdapter {
	    
	    private Activity activity;
	    private String[] ingredients;
	    private String[] ingredientsHindiName;
	    private String[] quantity;
	    private int[] eachImageID;
	    private static LayoutInflater inflater=null; 
	    ViewHolder holder;
	    
	    public IngredienceLazyAdapter(Activity a, String[] ingredients,String[] ingredientsHindiName,String[] quantity,int[] eachImageID) {
	       
	    	activity = a;
	        this.ingredients=ingredients;
	        this.ingredientsHindiName=ingredientsHindiName;
	        this.quantity=quantity;
	        this.eachImageID = eachImageID;
	        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	        //View vi=convertView;
	        if(convertView==null){
	        	//vi = inflater.inflate(R.layout.ingredience_item, null);
	        	convertView = inflater.inflate(R.layout.ingredience_item,parent, false);
	        	//image = (ImageView)vi.findViewById(R.id.imageView);
	        	holder = new ViewHolder();
	        	holder.image = (ImageView) convertView.findViewById(R.id.imageView2);
	        	holder.textView1 = (TextView)convertView.findViewById(R.id.textView1);
	        	holder.textView2 = (TextView)convertView.findViewById(R.id.textView2);
	        	holder.textView3 = (TextView)convertView.findViewById(R.id.textView3);
	        	convertView.setTag(holder);
	        }
	        else
	        	holder = (ViewHolder)convertView.getTag();
	        
	       
	        holder.textView1.setText(ingredients[position]);
	        holder.textView2.setText(ingredientsHindiName[position]);	        
	        holder.textView3.setText(quantity[position]);	        
	        holder.image.setImageResource(eachImageID[position]);
	        
	        return convertView;
	        
	    }

	    public int getCount() {
	        return ingredients.length;
	    }

	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }
	    
		public class ViewHolder{
			ImageView image;
			TextView textView1;
			TextView textView2;
			TextView textView3;
		}

}
