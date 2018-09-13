package com.example.robin.demoapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.robin.demoapp.R;
import com.example.robin.demoapp.adapters.GridViewAdapter;
import com.example.robin.demoapp.models.Category;
import com.example.robin.demoapp.models.Wallpaper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WallpapersGridActivity extends AppCompatActivity {

    private static final String TAG = WallpapersGridActivity.class.getName();

    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private RelativeLayout container;
    private Button pageNext, pagePrevious;

    private ArrayList<Wallpaper> arrayWallpapers;
    Context mContext;

    // Valeurs par défaut
    private int categoryId = 1;
    private int currentPage = 1;
    private final static int nbImgPerPage = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Category c = (Category) getIntent().getSerializableExtra("category");
        if(c != null) {
            categoryId = Integer.parseInt(c.getId());
            WallpapersGridActivity.this.setTitle(c.getLabel());
        }
        else {
            Log.e(TAG, "Erreur lors de la récupération de la categorie.");
            WallpapersGridActivity.this.setTitle("Catégorie");
        }

        setContentView(R.layout.activity_wallpapers_list);

        gridView = findViewById(R.id.gridView);
        container = findViewById(R.id.wp_list_container);
        pageNext = findViewById(R.id.btn_next_page);
        pagePrevious = findViewById(R.id.btn_previous_page);

        arrayWallpapers = new ArrayList<>();
        mContext = WallpapersGridActivity.this;

        // Click sur une image : ouverture de l'activité "Détails"
        gridView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        Intent mIntent = new Intent(mContext, WallpaperDetailsActivity.class);
                        mIntent.putExtra("wp", (Wallpaper) parent.getItemAtPosition(position));
                        mContext.startActivity(mIntent);
                    }
                }
        );

        // Demande de page suivante
        pageNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage++;
                fetchWallpapers(currentPage);

                if(currentPage > 1)
                    pagePrevious.setEnabled(true);
            }
        });

        // Demande de page précedente
        pagePrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPage == 2) {
                    currentPage--;
                    fetchWallpapers(currentPage);
                    pagePrevious.setEnabled(false);
                }
                else if(currentPage > 2) {
                    currentPage--;
                    fetchWallpapers(currentPage);
                }
            }
        });

        pagePrevious.setEnabled(false);

        // Récupération des images de la première page
        fetchWallpapers(1);
    }

    /**
     * Récupère les wallpapers et notifie la gridView
     */
    private void fetchWallpapers(int page) {
        if(arrayWallpapers.size() > 0)
            arrayWallpapers.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://www.hebus.com/api/wallpapers.php?service=get_search&cat_id=" + String.valueOf(categoryId)
                + "&page=" + page
                + "&nb_images_per_page=" + String.valueOf(nbImgPerPage);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray responseJSONArray = response.getJSONArray("wallpapers");

                    for (int i = 0; i < responseJSONArray.length(); i++) {
                        Wallpaper wp = new Wallpaper();
                        wp.setName(responseJSONArray.getJSONObject(i).getString("name"));
                        wp.setDownloads(responseJSONArray.getJSONObject(i).getString("downloads"));
                        wp.setRating(responseJSONArray.getJSONObject(i).getDouble("rating"));
                        wp.setUrl_thumbnail(responseJSONArray.getJSONObject(i).getString("url_thumb"));
                        wp.setUrl_image(responseJSONArray.getJSONObject(i).getString("url_mobile"));

                        arrayWallpapers.add(wp);
                    }

                    if(gridAdapter == null) {
                        gridAdapter = new GridViewAdapter(WallpapersGridActivity.this, R.layout.grid_item_layout, arrayWallpapers);
                        gridView.setAdapter(gridAdapter);
                    }
                    else {
                        gridView.smoothScrollToPosition(0);
                        gridAdapter.notifyDataSetChanged();
                    }

                    Toast.makeText(WallpapersGridActivity.this, "Page : " + currentPage, Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Snackbar snackbar = Snackbar.make(
                        container,
                        "Oups, une erreur est survenue !",
                        Snackbar.LENGTH_LONG);

                snackbar.show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
