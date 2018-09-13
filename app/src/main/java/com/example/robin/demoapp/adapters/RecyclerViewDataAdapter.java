package com.example.robin.demoapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.robin.demoapp.R;
import com.example.robin.demoapp.activity.WallpapersGridActivity;
import com.example.robin.demoapp.models.Category;
import com.example.robin.demoapp.models.SectionDataModel;

import java.util.ArrayList;

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder> {

    private ArrayList<SectionDataModel> dataList;
    private Context mContext;

    public RecyclerViewDataAdapter(Context context, ArrayList<SectionDataModel> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int i) {

        final Category category = dataList.get(i).getHeaderCategory();

        itemRowHolder.itemTitle.setText(category.getLabel());

        // Liste horizontale contenant les samples
        ArrayList categorySamplesList = dataList.get(i).getAllItemsInSection();
        SectionListDataAdapter samplesListDataAdapter = new SectionListDataAdapter(mContext, categorySamplesList);

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.recycler_view_list.setAdapter(samplesListDataAdapter);
        itemRowHolder.recycler_view_list.setNestedScrollingEnabled(false);

        // Click sur "voir +" : ouverture de la grille de wallpapers correspondant à la catégorie sélectionnée
        itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, WallpapersGridActivity.class);
                mIntent.putExtra("category", category);
                mContext.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;
        protected RecyclerView recycler_view_list;
        protected Button btnMore;

        public ItemRowHolder(View view) {
            super(view);
            this.itemTitle = view.findViewById(R.id.categoryLabel);
            this.recycler_view_list = view.findViewById(R.id.recycler_view_list);
            this.btnMore= view.findViewById(R.id.btnMore);
        }
    }
}