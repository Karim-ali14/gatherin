package com.orwa.gatherin.Teacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.GeneralResponse.GeneralResponse;
import com.orwa.gatherin.Models.GroupsModel.Datum;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.Teacher.Home.View.HomeTeacherActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.utils.SharedPrefManager;
import com.orwa.gatherin.databinding.ItemGroupNamesBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class GroupsInNotificationsAdapter extends RecyclerView.Adapter<GroupsInNotificationsAdapter.ViewHolder> {
    ArrayList<Datum> list;
    Context context;
    LayoutInflater layoutInflater;
    private int selectedPosition = -1;
    ArrayList<String> chosenServices = new ArrayList<>();
    String categoriesId = "0";
    ArrayList<String> items = new ArrayList();
    ImageView tvSave;
    SharedPrefManager sharedPrefManager;
    EditText etMsg;

    Date date; // the date instance


    public GroupsInNotificationsAdapter(Context context, ImageView tvSave,
                                        SharedPrefManager sharedPrefManager, EditText etMsg) {
        this.list = new ArrayList<>();
        this.context = context;
        this.tvSave = tvSave;
        this.etMsg = etMsg;
        this.sharedPrefManager = sharedPrefManager;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupsInNotificationsAdapter.ViewHolder(ItemGroupNamesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.tvGroupName.setText(list.get(position).getName());
        holder.binding.cbGroup.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                chosenServices.add(String.valueOf(list.get(position).getId()));
            } else {
                chosenServices.remove(String.valueOf(list.get(position).getId()));
            }
            Log.e("chosenList", chosenServices.toString() + "good");
        });
        tvSave.setOnClickListener(v -> {
            items.clear();
            Log.e("chosenServices", chosenServices.size() + "G");
            Log.e("chosenServices", chosenServices + "G");
            for (int i = 0; i < chosenServices.size(); i++) {
                if (!(chosenServices.get(i).equals(""))) {
                    items.add(String.valueOf(chosenServices.get(i)));
                }
            }
            categoriesId = android.text.TextUtils.join(",", items);
            if (categoriesId.equals("")) {
                ParentClass.makeErrorToast(context, context.getString(R.string.pleaseChooseAtLeastOneGroup));
            } else {
                Log.e("categoriesId", categoriesId + " GOOD");
                sendNotification();
            }
        });
    }

    private void sendNotification() {
        date=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String monthNumber = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String hours = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        Log.e("day", day + "GOOd");
        Log.e("monthNumber", monthNumber + "GOOd");
        Log.e("year", year + "GOOd");
        Log.e("hours", hours + "GOOd");
        Log.e("minute", minute + "GOOd");
        Log.e("token", sharedPrefManager.getAccessToken() + "GOOd");
            boolean cancel = false;
            View focusView = null;
            if (TextUtils.isEmpty(etMsg.getText().toString())) {
                etMsg.setError(context.getString(R.string.note));
            } else {
                ParentClass.showFlipDialog();
                Log.e("etMsg", etMsg.getText().toString() + "GOOd");
                Observable observable = RetroWeb.getClient().create(AppServices.class).sendNotificationToGroups(categoriesId, year, monthNumber, day, hours, minute, etMsg.getText().toString(),sharedPrefManager.getAccessToken())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                Observer<GeneralResponse> observer = new Observer<GeneralResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GeneralResponse userModel) {
                        ParentClass.dismissFlipDialog();
                        if (userModel != null) {
                            if (userModel.getStatus()) {
                                Intent intent = new Intent(context, HomeTeacherActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                context.startActivity(intent);
                                Bungee.split(context);
                                ParentClass.makeSuccessToast(context, userModel.getMessage());
                            } else {
                                ParentClass.makeErrorToast(context, userModel.getMessage());
                            }
                        } else {
                            ParentClass.makeErrorToast(context, context.getString(R.string.somethingWentWrong));
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        ParentClass.handleException(context, e);
                        Log.e("e", e.toString());
                        ParentClass.dismissFlipDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                };
                observable.subscribe(observer);


            }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getItemViewType(int position) {
        return position;
    }

    public void addAll(List<Datum> data) {
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemGroupNamesBinding binding;

        public ViewHolder(ItemGroupNamesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
