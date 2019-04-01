/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.onelabsdemo.Activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import tutorial.adit.com.onelabsdemo.R;
import tutorial.adit.com.onelabsdemo.model.Urls;

public class SingleImage extends AppCompatActivity {

    ImageView msingleView;

    public ProgressBar progressBar;
    int counter=0;
    String mUrl ;
    int size ;
    public  List<Urls> urlsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image);

        counter = getIntent().getIntExtra("position",0);
        urlsList = (ArrayList<Urls>)getIntent().getSerializableExtra("urlslist");
        size = urlsList.size();
        mUrl = getIntent().getStringExtra("Image");
        msingleView = (ImageView)findViewById(R.id.image_thumbnail);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);


        fetchImage();

        msingleView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent MsnEvtPsgVal)
            {
                flingActionVar.onTouchEvent(MsnEvtPsgVal);
                return true;
            }

            GestureDetector flingActionVar = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener()
            {
                private static final int flingActionMinDstVac = 120;
                private static final int flingActionMinSpdVac = 200;

                @Override
                public boolean onFling(MotionEvent fstMsnEvtPsgVal, MotionEvent lstMsnEvtPsgVal, float flingActionXcoSpdPsgVal, float flingActionYcoSpdPsgVal)
                {
                    if(fstMsnEvtPsgVal.getX() - lstMsnEvtPsgVal.getX() > flingActionMinDstVac && Math.abs(flingActionXcoSpdPsgVal) > flingActionMinSpdVac)
                    {

                        if(counter<size) {
                            Urls next = urlsList.get(counter);
                            counter++;
                            Glide.with(getApplicationContext()).load(next.getRegular()).centerCrop().placeholder(R.drawable.placeholder).into(msingleView);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"End of images", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }

                        // TskTdo :=> On Right to Left fling

                        return false;
                    }
                    else if (lstMsnEvtPsgVal.getX() - fstMsnEvtPsgVal.getX() > flingActionMinDstVac && Math.abs(flingActionXcoSpdPsgVal) > flingActionMinSpdVac) {
                        // TskTdo :=> On Left to Right fling
                        if(counter > 0)
                        {
                            Urls next = urlsList.get(counter);
                        counter--;
                        Glide.with(getApplicationContext()).load(next.getRegular()).centerCrop().placeholder(R.drawable.placeholder).into(msingleView);
                            }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Start of the images", Toast.LENGTH_LONG).show();
                            onBackPressed();
                            }


                        return false;

                    }

                    if(fstMsnEvtPsgVal.getY() - lstMsnEvtPsgVal.getY() > flingActionMinDstVac && Math.abs(flingActionYcoSpdPsgVal) > flingActionMinSpdVac)
                    {
                        // TskTdo :=> On Bottom to Top fling

                        return false;
                    }
                    else if (lstMsnEvtPsgVal.getY() - fstMsnEvtPsgVal.getY() > flingActionMinDstVac && Math.abs(flingActionYcoSpdPsgVal) > flingActionMinSpdVac)
                    {
                        // TskTdo :=> On Top to Bottom fling

                        return false;
                    }
                    return false;
                }
            });
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void fetchImage()
    {

        Glide.with(getApplicationContext())
                .load(mUrl)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                         progressBar.setVisibility(View.GONE);
                        msingleView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(msingleView);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
