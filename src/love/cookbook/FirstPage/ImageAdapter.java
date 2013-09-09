package love.cookbook.FirstPage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	private static LayoutInflater inflater=null;
	Activity activity;
	int imageIngredientsID [];
	Bitmap bitmapImages[];
	String timeToPrepareString [],nonVeg [];
	ViewHolder holder;

	public ImageAdapter(Activity activity,Bitmap [] bitmapImages,String [] timeToPrepareString,String [] nonVeg) {
		// TODO Auto-generated constructor stub
		this.activity=activity;
		this.bitmapImages = bitmapImages;
		this.imageIngredientsID = imageIngredientsID;
		this.timeToPrepareString = timeToPrepareString;
		this.nonVeg = nonVeg;
        inflater = (LayoutInflater)this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	
	public int getCount() {
		// TODO Auto-generated method stub
		return bitmapImages.length;
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
        	holder.vegNonveggImageView = (ImageView)convertView.findViewById(R.id.vegNonVegImageView);
        	holder.preperationTimeTextView = (TextView)convertView.findViewById(R.id.preperationTimeTextView);
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
        
        holder.preperationTimeTextView.setText(timeToPrepareString[position]);
        
        holder.image1.setMaxHeight(150);
        holder.image1.setMinimumHeight(150);
        holder.image1.setScaleType(ImageView.ScaleType.FIT_XY );
        holder.image1.setImageBitmap(bitmapImages[position]);
        
        if(nonVeg[position].equals("1")){
        	//System.out.println("Inside non veg");
        	holder.vegNonveggImageView.setImageResource(R.drawable.non_veg_symbol);
        }
        else{
        	//System.out.println("Inside veg");
        	holder.vegNonveggImageView.setImageResource(R.drawable.veg_symbol);
        }

        return convertView;
	}
	
	public class ViewHolder{
		ImageView image1;
		TextView preperationTimeTextView;
		ImageView vegNonveggImageView;
	}

}


