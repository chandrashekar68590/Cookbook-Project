package love.cookbook.FirstPage;



import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import android.content.Intent;
import android.database.Cursor;
import android.os.*;
import android.view.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.*;

public class ListViewSampleActivity extends SherlockListActivity {
	LazyAdapter adapter;
	
	public String packageName;
	public int imageID;
	String ingredientsImageName;
	
	public MySqliteHelper dbHelper;
	Cursor cur;
	
	String eachIngredientsImageName [];

	String listItemName;
	
    final Runnable mUpdateFacebookNotification = new Runnable() {
        public void run() {
        	Toast.makeText(getBaseContext(), "Facebook updated !", Toast.LENGTH_LONG).show();
        }
    };
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Intent intent = getIntent();
    	
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.list_main);
		dbHelper = new MySqliteHelper(this);
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
         * This code snippet is used to fetch the image id from the resource folder from the name fetched from database.
         */
        ARRAY.image = new int[ARRAY.imageName.length];
        
        packageName=this.getPackageName();
        for(int i=0;i<ARRAY.imageName.length;i++){
        	ARRAY.image[i]=getResources().getIdentifier(ARRAY.imageName[i].toLowerCase(), "drawable", packageName);
        	//if(ARRAY.image[i]==0)
        	//	ARRAY.image[i]= 2130837507;
        }
        
       final ListView list=(ListView)findViewById(android.R.id.list);
      // list.setEnabled(false);
       
        adapter=new LazyAdapter	(this, ARRAY.dishes,ARRAY.description,ARRAY.timeToPrepare,ARRAY.image,ARRAY.lock,ARRAY.nonVeg);
        list.setAdapter(adapter);
        
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
   	   		    
   	   		    //System.out.println("The Recipe ID is: "+recipeID);
   	   		    
   	   		   // if(lock.equals("0")){		//Commented for next release
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
   	   		   /* }
   	   		    else{
   	   		    	Toast t = Toast.makeText(getApplicationContext(), "This item is locked", Toast.LENGTH_SHORT);
   	   		    	t.show();
   	   		    }*/
    		}
 		
    	});
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
}