/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.onelabsdemo.Activity;

import android.content.Context;
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

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorial.adit.com.onelabsdemo.R;
import tutorial.adit.com.onelabsdemo.adapter.ImageListAdapter;
import tutorial.adit.com.onelabsdemo.api.WallpaperApi;
import tutorial.adit.com.onelabsdemo.model.ResultsArray;
import tutorial.adit.com.onelabsdemo.model.SearchResultmodel;
import tutorial.adit.com.onelabsdemo.model.Urls;

public class Search extends AppCompatActivity {

	private static final String TAG = "stages";
	public String mquery;
	static int page = 1;
	int cache_size =0;
	public static List<Urls> urlsList;
	RecyclerView imageRecyclerView;
	GridLayoutManager mLayoutManager;
	ImageListAdapter imageListAdapter;
	Context ctx;
	List<String> suggest_add ;
	boolean isSearch = false;
	boolean isLoading = false;
	static boolean isonline = false;
	static boolean mtextsubmited = false ;
	MaterialSearchView searchView;
	static  List<String> suggestlist = new ArrayList<>();
	public ProgressBar progressBar;
	Boolean onscroll = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		urlsList = new ArrayList<>();
		ctx = this;



		mquery  = getIntent().getStringExtra("mquery");
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitleTextColor(Color.WHITE);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		progressBar = (ProgressBar)findViewById(R.id.progress_bar);
		searchView = (MaterialSearchView) findViewById(R.id.search_view);
		imageRecyclerView =  (RecyclerView)findViewById(R.id.images_recycler_view);
		mLayoutManager = new GridLayoutManager(ctx, 2);
		imageRecyclerView.setLayoutManager(mLayoutManager);
		imageRecyclerView.setItemAnimator(new DefaultItemAnimator());
		imageListAdapter = new ImageListAdapter(ctx);
		imageRecyclerView.setAdapter(imageListAdapter);

		searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				mquery = query;
				urlsList.clear();
				getFirstSearchPhotos();
				searchView.closeSearch();
				return true ;
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
				// Check if end of page has been reached
				if(!isLoading && ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition() == imageListAdapter.getItemCount() - 1) {

					isLoading = true;
					page++;

					if(isonline) {

						onscroll = true ;
						getFirstSearchPhotos();
					}
				}
			}
		});


	}


	@Override
	protected void onStart() {
		super.onStart();
		getFirstSearchPhotos();

	}

	private void getFirstSearchPhotos() {
		int itemsPerPage = 20;


		WallpaperApi.Client.getInstance().getSearchPhotos(mquery,page,itemsPerPage).enqueue(new Callback<SearchResultmodel>() {
			@Override
			public void onResponse(Call<SearchResultmodel> call, Response<SearchResultmodel> response) {
				isonline = true;

				List<Urls> murls = new ArrayList<>();
				murls = getImageUrls(response.body());

				if(murls.size() > 8) {
					cache_size = 8;
				} else cache_size = murls.size();
				int i = 1;
				if(onscroll)
				{

				}
				else {
					while(i <= cache_size) {
						saveurl(getBaseContext(), mquery, i, murls.get(i).getRegular());

						i++;
					}
					for(int k = 1; k < 9; k++) {

					}
				}
					progressBar.setVisibility(View.GONE);
					imageListAdapter.addImages(getImageUrls(response.body()));
					isLoading = false;


				}



			@Override
			public void onFailure(Call<SearchResultmodel> call, Throwable t) {
				isonline = false;


				List<Urls> murls = new ArrayList<>();
				for(int i = 1 ; i<9;i++) {
					String  name = getUrl(getApplicationContext(),mquery,i);

					Urls mm = new Urls();
					mm.setRegular(name);

					murls.add(mm);
				}


				progressBar.setVisibility(View.GONE);
				imageListAdapter.addImages(murls);
			}
		});
	}

	public List<Urls> getImageUrls(SearchResultmodel wallpapers)
	{
		List<Urls> murls = new ArrayList<>();
		List<ResultsArray> mResults = wallpapers.getResults();
		for (int i=0; i< mResults.size(); i++)
		{

			murls.add(mResults.get(i).getUrls());
		}

		return  murls;
	}



	public static void saveurl(Context context, String preferenceFileName, int url1, String url2) {
		SharedPreferences.Editor sharedPreferences = context.getSharedPreferences(preferenceFileName, 0).edit();


		sharedPreferences.putString(String.valueOf(url1), url2);


		sharedPreferences.apply();
	}

	public static String getUrl(Context context, String preferenceFileName, int url1) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
		String url = sharedPreferences.getString(String.valueOf(url1),"");
		return url ;
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
		// Handle toolbar item clicks here. It'll
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch (id) {

			case android.R.id.home:
				finish();
				return true;

			case R.id.span2:
				// Open the search view on the menu item click.
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
