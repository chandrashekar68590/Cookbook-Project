package love.cookbook.FirstPage;

import java.io.IOException;
import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.view.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.*;

public class FirstPageActivity extends Activity {
	
	public Button searchButton,mainCourseButton,breakfastButton,bakersDelightButton,sweetToothButton,favouriteButton,galleryButton,settingButton,notificationButton,SMSButton;
	public EditText searchEditText;
	CheckBox nonVegCheckBox;
	Spinner sortSpinner;
	AlertDialog.Builder alt_bld;
	View eulayout;
	final Context context = this;
	
	Intent intent;
	
	NotificationManager notificationManager;
	
	private MySqliteHelper dbHelper;
	private Cursor cur;
	int columnIndex;
	int cursorEnd;
	int cursorCount;
  
	private static String catagory;
	private static String tab1;
	private static String tab2;
	private static String tab3;
	private static String tab1SubCatagory;
	private static String tab2SubCatagory;
	private static String tab3SubCatagory;
	private static int noOfTabs;
	public static String sortOption;
	
	int [] position = new int[2];
	
	private static final IntentFilter intentFilter = 
	        new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
    Messenger mService = null;
    boolean mIsBound;
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
        }
    }
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //This will remove default the title bar which will be on the top of the activity 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        /*
        long heapsize = Runtime.getRuntime().totalMemory();
        long heapMaxSize = Runtime.getRuntime().maxMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory(); 
        
        System.out.println("Total Size: "+heapsize/1024+" Max: "+heapMaxSize/1024+" Free: "+heapFreeSize/1024);
         */
        
        /* For full screen app
         * getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
         */
        
        setContentView(R.layout.activity_main1);

        /*Creating a database connection.
         * The below lines of codes call the SQLiteHelper class with opens the database and 
         * copies the local database created by us to the created database.         
         */

        /*
         * This code snippet is to generate a correct key hash to be used in to connect to facebook. Unhash this if you are working in different PC and configure this new key
         * in facebook developer
        try{ 
        	System.out.println("Checking signs");
        	PackageInfo info = getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
        	for (Signature signature : info.signatures) {
        		MessageDigest md = MessageDigest.getInstance("SHA");
        		md.update(signature.toByteArray());
        		System.out.println(Base64.encodeToString(md.digest(), Base64.DEFAULT));
        	}
        } catch (NameNotFoundException e) {
        	e.printStackTrace();
        	System.out.println(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
        	e.printStackTrace();
        	System.out.println(e.getMessage());
        }
        */
        
        dbHelper = new MySqliteHelper(this);
		 try {
			 dbHelper.createDataBase();
		}catch (IOException ioe) {
			 throw new Error("Unable to create database");
		}
			  
		try {
			dbHelper.openDataBase();
		}catch(SQLException sqle){
			 throw sqle;
		}
			
		dbHelper.close();		
		
    	/*
		 * Register the Broadcast Receiver using an Intent Filter
		 */
     //   registerReceiver(receiver, intentFilter);
        
        
        /*
         * Adding listener's to all the main buttons
         */

        searchButton = (Button)findViewById(R.id.searchbutton1);
        searchButton.setOnClickListener(new SearchButtonListener());        

        mainCourseButton = (Button)findViewById(R.id.mainCourseButton);
        mainCourseButton.setOnClickListener(new MainCourseButtonListener());
        
        
        breakfastButton = (Button)findViewById(R.id.breakfastButton);
        breakfastButton.setOnClickListener(new BreakfastButtonListener());
        
        sweetToothButton = (Button)findViewById(R.id.sweetToothButton);
        sweetToothButton.setOnClickListener(new SweetToothButtonListener());
        
        bakersDelightButton = (Button)findViewById(R.id.bakersDelightButton);
        bakersDelightButton.setOnClickListener(new BakersDelightButtonListener());
        
        favouriteButton = (Button)findViewById(R.id.favouriteButton);
        favouriteButton.setOnClickListener(new FavouriteButtonListener());
        
        galleryButton = (Button)findViewById(R.id.galleryButton);
        galleryButton.setOnClickListener(new GalleryButtonListener());
        
        settingButton = (Button)findViewById(R.id.settingButton);
        settingButton.setOnClickListener(new SettingButtonListener());
   
    }
   
    
    @Override
    protected void onStop()
    {
        //unregisterReceiver(receiver);
        super.onStop();
    }
    
    protected void onDestroy(){
    	super.onDestroy();
    	unbindDrawables(findViewById(R.id.RootView));
        System.gc();
    }
    
    protected void onPause(){
    	super.onPause();
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

    /*Function to create an array from the result of 
     * select query to be used to pass it to respective activities.
     */
    public String[] createDishArray(Cursor cur,String columnName){
  		 cursorEnd = cur.getCount();
  		 cursorCount=0;
     	String[] arrayString = new String[cursorEnd];

  		 cur.moveToFirst();
  		 while(cursorCount!=cursorEnd){
	   		 columnIndex = cur.getColumnIndex(columnName);
	   		 arrayString[cursorCount] = cur.getString(columnIndex);
	   		 cur.moveToNext();
	   		 cursorCount ++;
  		 }
  		 return arrayString;

    }

    public String getSortOption(){
	    SharedPreferences settings = getSharedPreferences(VARIABLES.PREFS_NAME, 0);
	    String sortOptionToReturn = null;
			if(settings.getString("SpinnerChoice", "Sort By Default").equals("Sort By Default")){
				sortOptionToReturn = "Z_PK";
			}
			else if(settings.getString("SpinnerChoice", "Sort By Default").equals("Sort By Alphabet")){
				sortOptionToReturn = "ZNAME";
			}
			else if(settings.getString("SpinnerChoice", "Sort By Default").equals("Sort By Preperation time")){
				sortOptionToReturn = "ZTIMETOPREPARE";
			}
			
    	return sortOptionToReturn;
    }

	private class SettingButtonListener extends Object implements android.view.View.OnClickListener{
	    public SharedPreferences settings = getSharedPreferences(VARIABLES.PREFS_NAME, 0);
	    String spinnerItemSeleted;

		@Override
		public  void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			alt_bld = new AlertDialog.Builder(context);
			LayoutInflater adbInflater = LayoutInflater.from(context);
			eulayout = adbInflater.inflate(R.layout.checkbox, null);
			nonVegCheckBox = (CheckBox)eulayout.findViewById(R.id.nonVegcheckBox);
			sortSpinner = (Spinner)eulayout.findViewById(R.id.spinner1);
			sortSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					spinnerItemSeleted = parent.getItemAtPosition(position).toString();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
			ArrayAdapter myAdap = (ArrayAdapter) sortSpinner.getAdapter(); //cast to an ArrayAdapter
			int spinnerPosition = myAdap.getPosition(settings.getString("SpinnerChoice", "Sort By Default"));
	
			//alt_bld.setIcon(R.drawable.cookbook_logo);
			alt_bld.setTitle("Set Preference");
			alt_bld.setView(eulayout);
			alt_bld.setPositiveButton("Done", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   SharedPreferences.Editor editor = settings.edit();
            	      
            	      editor.putBoolean("NonVegCheckBoxResult", nonVegCheckBox.isChecked());
  					  editor.putString("SpinnerChoice", spinnerItemSeleted);
  					 

            	      // Commit the edits!
            	      editor.commit();
          			
	               }
	        });

			nonVegCheckBox.setChecked(settings.getBoolean("NonVegCheckBoxResult", false));
			sortSpinner.setSelection(spinnerPosition);
			
			AlertDialog alert = alt_bld.create();
			alert.show();

		}
	
	}
	private class MainCourseButtonListener extends Object implements android.view.View.OnClickListener{

		public void onClick(View arg0) {
			noOfTabs = 3;
			tab1 = "Indian Breads";
			tab1SubCatagory="Bread";
			
			tab2 = "Curries";
			tab2SubCatagory="Curry";
			
			tab3 = "Rice";
			tab3SubCatagory="Rice";
			
			catagory = "Meal";
			
    		intent = new Intent(FirstPageActivity.this,MainCourseTab.class);
			intent.putExtra("NUMBEROFTABS", noOfTabs);
			intent.putExtra("TAB1", tab1);
			intent.putExtra("TAB2", tab2);
			intent.putExtra("TAB3", tab3);
			intent.putExtra("CATAGORY",catagory);
			intent.putExtra("TAB1SUBCATAGORY",tab1SubCatagory);
			intent.putExtra("TAB2SUBCATAGORY",tab2SubCatagory);
			intent.putExtra("TAB3SUBCATAGORY",tab3SubCatagory);

    		startActivity(intent);
    		
    	}
	
    }
    
    private class BreakfastButtonListener extends Object implements android.view.View.OnClickListener{
        public SharedPreferences settings = getSharedPreferences(VARIABLES.PREFS_NAME, 0);

    	public void onClick(View arg0) {
    		
			noOfTabs = 1;
			tab1 = "Breakfast";
			tab1SubCatagory="Breakfast";
			catagory="Breakfast";

    		intent = new Intent(FirstPageActivity.this,MainCourseTab.class);
			intent.putExtra("NUMBEROFTABS", noOfTabs);
			intent.putExtra("TAB1", tab1);
			intent.putExtra("CATAGORY",catagory);
			intent.putExtra("TAB1SUBCATAGORY",tab1SubCatagory);

    		startActivity(intent);
    	}
	
    }
    
    
    
    private class SweetToothButtonListener extends Object implements android.view.View.OnClickListener{

        public void onClick(View arg0) {
			noOfTabs = 2;
			tab1 = "Desserts";
			tab1SubCatagory="Dessert";
			
			tab2 = "Beverages";
			tab2SubCatagory="Beverages";
			
			catagory = "Sweet Tooth!";
			
    		intent = new Intent(FirstPageActivity.this,MainCourseTab.class);
			intent.putExtra("NUMBEROFTABS", noOfTabs);
			intent.putExtra("TAB1", tab1);
			intent.putExtra("TAB2", tab2);
			intent.putExtra("CATAGORY",catagory);
			intent.putExtra("TAB1SUBCATAGORY",tab1SubCatagory);
			intent.putExtra("TAB2SUBCATAGORY",tab2SubCatagory);			

    		startActivity(intent);
    		
    	}
    	
    }
    
    private class BakersDelightButtonListener extends Object implements android.view.View.OnClickListener{

    	public void onClick(View arg0) {
			noOfTabs = 2;
			tab1 = "Cakes";
			tab1SubCatagory="Cakes";
			
			tab2 = "Muffins";
			tab2SubCatagory="Muffins";
			
			catagory = "Baker's delight";
			
    		intent = new Intent(FirstPageActivity.this,MainCourseTab.class);
			intent.putExtra("NUMBEROFTABS", noOfTabs);
			intent.putExtra("TAB1", tab1);
			intent.putExtra("TAB2", tab2);
			intent.putExtra("TAB3", tab3);
			intent.putExtra("CATAGORY",catagory);
			intent.putExtra("TAB1SUBCATAGORY",tab1SubCatagory);
			intent.putExtra("TAB2SUBCATAGORY",tab2SubCatagory);
			intent.putExtra("TAB3SUBCATAGORY",tab3SubCatagory);

    		startActivity(intent);
    	}
	
    }
    
    private class FavouriteButtonListener extends Object implements android.view.View.OnClickListener{
        public SharedPreferences settings = getSharedPreferences(VARIABLES.PREFS_NAME, 0);

    	public void onClick(View arg0) {
			catagory="Breakfast";
			sortOption = getSortOption();
			
			if(!settings.getBoolean("NonVegCheckBoxResult", false))
				cur  = dbHelper.getUpdatedValue(sortOption,"0");
			else
				cur  = dbHelper.getUpdatedValue(sortOption,null);
			
			ARRAY.recipeID = new String[cur.getCount()];
  			ARRAY.recipeID = createDishArray(cur, "Z_PK");
  			
			ARRAY.dishes = new String[cur.getCount()];
			ARRAY.dishes = createDishArray(cur,VARIABLES.columnName1);
    		
			ARRAY.description = new String[cur.getCount()];
			ARRAY.description = createDishArray(cur, VARIABLES.columnName2);
    		
			ARRAY.timeToPrepare = new String[cur.getCount()];
			ARRAY.timeToPrepare = createDishArray(cur, VARIABLES.columnName3);
    		
			ARRAY.lock = new String[cur.getCount()];
			ARRAY.lock = createDishArray(cur, VARIABLES.lockColumn);
    		
			ARRAY.imageName = new String[cur.getCount()];
			ARRAY.imageName = createDishArray(cur, VARIABLES.imageNameColumn);
    		
			ARRAY.nonVeg = new String[cur.getCount()];
			ARRAY.nonVeg = createDishArray(cur, VARIABLES.nonVegColumn);
    		
			ARRAY.isFavourite = new String[cur.getCount()];
			ARRAY.isFavourite = createDishArray(cur, VARIABLES.isFavouriteColumn);
			
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
    		
    		cur.close();
    		
    		intent = new Intent(FirstPageActivity.this,ListViewSampleActivity.class);
			intent.putExtra("DISHES", ARRAY.dishes);
			intent.putExtra("DESCRIPTION", ARRAY.description);
			intent.putExtra("TIMETOPREPARE", ARRAY.timeToPrepareString);
	        intent.putExtra("LOCKVALUE", ARRAY.lock);
	        intent.putExtra("IMAGENAME", ARRAY.gridImageName);
	        intent.putExtra("NONVEG",ARRAY.nonVeg);
	        intent.putExtra("ISFAVOURITE", ARRAY.isFavourite);

    		startActivity(intent);
    	}
	
    }
    
    private class GalleryButtonListener extends Object implements android.view.View.OnClickListener{
    	
    	public void onClick(View arg0) {
    		intent = new Intent(FirstPageActivity.this,GalleryActivity.class);
    		
    		startActivity(intent);
    	}
	
    }
    
    private class SearchButtonListener implements android.view.View.OnClickListener{
        public SharedPreferences settings = getSharedPreferences(VARIABLES.PREFS_NAME, 0);

        public void onClick(View arg0) {
			
			int countOfCursor;
			System.out.println("Search button clicked");
									
	        searchEditText = (EditText)findViewById(R.id.searchEditText1);
			String searchText = searchEditText.getText().toString();
			sortOption = getSortOption();
			if(searchText.length()!=0){
				
				if(!settings.getBoolean("NonVegCheckBoxResult", false))
					cur=dbHelper.getSearchResult(VARIABLES.tabelName,VARIABLES.columnName1,searchText,sortOption,"0");
				else
					cur=dbHelper.getSearchResult(VARIABLES.tabelName,VARIABLES.columnName1,searchText,sortOption,null);

				countOfCursor = cur.getCount();
				
				if(countOfCursor!=0){
					
					ARRAY.recipeID = new String[cur.getCount()];
					ARRAY.recipeID = createDishArray(cur, "Z_PK");
					
					ARRAY.dishes = new String[countOfCursor];
					ARRAY.dishes = createDishArray(cur,VARIABLES.columnName1);
		    		
					ARRAY.description = new String[countOfCursor];
					ARRAY.description = createDishArray(cur, VARIABLES.columnName2);
		    		
					ARRAY.timeToPrepare = new String[countOfCursor];
					ARRAY.timeToPrepare = createDishArray(cur, VARIABLES.columnName3);
		    		
					ARRAY.lock = new String[cur.getCount()];
					ARRAY.lock = createDishArray(cur, VARIABLES.lockColumn);
		    		
					ARRAY.imageName = new String[cur.getCount()];
					ARRAY.imageName = createDishArray(cur, VARIABLES.imageNameColumn);
		    		
					ARRAY.nonVeg = new String[cur.getCount()];
					ARRAY.nonVeg = createDishArray(cur, VARIABLES.nonVegColumn);
		    		
					ARRAY.isFavourite = new String[cur.getCount()];
					ARRAY.isFavourite = createDishArray(cur, VARIABLES.isFavouriteColumn);
					
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
			
		    		cur.close();
		    		dbHelper.close();
	
		    		intent = new Intent(FirstPageActivity.this,ListViewSampleActivity.class);
					intent.putExtra("DISHES", ARRAY.dishes);
					intent.putExtra("DESCRIPTION", ARRAY.description);
					intent.putExtra("TIMETOPREPARE", ARRAY.timeToPrepareString);
			        intent.putExtra("LOCKVALUE", ARRAY.lock);
			        intent.putExtra("IMAGENAME", ARRAY.gridImageName);
			        intent.putExtra("NONVEG",ARRAY.nonVeg);
			        intent.putExtra("ISFAVOURITE", ARRAY.isFavourite);

		    		startActivity(intent);
				}else{
					Toast t = Toast.makeText(getApplicationContext(), "The dish name entered could not be found.", Toast.LENGTH_SHORT);
					t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
					t.show();
				}
					
			}
			else{
				Toast t = Toast.makeText(getApplicationContext(), "Please enter a search text", Toast.LENGTH_SHORT);
				t.show();
			}
		}
    }
}