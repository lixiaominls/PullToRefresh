package com.xinmai.pulltorefresh.ui;

import com.xinmai.pulltorefresh.R;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PullToRefreshListView extends ListView {

	private View view;
	private static final int IDLE = 0;
	private static final int PULL_TO_REFRESH = 1;
	private static final int RELEASH_TO_REFRESH = 2;
	private int mListStatus = IDLE;
	int startY;
	private int height;
	private int paddingtop;
	private TextView mTv;
	private ProgressBar mPb;
	private ImageView mIv;

	private PullToRefreshAsyncTask task;

	Handler handler = new Handler();

	public void setPullToRefreshAsyncTask(PullToRefreshAsyncTask task) {
		this.task = task;
	}

	public interface PullToRefreshAsyncTask {
		void onPreExecute();

		void doInBackground();

		void onPostExecute();
	}

	public PullToRefreshListView(Context context) {
		super(context);
		initView(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		view = View.inflate(context, R.layout.header, null);
		mIv = (ImageView) view.findViewById(R.id.iv_arrow);
		mPb = (ProgressBar) view.findViewById(R.id.loading);
		mTv = (TextView) view.findViewById(R.id.text);

		view.measure(0, 0);
		height = view.getMeasuredHeight();
		paddingtop = -height;
		addHeaderView(view);
		view.setPadding(0, paddingtop, 0, 0);
	}

	public boolean onTouchEvent(MotionEvent ev) {
		int position = getFirstVisiblePosition();
		if (position > 0) {
			return super.onTouchEvent(ev);
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getRawY();
			break;

		case MotionEvent.ACTION_MOVE:
			int endY = (int) ev.getRawY();
			int dY = endY - startY;
			paddingtop += dY;
			startY = (int) ev.getRawY();
			view.setPadding(0, paddingtop, 0, 0);
			if (paddingtop <= 0 && paddingtop >= -height) {
				mTv.setText("继续下拉刷新");
				mListStatus = PULL_TO_REFRESH;
			} else if (paddingtop > 0) {
				mTv.setText("松手立即刷新~~");
				mListStatus = RELEASH_TO_REFRESH;
			} else if (paddingtop < -height) {
				mListStatus = IDLE;
				paddingtop = -height;
			}
			break;

		case MotionEvent.ACTION_UP:
			switch (mListStatus) {
			case IDLE:
				paddingtop = -height;
				view.setPadding(0, paddingtop, 0, 0);
				mPb.setVisibility(View.INVISIBLE);
				mIv.setVisibility(View.VISIBLE);
				mListStatus = IDLE;
				break;
			case PULL_TO_REFRESH:
				paddingtop = -height;
				view.setPadding(0, paddingtop, 0, 0);
				mPb.setVisibility(View.INVISIBLE);
				mIv.setVisibility(View.VISIBLE);
				mListStatus = IDLE;
				break;
			case RELEASH_TO_REFRESH:
					paddingtop = 0;
					view.setPadding(0, paddingtop, 0, 0);
					mPb.setVisibility(View.VISIBLE);
					mIv.setVisibility(View.INVISIBLE);
					mTv.setText("刷新start~");
					if (task != null) {
						task.onPreExecute();
						new Thread(new Runnable() {
							public void run() {
								task.doInBackground();
								handler.post(new Runnable() {
									public void run() {
											task.onPostExecute();
										paddingtop = -height;
										view.setPadding(0, paddingtop, 0, 0);
										mPb.setVisibility(View.INVISIBLE);
										mIv.setVisibility(View.VISIBLE);
										mListStatus = IDLE;
									}
								});
							}
						}).start();
				}
				break;
			}
			break;
		}
		return super.onTouchEvent(ev);
	}
}
