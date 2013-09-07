package love.cookbook.FirstPage;

import java.io.IOException;
import java.net.MalformedURLException;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.*;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.*;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class IngredientActivity extends SherlockActivity {
	IngredienceLazyAdapter adapter;
	public int imageID;
	public int noOfSteps;
	int listViewTouchAction;
	
	String eachIngredientsImageName [];
	int eachImageID [];
	public String packageName;

	
	private final Handler mFacebookHandler = new Handler();
	private FacebookConnector facebookConnector;
	
    final Runnable mUpdateFacebookNotification = new Runnable() {
        public void run() {
        	Toast.makeText(getBaseContext(),  "Posted on Facebook wall!", Toast.LENGTH_LONG).show();
        	
        	
        }
    };
    
	Cursor cur;
	String recipeID,recipeName,serving;
	MySqliteHelper dbHelper;
	final FirstPageActivity firstPage = new FirstPageActivity();
	 
	 
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
   	
	    dbHelper = new MySqliteHelper(this);
	    
	    cur=dbHelper.getIsFavourite(recipeName);
	    
 		 cur.moveToFirst();
 		 int isFavouriteValue = cur.getInt(cur.getColumnIndex("ZISFAVOURITE"));
	    
	    
	   	SubMenu subMenu = menu.addSubMenu("Add");
	
	   	subMenu.add(0, VARIABLES.FACEBOOK, 0,"Share on FB")
	   	.setIcon(R.drawable.fb);
	   	
	   	subMenu.add(0, VARIABLES.SHARE,0,"Share Via Message")
	   	.setIcon(R.drawable.message_icon);
	   	
	   	subMenu.add(0,VARIABLES.MAIL,0,"Share Via Mail")
	   	.setIcon(R.drawable.message_icon);
	   	
	   	if(isFavouriteValue == 0)
	   		subMenu.add(0, VARIABLES.FAVOURITE,0,"Mark as Favourite")
	   		.setIcon(R.drawable.message_icon);
	   	
	   	else
	   		subMenu.add(0, VARIABLES.FAVOURITE,0,"My Favourite")
	   		.setIcon(R.drawable.message_icon);
	   		
	   	MenuItem subMenu1Item = subMenu.getItem();
	   	subMenu1Item.setIcon(R.drawable.abs__ic_menu_share_holo_dark);
	   	subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	   	
	   	cur.close();
	   	dbHelper.close();
	
			/*
			//Create the search view
	       SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());
	       searchView.setQueryHint("Search for Recipes");
	*/
	   	
	
	       return super.onCreateOptionsMenu(menu);
   }
   

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
   	
       switch (item.getItemId()) {
       case VARIABLES.FACEBOOK:
    	   postMessage();
           return true;
           
       case VARIABLES.SHARE:
    	   Intent sendIntent = new Intent(Intent.ACTION_VIEW);
	         sendIntent.putExtra("sms_body", "Hi, i found a way to cook delicious foods.You should try it too. Download app from: https://play.google.com/store/apps");
	         sendIntent.setType("vnd.android-dir/mms-sms");
	         startActivity(sendIntent);
	         return true;
	       
       case VARIABLES.MAIL:
    	   System.out.println("Mail");
    	   Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
    	   emailIntent.setType("message/rfc822");
    	  // emailIntent.setType("image/jpeg");
    	   emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Cookbook App");
    	   emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<b>Hi, i found a way to cook delicious foods.You should try it too.</b> Download app from: https://play.google.com/store/apps"));
    	   //emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, R.drawable.message_icon);
    	   emailIntent.putExtra("sms_body", "Hi, i found a way to cook delicious foods.You should try it too. Download app from: https://play.google.com/store/apps");
    	   startActivity(Intent.createChooser(emailIntent, "Share Via"));
    	   return true;
	         
       case VARIABLES.FAVOURITE:
    	   dbHelper = new MySqliteHelper(this);
    	   
    	   cur = dbHelper.getIsFavourite(recipeName);
	  		 cur.moveToFirst();
	  		 
	  		 int isFavouriteValue = cur.getInt(cur.getColumnIndex("ZISFAVOURITE"));
	  		 
	  		 if(isFavouriteValue==0)
	  			 dbHelper.updateTable(recipeName,1);
	  		 else 
	  			 dbHelper.updateTable(recipeName,0);
    	   
    	   return true;
    	   
       case android.R.id.home:	
    	   /*This will end the current activity and loads previous activity which called this
    	    * activity.
    	    */
    	   finish();
    	   return true;
       
       default:
           return super.onOptionsItemSelected(item);
       }
   }
   
    public void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
        
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.list_ingredients);
        
        /*
         * This will enable back button image in action bar home button.
         */
        com.actionbarsherlock.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        
        this.facebookConnector = new FacebookConnector(VARIABLES.FACEBOOK_APPID, this, getApplicationContext(), new String[] {VARIABLES.FACEBOOK_PERMISSION});
        final Context c = this;
        
        Intent intent = getIntent();
        ARRAY.ingredients = intent.getStringArrayExtra("INGREDIENTS");
        ARRAY.ingredientsHindiname = intent.getStringArrayExtra("HINDINAME");
        ARRAY.quantity = intent.getStringArrayExtra("QUANTITY");
        eachIngredientsImageName = intent.getStringArrayExtra("EACHINGREDIENTSIMAGENAME");
        recipeID = intent.getStringExtra("RECIPE_ID");
        recipeName = intent.getStringExtra("RECIPE_NAME");
        imageID = intent.getIntExtra("IMAGEID", 0);
        serving = intent.getStringExtra("SERVING");
        
       
        
        eachImageID = new int[eachIngredientsImageName.length];

        packageName=this.getPackageName();
        for(int i=0;i<eachIngredientsImageName.length;i++)
        	eachImageID[i]=getResources().getIdentifier(eachIngredientsImageName[i].toLowerCase(), "drawable", packageName);
        

        final ListView list=(ListView)findViewById(R.id.listView1);

        View header1 =  getLayoutInflater().inflate(R.layout.list_header_view, null, false);
        list.addHeaderView(header1, null, false);
        
        ImageView image1 = (ImageView)header1.findViewById(R.id.imageView1);
        image1.setImageResource(imageID);

        adapter=new IngredienceLazyAdapter(this, ARRAY.ingredients,ARRAY.ingredientsHindiname,ARRAY.quantity,eachImageID);
        list.setAdapter(adapter);
        
        TextView textview1 = (TextView)findViewById(R.id.recipeTextView);
        textview1.setText(recipeName);
        
        TextView textView2 = (TextView)findViewById(R.id.servingTextView);
        textView2.setText(serving);

        
        
        Button methodButton = (Button)findViewById(R.id.methodButton);
        methodButton.setOnClickListener(new MethodButtonListener());

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
    
    private class MethodButtonListener extends Object implements android.view.View.OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			cur = dbHelper.getPreperationSteps(recipeID);
			noOfSteps = cur.getCount();
			ARRAY.preparationSteps = firstPage.createDishArray(cur, "ZCIRCLE1");
			ARRAY.timeToPrepare= firstPage.createDishArray(cur, "ZTIME");
			ARRAY.arrowValue= firstPage.createDishArray(cur, "ZARROW");
					
			cur.close();
			dbHelper.close();
			
			Intent intent = new Intent(IngredientActivity.this,MethodActivity.class);
			intent.putExtra("STEPS", ARRAY.preparationSteps);
			intent.putExtra("TIMETOPREPARE", ARRAY.timeToPrepare);
			intent.putExtra("ARROW", ARRAY.arrowValue);
			intent.putExtra("NUMBEROFSTEPS", noOfSteps);
			startActivity(intent);
			
			/*
			Intent intent = new Intent(IngredientActivity.this,SampleMethodActivity.class);
			startActivity(intent);
			*/
			
		}
    	
		
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		this.facebookConnector.getFacebook().authorizeCallback(requestCode, resultCode, data);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		updateLoginStatus();
	}
	
	public void updateLoginStatus() {
		//loginStatus.setText("Logged into Facebook : " + facebookConnector.getFacebook().isSessionValid());
	}
	 
	private String getFacebookMsg() {
		return "#Testing my app: I tried preparing "+ recipeName+ " today through CookBook."+" Try CookBook app. Its cool. https://play.google.com/store/apps";
		//+ new Date().toLocaleString();
	}	
	
	public void postMessage() {
		System.out.println("Inside post message");
		
		if (facebookConnector.getFacebook().isSessionValid()) {
			postMessageInThread();
		} else {
			SessionEvents.AuthListener listener = new SessionEvents.AuthListener() {
				
				@Override
				public void onAuthSucceed() {
					postMessageInThread();
				}
				
				@Override
				public void onAuthFail(String error) {
					
				}
			};
			SessionEvents.addAuthListener(listener);
			facebookConnector.login();
		}
	}

	private void postMessageInThread() {
		Thread t = new Thread() {
			public void run() {
		    	
		    	try {
		    		facebookConnector.postMessageOnWall(getFacebookMsg());
					mFacebookHandler.post(mUpdateFacebookNotification);
				} catch (Exception ex) {
					Log.e(VARIABLES.TAG, "Error sending msg",ex);
				}
		    }
		};
		t.start();
	}

	private void clearCredentials() {
		try {
			facebookConnector.getFacebook().logout(getApplicationContext());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
