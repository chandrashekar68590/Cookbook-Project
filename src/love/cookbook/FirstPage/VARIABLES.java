package love.cookbook.FirstPage;

import android.util.SparseBooleanArray;
import love.cookbook.FirstPage.util.IabHelper;

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
	
	//In-App Billing Variables.
	public static IabHelper mHelper;
	public static SparseBooleanArray unlocked = new SparseBooleanArray();
	public static final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAi3krxChOC+uSgSGmBx6ntPjyOdZ1i+dEGEW3bCOZH1F4JI+eeb0Xd8l/iFWlfE9db2AkjVdzFrDyGP+xPe5Wc974XjzcC8MaWrubGF3/8ceVeKO/Inn5SUMupVUqUl033yGucTLBkVn9tmij5nsN62fjdljnaMhmLDGB9NJ8opDWPX+Wp4ZKQnQkpNM6K1bcZp9nhxNu539a/I4tpTss4WXeQheWc/u+O0rzJUvBE4ADSUF70BDovoq5WguH+MPMtE/lxWieb4JNnoJW4Q2YpmS9ePTmBySXFTW8Vvy0GmQxTsRYvLG5yRlpexSXyuez4i1y2PV5QUGrNvyyFAe47wIDAQAB";
	public static final String ITEM_SKU = "android.test.purchased";

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
	public static int navigationBarHeight;
	public static int ingredientButtonWidth;
	public static int methodButtonWidth;
}
