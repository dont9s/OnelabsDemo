/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.onelabsdemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import tutorial.adit.com.onelabsdemo.R;
import tutorial.adit.com.onelabsdemo.Activity.SingleImage;
import tutorial.adit.com.onelabsdemo.model.Urls;


public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {

    private static final String TAG = "staged";
    Context ctx;
    public  ArrayList<Urls> urlsList;
    private static RecyclerViewClickListener itemListener;

    public interface RecyclerViewClickListener
    {
        public void recyclerViewListClicked(View v, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView thumbnail;
        public TextView textView;
        public ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            thumbnail = (ImageView) itemView.findViewById(R.id.image_thumbnail);
            textView = (TextView) itemView.findViewById(R.id.urlTextView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(ctx, "onClick works!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onClick works! Position: " + this.getLayoutPosition() + " clicked!");
            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());
        }
    }

    public ImageListAdapter(Context context) {
        Log.d(TAG,"ImageListAdapter");

        ctx = context;
        urlsList = new ArrayList<>();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(ctx).inflate(R.layout.image_list_item, parent, false);

        Log.d(TAG,"onCreateViewHolder");

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ctx, SingleImage.class);
                i.putExtra("Image",urlsList.get(position).getRegular());
                i.putExtra("urlslist",urlsList);
                i.putExtra("position",position);
                holder.thumbnail.setTransitionName("profile");
                Pair<View,String> pair=Pair.create((View)holder.thumbnail,holder.thumbnail.getTransitionName());
                ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) ctx,pair);
                ctx.startActivity(i,optionsCompat.toBundle());


            }
        });
        Urls image_url = urlsList.get(position);

        String imageUrl = "";
        imageUrl = image_url.getRegular();
        holder.textView.setText( imageUrl);
        holder.progressBar.setVisibility(View.VISIBLE);

      //  Log.d(TAG,"----****passing imageurl to glide" );

        Glide.with(ctx).load(imageUrl)
                .thumbnail(0.5f)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.thumbnail.setVisibility(View.VISIBLE);
                        return false;
                    }

                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);
    }


    @Override
    public int getItemCount() {
        Log.d("TAG","getItemCount :"+urlsList.size());
        return urlsList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void addImages(List<Urls> list){

        Log.d(TAG,"------**sent adapter"+list.size());

        urlsList.addAll(list);
        Log.d(TAG,"----**now adapter size "+urlsList.size());


        notifyDataSetChanged();
    }



}
