package love.cookbook.FirstPage;

import java.util.List;

import android.support.v4.app.*;

public class MainPagerAdapter extends FragmentPagerAdapter{
	private List<Fragment> mFragments;
	private String[] titles ;
	private int mCount;

	public MainPagerAdapter(FragmentManager fm, List<Fragment> f,String tab1,String tab2,String tab3) {
		super(fm);
		mFragments = f;
		if(tab2 == null && tab3 == null)
			titles = new String [] {tab1};
		else if (tab3 == null)
			titles = new String [] {tab1,tab2};
		else
			titles = new String [] {tab1,tab2,tab3};
		mCount = titles.length;

		
	}

	public MainPagerAdapter(FragmentManager fm){
		super(fm);
	}	
	
	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}


}
