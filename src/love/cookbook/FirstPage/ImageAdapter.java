package love.cookbook.FirstPage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class ImageAdapter extends BaseAdapter {
	private static LayoutInflater inflater=null;
	Activity activity;
	int imageIngredientsID [];
	ViewHolder holder;

	public ImageAdapter(Activity activity,int [] imageIngredientsID) {
		// TODO Auto-generated constructor stub
		this.activity=activity;
		this.imageIngredientsID = imageIngredientsID;
        inflater = (LayoutInflater)this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	
	public int getCount() {
		// TODO Auto-generated method stub
		return imageIngredientsID.length;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//ImageView image = null;
		//View vi;
        if (convertView == null){  // if it's not recycled, initialize some attributes
        	//vi = inflater.inflate(R.layout.each_image,parent, false);
        	convertView = inflater.inflate(R.layout.each_image,parent, false);
        	//image = (ImageView)vi.findViewById(R.id.imageView);
        	holder = new ViewHolder();
        	holder.image1 = (ImageView) convertView.findViewById(R.id.imageView);
        	convertView.setTag(holder);
		}
        else{
        	
        	holder = (ViewHolder)convertView.getTag();
        	//vi=convertView;
        	//image = (ImageView)convertView;
		}     	
    	
    	/*image.setMaxHeight(150);
    	image.setMinimumHeight(150);
    	image.setScaleType(ImageView.ScaleType.FIT_XY );
    	image.setImageResource(imageIngredientsID[position]); */
        
        holder.image1.setMaxHeight(150);
        holder.image1.setMinimumHeight(150);
        holder.image1.setScaleType(ImageView.ScaleType.FIT_XY );
        holder.image1.setImageResource(imageIngredientsID[position]);

        return convertView;
	}
	
	public class ViewHolder{
		ImageView image1;
	}

}


