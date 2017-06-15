package com.enet.smartrestaurent;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by asela on 6/15/17.
 */
public class HorizontalRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ActiveOrderItem> mDataList;
    private int mRowIndex = -1;

    public HorizontalRVAdapter() {
    }

    public void setData(List<ActiveOrderItem> data) {
        if (mDataList != data) {
            mDataList = data;
            notifyDataSetChanged();
        }
    }

    public void setRowIndex(int index) {
        mRowIndex = index;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView text;
        private TextView status;

        public ItemViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.order_item_text);
            status = (TextView) itemView.findViewById(R.id.order_item_status_text);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.active_order_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position) {
        ItemViewHolder holder = (ItemViewHolder) rawHolder;
        holder.text.setText(mDataList.get(position).qty+"x "+mDataList.get(position).itemName);
        String state = mDataList.get(position).state;
        holder.status.setText(state);
        if(state.equals(Constants.ITEM_STATE_SENT)){
            holder.status.setBackgroundColor(Color.GRAY);
        }
        else if(state.equals(Constants.ITEM_STATE_READY)){
            holder.status.setBackgroundColor(Color.RED);
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

}