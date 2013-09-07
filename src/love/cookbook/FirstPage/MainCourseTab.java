package love.cookbook.FirstPage;


import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.widget.*;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.viewpagerindicator.TitlePageIndicator;

@SuppressLint("ParserError")
public class MainCourseTab extends SherlockFragmentActivity{

	private String catagory = "NULLLLLL";

	private MySqliteHelper dbHelper;
	private FirstPageActivity firstPage;
	private Cursor cur;
	
	
	
	
	public String sortOption;
	public int noOfTabs;
	public String tab1;
	public String tab2;
	public String tab3;
	private String tab1SubCatagory;
	private String tab2SubCatagory;
	private String tab3SubCatagory;
	
	Intent intent;
	SharedPreferences settings;
	
	
	//Variables used for scroll tabs
	
	
	private ViewPager mPager;
	private MainPagerAdapter mAdapter;
	private TitlePageIndicator mIndicator;
	private List<Fragment> mFragments;
	
	private static final String FRAGMENT1 = Fragment1.class.getName();
	private static final String FRAGMENT2 = Fragment2.class.getName();
	private static final String FRAGMENT3 = Fragment3.class.getName();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        dbHelper = new MySqliteHelper(this);
        firstPage = new FirstPageActivity();
                
        Intent intent = getIntent();  // Reusable Intent for each tab
        
        //This will set the back arrow key with the home icon in action bar Sherlock
     //   com.actionbarsherlock.app.ActionBar ab = getSupportActionBar();
     //   ab.setDisplayHomeAsUpEnabled(true);

        noOfTabs=intent.getIntExtra("NUMBEROFTABS", 0);
        tab1=intent.getStringExtra("TAB1");
        tab2=intent.getStringExtra("TAB2");
        tab3=intent.getStringExtra("TAB3");
        catagory=intent.getStringExtra("CATAGORY");
        tab1SubCatagory=intent.getStringExtra("TAB1SUBCATAGORY");
        tab2SubCatagory=intent.getStringExtra("TAB2SUBCATAGORY");
        tab3SubCatagory=intent.getStringExtra("TAB3SUBCATAGORY");
        
	    SharedPreferences settings1 = getSharedPreferences(VARIABLES.PREFS_NAME, 0);

        SharedPreferences.Editor editor = settings1.edit();
        editor.putString("Catagory", catagory);
        editor.putString("SubCatagory1", tab1SubCatagory);
        editor.putString("SubCatagory2", tab2SubCatagory);
        editor.putString("SubCatagory3", tab3SubCatagory);
        editor.commit();

        SharedPreferences settings = getSharedPreferences(VARIABLES.PREFS_NAME, 0);
        
			if(settings.getString("SpinnerChoice", "Sort By Default").equals("Sort By Default")){
				sortOption = "Z_PK";
			}
			else if(settings.getString("SpinnerChoice", "Sort By Default").equals("Sort By Alphabet")){
				sortOption = "ZNAME";
			}
			else if(settings.getString("SpinnerChoice", "Sort By Default").equals("Sort By Preperation time")){
				sortOption = "ZTIMETOPREPARE";
			}
        
     // add fragments
	        mFragments = new ArrayList<Fragment>();

			if(noOfTabs == 1){
				mFragments.add(Fragment.instantiate(this, FRAGMENT1));
				mAdapter = new MainPagerAdapter(getSupportFragmentManager(), mFragments,tab1,null,null);
			}
			
	        if(noOfTabs == 2){
	        	
	        	mFragments.add(Fragment.instantiate(this, FRAGMENT1));				
	        	mFragments.add(Fragment.instantiate(this, FRAGMENT2));
        		mAdapter = new MainPagerAdapter(getSupportFragmentManager(), mFragments,tab1,tab2,null);
        	}

     		if(noOfTabs==3){
     			
     			mFragments.add(Fragment.instantiate(this, FRAGMENT1));				
	        	mFragments.add(Fragment.instantiate(this, FRAGMENT2));        		
     			mFragments.add(Fragment.instantiate(this, FRAGMENT3));
     			mAdapter = new MainPagerAdapter(getSupportFragmentManager(), mFragments,tab1,tab2,tab3);

     		}
     		
     		// adapter
     		
     		// pager
     		mPager = (ViewPager) findViewById(R.id.view_pager);
     		mPager.setAdapter(mAdapter);

     		mIndicator = (TitlePageIndicator) findViewById(R.id.title_indicator);
     		mIndicator.setViewPager(mPager);
        
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Create the search view
		settings = getSharedPreferences(VARIABLES.PREFS_NAME, 0);
		
        SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setQueryHint("Search for Recipes");
        
        menu.add("")
        .setIcon(R.drawable.ic_action_search)
        .setActionView(searchView)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        
        
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				
				int countOfCursor;

				if(settings.getString("SpinnerChoice", "Sort By Default").equals("Sort By Default")){
					sortOption = "Z_PK";
				}
				else if(settings.getString("SpinnerChoice", "Sort By Default").equals("Sort By Alphabet")){
					sortOption = "ZNAME";
				}
				else if(settings.getString("SpinnerChoice", "Sort By Default").equals("Sort By Preperation time")){
					sortOption = "ZTIMETOPREPARE";
				}
				
				if(query.length()!=0){
    				
    				if(!settings.getBoolean("NonVegCheckBoxResult", false))
    					cur=dbHelper.getSearchResult(VARIABLES.tabelName,VARIABLES.columnName1,query,sortOption,"0");
    				else
    					cur=dbHelper.getSearchResult(VARIABLES.tabelName,VARIABLES.columnName1,query,sortOption,null);

    				countOfCursor = cur.getCount();
    				
    				if(countOfCursor!=0){
    					
    					ARRAY.recipeID = new String[cur.getCount()];
    					ARRAY.recipeID = firstPage.createDishArray(cur, "Z_PK");
    					
    		    		ARRAY.dishes = new String[countOfCursor];
    		    		ARRAY.dishes = firstPage.createDishArray(cur,VARIABLES.columnName1);
    		    		
    		    		ARRAY.description = new String[countOfCursor];
    		    		ARRAY.description = firstPage.createDishArray(cur, VARIABLES.columnName2);
    		    		
    		    		ARRAY.timeToPrepare = new String[countOfCursor];
    		    		ARRAY.timeToPrepare = firstPage.createDishArray(cur, VARIABLES.columnName3);
    		    		
    		    		ARRAY.lock = new String[cur.getCount()];
    		    		ARRAY.lock = firstPage.createDishArray(cur,VARIABLES.lockColumn);
    		    		
    		    		ARRAY.imageName = new String[cur.getCount()];
    		    		ARRAY.imageName = firstPage.createDishArray(cur, VARIABLES.imageNameColumn);
    		    		
    		    		ARRAY.nonVeg = new String[cur.getCount()];
    		    		ARRAY.nonVeg = firstPage.createDishArray(cur,VARIABLES.nonVegColumn);
    		    		
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
    		  				
    		  				System.out.println("Time: "+ARRAY.timeToPrepareString);
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
    		    	
    	
    		    		intent = new Intent(getApplicationContext(),ListViewSampleActivity.class);
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
    				t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
    				t.show();
    			}
    			
            	return false; 
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
		});
        
        return super.onCreateOptionsMenu(menu);
	}

	
	 @Override   
	 public void onBackPressed(){
		 super.onBackPressed();
		
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
}
