package love.cookbook.FirstPage;

public final class VARIABLES {
	
	//Database column name variables
	public static final String tabelName="ZRECIPES";
	public static final String columnName1="ZNAME";
	public static final String columnName2="ZDESCR";
	public static final String columnName3="ZTIMETOPREPARE";
	public static final String columnName4="ZISNONVEG";
	public static final String lockColumn="ZISLOCKED";
	public static final String imageNameColumn="ZIMAGE";
	public static final String nonVegColumn="ZISNONVEG";
	public static final String isFavouriteColumn="ZISFAVOURITE";
	public static final String whereColumnName="ZCATEGORY";
	
	public static final String PREFS_NAME = "MyPrefsFile";

	//SMS variables
	public static final String queryString = "Android";
	public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	
	//Facebook variables
	public static final String FACEBOOK_APPID = "299456790162536";
	public static final String FACEBOOK_PERMISSION = "publish_stream";
	public static final String TAG = "Cookbook";
	public static final String MSG = "Message from Cookbook: ";
	public static final int NOTIFICATION_ID = 1;
	
	//id variables for options menu.
	public static final int SHARE = 1;
	public static final int FACEBOOK = 2;
	public static final int FAVOURITE = 3;
	public static final int MAIL = 4;
}
