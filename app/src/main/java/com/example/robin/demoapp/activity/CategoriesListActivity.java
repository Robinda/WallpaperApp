package com.example.robin.demoapp.activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.robin.demoapp.R;
import com.example.robin.demoapp.adapters.RecyclerViewDataAdapter;
import com.example.robin.demoapp.models.Category;
import com.example.robin.demoapp.models.SectionDataModel;
import com.example.robin.demoapp.models.SingleItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoriesListActivity extends AppCompatActivity {

    private ArrayList<SectionDataModel> allSampleData;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CategoriesListActivity.this.setTitle("Catégories");
        setContentView(R.layout.activity_categories_list);

        container = findViewById(R.id.container);

        allSampleData = new ArrayList<>();
        getCategories();
    }

    /**
     * Récupère les catégories depuis le JSON renvoyé par l'API
     */
    private void getCategories() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int nbSamples = 4;
        int wallpaperMaxSize = 5000;
        String url = "https://www.hebus.com/api/wallpapers.php?service=get_categories&nb_samples=" + String.valueOf(nbSamples)
                + "&size=" + String.valueOf(wallpaperMaxSize);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray responseJSONArray = response.getJSONArray("categories");

                    for (int i = 0; i < responseJSONArray.length(); i++) {
                        SectionDataModel dm = new SectionDataModel();
                        dm.setHeaderTitle(responseJSONArray.getJSONObject(i).getString("label"));

                        Category c = new Category();
                        c.setId(responseJSONArray.getJSONObject(i).getString("cat_id"));
                        c.setLabel(responseJSONArray.getJSONObject(i).getString("label"));
                        dm.setHeaderCategory(c);

                        JSONArray arrayCatSamples = responseJSONArray.getJSONObject(i).getJSONArray("samples");
                        ArrayList<SingleItemModel> mSamples = new ArrayList<>();

                        for(int j = 0; j < arrayCatSamples.length() ; j++) {
                            mSamples.add(new SingleItemModel(arrayCatSamples.getJSONObject(j).getString("id"), arrayCatSamples.getJSONObject(j).getString("url_thumb")));
                        }

                        dm.setAllItemsInSection(mSamples);

                        allSampleData.add(dm);
                    }

                    setRecyclerView();
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
                        "Oups, une erreur est survenue ! Veuillez vérifier votre connexion internet.",
                        Snackbar.LENGTH_LONG);

                snackbar.show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Gestion du RecyclerView
     */
    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(this, allSampleData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}
