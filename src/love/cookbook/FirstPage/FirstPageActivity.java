package love.cookbook.FirstPage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
	private MethodActivity methodActivity;
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

    /*
    BroadcastReceiver receiver = new BroadcastReceiver() {
    	
		@Override
		public void onReceive(Context _context, Intent _intent) {
			// TODO Auto-generated method stub
			SmsManager sms = SmsManager.getDefault();
			Bundle bundle = _intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++)
					messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				for (SmsMessage message : messages) {
					String msg = message.getMessageBody();
					
					System.out.println("Message Received is: "+ msg);
					String to = message.getOriginatingAddress();
					System.out.println("Received message from: "+ to);
					String cutString = to.substring(7,11);
					System.out.println("Cut string is: "+ cutString);
					
					if (msg.equalsIgnoreCase(VARIABLES.queryString)) {
						System.out.println("Inside IF");
						String out = "I Received your message";
						sms.sendTextMessage("5556", null, out, null, null);
						//Intent i = new Intent(FirstPageActivity.this,SimpleActivity.class);
						//startActivity(i);
					}
				}
			}
			
		}
    	
    };*/
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //This will remove default the title bar which will be on the top of the activity 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        methodActivity = new MethodActivity();
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
         /*
        notificationButton = (Button)findViewById(R.id.notificationButton);
        notificationButton.setOnClickListener(new NotificationButtonListener());
        
        SMSButton = (Button)findViewById(R.id.SMSbutton);
        SMSButton.setOnClickListener(new SMSButtonListener());
        */
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

    
    /*
    private class SMSButtonListener extends Object implements android.view.View.OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			SmsManager smsManager = SmsManager.getDefault();
			String sendTo = "07875067527";
			String myMessage = "Android supports programmatic SMS messaging!";
			smsManager.sendTextMessage(sendTo, null, myMessage, null, null);
			Toast t = Toast.makeText(getApplicationContext(), "SMS Sent", Toast.LENGTH_SHORT);
			t.show();
			
		}
    
    }
    private class NotificationButtonListener extends Object implements android.view.View.OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			int icon = R.drawable.ic_launcher;
			String tickerText = "New Recipe added";
			long when = System.currentTimeMillis();
			newNotification = new Notification(icon, tickerText, when);
			long[] vibrate = new long[] { 1000, 1000, 1000};
			
			//Setting different notification properties.
			newNotification.number++; 
			newNotification.vibrate = vibrate;
			newNotification.ledARGB = Color.RED;
			newNotification.ledOffMS = 0;
			newNotification.ledOnMS = 1;
			newNotification.flags = newNotification.flags | Notification.FLAG_SHOW_LIGHTS;
			
			String svcName = Context.NOTIFICATION_SERVICE;
			notificationManager = (NotificationManager)getSystemService(svcName);
			Context context = getApplicationContext();
			String expandedText = "This is an extended text to be displayed in the bottom";
			String expandedTitle = "New recipe";
			
			Intent startActivityIntent = new Intent(FirstPageActivity.this, SimpleActivity.class);
			PendingIntent launchIntent = PendingIntent.getActivity(context,0,startActivityIntent,0);
			
			newNotification.setLatestEventInfo(context, expandedTitle, expandedText, launchIntent);
			
			notificationManager.notify(NOTIFICATION_ID, newNotification);
					
		}
    	
    }
    */
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
    		Toast t = Toast.makeText(getApplicationContext(), "Main Course Activity", Toast.LENGTH_SHORT);
			t.show();
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
    		Toast t = Toast.makeText(getApplicationContext(), "Breakfast Activity", Toast.LENGTH_SHORT);
			t.show();
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
    		Toast t = Toast.makeText(getApplicationContext(), "SweetTooth Activity", Toast.LENGTH_SHORT);
			t.show();
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
    		Toast t = Toast.makeText(getApplicationContext(), "Baker's Delight Activity", Toast.LENGTH_SHORT);
			t.show();
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
  			
				
			/*
  			for(int i=0;i<ARRAY.recipeID.length;i++){
  				cur1 = dbHelper.getKeyIngredients(ARRAY.recipeID[i]);
  				ARRAY.recipeKeyIngredient = new String[cur1.getCount()];
  				ARRAY.recipeKeyIngredient = createDishArray(cur1, "ZNAME");
  				//System.out.println(ARRAY.recipeID[i]+": "+ARRAY.recipeKeyIngredient.length);
  				
  				if(ARRAY.recipeKeyIngredient.length == 1)
  					ARRAY.recipeDescription[i] = "The dish is made of "+ARRAY.recipeKeyIngredient[0].toUpperCase();
  				else if (ARRAY.recipeKeyIngredient.length == 2)
  					ARRAY.recipeDescription[i] = "The dish is made of "+ARRAY.recipeKeyIngredient[0].toUpperCase()+" and "+ARRAY.recipeKeyIngredient[1].toUpperCase();
  				else
  					ARRAY.recipeDescription[i] = " ";
  			}
  			 */ 			
  			//cur1.close();
    		
    		cur.close();
    		
    		intent = new Intent(FirstPageActivity.this,ListViewSampleActivity.class);
    		Toast t = Toast.makeText(getApplicationContext(), "Favourite Activity", Toast.LENGTH_SHORT);
			t.show();
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
    		
    		Toast t = Toast.makeText(getApplicationContext(), "Gallery Activity", Toast.LENGTH_SHORT);
			t.show();
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
					
					/*
					ARRAY.recipeDescription = new String[ARRAY.recipeID.length];
					
		  			for(int i=0;i<ARRAY.recipeID.length;i++){
		  				cur1 = dbHelper.getKeyIngredients(ARRAY.recipeID[i]);
		  				ARRAY.recipeKeyIngredient = new String[cur1.getCount()];
		  				ARRAY.recipeKeyIngredient = createDishArray(cur1, "ZNAME");
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