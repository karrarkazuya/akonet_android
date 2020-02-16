package sumerianapps.android.akonet.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;


import sumerianapps.android.akonet.R;
import sumerianapps.android.akonet.config.Statics;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.PostersViewHolder>{
    private JSONArray items;
    Context context;

    public ListAdapter(JSONArray items, Context context){
        this.items= items;

        this.context = context;
    }

    @Override
    public PostersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View PostersProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        PostersViewHolder gvh = new PostersViewHolder(PostersProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(final PostersViewHolder holder, final int position) {
        try {
            //holder.imageView.setImageBitmap(Requester.LoadPoster(items.getJSONObject(position).getString("image")));
            Picasso.get().load(Statics.akonet+"/images/isp/"+items.getJSONObject(position).getString("logo")).placeholder(R.drawable.no_poster).into(holder.imageView);
            holder.txtview.setText(items.getJSONObject(position).getString("name"));
            int status = items.getJSONObject(position).getInt("status");
            if(status == 1){
                holder.statusview.setText("up");
                holder.statusview.setTextColor(context.getColor(R.color.green));
            }else{
                holder.statusview.setTextColor(context.getColor(R.color.red));
                holder.statusview.setText("down");
            }

            holder.last_up.setText("Last up: "+items.getJSONObject(position).getString("lastup"));
            holder.last_down.setText("Last down: "+items.getJSONObject(position).getString("lastdown"));
            holder.ping.setText("Ping: "+items.getJSONObject(position).getString("ping")+" ms");
            holder.packet_lose.setText("packet lose: "+items.getJSONObject(position).getString("loss")+"%");


            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(holder.info.getVisibility() == View.GONE){
                        holder.info.setVisibility(View.VISIBLE);
                    }else{
                        holder.info.setVisibility(View.GONE);
                    }

                }
            });


            holder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        holder.info.setVisibility(View.GONE);
                }
            });

        }catch (Exception e){
        }
    }

    @Override
    public int getItemCount() {
        return items.length();
    }

    public class PostersViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtview;
        TextView statusview;
        LinearLayout item;
        LinearLayout info;


        TextView last_up;
        TextView last_down;
        TextView ping;
        TextView packet_lose;
        public PostersViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.item_image);
            txtview = view.findViewById(R.id.item_title);
            statusview = view.findViewById(R.id.item_status);
            item = view.findViewById(R.id.item);
            info = view.findViewById(R.id.info);


            last_up = view.findViewById(R.id.last_up);
            last_down = view.findViewById(R.id.last_down);
            ping = view.findViewById(R.id.ping);
            packet_lose = view.findViewById(R.id.packet_lose);
        }
    }
}

