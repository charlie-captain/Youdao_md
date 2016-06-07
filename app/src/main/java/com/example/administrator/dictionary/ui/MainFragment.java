package com.example.administrator.dictionary.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.dictionary.R;
import com.example.administrator.dictionary.base.NotificationSearch;
import com.example.administrator.dictionary.config.ConfigFinal;
import com.example.administrator.dictionary.https.ImageJson;
import com.example.administrator.dictionary.https.SendHttpRequest;
import com.example.administrator.dictionary.utils.HandleImage;
import com.example.administrator.dictionary.utils.Media;
import com.example.administrator.dictionary.view.MainViewPager;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Administrator on 2016/5/18.
 */
public class MainFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View rootView;
    private ImageView imageView;
    private LinearLayout mainSearch, mainSound;
    private RelativeLayout mainText;
    private TextView content, chinese;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Set<Integer> timeRandom = new HashSet<Integer>();
    private Media mediaPlayer;

    private static int mode = ConfigFinal.IMAGE;
    private int year;
    private int month;
    private int day;

    private Handler handler = new Handler() {             //一个消息处理机制
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConfigFinal.IMAGE:
                    mainText.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    imageView.setImageBitmap((Bitmap) msg.obj);
                    content.setText(ImageJson.getContent());
                    chinese.setText(ImageJson.getNote());
                    mainSound.setVisibility(View.VISIBLE);
                    Toast.makeText(MainViewPager.getContext(), "更新成功", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    break;
                case ConfigFinal.SD_IMAGE:
                    mainText.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap((Bitmap) msg.obj);
                    content.setText(HandleImage.getContent());
                    chinese.setText(HandleImage.getNote());
                    mainSound.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    Toast.makeText(MainViewPager.getContext(), "更新成功", Toast.LENGTH_SHORT).show();
                    break;
                case ConfigFinal.EROOR:
                    initImage(ConfigFinal.EROOR);
                    break;
                case ConfigFinal.NOT_CONNECT:
                    swipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    Toast.makeText(MainViewPager.getContext(), "网络未连接， 请检查网络设置", Toast.LENGTH_SHORT).show();
                    break;
            }


        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.main_fragment, container, false);
            init(rootView);
        }
        return rootView;
    }

    private void init(View view) {
        imageView = (ImageView) view.findViewById(R.id.imageView);
        mainSearch = (LinearLayout) view.findViewById(R.id.main_search);
        mainSound = (LinearLayout) view.findViewById(R.id.main_sound);
        content = (TextView) view.findViewById(R.id.main_sentence);
        chinese = (TextView) view.findViewById(R.id.main_chinese);
        mainText = (RelativeLayout) view.findViewById(R.id.main_text);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.main_swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.red);

        initEvent();
    }

    private void initEvent() {
        mediaPlayer = new Media();
        content.setText(ConfigFinal.NORMAL_SENTENCE);
        chinese.setText(ConfigFinal.NORMAL_CONTENT);

        initImage(ConfigFinal.RIGHT);
        mainSearch.setOnClickListener(this);
        mainSound.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    public void initImage(final int what) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                day = calendar.get(Calendar.DAY_OF_MONTH);
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                });
                if (!HandleImage.isBitmapHere(year, month, day) || what == ConfigFinal.EROOR) {
                    SendHttpRequest.sendHttpRequestImage(ConfigFinal.IMAGE_BASE + year + "-" + month + "-" + day, handler, year, month, day, ConfigFinal.IMAGE);
                    mode = ConfigFinal.IMAGE;
                } else {
                    HandleImage.getImageSentence(MainViewPager.getDbSqlite(), handler, year, month, day);
                    mode = ConfigFinal.SD_IMAGE;
                }
            }
        }).start();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_search:
                Intent intent = new Intent(MainViewPager.getContext(), NotificationSearch.class);
                intent.putExtra("theme", "main");
                startActivity(intent);
                break;
            case R.id.main_sound:
                if (mode == ConfigFinal.IMAGE) {
                    Media.musicSound(ImageJson.getMp3());
                } else if (mode == ConfigFinal.SD_IMAGE) {
                    Media.musicSound(HandleImage.getMp3());
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR) - 1;
                int month = 14 - calendar.get(Calendar.MONTH) + random.nextInt(3);
                int day = random.nextInt(28) + 1;
                if (!timeRandom.contains(day) && timeRandom.size() < 28) {
                    timeRandom.add(day);
                    Log.d("http", Integer.toString(day));
                    SendHttpRequest.sendHttpRequestImage(ConfigFinal.IMAGE_BASE + year + "-" + month + "-" + day, handler, year, month, day, ConfigFinal.REFRESH);
                    mode = ConfigFinal.IMAGE;
                    Media.setMediaStop();           //停止播放音乐
                } else if (timeRandom.size() == 28) {
                    timeRandom.clear();
                } else {
                    onRefresh();
                }
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }


}
