package love.cookbook.FirstPage;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.*;
import android.widget.*;

public class GalleryActivity extends SherlockActivity {
		
	ImageAdapter adapter;
	String columnName;
	String whereColumnName;
	
	Cursor cur;
	int columnIndex;

	int cursorEnd,cursorCount;
	public String packageName;
	private BitmapDecoder bitmapDecoder;
	
	String eachIngredientsImageName [];

	
	final MySqliteHelper dbHelper = new MySqliteHelper(this);
	final FirstPageActivity firstPage = new FirstPageActivity();
	   
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
        setContentView(R.layout.grid);
        
        bitmapDecoder = new BitmapDecoder();
	    GridView gridview = (GridView)findViewById(R.id.gridView);
	    
	      SharedPreferences settings = getSharedPreferences(VARIABLES.PREFS_NAME, 0);
	      

		if(!settings.getBoolean("NonVegCheckBoxResult", false))
			cur = dbHelper.getIngredients(null,"0");
		else 
			cur = dbHelper.getIngredients(null, null);
		
	    
	    ARRAY.imageIngredientsName = new String [cur.getCount()];
	    ARRAY.imageIngredientsName = firstPage.createDishArray(cur, "ZIMAGE");
	    
	    ARRAY.recipeName = new String [cur.getCount()];
	    ARRAY.recipeName = firstPage.createDishArray(cur, "ZNAME");
	    
	    ARRAY.nonVeg = new String[cur.getCount()];
		ARRAY.nonVeg = firstPage.createDishArray(cur, VARIABLES.nonVegColumn);
	    
	    ARRAY.timeToPrepare = new String[cur.getCount()];
	    ARRAY.timeToPrepare = firstPage.createDishArray(cur, VARIABLES.columnName3);
	    
	    ARRAY.timeToPrepareString = new String[cur.getCount()];
  		
		for(int i=0;i<ARRAY.timeToPrepare.length;i++){
			if(ARRAY.timeToPrepare[i].equals("30"))
				ARRAY.timeToPrepareString[i] = "Around 30 mins";
			else if(ARRAY.timeToPrepare[i].equals("60"))
				ARRAY.timeToPrepareString[i] = "Around an Hour";
			else if(ARRAY.timeToPrepare[i].equals("90"))
				ARRAY.timeToPrepareString[i] = "Around 90 mins";
		}
	    
	    for(int i=0;i<ARRAY.imageIngredientsName.length;i++){
	    	ARRAY.imageIngredientsName[i]=ARRAY.imageIngredientsName[i].toLowerCase();
	    }
	    
	    ARRAY.imageIngredientsID = new int[ARRAY.imageIngredientsName.length];	  
	    ARRAY.bitmapImages = new Bitmap[ARRAY.imageIngredientsName.length];
	    
	    packageName=this.getPackageName();
        for(int i=0;i<ARRAY.imageIngredientsID.length;i++)
        	ARRAY.bitmapImages[i] = bitmapDecoder.decodeSampledBitmapFromResource(getResources(), getResources().getIdentifier(ARRAY.imageIngredientsName[i], "drawable", packageName), 130, 90);
        
        cur.close();
        dbHelper.close();
	    
	    adapter=new ImageAdapter(this,ARRAY.bitmapImages,ARRAY.timeToPrepareString,ARRAY.nonVeg);
        gridview.setAdapter(adapter);
	    gridview.setOnItemClickListener(new GridViewClickListener());
	    
	}

	   @Override
	   public boolean onOptionsItemSelected(MenuItem item) {
		   switch (item.getItemId()) {
		   
		   case android.R.id.home:
			   finish();
		   default:
	           return super.onOptionsItemSelected(item);
		   }
		   
	   }
	   
    protected void onDestroy(){
    	super.onDestroy();
    	unbindDrawables(findViewById(R.id.RootView));
        System.gc();
    }
    
    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
        view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
            unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
        ((ViewGroup) view).removeAllViewsInLayout();
        }
    }
    
	public class GridViewSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> parent, View v, int positio,
				long arg3) {
			// TODO Auto-generated method stub
			v.setLayoutParams(new Gallery.LayoutParams(500,5000));
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public class GridViewClickListener implements OnItemClickListener{
		Intent intent;
		String recipeID;
		String recipeName;

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long id) {
			// TODO Auto-generated method stub
			recipeName= ARRAY.recipeName[position];
			columnName = "Z_PK";
			whereColumnName = "ZNAME";
						
			cur = dbHelper.getTableValues(VARIABLES.tabelName,whereColumnName, recipeName,null,"Z_PK");
			
			cur.moveToFirst();
   		    columnIndex = cur.getColumnIndex("Z_PK");
   		    recipeID = cur.getString(columnIndex);
   		    
   		    columnIndex = cur.getColumnIndex("ZSERVING");
		    String serving = cur.getString(columnIndex);
		    
		    if(serving.equals("NA"))
		    	serving="";
   		       		    
		    
   		    cur = dbHelper.getIngredients(recipeID,null);
   		    cursorEnd = cur.getCount();    
  		
   		    ARRAY.ingredients = new String [cursorEnd];
   		    ARRAY.ingredients = firstPage.createDishArray(cur, whereColumnName);
   		    
	   		ARRAY.ingredientsHindiname = new String[cursorEnd];
	   		ARRAY.ingredientsHindiname = firstPage.createDishArray(cur, "ZHINDINAME");
	   		    
	   		ARRAY.quantity = new String[cursorEnd];
	   		ARRAY.quantity = firstPage.createDishArray(cur, "ZQUANTITY");
	   		
   		    eachIngredientsImageName = new String[cursorEnd];
   		    eachIngredientsImageName = firstPage.createDishArray(cur, "ZTHUMBNAIL");
   		    
	   		    for(int i=0;i<ARRAY.ingredientsHindiname.length;i++)
   	   		    	if(ARRAY.ingredientsHindiname[i].equals("NA"))
   	   		    		ARRAY.ingredientsHindiname[i]="";
   	   		    
   	   		    for(int i=0;i<ARRAY.quantity.length;i++)
   	   		    	if(ARRAY.quantity[i].equals("NA"))
   	   		    		ARRAY.quantity[i]="";
   		    
			intent = new Intent(GalleryActivity.this,IngredientActivity.class);
			intent.putExtra("INGREDIENTS", ARRAY.ingredients);
			intent.putExtra("HINDINAME",ARRAY.ingredientsHindiname);
			intent.putExtra("QUANTITY", ARRAY.quantity);
			intent.putExtra("RECIPE_ID", recipeID);
			intent.putExtra("SERVING", serving);
			intent.putExtra("RECIPE_NAME", recipeName);
			intent.putExtra("IMAGEID", ARRAY.imageIngredientsID[position]);
			intent.putExtra("EACHINGREDIENTSIMAGENAME", eachIngredientsImageName);

			
			startActivity(intent);
		}
		
	}

}
