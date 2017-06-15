package com.enet.smartrestaurent;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asela on 6/15/17.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{
    List<ActiveOrder> orders;

    private static RecyclerView horizontalList;
    private final Context mContext;

    RVAdapter(Context context,List<ActiveOrder> orders){
        this.mContext = context;
        this.orders = orders;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.table.setText("Table "+orders.get(position).tableId);
        ArrayList<String> list = new ArrayList<>();
        list.add("item1");
        list.add("item2");
        holder.horizontalAdapter.setData(list); // List of Strings
        holder.horizontalAdapter.setRowIndex(position);




//        personViewHolder.personAge.setText(persons.get(i).age);
//        personViewHolder.personPhoto.setImageResource(persons.get(i).photoId);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public int getItemCount() {
        return orders.size();
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_view, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView table;
        private HorizontalRVAdapter horizontalAdapter;
//        TextView personAge;
//        ImageView personPhoto;





        PersonViewHolder(View itemView) {
            super(itemView);
            Context context = itemView.getContext();

            cv = (CardView)itemView.findViewById(R.id.cv);
            table = (TextView)itemView.findViewById(R.id.table_text);


            horizontalList = (RecyclerView) itemView.findViewById(R.id.horizontal_list);
            horizontalList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            horizontalAdapter = new HorizontalRVAdapter();
            horizontalList.setAdapter(horizontalAdapter);
        }
//            personAge = (TextView)itemView.findViewById(R.id.person_age);
//            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }

    }



