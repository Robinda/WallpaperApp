package com.example.robin.demoapp.activity;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.robin.demoapp.R;
import com.example.robin.demoapp.helper.GlideApp;
import com.example.robin.demoapp.models.Wallpaper;

import java.io.IOException;
import java.text.DecimalFormat;

public class WallpaperDetailsActivity extends AppCompatActivity {

    private static final String TAG = WallpaperDetailsActivity.class.getName();

    TextView author, name, downloads, rating;
    ImageView ivWallpaper, closeActivity;
    Button setAsWallpaper;
    ProgressBar progressBar;

    WallpaperManager wallpaperManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wallpaper_details);

        name = findViewById(R.id.wp_name);
        author = findViewById(R.id.wp_author);
        downloads = findViewById(R.id.wp_downloads);
        rating = findViewById(R.id.wp_rating);
        ivWallpaper = findViewById(R.id.iv_wallpaper);
        closeActivity = findViewById(R.id.icon_close);
        setAsWallpaper = findViewById(R.id.btn_set_wp);
        progressBar = findViewById(R.id.pb_img);

        Wallpaper wp = (Wallpaper) getIntent().getSerializableExtra("wp");
        if(wp != null)
            setWallpaperData(wp);
        else
            Log.e(TAG, "Erreur lors de la récupération des données de l'image.");

        // Fermeture de l'activité lorsque l'on clique sur l'icon "croix"
        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setAsWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageAsWallpaper();
            }
        });
    }

    private void setWallpaperData(Wallpaper wp) {
        name.setText(wp.getName());
        author.setText(wp.getAuthor());
        downloads.setText(wp.getDownloads());

        // formatage de la note ("rating")
        DecimalFormat df2 = new DecimalFormat(".##");
        rating.setText(String.valueOf(df2.format(wp.getRating())));

        // Affichage du wallpaper
        GlideApp.with(this)
                .load(wp.getUrl_image())
                .transition(DrawableTransitionOptions.withCrossFade())
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(ivWallpaper);
    }

    /**
     * Définiton de l'image en nouveau fond d'écran
     */
    private void setImageAsWallpaper() {
        wallpaperManager  = WallpaperManager.getInstance(WallpaperDetailsActivity.this);
        Bitmap bitmap = ((BitmapDrawable)((LayerDrawable) ivWallpaper.getDrawable()).getDrawable(1)).getBitmap();

        try {
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(WallpaperDetailsActivity.this,
                    "L'image a bien été définie comme fond d'écran",
                    Toast.LENGTH_SHORT)
                    .show();
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(WallpaperDetailsActivity.this,
                    "Erreur, impossible de définir cette image comme fond d'écran",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
