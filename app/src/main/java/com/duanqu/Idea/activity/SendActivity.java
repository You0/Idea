package com.duanqu.Idea.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.duanqu.Idea.CustomView.HorizontalScrollViewEx;
import com.duanqu.Idea.CustomView.ToSomeOne;
import com.duanqu.Idea.CustomView.UserTag;
import com.duanqu.Idea.R;
import com.duanqu.Idea.fragment.TextPostFragment;

/**
 * Created by Administrator on 2016/9/13.
 */
public class SendActivity extends AppCompatActivity implements View.OnClickListener{
    private String id;
    private Toolbar toolbar;
    private ToSomeOne toSomeOne;
    private FrameLayout frameLayout;
    private HorizontalScrollViewEx hs;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sent_layout);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        init();
        chooseFragment();
    }

    private void chooseFragment() {
        if(id == "image"){


        }else if(id == "text"){
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_content,new TextPostFragment()).commit();


        }else if(id == "video"){

        }



    }



    private void init() {
        //toSomeOne = (ToSomeOne) findViewById(R.id.tosomeone);

        View view = UserTag.Build(this,"http://115.159.159.65:8080/EAsy/Headurl/3ebcfd6f-f456-429a-bf64-ca04f508.jpg","五颜六色");
        View view1 = UserTag.Build(this,"http://115.159.159.65:8080/EAsy/Headurl/f6aa7245-5d3c-4887-8e78-8482fb28.jpg","动力小车@何叔平");

        view.setPadding(15,5,0,5);
        view1.setPadding(15,5,0,5);


//        toSomeOne.addView(view);
//        toSomeOne.addView(view2);
//        toSomeOne.addView(view1);


        hs = (HorizontalScrollViewEx) findViewById(R.id.hs);

        hs.addView(view1);
        hs.addView(view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("发送");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        hs.setOnClickListener(this);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(id == "image"){


        }else if(id == "text"){




        }else if(id == "video"){

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hs:{




                break;
            }

        }
    }
}
