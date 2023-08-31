package com.orwa.gatherin.User.ChatUser.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;


import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


import com.orwa.gatherin.R;
import com.orwa.gatherin.User.SendMessageToLeader.View.SendMessageToLeaderActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.databinding.ActivityChatUserBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import spencerstudios.com.bungeelib.Bungee;

public class ChatUserActivity extends ParentClass {
    ActivityChatUserBinding binding;
    private Socket mSocket;

    {
        try {//https://64.225.17.119:6080
            mSocket = IO.socket("https://backend.gathering.host");
        } catch (URISyntaxException e) {
            Log.e("errorrr", "maysloh");
        }
    }

    Emitter.Listener onLocationChanged = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    Log.e("hahah", String.valueOf(args.length));
                    Log.e("args", String.valueOf(args) + "GOOD");
                    JSONArray data = (JSONArray) args[0];

                    Log.e("data", data + " GooD");

//                    try {
//                        if (data.getJSONObject(0).getString("lat") != "") {
//                            if (first) {
//                                sharedPrefManager.setSocketLat(data.getJSONObject(0).getString("lat"));
//                                sharedPrefManager.setSocketLng(data.getJSONObject(0).getString("lng"));
//                                drawLineAndPutMarkers(data);
//                                first = false;
//
//
//                            }
//
//                        }
//                        if (meterDistanceBetweenPoints(Double.parseDouble(data.getJSONObject(0).getString("lat")), Double.parseDouble(data.getJSONObject(0).getString("lng")),
//                                Double.parseDouble(sharedPrefManager.getSocketLat()),
//                                Double.parseDouble(sharedPrefManager.getSocketLng()))
//                                > 5) {
//                            sharedPrefManager.setSocketLat(data.getJSONObject(0).getString("lat"));
//                            sharedPrefManager.setSocketLng(data.getJSONObject(0).getString("lng"));
//                            drawLineAndPutMarkers(data);
//
//                            Log.e("driverLatSock", data.getJSONObject(0).getString("lat") + "GOOD");
//                            Log.e("driverLngSock", data.getJSONObject(0).getString("lng") + "GOOD");
//
//
//                        } else {
//                            Log.e("distanceCheck", "did not send because distance is short");
//                        }
//
//
//                    } catch (Exception e) {
//                        Log.e("exceptionRoute", e.toString());
//                    }

                    //Write whatever to want to do after delay specified (1 sec)
                }
            });


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initEventDriven();
        mSocket.connect();
        if (getIntent().getStringExtra("type").equals("tech")) {
            Log.e("args2", getIntent().getStringExtra("id") +
                    getIntent().getStringExtra("tech_id")+" gOOD");
            mSocket.on("getchatmessage_" + getIntent().getStringExtra("id") + getIntent().getStringExtra("tech_id"), onLocationChanged);
        }



        mSocket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("argss",args+"GO");
//                options.auth.put("authorization", "bearer 1234");
                mSocket.connect();
            }
        });
    }

    private void initEventDriven() {
        binding.rlSendAnswer.setOnClickListener(v -> {
            Intent intent = new Intent(ChatUserActivity.this, SendMessageToLeaderActivity.class);
            startActivity(intent);
            Bungee.split(ChatUserActivity.this);
        });
        binding.ivBack.setOnClickListener(v -> {
            finish();
            Bungee.split(ChatUserActivity.this);
        });
        binding.ivSend.setOnClickListener(v -> {
            attemptSendTeacher();
        });
    }

    private void attemptSendTeacher() {
        boolean cancel = false;
        if (TextUtils.isEmpty(binding.etChat.getText().toString())) {
            binding.etChat.setError(getString(R.string.writeMsg));
            cancel = true;
        }
        if (cancel) {
        } else {
            final JSONObject obj = new JSONObject();
            try {
                Log.e("chatId", getIntent().getStringExtra("id") + ParentClass.sharedPrefManager.getUserDate().getId() + "GOOD");
                Log.e("senderId", ParentClass.sharedPrefManager.getUserDate().getId() + "GOOD");
                Log.e("text", binding.etChat.getText().toString() + "GOOD");

                obj.put("chatId", getIntent().getStringExtra("id") + getIntent().getStringExtra("tech_id"));
                obj.put("senderId", sharedPrefManager.getUserDate().getId());
                obj.put("senderName", sharedPrefManager.getUserDate().getUsername());
                obj.put("text", binding.etChat.getText().toString());
                obj.put("questionType", "text");
//                obj.put("question", false);
                mSocket.emit("addchatmessage", obj);
                binding.etChat.setText("");

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("emit", e.toString());
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("getchatmessage_" + getIntent().getStringExtra("id") + getIntent().getStringExtra("tech_id"), onLocationChanged);

    }
}