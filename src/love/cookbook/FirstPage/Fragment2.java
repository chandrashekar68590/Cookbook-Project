package love.cookbook.FirstPage;

import love.cookbook.FirstPage.util.IabHelper;
import love.cookbook.FirstPage.util.IabResult;
import love.cookbook.FirstPage.util.Inventory;
import love.cookbook.FirstPage.util.Purchase;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.*;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;

@SuppressLint("DefaultLocale")
public class Fragment2 extends SherlockFragment {
	MainCourseTab mainCourseTab;
	public String sortOption;
	private MySqliteHelper dbHelper;
	private FirstPageActivity firstPage;
	public Fragment1 fragment1;
	private Cursor cur;
	
	public String catagory;
	public String subCatagory2;
	private BitmapDecoder bitmapDecoder;
	public String packageName;
	
	String eachIngredientsImageName [];

	
	//private ViewPager mPager;
	//private MainPagerAdapter mAdapter;
	
	public int imageID;
	String ingredientsImageName;
	
	public AlertDialog.Builder alert;
	
	ListView list;
	LazyAdapter adapter;

	public FacebookConnector facebookConnector;

	private String mContent = "???";

	
	public static Fragment2 newInstance(String content) {
		Fragment2 fragment2 = new Fragment2();

	    fragment2.mContent = content;

	    return fragment2;
	}
	
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup grp, Bundle icicle) {
		
		
		View v = inf.inflate(R.layout.list_main, grp, false);
		dbHelper = new MySqliteHelper(getActivity());
        firstPage = new FirstPageActivity();
        mainCourseTab = new MainCourseTab();
        fragment1 = new Fragment1();
        bitmapDecoder = new BitmapDecoder();
        
        alert = new AlertDialog.Builder(getActivity());
        
		SharedPreferences settings = getActivity().getSharedPreferences(VARIABLES.PREFS_NAME, 0);
		if(settings.getString("SpinnerChoice", "Sort By Default").equals("Sort By Default")){
			sortOption = "Z_PK";
		}
		else if(settings.getString("SpinnerChoice", "Sort By Default").equals("Sort By Alphabet")){
			sortOption = "ZNAME";
		}
		else if(settings.getString("SpinnerChoice", "Sort By Default").equals("Sort By Preperation time")){
			sortOption = "ZTIMETOPREPARE";
		}
		
		subCatagory2 = settings.getString("SubCatagory2", "NULL");
      	catagory = settings.getString("Catagory", "No Value returned");

		fetchDishCatagory(catagory,subCatagory2,sortOption);

        this.facebookConnector = new FacebookConnector(VARIABLES.FACEBOOK_APPID, getActivity(), getActivity().getApplicationContext(), new String[] {VARIABLES.FACEBOOK_PERMISSION});
		dbHelper = new MySqliteHelper(getActivity());
		//final FirstPageActivity firstPage = new FirstPageActivity();
		

		ARRAY.bitmapImages = new Bitmap[ARRAY.gridImageName.length];

        packageName=this.getActivity().getPackageName();
        for(int i=0;i<ARRAY.gridImageName.length;i++)
        	ARRAY.bitmapImages[i] = BitmapDecoder.decodeSampledBitmapFromResource(getResources(), getResources().getIdentifier(ARRAY.gridImageName[i].toLowerCase(), "drawable", packageName), 100, 91);

		View v1 = inf.inflate(R.layout.list_main, grp, false);
		list = (ListView)v1.findViewById(android.R.id.list);
        
		return v1;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		        
		adapter=new LazyAdapter	(getActivity(), ARRAY.dishes,ARRAY.description,ARRAY.timeToPrepareString,ARRAY.bitmapImages,ARRAY.lock,ARRAY.nonVeg);
        list.setAdapter(adapter);
        adapter.setChecked(VARIABLES.unlocked);
        
       	list.setOnItemClickListener(new OnItemClickListener(){
    		String tableName;
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

    			//System.out.println("Recipe name is: "+recipeName);
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
   	   		    
   	   		    //System.out.println("The Recipe ID is: "+recipeID);
   	   		    
   	   		    if(lock.equals("0")){     //Commented for next release.
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
	   	   		    
	    			intent = new Intent(getActivity(),IngredientActivity.class);
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
	    	
			System.out.println("Before Launch");
			VARIABLES.mHelper.launchPurchaseFlow(getActivity(), VARIABLES.ITEM_SKU, 10001, mPurchaseFinishedListener,"");
	    	//startActivityForResult(new Intent(), 10001);
	    	
		}
	
	public void fetchDishCatagory(String catagory,String subCatagory, String sortOption) {
		
		// TODO Auto-generated method stub
	      	SharedPreferences settings1 = getActivity().getSharedPreferences(VARIABLES.PREFS_NAME, 0);
	      		      	
  		if(!settings1.getBoolean("NonVegCheckBoxResult", false))
  			cur  = dbHelper.getMealValues(VARIABLES.tabelName,VARIABLES.whereColumnName,catagory,subCatagory,"0",sortOption);
  		else
  			cur  = dbHelper.getMealValues(VARIABLES.tabelName,VARIABLES.whereColumnName,catagory,subCatagory,null,sortOption);
  		
  			ARRAY.recipeID = new String[cur.getCount()];
			ARRAY.recipeID = firstPage.createDishArray(cur, "Z_PK");
  		
  			ARRAY.dishes = new String[cur.getCount()];
  			ARRAY.dishes = firstPage.createDishArray(cur,VARIABLES.columnName1);
  			
  			ARRAY.description = new String[cur.getCount()];
  			ARRAY.description = firstPage.createDishArray(cur, VARIABLES.columnName2);
  			
  			ARRAY.timeToPrepare = new String[cur.getCount()];
  			ARRAY.timeToPrepare = firstPage.createDishArray(cur, VARIABLES.columnName3);
  			
  			ARRAY.lock = new String[cur.getCount()];
  			ARRAY.lock = firstPage.createDishArray(cur, VARIABLES.lockColumn);
      		
  			ARRAY.imageName = new String[cur.getCount()];
  			ARRAY.imageName = firstPage.createDishArray(cur, VARIABLES.imageNameColumn);
      		
  			ARRAY.nonVeg = new String[cur.getCount()];
  			ARRAY.nonVeg = firstPage.createDishArray(cur, VARIABLES.nonVegColumn);
      		
  			ARRAY.isFavourite = new String[cur.getCount()];
  			ARRAY.isFavourite = firstPage.createDishArray(cur, VARIABLES.isFavouriteColumn);
  			
  			ARRAY.gridImageName = new String[cur.getCount()];
  			for(int i=0;i<ARRAY.imageName.length;i++)
  				ARRAY.gridImageName[i] = "grid_"+ARRAY.imageName[i];

  			ARRAY.timeToPrepareString = new String[cur.getCount()];
  	  		
  			for(int i=0;i<ARRAY.timeToPrepare.length;i++){
  				if(ARRAY.timeToPrepare[i].equals("30"))
  					ARRAY.timeToPrepareString[i] = "Around 30 mins";
  				else if(ARRAY.timeToPrepare[i].equals("60"))
  					ARRAY.timeToPrepareString[i] = "Around an Hour";
  				else if(ARRAY.timeToPrepare[i].equals("90"))
  					ARRAY.timeToPrepareString[i] = "Around 90 mins";
  			}
  			
			/*	
  			for(int i=0;i<ARRAY.recipeID.length;i++){
  				cur1 = dbHelper.getKeyIngredients(ARRAY.recipeID[i]);
  				ARRAY.recipeKeyIngredient = new String[cur1.getCount()];
  				ARRAY.recipeKeyIngredient = firstPage.createDishArray(cur1, "ZNAME");
  				//System.out.println(ARRAY.recipeID[i]+": "+ARRAY.recipeKeyIngredient.length);
  				
  				if(ARRAY.recipeKeyIngredient.length == 1)
  					ARRAY.recipeDescription[i] = "The dish is made of "+ARRAY.recipeKeyIngredient[0].toUpperCase();
  				else if (ARRAY.recipeKeyIngredient.length == 2)
  					ARRAY.recipeDescription[i] = "The dish is made of "+ARRAY.recipeKeyIngredient[0].toUpperCase()+" and "+ARRAY.recipeKeyIngredient[1].toUpperCase();
  				else
  					ARRAY.recipeDescription[i] = " ";
  			}
  			 */ 		
      					
  			cur.close();
  			dbHelper.close();
		
	}
}
