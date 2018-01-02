package com.lenovo.tvflowrecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private VodHomeAdapter mVodHomeAdapter;
    private List<Module> mModuleList = new ArrayList<>();
    private TvRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (TvRecyclerView) findViewById(R.id.recycler_view);
        mVodHomeAdapter = new VodHomeAdapter(this, mModuleList);
        mRecyclerView.setAdapter(mVodHomeAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        bindChannelData();
    }

    /**
     * view 绑定数据
     */
    private void bindChannelData() {
        Module module ;
        for (int i = 0; i < 10; i++) {
            int flag = i % 3;
            if (flag == 0) {
                module = new Module();
                module.setStyleType(Constants.ITEM_TYPE_EIGHTEEN);
                mModuleList.add(module);
            } else if (flag == 1) {
                module = new Module();
                module.setStyleType(Constants.ITEM_TYPE_THREE);
                mModuleList.add(module);
            } else if (flag == 2) {
                module = new Module();
                module.setStyleType(Constants.ITEM_TYPE_ONE);
                mModuleList.add(module);
            }

        }
        LogUtil.i(this,"MainActivity.bindChannelData"+mModuleList);
        mVodHomeAdapter.notifyDataSetChanged();
    }

}
