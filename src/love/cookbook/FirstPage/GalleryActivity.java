package love.cookbook.FirstPage;

import love.cookbook.FirstPage.util.*;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import android.app.AlertDialog;
import android.content.*;
import android.database.Cursor;
import android.graphics.*;
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

	public AlertDialog.Builder alert;
	
	final MySqliteHelper dbHelper = new MySqliteHelper(this);
	final FirstPageActivity firstPage = new FirstPageActivity();
	   
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
        setContentView(R.layout.grid);
        
        alert = new AlertDialog.Builder(this);
        
        setUpBillingConnection();
        
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
		
		ARRAY.lock = new String[cur.getCount()];
		ARRAY.lock = firstPage.createDishArray(cur, VARIABLES.lockColumn);
	    
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
        for(int i=0;i<ARRAY.imageIngredientsID.length;i++){
        	ARRAY.imageIngredientsID[i] =  getResources().getIdentifier(ARRAY.imageIngredientsName[i], "drawable", packageName);
        	ARRAY.bitmapImages[i] = bitmapDecoder.decodeSampledBitmapFromResource(getResources(), ARRAY.imageIngredientsID[i], 130, 90);
        }
        cur.close();
        dbHelper.close();
	    
	    adapter=new ImageAdapter(this,ARRAY.bitmapImages,ARRAY.timeToPrepareString,ARRAY.nonVeg,ARRAY.lock);
        gridview.setAdapter(adapter);
        adapter.setChecked(VARIABLES.unlocked);
        
	    gridview.setOnItemClickListener(new OnItemClickListener(){
			Intent intent;
			String recipeID;
			String recipeName;
			String lock;

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
	   		    
	   		    columnIndex = cur.getColumnIndex("ZISLOCKED");
	   		    lock = cur.getString(columnIndex);
	   		    
	   		    columnIndex = cur.getColumnIndex("ZSERVING");
			    String serving = cur.getString(columnIndex);
			    
			    if(serving.equals("NA"))
			    	serving="";
	   		       		    
			    
			    if(lock.equals("0")){
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
			    else{
			    	alert.setTitle("Buy Dish");
		 	    	alert.setMessage("Do you want to unlock all locked recipes?");
		 	    	
		 	    	 alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int which) {
                            // Some code
                        	 initiateBillingProcess();
                         }

                      });
		 	    	 alert.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int which) {
                             // Some code
                          }

                       });
		 	    	 
        	    	 alert.show();
		   		 }
			}
			
		});
	    
	}
	
    public void setUpBillingConnection(){
    	VARIABLES.mHelper = new IabHelper(this, VARIABLES.base64EncodedPublicKey);
        System.out.println("Inside setUpBilling");
    	VARIABLES.mHelper.startSetup(new 
		IabHelper.OnIabSetupFinishedListener() {
			@Override
			public void onIabSetupFinished(IabResult result) {
				// TODO Auto-generated method stub
				System.out.println("Inside Onsetup finished");
				 if (!result.isSuccess()) 
					 System.out.println("In-app Billing setup failed: " + result);
	    	     else             
	    	    	 System.out.println("In-app Billing is set up OK"+result);
			}
    	});
    }
    
    public void initiateBillingProcess(){

    	    	
    	final IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
    			  new IabHelper.OnConsumeFinishedListener() {
    			   public void onConsumeFinished(Purchase purchase, 
    		             IabResult result) {

    			 if (result.isSuccess()) {		    	 
    				 	System.out.println("Item Consumed");
    				 	dbHelper.updateLockedValues();
    				 	VARIABLES.unlocked.put(1, true);
    				 	adapter.setChecked(VARIABLES.unlocked);
    				
    			 } else {
    			         // handle error
    			 }
    		  }
    		};

    	
    	final IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener 
    	   = new IabHelper.QueryInventoryFinishedListener() {
    		   public void onQueryInventoryFinished(IabResult result,
    		      Inventory inventory) {
    			   
    		      if (result.isFailure()) {
    			  // Handle failure
    		      } else {
    		    	  VARIABLES.mHelper.consumeAsync(inventory.getPurchase(VARIABLES.ITEM_SKU), 
    				mConsumeFinishedListener);
    		      }
    	    }
    	};

    	
    	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener 
    	= new IabHelper.OnIabPurchaseFinishedListener() {
		@Override
			public void onIabPurchaseFinished(IabResult result,
					Purchase purchase) {
				// TODO Auto-generated method stub
				 if (result.isFailure()) {
		    	      // Handle error
		    	      return;
		    	 }      
		    	 else if (purchase.getSku().equals(VARIABLES.ITEM_SKU)) {
		    		 VARIABLES.mHelper.queryInventoryAsync(mReceivedInventoryListener);
		    	   System.out.println("Item purchased");
		    	}
				
			}
    	};
    	
		
    	VARIABLES.mHelper.launchPurchaseFlow(this, VARIABLES.ITEM_SKU, 10001, mPurchaseFinishedListener,"");

	}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, 
         Intent data) 
    {
    	System.out.println("Result: "+resultCode+" Request: "+requestCode);
          if (!VARIABLES.mHelper.handleActivityResult(requestCode, 
                  resultCode, data)) {     
        	super.onActivityResult(requestCode, resultCode, data);
        	
        	
          }
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
        
        /* UNCOMMENT BEFORE RELEASE
        for(int i =0;i<ARRAY.bitmapImages.length;i++)
        	ARRAY.bitmapImages[i].recycle();
        */
        
        if (VARIABLES.mHelper != null) 
        	VARIABLES.mHelper.dispose();
        VARIABLES.mHelper = null; 
        
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
	
}
