package com.orwa.gatherin.User.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.location.LocationManager;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.ChatModel.ChatModel;
import com.orwa.gatherin.R;
import com.orwa.gatherin.utils.ParentClass;
import com.github.florent37.shapeofview.shapes.RoundRectView;

import java.util.ArrayList;
import java.util.List;

//import butterknife.BindView;
//import butterknife.ButterKnife;


public class NewChatAdapter extends RecyclerView.Adapter<NewChatAdapter.ViewHolder> {
    List<ChatModel> messagesList;
    Context context;
    LayoutInflater layoutInflater;
    int lastPosition = -1;
    RecyclerView rvChat;
    public static boolean firstTime = true;
    String uniqueId = "";
    String selectedPosition = "";


    private static final String MAP_VIEW_BUNDLE_KEY = "AIzaSyC-YnvrMe0dHSEHcw3Lz8xwt1O_51YS_sg";
    protected LocationManager locationManager;


    public NewChatAdapter(Context context, RecyclerView rvChat) {
        this.context = context;
        this.messagesList = new ArrayList<>();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.rvChat = rvChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = layoutInflater.from(parent.getContext()).inflate(R.layout.item_new_chat, parent, false);
        return new NewChatAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
//        Log.e("msg", messagesList.get(position).getMsg());
        holder.tvMsg.setText(messagesList.get(position).getMsg());

        Linkify.addLinks(holder.tvMsg, Linkify.WEB_URLS);
        holder.tvMsg.setAutoLinkMask(Linkify.PHONE_NUMBERS);
        holder.tvMsg.setLinksClickable(true);
        holder.tvMsg.setMovementMethod(LinkMovementMethod.getInstance());
        holder.tvMsg.setLinkTextColor(Color.parseColor("#0000EC"));

        if (messagesList.get(position).getMyId().equals(String.valueOf(ParentClass.sharedPrefManager.getUserDate().getId()))) {
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (ParentClass.getLang(context).equals("ar")) {

                lp1.setMargins(450, 0, 50, 0);
                lp1.gravity = Gravity.END;

                lp2.setMargins(0, 0, 40, 0);
//                    lp2.gravity = Gravity.END;
                holder.item_rounded.setLayoutParams(lp1);
                holder.item_rounded.setBottomLeftRadius(35);
                holder.item_rounded.setBottomRightRadius(35);
                holder.item_rounded.setTopLeftRadius(35);
                holder.item_rounded.setTopRightRadius(0);
            } else {
                lp1.setMargins(50, 0, 450, 0);
                lp1.gravity = Gravity.START;

                lp2.setMargins(40, 0, 0, 0);
//                    lp2.gravity = Gravity.START;
                holder.item_rounded.setLayoutParams(lp1);
                holder.item_rounded.setBottomLeftRadius(35);
                holder.item_rounded.setBottomRightRadius(35);
                holder.item_rounded.setTopLeftRadius(0);
                holder.item_rounded.setTopRightRadius(35);
            }
            holder.relativeTextType.setBackgroundColor(Color.parseColor("#4D0F51"));
            holder.tvMsg.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (ParentClass.getLang(context).equals("ar")) {
                lp1.setMargins(50, 0, 450, 0);
                lp1.gravity = Gravity.START;


                holder.item_rounded.setLayoutParams(lp1);
                holder.item_rounded.setBottomLeftRadius(0);
                holder.item_rounded.setBottomRightRadius(35);
                holder.item_rounded.setTopLeftRadius(35);
                holder.item_rounded.setTopRightRadius(35);
            } else {
                lp1.setMargins(450, 0, 50, 0);
                lp1.gravity = Gravity.END;


                holder.item_rounded.setLayoutParams(lp1);
                holder.item_rounded.setBottomLeftRadius(35);
                holder.item_rounded.setBottomRightRadius(0);
                holder.item_rounded.setTopLeftRadius(35);
                holder.item_rounded.setTopRightRadius(35);
            }
            holder.relativeTextType.setBackgroundColor(Color.parseColor("#F1F2F9"));
            holder.tvMsg.setTextColor(Color.parseColor("#000000"));
        }


        if (firstTime) {
            rvChat.post(new Runnable() {
                @Override
                public void run() {
                    rvChat.smoothScrollToPosition(messagesList.size() - 1);
                }
            });
            firstTime = false;
        }

    }


    @Override
    public int getItemCount() {
//        Log.e("messagesList.size()", String.valueOf(messagesList.size()));
        return messagesList.size();

    }


    public void addAll(List<ChatModel> data) {
        messagesList.clear();
        messagesList.addAll(data);
        notifyDataSetChanged();
    }

    public void updateModel(int index, ChatModel chatModel) {
        messagesList.set(index, chatModel);
        notifyDataSetChanged();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.relativeTextType)
        RelativeLayout relativeTextType;
//        @BindView(R.id.item_rounded)
        RoundRectView item_rounded;
//        @BindView(R.id.tvMsg)
        TextView tvMsg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);


        }
    }
}


