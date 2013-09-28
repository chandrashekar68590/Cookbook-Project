package love.cookbook.FirstPage;

import java.io.*;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.*;

public class MySqliteHelper extends SQLiteOpenHelper{
	 //The Android's default system path of your application database.
	private static String DB_PATH = "/data/data/love.cookbook.FirstPage/databases/";
	private static String DB_NAME = "CookBook";
	 
	private SQLiteDatabase myDataBase;
	Cursor cur;
	private final Context myContext;
	 
	/**
	  * Constructor
	  * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
	  * @param context
	  */
	public MySqliteHelper(Context context) {
	 
		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}	
	 
	/**
	  * Creates a empty database on the system and rewrites it with your own database.
	  * */
	public void createDataBase() throws IOException{
	 
	boolean dbExist = checkDataBase();
	 
	if(dbExist){
	//do nothing - database already exist
	}else{
	 
	//By calling this method and empty database will be created into the default system path
	//of your application so we are gonna be able to overwrite that database with our database.
	this.getReadableDatabase();
	 
	try {
	 
	copyDataBase();
	 
	} catch (IOException e) {
	 
	throw new Error("Error copying database");
	 
	}
	}
	 
	}
	 
	/**
	  * Check if the database already exist to avoid re-copying the file each time you open the application.
	  * @return true if it exists, false if it doesn't
	  */
	private boolean checkDataBase(){
	 
	SQLiteDatabase checkDB = null;
	 
	try{
	String myPath = DB_PATH + DB_NAME;
	checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
	}catch(SQLiteException e){
	 
		 System.out.println("No database exists"+e);
	 
	}
	 
	if(checkDB != null){
	 
	checkDB.close();
	 
	}
	 
	return checkDB != null ? true : false;
	}
	 
	/**
	  * Copies your database from your local assets-folder to the just created empty database in the
	  * system folder, from where it can be accessed and handled.
	  * This is done by transfering bytestream.
	  * */
	private void copyDataBase() throws IOException{
	//Open your local db as the input stream
	InputStream myInput = myContext.getAssets().open(DB_NAME);

	// Path to the just created empty db
	String outFileName = DB_PATH + DB_NAME;
	 
	//Open the empty db as the output stream
	OutputStream myOutput = new FileOutputStream(outFileName);
	 
	//transfer bytes from the inputfile to the outputfile
	byte[] buffer = new byte[1024];
	int length;
	while ((length = myInput.read(buffer))>0){
	myOutput.write(buffer, 0, length);
	}
	
	 
	//Close the streams
	myOutput.flush();
	myOutput.close();
	myInput.close();
	 
	}
	 
	public void openDataBase() throws SQLException{
	 
	//Open the database
	String myPath = DB_PATH + DB_NAME;
	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
	 
	}
	 
	@Override
	public synchronized void close() {
	 
	if(myDataBase != null)
	myDataBase.close();
	 
	super.close();
	 
	}
	 
	@Override
	public void onCreate(SQLiteDatabase db) {
	 
	}
	 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	 
	}
	 
	public Cursor getTableValues(String tableName,String whereColumnName,String catogory,String isNonVeg,String sortOption){
		myDataBase = this.getReadableDatabase();
		if(isNonVeg != null)
			cur=myDataBase.rawQuery("SELECT *  from "+tableName+" where "+whereColumnName+" = ? AND ZISNONVEG = ? ORDER BY "+sortOption 
					 ,new String [] {catogory,isNonVeg});
		else 
			cur=myDataBase.rawQuery("SELECT *  from "+tableName+" where "+whereColumnName+" = ? ORDER BY "+sortOption 
					 ,new String [] {catogory});

		return cur;
		
	}
	
	public Cursor getMealValues(String tableName,String whereColumnName,String catagory,String subCatagory,String isNonVeg,String sortOption){
		myDataBase = this.getReadableDatabase();
		if(isNonVeg!= null)
			cur=myDataBase.rawQuery("SELECT *  from "+tableName+" where "+whereColumnName+" = ? AND ZSUBCATEGORY = ? AND ZISNONVEG = ? ORDER BY "+sortOption
					 ,new String [] {catagory,subCatagory,isNonVeg});
		else
			cur=myDataBase.rawQuery("SELECT *  from "+tableName+" where "+whereColumnName+" = ? AND ZSUBCATEGORY = ? ORDER BY "+sortOption
					 ,new String [] {catagory,subCatagory});

		return cur;
		
	}
	
	public Cursor getKeyIngredients(String recipeID){
		myDataBase = this.getReadableDatabase();
		cur = myDataBase.rawQuery("SELECT ZNAME FROM ZINGREDIENTS where ZRECIPE=? AND ZISKEY=1", new String[]{recipeID});
		return cur;
	}

	@SuppressLint("ParserError")
	public Cursor getIngredients(String id,String isNonVeg){
		myDataBase =this.getReadableDatabase();
		if(id !=null)
			cur = myDataBase.rawQuery("SELECT * FROM ZINGREDIENTS where ZRECIPE=? ORDER BY ZISKEY DESC"
				, new String []{id});
		else if(isNonVeg !=null)
			cur = myDataBase.rawQuery("SELECT * FROM ZRECIPES WHERE ZISNONVEG = ?",new String [] {isNonVeg});
		else
			cur = myDataBase.rawQuery("SELECT * FROM ZRECIPES",null);
		
		return cur;
	}
	
	
	public Cursor getSearchResult(String tableName,String whereColumnName,String columnValue,String sortOption,String isNonVeg){
		myDataBase =this.getReadableDatabase();
		if(isNonVeg != null)
			cur=myDataBase.rawQuery("SELECT * FROM "+tableName+" where "+whereColumnName+" LIKE '%' || ? || '%' AND ZISNONVEG = ? ORDER BY "+sortOption
				 ,new String [] {columnValue,isNonVeg});
		else
			cur=myDataBase.rawQuery("SELECT * FROM "+tableName+" where "+whereColumnName+" LIKE '%' || ? || '%' ORDER BY "+sortOption
					 ,new String [] {columnValue});
		return cur;
	}
	
	public Cursor getPreperationSteps(String id){
		myDataBase =this.getReadableDatabase();
		cur = myDataBase.rawQuery("SELECT * FROM ZPREPARATIONSTEPS where ZRECIPE=? ORDER BY ZSEQUENCE"
				, new String []{id});
		return cur;
		
	}
	
	public Cursor getTimeToPrepare(String step){
		myDataBase = this.getReadableDatabase();
		cur = myDataBase.rawQuery("SELECT ZTIME FROM ZPREPARATIONSTEPS where ZCIRCLE1=?"
				, new String []{step});
		return cur;
	}
	
	public Cursor getUpdatedValue(String sortOption,String isNonVeg){
		myDataBase = this.getReadableDatabase();
		if(isNonVeg != null)
			cur = myDataBase.rawQuery("SELECT * FROM ZRECIPES WHERE ZISFAVOURITE=1 AND ZISNONVEG = ? ORDER BY "+sortOption,new String[]{isNonVeg});
		else
			cur = myDataBase.rawQuery("SELECT * FROM ZRECIPES WHERE ZISFAVOURITE=1 ORDER BY "+sortOption,null);

		return cur;
	}
	
	
	public Cursor getIsFavourite(String recipeName){
		myDataBase = this.getReadableDatabase();
		cur = myDataBase.rawQuery("SELECT ZISFAVOURITE FROM ZRECIPES WHERE ZNAME=?",new String []{recipeName});
		return cur;
	}
	
	public int updateTable(String dishName,int updateValue){
		String columnName="ZISFAVOURITE";
		String tableName = "ZRECIPES";
		myDataBase = this.getWritableDatabase();
		ContentValues args = new ContentValues();
		args.put(columnName,updateValue);
		return myDataBase.update(tableName, args, "ZNAME = '"+dishName+ "'", null);
		
	}
}
