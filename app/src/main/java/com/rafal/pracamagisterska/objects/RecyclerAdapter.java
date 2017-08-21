package com.rafal.pracamagisterska.objects;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rafal.pracamagisterska.R;
import com.rafal.pracamagisterska.activities.MapsActivity;

import java.util.List;

/**
 * Created by Rafal on 2017-06-10.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Route> routes;
    private String startNodeId, endNodeId;

    private String[] routeName,driverName,price,seatsNumber;
    private int[] avatarImage;
    private Context context;

    public RecyclerAdapter(List<Route> routes, String startNodeId, String endNodeId,  Context context) {
        this.routes = routes;
        this.context = context;
        this.startNodeId = startNodeId;
        this.endNodeId = endNodeId;
        getData();
    }

    private void getData() {

        routeName = new String[routes.size()];
        driverName = new String[routes.size()];
        avatarImage = new int[routes.size()];
        price = new String[routes.size()];
        seatsNumber = new String[routes.size()];

        for(int i = 0; i < routes.size(); i++){
            System.out.println(routes.get(i).toString());
            routeName[i] = routes.get(i).getName();
            driverName[i] = routes.get(i).getUserName();
            avatarImage[i] = R.mipmap.ic_avatar;
            price[i] = routes.get(i).getCost().toString();
            seatsNumber[i] = Integer.toString(routes.get(i).getSeatNumber()) ;
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder{



        public int currentItem;
        public ImageView itemImage;
        public TextView routeName;
        public TextView driverName;
        public TextView seatsNumber;
        public TextView price;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            routeName = (TextView)itemView.findViewById(R.id.item_route_name);
            driverName = (TextView)itemView.findViewById(R.id.item_driver_name);
            seatsNumber = (TextView)itemView.findViewById(R.id.item_seats_number);
            price = (TextView)itemView.findViewById(R.id.item_price);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar.make(v, "Click detected on item " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    Intent intent = new Intent(context, MapsActivity.class);
                    intent.putStringArrayListExtra("Path", routes.get(position).getNodesList());
                    intent.putExtra("startNodeId", startNodeId);
                    intent.putExtra("endNodeId", endNodeId);
                    context.startActivity(intent);


                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.routeName.setText(routeName[i]);
        viewHolder.driverName.setText(driverName[i]);
        viewHolder.itemImage.setImageResource(avatarImage[i]);
        viewHolder.price.setText(price[i]);
        viewHolder.seatsNumber.setText(seatsNumber[i]);
    }

    @Override
    public int getItemCount() {
        return routeName.length;
    }
}
