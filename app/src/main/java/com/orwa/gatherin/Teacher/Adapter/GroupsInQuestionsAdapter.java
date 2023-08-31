package com.orwa.gatherin.Teacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.GeneralResponse.GeneralResponse;
import com.orwa.gatherin.Models.GroupsModel.Datum;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.Teacher.Home.View.HomeTeacherActivity;
import com.orwa.gatherin.Teacher.SendQuestionsToGroups.View.SendQuestionsToGroupsActivity;
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

public class GroupsInQuestionsAdapter extends RecyclerView.Adapter<GroupsInQuestionsAdapter.ViewHolder> {
    ArrayList<Datum> list;
    Context context;
    LayoutInflater layoutInflater;
    private int selectedPosition = -1;
    ArrayList<String> chosenServices = new ArrayList<>();
    String categoriesId = "0";
    ArrayList<String> items = new ArrayList();
    TextView tvSave;
    SharedPrefManager sharedPrefManager;
    EditText etEssay;
    EditText etMCQ;
    EditText etFirstChoice;
    EditText etSecondChoice;
    EditText etThirdChoice;
    EditText etFourthChoice;

    String mcq;
    Date date; // the date instance


    public GroupsInQuestionsAdapter(Context context, TextView tvSave,
                                    SharedPrefManager sharedPrefManager, EditText etEssay,
                                    EditText etMCQ, EditText etFirstChoice,
                                    EditText etSecondChoice, EditText etThirdChoice,
                                    EditText etFourthChoice) {
        this.list = new ArrayList<>();
        this.context = context;
        this.tvSave = tvSave;
        this.etEssay = etEssay;
        this.etMCQ = etMCQ;
        this.etFirstChoice = etFirstChoice;
        this.etSecondChoice = etSecondChoice;
        this.etThirdChoice = etThirdChoice;
        this.etFourthChoice = etFourthChoice;
        this.sharedPrefManager = sharedPrefManager;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupsInQuestionsAdapter.ViewHolder(ItemGroupNamesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

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


                sendQuestion();
            }
        });
    }

    void sendQuestion() {
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
        Log.e("sender", "Teachers/" + sharedPrefManager.getUserDate().getId() + "GOOd");
        if (SendQuestionsToGroupsActivity.type.equals("essay")) {
            boolean cancel = false;
            View focusView = null;
            if (TextUtils.isEmpty(etEssay.getText().toString())) {
                etEssay.setError(context.getString(R.string.essayQuestion));
//                focusView = binding.etPassword;
//                cancel = true;
            } else {
                ParentClass.showFlipDialog();
                Log.e("etEssay", etEssay.getText().toString() + "GOOd");


                Observable observable = RetroWeb.getClient().create(AppServices.class).send_questionEssay(categoriesId, year, monthNumber, day, hours, minute, "Teachers/" + sharedPrefManager.getUserDate().getId(), etEssay.getText().toString(), "essay", sharedPrefManager.getAccessToken())
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
        } else {
            boolean cancel = false;
            View focusView = null;
            if (TextUtils.isEmpty(etFirstChoice.getText().toString())) {
                etFirstChoice.setError(context.getString(R.string.pleaseFillAllTheAnswers));
//                focusView = binding.etPassword;
                cancel = true;
            }
            if (TextUtils.isEmpty(etMCQ.getText().toString())) {
                etMCQ.setError(context.getString(R.string.mcqQuestion));
//                focusView = binding.etPassword;
                cancel = true;
            }

            if (TextUtils.isEmpty(etSecondChoice.getText().toString())) {
                etSecondChoice.setError(context.getString(R.string.pleaseFillAllTheAnswers));
//                focusView = binding.etPassword;
                cancel = true;
            }

            if (SendQuestionsToGroupsActivity.numberOfMcq > 2) {
                if (TextUtils.isEmpty(etThirdChoice.getText().toString())) {
                    etThirdChoice.setError(context.getString(R.string.pleaseFillAllTheAnswers));
//                focusView = binding.etPassword;
                    cancel = true;
                }
            }
            if (SendQuestionsToGroupsActivity.numberOfMcq > 3) {
                if (TextUtils.isEmpty(etFourthChoice.getText().toString())) {
                    etFourthChoice.setError(context.getString(R.string.pleaseFillAllTheAnswers));
//                focusView = binding.etPassword;
                    cancel = true;
                }
            }
            if (cancel) {

            } else {
                if (SendQuestionsToGroupsActivity.numberOfMcq == 2) {

                    mcq = etFirstChoice.getText().toString() + "," +
                            etSecondChoice.getText().toString();
                }
                if (SendQuestionsToGroupsActivity.numberOfMcq == 3) {
                    mcq = etFirstChoice.getText().toString() + "," +
                            etSecondChoice.getText().toString() + "," +
                            etThirdChoice.getText().toString();


                }
                if (SendQuestionsToGroupsActivity.numberOfMcq == 4) {
                    mcq =
                            etFirstChoice.getText().toString() +","+
                                    etSecondChoice.getText().toString() +","+
                                    etThirdChoice.getText().toString() +","+
                                    etFourthChoice.getText().toString();

                }
                ParentClass.showFlipDialog();
                Log.e("mcq", mcq + "GOOd");


                Observable observable = RetroWeb.getClient().create(AppServices.class).send_question(categoriesId, year, monthNumber, day, hours, minute, "Teachers/" + sharedPrefManager.getUserDate().getId(), etEssay.getText().toString(), "essay",mcq, sharedPrefManager.getAccessToken())
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
