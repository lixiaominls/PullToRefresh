package com.xinmai.pulltorefresh;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xinmai.pulltorefresh.ui.PullToRefreshListView;
import com.xinmai.pulltorefresh.ui.PullToRefreshListView.PullToRefreshAsyncTask;
import com.xinmai.pulltorefresh.R;

public class MainActivity extends Activity {
	private PullToRefreshListView mListView;
	private List<String> mList;
	private MyAdapter adapter;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		adapter = new MyAdapter();
		mListView = (PullToRefreshListView) findViewById(R.id.prlv);
		mList = new ArrayList<String>();
		mList.add("一");
		mList.add("二");
		mList.add("三");
		mList.add("四");
		mList.add("五");
		mList.add("666666666666666666666666666666666666666666666666");
		mList.add("泥猴");
		
		mList.add("支持");
		mList.add("宁泽涛得了金牌");
		mList.add("宁泽涛得了金牌+1");
		mList.add("宁泽涛得了金牌+2");
		mList.add("宁泽涛得了金牌+3");
		mList.add("宁泽涛得了金牌+4");
		mList.add("宁泽涛得了金牌+5");
		
		mListView.setAdapter(adapter);
		mListView.setPullToRefreshAsyncTask(new PullToRefreshAsyncTask() {
			
			@Override
			public void onPreExecute() {
				Toast.makeText(MainActivity.this, "为您玩命刷新中", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void doInBackground() {
				SystemClock.sleep(5000);
				Random random = new Random();
				mList.add(0, "新增ListView"+(random.nextInt(2000)));
			}
			@Override
			public void onPostExecute() {
				adapter.notifyDataSetChanged();
				
			}
		});
	}
	private class MyAdapter extends BaseAdapter{
		public int getCount() {
			return mList.size();
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = new TextView(getApplicationContext());
			String news = mList.get(position);
			tv.setTextSize(20);
			tv.setTextColor(Color.BLACK);
			tv.setText(news);
			tv.setPadding(20, 20, 20, 20);
			return tv;
		}
		public Object getItem(int position) {
			return null;
		}
		public long getItemId(int position) {
			return 0;
		}
	}
}
