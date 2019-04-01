/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.onelabsdemo.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorial.adit.com.onelabsdemo.R;
import tutorial.adit.com.onelabsdemo.adapter.ImageListAdapter;
import tutorial.adit.com.onelabsdemo.api.WallpaperApi;
import tutorial.adit.com.onelabsdemo.model.Urls;
import tutorial.adit.com.onelabsdemo.model.Wallpaper;



public class MainActivity extends AppCompatActivity  {
	static final int universal_cache = 10;
	private static final String TAG = "stage";
	public String mquery;
	static int page = 1;
	int cache_size = 0;
	public static List<Urls> urlsList;
	RecyclerView imageRecyclerView;
	GridLayoutManager mLayoutManager;
	ImageListAdapter imageListAdapter;
	Context ctx;
	Boolean onscroll = false;
	List<String> suggest_add;
	boolean isSearch = false;
	boolean isLoading = false;
	static boolean isonline = false;
	static boolean mtextsubmited = false;
	MaterialSearchView searchView;
	static List<String> suggestlist = new ArrayList<>();
	public ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		urlsList = new ArrayList<>();
		ctx = this;
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		searchView = (MaterialSearchView) findViewById(R.id.search_view);
		searchView.setVoiceSearch(false);
		imageRecyclerView = (RecyclerView) findViewById(R.id.images_recycler_view);
		mLayoutManager = new GridLayoutManager(ctx, 2);
		imageRecyclerView.setLayoutManager(mLayoutManager);
		imageRecyclerView.setItemAnimator(new DefaultItemAnimator());
		imageListAdapter = new ImageListAdapter(ctx);
		imageRecyclerView.setAdapter(imageListAdapter);

		searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				mquery = query;
				Intent searchActivity = new Intent(MainActivity.this, Search.class);
				searchActivity.putExtra("mquery", mquery);
				startActivity(searchActivity);
				searchView.closeSearch();
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
		searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
			@Override
			public void onSearchViewShown() {
			}

			@Override
			public void onSearchViewClosed() {
			}
		});
		imageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				onscroll = true ;
				// Check if end of page has been reached
				if(!isLoading && ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition() == imageListAdapter.getItemCount() - 1) {

					isLoading = true;
					page++;


					if(isonline) {

						downloadImages();
					}
				}
			}
		});

		downloadImages();
	}

	@Override
	protected void onStart() {

		//downloadImages();
		super.onStart();

	}

	@Override
	protected void onStop() {

		super.onStop();



	}


	public void




	downloadImages() {

		int itemsPerPage = 20;
		WallpaperApi.Client.getInstance().getWallpapers(itemsPerPage, page).enqueue(new Callback<List<Wallpaper>>() {
			@Override
			public void onResponse(Call<List<Wallpaper>> call, Response<List<Wallpaper>> response) {
				isonline = true;

				List<Urls> murls = new ArrayList<>();
				murls = getImageUrls(response.body());

				if(murls.size() > universal_cache) {
					cache_size = universal_cache;
				} else cache_size = murls.size();
				int i = 1;
				if(onscroll)
				{

				}
				else {

					while(i <= cache_size) {
						saveurl(getBaseContext(), "basic", i, murls.get(i).getRegular());
						i++;
					}
				}

				progressBar.setVisibility(View.GONE);
					imageListAdapter.addImages(getImageUrls(response.body()));
					isLoading = false;
			}
			@Override
			public void onFailure(Call<List<Wallpaper>> call, Throwable t) {

				isonline = false;
				String check = getUrl(getApplicationContext(), "basic", 1);
				if(check.isEmpty())
				{
					Toast.makeText(getApplicationContext(),"No Cache Availble, Switch internet ON",Toast.LENGTH_LONG).show();
				}
				else {
					Toast.makeText(getApplicationContext(),"Fetching data from Cache",Toast.LENGTH_LONG).show();
				}
				if(onscroll)
				{

					for(int i = 1; i < 9; i++) {
						String name = getUrl(getApplicationContext(), "basic", i);

					}
				}
				else {

					List<Urls> murls = new ArrayList<>();
					for(int i = 1; i < 9; i++) {
						String name = getUrl(getApplicationContext(), "basic", i);
						Urls mm = new Urls();
						mm.setRegular(name);
						murls.add(mm);
					}

					progressBar.setVisibility(View.GONE);
					imageListAdapter.addImages(murls);
				}
			}
		});
	}

	public static void saveurl(Context context, String preferenceFileName, int url1, String url2) {
		SharedPreferences.Editor sharedPreferences = context.getSharedPreferences(preferenceFileName, 0).edit();

		sharedPreferences.putString(String.valueOf(url1), url2);
		sharedPreferences.apply();

	}

	public static String getUrl(Context context, String preferenceFileName, int url1) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
		String url = sharedPreferences.getString(String.valueOf(url1), "");
		return url;
	}

	public List<Urls> getImageUrls(List<Wallpaper> wallpapers) {
		List<Urls> murls = new ArrayList<>();
		for(int i = 0; i < wallpapers.size(); i++) {
			murls.add(wallpapers.get(i).getUrls());
		}
		return murls;
	}

	@Override
	public void onBackPressed() {
		if(searchView.isSearchOpen()) {

			searchView.closeSearch();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		final MenuItem searchItem = menu.findItem(R.id.action_search);
		searchView.setMenuItem(searchItem);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch(id) {
			case R.id.span2:
				mLayoutManager.setSpanCount(2);
				return true;
			case R.id.span3:
				mLayoutManager.setSpanCount(3);
				return true;
			case R.id.span4:
				mLayoutManager.setSpanCount(4);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

