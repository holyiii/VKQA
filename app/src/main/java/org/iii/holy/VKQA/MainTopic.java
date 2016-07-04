package org.iii.holy.VKQA;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.ImageButton;


public class MainTopic extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageButton ibtnENQ;
    private ImageButton ibtnPWQ;
    private ImageButton ibtnACQ;
    private ImageButton ibtnBKQ;
    private ImageButton ibtnETQ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_topic);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ibtnENQ=(ImageButton) findViewById(R.id.ibtnENQ);
        ibtnPWQ=(ImageButton) findViewById(R.id.ibtnPWQ);
        ibtnACQ=(ImageButton) findViewById(R.id.ibtnACQ);
        ibtnBKQ=(ImageButton) findViewById(R.id.ibtnBKQ);
        ibtnETQ=(ImageButton) findViewById(R.id.ibtnETQ);
        ibtnENQ.setOnClickListener(enqLis);
        ibtnPWQ.setOnClickListener(pwqLis);
        ibtnACQ.setOnClickListener(acqLis);
        ibtnBKQ.setOnClickListener(bkqLis);
        ibtnETQ.setOnClickListener(etqLis);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#ff3333"));
        toolbar.setTitle("CarQ");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
            }
        });


    }
    private View.OnClickListener enqLis=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            enterTopic("engine");
        }
    };
    private View.OnClickListener pwqLis=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            enterTopic("power");
        }
    };
    private View.OnClickListener acqLis=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            enterTopic("air-conditioning");
        }
    };
    private View.OnClickListener bkqLis=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            enterTopic("brakes");
        }
    };
    private View.OnClickListener etqLis=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            enterTopic("others");
        }
    };

    private void enterTopic(String SubTopic)
    {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        bundle.putString("subtopic", SubTopic);
        //使用者帳號資訊 Start
        bundle.putString("name" , "ABC123");
        bundle.putString("token" , "zzzzzz");
        //使用者帳號資訊 End
        intent.putExtra("Bundle" , bundle);
        intent.setClass(this , QAActivity.class);
        startActivity(intent);

    }
}
