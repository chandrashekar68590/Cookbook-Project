package love.cookbook.FirstPage;

import love.cookbook.FirstPage.util.IabHelper;
import love.cookbook.FirstPage.util.IabResult;
import love.cookbook.FirstPage.util.Inventory;
import love.cookbook.FirstPage.util.Purchase;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;


public class ListViewSampleActivity extends SherlockListActivity {
	
    
    ListView list;
    
	LazyAdapter adapter;
	
	public String packageName;
	public int imageID;
	String ingredientsImageName;
	
	public MySqliteHelper dbHelper;
	Cursor cur;
	
	public AlertDialog.Builder alert;
	
	private static final String TAG = "love.cookbook.FirstPage";
	
	String eachIngredientsImageName [];

	String listItemName;
	
    final Runnable mUpdateFacebookNotification = new Runnable() {
        public void run() {
        	Toast.makeText(getBaseContext(), "Facebook updated !", Toast.LENGTH_LONG).show();
        }
    };

	private BitmapDecoder bitmapDecoder;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Intent intent = getIntent();
    	bitmapDecoder = new BitmapDecoder();
    	
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        alert = new AlertDialog.Builder(this);

        setContentView(R.layout.list_main);
		dbHelper = new MySqliteHelper(this);
		
		setUpBillingConnection();
        
		list=(ListView)findViewById(android.R.id.list);
		
		final FirstPageActivity firstPage = new FirstPageActivity();
		
        //com.actionbarsherlock.app.ActionBar ab = getSupportActionBar();
        //ab.setDisplayHomeAsUpEnabled(true);

        ARRAY.dishes = intent.getStringArrayExtra("DISHES");
        ARRAY.description = intent.getStringArrayExtra("DESCRIPTION");
        ARRAY.timeToPrepare = intent.getStringArrayExtra("TIMETOPREPARE");
        ARRAY.lock = intent.getStringArrayExtra("LOCKVALUE");
        ARRAY.imageName = intent.getStringArrayExtra("IMAGENAME");
       
        ARRAY.nonVeg = intent.getStringArrayExtra("NONVEG");
        ARRAY.isFavourite = intent.getStringArrayExtra("ISFAVOURITE");
        
        /*
         * This code snippet is used to fetch the image id from the resource folder from the name fetched from database and sent to Bitmap decoder.
         */

        ARRAY.bitmapImages = new Bitmap[ARRAY.imageName.length];
        
        packageName=this.getPackageName();
        for(int i=0;i<ARRAY.imageName.length;i++)
        	ARRAY.bitmapImages[i] = BitmapDecoder.decodeSampledBitmapFromResource(getResources(), getResources().getIdentifier(ARRAY.imageName[i].toLowerCase(), "drawable", packageName), 100, 91);
                              
       
        adapter=new LazyAdapter	(this, ARRAY.dishes,ARRAY.description,ARRAY.timeToPrepare,ARRAY.bitmapImages,ARRAY.lock,ARRAY.nonVeg);
        list.setAdapter(adapter);
        adapter.setChecked(VARIABLES.unlocked);
        
        getListView().setDivider(null);
        getListView().setDividerHeight(0);
            	
    	list.setOnItemClickListener(new OnItemClickListener(){
    		String columnName;
    		String whereColumnName;
    		
    		String recipeName;
    		
    		Cursor cur;
    		int columnIndex;
    		String ingredients [];
    		String ingredientsHindiname [];
    		String quantity [];
    		String lock;
    		int cursorEnd;

    		public void onItemClick(AdapterView<?> parent, View view, int position,
    				long id) {
    			Intent intent;
    			String recipeID;
    			columnName = "Z_PK";
    			whereColumnName = "ZNAME";
    			
    			// TODO Auto-generated method stub    		
    		      LinearLayout ll = (LinearLayout) view;
    			//View view1 = list.getChildAt(position);
    			TextView tv = (TextView)ll.findViewById(R.id.textView1);
    			recipeName = tv.getText().toString();

    			cur = dbHelper.getTableValues(VARIABLES.tabelName,whereColumnName, recipeName,null,"Z_PK");
    			    			
    			cur.moveToFirst();
   	   		    columnIndex = cur.getColumnIndex("Z_PK");
   	   		    recipeID = cur.getString(columnIndex);
   	   		    
   	   		    columnIndex = cur.getColumnIndex("ZSERVING");
   	   		    String serving = cur.getString(columnIndex);
   	   		    
   	   		    if(serving.equals("NA"))
	   		    	serving="";
   	   		    
   	   		    ingredientsImageName = cur.getString(cur.getColumnIndex("ZIMAGE"));
   	   		    
   	   		    imageID = getResources().getIdentifier(ingredientsImageName.toLowerCase(), "drawable", packageName);
   	   		    
   	   		    if(imageID==0)
   	   		    	imageID=2130837507;
   	   		  
   	   		    cur.moveToFirst();
   	   		    columnIndex = cur.getColumnIndex("ZISLOCKED");
   	   		    lock = cur.getString(columnIndex);
   	   		    
   	   		    cur.close();
   	   		    dbHelper.close();
   	   		    
   	   		    if(lock.equals("0")){		//Commented for next release
	   	   		    cur = dbHelper.getIngredients(recipeID,null);
	      	  		cursorEnd = cur.getCount();
	      	  		
	   	   		    ingredients = new String [cursorEnd];
	   	   		    ingredients = firstPage.createDishArray(cur, whereColumnName);
	   	   		    
	   	   		    ingredientsHindiname = new String[cursorEnd];
	   	   		    ingredientsHindiname = firstPage.createDishArray(cur, "ZHINDINAME");
	   	   		    
	   	   		    quantity = new String[cursorEnd];
	   	   		    quantity = firstPage.createDishArray(cur, "ZQUANTITY");
	   	   		    
	   	   		    eachIngredientsImageName = new String[cursorEnd];
	   	   		    eachIngredientsImageName = firstPage.createDishArray(cur, "ZTHUMBNAIL");
	   	   		    
	   	   		    for(int i=0;i<ingredientsHindiname.length;i++)
	   	   		    	if(ingredientsHindiname[i].equals("NA"))
	   	   		    		ingredientsHindiname[i]="";
	   	   		    
	   	   		    for(int i=0;i<quantity.length;i++)
	   	   		    	if(quantity[i].equals("NA"))
	   	   		    		quantity[i]="";
	   	   		    
	    			intent = new Intent(ListViewSampleActivity.this,IngredientActivity.class);
	    			intent.putExtra("INGREDIENTS", ingredients);
	    			intent.putExtra("HINDINAME",ingredientsHindiname);
	    			intent.putExtra("QUANTITY", quantity);
	    			intent.putExtra("RECIPE_ID", recipeID);
	    			intent.putExtra("SERVING", serving);
	    			intent.putExtra("RECIPE_NAME", recipeName);
	    			intent.putExtra("IMAGEID", imageID);
	    			intent.putExtra("EACHINGREDIENTSIMAGENAME", eachIngredientsImageName);

	    			
	    			startActivity(intent);
   	   		    }
   	   		    else{
   	   		    	VARIABLES.unlocked.get(1,false);
   	   		    	
		   	   		  alert.setTitle("Buy Dish");
			 	    	alert.setMessage("Do you want to unlock all locked recipes?");
			 	    	
			 	    	 alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		                 public void onClick(DialogInterface dialog, int which) {
		                	 

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
        
    	VARIABLES.mHelper.startSetup(new 
		IabHelper.OnIabSetupFinishedListener() {
			@Override
			public void onIabSetupFinished(IabResult result) {
				// TODO Auto-generated method stub
				System.out.println("Inside Onsetup finished");
				 if (!result.isSuccess()) 
					 Log.d(TAG, "In-app Billing setup failed: " + result);
	    	     else             
	    	    	 Log.d(TAG, "In-app Billing is set up OK"+result);
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
        
        /* To be uncommented at the end
        
        if (VARIABLES.mHelper != null) 
        	VARIABLES.mHelper.dispose();
        VARIABLES.mHelper = null; 
        */
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