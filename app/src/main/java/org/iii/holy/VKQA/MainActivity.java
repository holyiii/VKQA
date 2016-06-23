package org.iii.holy.VKQA;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.iii.holy.model.AnswerItem;
import org.iii.holy.model.Auth;
import org.iii.holy.model.LoginItem;
import org.iii.holy.model.QuestionItem;
import org.iii.holy.model.QuestionShort;
import org.iii.holy.model.SimplifyQuestion;
import org.iii.holy.model.detailKarmaRule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * @author Leo
 */
public class MainActivity extends AppCompatActivity {


    private final String sHostip = "http://140.92.63.141:8080";
    //private final String sHostip = "http://210.61.217.168:28080";
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    int RListType = 1;
    int lastgetpara = 0;
    RestFulSrv restfulobj;
    detailKarmaRule karmaRule = new detailKarmaRule();
    private boolean isLoading;
    private Button btnPost;
    private EditText edtPost;
    private String loginInfo;
    private List<Map<String, Object>> data = new ArrayList<>();
    private RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, data);
    private Handler handler = new Handler();
    private Spinner spTag;

    public String getHostIp() {
        return this.sHostip;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login: //點了settings

                LayoutInflater inflater = this.getLayoutInflater();
                final View vDig = inflater.inflate(R.layout.dig_login, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(vDig)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EditText etUsrName = (EditText) vDig.findViewById(R.id.edtUserNameDialog);
                                EditText etPassword = (EditText) vDig.findViewById(R.id.edtPasswordDialog);
                                login(etUsrName.getText().toString(), etPassword.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                return;
                            }

                        });
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            case R.id.action_logout:
                logout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loginInfo = login("abc123","1111");
        setContentView(R.layout.activity_notice);
        btnPost = (Button) findViewById(R.id.btnSentPost);
        edtPost = (EditText) findViewById(R.id.edtPost);
        spTag = (Spinner) findViewById(R.id.sp_tag);
        ButterKnife.inject(this);
        restfulobj = new RestFulSrv(this);
        initView();
        initData();


        btnPost.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                String postResponse;
                Toast toast;
                ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = CM.getActiveNetworkInfo();
                boolean bNetisConn = netInfo != null && netInfo.isConnectedOrConnecting();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (bNetisConn) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
                    Auth mAuser = gson.fromJson(loginInfo, Auth.class);
                    if (mAuser != null && mAuser.isSuccess()) {


                        String sPostContents = edtPost.getText().toString();
                        String[] sAryTitleContents = sPostContents.split("\n");
                        String sPostTitle = "", sPostContent = "";
                        switch (RListType) {
                            case 1: //post a question
                                if (sAryTitleContents.length > 1) {
                                    sPostTitle = sAryTitleContents[0];
                                    for (int i = 1; i < sAryTitleContents.length; i++)
                                        sPostContent = sPostContent + sAryTitleContents[i] + "\n";
                                    if (sPostTitle.length() > 15 && sPostContent.length() > 30) {
                                        int TagType = spTag.getSelectedItemPosition();
                                        String sArrayTag[] = {"repair", "trade", "insurance", "misc"};
                                        postQuestion(sPostTitle, sPostContent, sArrayTag[TagType]);
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                edtPost.setText("");
                                            }
                                        }, 100);

                                        if (imm.isActive())
                                            imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                                    } else {
                                        toast = Toast.makeText(MainActivity.this,
                                                "第一行必須至少15字，第二行之後必須至少30字", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                } else {
                                    toast = Toast.makeText(MainActivity.this,
                                            "請輸入兩行文字以上，第一行必須至少15字，第二行之後必須至少30字", Toast.LENGTH_SHORT);
                                    //顯示Toast
                                    toast.show();
                                }
                                break;
                            case 2:// post the answer
                                if (sPostContents.length() > 29) {
                                    postAnswer(sPostContents);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            edtPost.setText("");
                                        }
                                    }, 100);
                                    if (imm.isActive())
                                        imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                } else {
                                    toast = Toast.makeText(MainActivity.this,
                                            "回答之文字必須多於30字", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                break;
                        }

                    } else {

                        toast = Toast.makeText(MainActivity.this,
                                "帳戶登入失敗", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                } else {
                    toast = Toast.makeText(MainActivity.this,
                            "網路連線異常", Toast.LENGTH_SHORT);
                    toast.show();

                }


            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //logout
    }


    public void setQlistStyle() //set Q page Style
    {
        RListType = 1;
        setSupportActionBar(toolbar);
        toolbar.setTitle("問題列表");
        toolbar.setBackgroundColor(Color.parseColor("#ffbb33"));
        // load Q list
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
            }
        });
        spTag.setEnabled(true);
    }

    public void setQAlistStyle() // Set QA page Style
    {
        RListType = 2;
        setSupportActionBar(toolbar);
        toolbar.setTitle("問題回覆");
        toolbar.setBackgroundColor(Color.parseColor("#99cc00"));
        // load QA list
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setQlistStyle();
                data.clear();
                getQpageData(0, 34);
            }
        });
        spTag.setEnabled(false);
    }

    public void initView() {
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.notice);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RListType == 1)
                    finish();
                else {
                    setQlistStyle();
                    //getQpageData(0, 34);
                }
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.blueStatus);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        data.clear();
                        if (RListType == 1) {
                            lastgetpara = 0;
                            adapter.showfoot();
                            getQpageData(0, 34);
                            adapter.hidefoot();
                        } else {
                            adapter.showfoot();
                            getApageData(lastgetpara);
                            adapter.hidefoot();
                        }
                    }
                }, 1000);
            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


            }

            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {

                    boolean isRefreshing = swipeRefreshLayout.isRefreshing();
                    if (isRefreshing) {
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        adapter.showfoot();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (RListType == 1) {
                                    getQpageData(layoutManager.findLastVisibleItemPosition(), 35);
                                } else {
                                    getApageData(lastgetpara);
                                }

                                isLoading = false;
                                adapter.hidefoot();

                            }
                        }, 1000);
                    }
                }
            }
        });


        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (RListType == 1) {

                    adapter.showfoot();
                    setQAlistStyle();
                    //get Q ID
                    getApageData(Integer.valueOf(data.get(position).get("article_id").toString()));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.hidefoot();
                        }
                    }, 1000);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }


    public void initData() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setQlistStyle();
                adapter.showfoot();
                getQpageData(0, 34);
                adapter.hidefoot();
                toolbar.setSubtitle("遊客");
                getKarmaRule();
            }
        }, 1000);


    }

    private void getQpageData(int startSeqNum, int qryRecordsNum)

    {
        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = CM.getActiveNetworkInfo();
        boolean bNetisConn = netInfo != null && netInfo.isConnectedOrConnecting();
        if (bNetisConn) {
            double pagenum = Math.ceil((double) (startSeqNum + qryRecordsNum) / 35.0);
            restfulobj.get(sHostip + "/api/q/list/" + (int) pagenum);
            lastgetpara = (int) pagenum;
        } else {
            Toast toast = Toast.makeText(MainActivity.this,
                    "網路連線異常", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void freshQpageUI(Map<String, Object> resResult) {
        String sQlist;
        if (resResult != null) {
            sQlist = resResult.get("body").toString();
            Gson gson = new Gson();
            List<QuestionShort> qlist;
            qlist = Arrays.asList(gson.fromJson(sQlist, QuestionShort[].class));
            for (int i = 0; i < qlist.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                long postTime = qlist.get(i).getCreatedAt();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTimeStamp = dateFormat.format(postTime);

                String sArtInfo = Long.toString(qlist.get(i).getAnswerCount()) + " 人回答.";
                map.put("article_info", sArtInfo);

                map.put("postTime", currentTimeStamp);
                map.put("context", qlist.get(i).getTitle());
                map.put("user", qlist.get(i).getAuthor().getName());
                map.put("karma", Long.toString(qlist.get(i).getAuthor().getKarma()));
                map.put("article_id", qlist.get(i).getId().toString());
                boolean bTagEmpty = qlist.get(i).getTagList().isEmpty();
                String cTag = "無";
                if (!bTagEmpty) {
                    String eTag = qlist.get(i).getTagList().get(0).getName();
                    switch (eTag) {
                        case "repair":
                            cTag = "維修保養";
                            break;
                        case "trade":
                            cTag = "買賣交易";
                            break;
                        case "insurance":
                            cTag = "車輛保險";
                            break;
                        default:
                            cTag = "其它問題";
                    }
                }
                map.put("tag", cTag);
                data.add(map);
            }

        }
        adapter.setQAListType(RListType);
        swipeRefreshLayout.setRefreshing(false);
        adapter.notifyItemRemoved(adapter.getItemCount());
        adapter.notifyDataSetChanged();

    }

    private void getApageData(int articleID)
    //articleID is the Question article unique id
    {
        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = CM.getActiveNetworkInfo();
        boolean bNetisConn = netInfo != null && netInfo.isConnectedOrConnecting();
        if (bNetisConn) {

            restfulobj.get(sHostip + "/api/qDetail/get/" + articleID);
            lastgetpara = (int) articleID;

        } else {
            Toast toast = Toast.makeText(MainActivity.this,
                    "網路連線異常", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public void freshApageUI(Map<String, Object> resResult)
    //articleID is the Question article unique id
    {

        data.clear();
        String sAlist = resResult.get("body").toString();
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        long postTime = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeStamp = null;
        if (sAlist != null) {
            sAlist = sAlist.replace("\n", "");

            QuestionItem qitem = new QuestionItem();
            qitem = gson.fromJson(sAlist, QuestionItem.class);
            //qitem = Arrays.asList(gson.fromJson(sAlist, QuestionItem[].class));
            //pust q item into data
            Map<String, Object> map = new HashMap<>();
            String Title_Content = "<h4><font color='#99cc00''>" + qitem.getTitle().toString() + "</h4><p>" + qitem.getContent().toString();

            postTime = qitem.getCreatedAt();
            currentTimeStamp = dateFormat.format(postTime);
            map.put("postTime", currentTimeStamp);

            String sArtInfo = Long.toString(qitem.getVoteCount()) + " 人覺得有深度.";
            map.put("article_info", sArtInfo);

            map.put("context", Title_Content);
            map.put("user", qitem.getAuthor().getName());
            map.put("karma", Long.toString(qitem.getAuthor().getKarma()));
            map.put("article_id", qitem.getId().toString());

            boolean bTagEmpty = qitem.getTagList().isEmpty();
            String cTag = "無";
            if (!bTagEmpty) {
                String eTag = qitem.getTagList().get(0).getName();
                switch (eTag) {
                    case "repair":
                        cTag = "維修保養";
                        break;
                    case "trade":
                        cTag = "買賣交易";
                        break;
                    case "insurance":
                        cTag = "車輛保險";
                        break;
                    default:
                        cTag = "其它問題";
                }
            }
            map.put("tag", cTag);
            data.add(map);
            List<AnswerItem> alist = new ArrayList<>();
            alist = qitem.getAnswers();
            for (int i = alist.size() - 1; i >= 0; i--) {
                map = new HashMap<>();
                postTime = alist.get(i).getCreatedAt();
                currentTimeStamp = dateFormat.format(postTime);
                map.put("postTime", currentTimeStamp);

                sArtInfo = Long.toString(alist.get(i).getVoteCount()) + " 人認為答的好.";
                map.put("article_info", sArtInfo);

                map.put("context", alist.get(i).getContent());
                map.put("user", alist.get(i).getAuthor().getName());
                map.put("karma", Long.toString(alist.get(i).getAuthor().getKarma()));
                map.put("article_id", alist.get(i).getId().toString());
                map.put("tag", cTag);
                data.add(map);
            }
        }
        adapter.setQAListType(RListType);
        swipeRefreshLayout.setRefreshing(false);
        adapter.notifyItemRemoved(adapter.getItemCount());
        adapter.notifyDataSetChanged();
    }

    private void login(String usrName, String mtoken) {

        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = CM.getActiveNetworkInfo();
        boolean bNetisConn = netInfo != null && netInfo.isConnectedOrConnecting();
        loginInfo = null;
        if (bNetisConn) {
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            LoginItem testUser = new LoginItem();
            testUser.setName(usrName);
            testUser.setToken(mtoken);
            String sloginItem = gson.toJson(testUser);
            sloginItem = sloginItem.replace("\n", "");
            restfulobj.post(sHostip + "/api/auth", "login", sloginItem);

        } else {
            Toast toast = Toast.makeText(MainActivity.this,
                    "網路連線異常", Toast.LENGTH_SHORT);
            toast.show();

        }
        return;
    }

    public void setAuth(Map<String, Object> resResult) {
        loginInfo = resResult.get("body").toString();
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        Auth mAuser = gson.fromJson(loginInfo, Auth.class);
        if (mAuser != null && mAuser.isSuccess()) {
            toolbar.setSubtitle("Hi, " + mAuser.getUser().getName().toString());
            adapter.setAuth(mAuser);
            Toast toast = Toast.makeText(MainActivity.this,
                    "登入成功", Toast.LENGTH_SHORT);
            toast.show();
            adapter.notifyDataSetChanged();
        } else {
            toolbar.setSubtitle("遊客");
            adapter.clearAuth();
            Toast toast = Toast.makeText(MainActivity.this,
                    "無法登入", Toast.LENGTH_SHORT);
            toast.show();
            loginInfo = null;
        }
    }

    private void logout() {

        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = CM.getActiveNetworkInfo();
        boolean bNetisConn = netInfo != null && netInfo.isConnectedOrConnecting();
        if (bNetisConn) {
            restfulobj.get(sHostip + "/api/logout");

        } else {
            Toast toast = Toast.makeText(MainActivity.this,
                    "網路連線異常", Toast.LENGTH_SHORT);
            toast.show();

        }

    }

    public void freshLogoutUI(Map<String, Object> resResult) {

        String sLogout = resResult.get("body").toString();
        if (sLogout != null) {
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            Auth mAuser = gson.fromJson(sLogout, Auth.class);

            if (mAuser != null && mAuser.isSuccess()) {
                Toast toast = Toast.makeText(MainActivity.this,
                        "登出成功", Toast.LENGTH_SHORT);
                toast.show();
                loginInfo = null;
                restfulobj.ClearCookie();
                toolbar.setSubtitle("遊客");
                adapter.clearAuth();
                adapter.notifyDataSetChanged();

            } else {
                Toast toast = Toast.makeText(MainActivity.this,
                        "登出失敗", Toast.LENGTH_SHORT);
                toast.show();
            }


        } else {
            Toast toast = Toast.makeText(MainActivity.this,
                    "登出失敗", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    private void postQuestion(String title, String content, String tag) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        SimplifyQuestion simpleQ = new SimplifyQuestion();
        simpleQ.setTitle(title);
        simpleQ.setContent(content);
        simpleQ.setTag(tag);
        String sSimpleQ = gson.toJson(simpleQ);
        //modify it
        restfulobj.post(sHostip + "/api/q/new", "ask", sSimpleQ);
        return;
    }

    private void postAnswer(String content) {
        //modify it
        restfulobj.post(sHostip + "/api/q/newAnswer/" + lastgetpara, "answer", content);
        return;
    }

    private void getKarmaRule() {
        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = CM.getActiveNetworkInfo();
        boolean bNetisConn = netInfo != null && netInfo.isConnectedOrConnecting();
        if (bNetisConn) {
            restfulobj.get(sHostip + "/api/karmaRule");
        } else {
            Toast toast = Toast.makeText(MainActivity.this,
                    "網路連線異常", Toast.LENGTH_SHORT);
            toast.show();

        }
    }

    public void setKarma(Map<String, Object> resResult) {
        String sKarmaRule = null;
        if (resResult != null) {
            sKarmaRule = resResult.get("body").toString();
            Gson gson = new Gson();
            detailKarmaRule karmaRule = new detailKarmaRule();
            karmaRule = gson.fromJson(sKarmaRule, detailKarmaRule.class);
            adapter.setKarmaRule(karmaRule);

        }
    }

    public void RefreshQA() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                data.clear();
                if (RListType == 1) {
                    lastgetpara = 0;
                    adapter.showfoot();
                    getQpageData(0, 34);
                    adapter.hidefoot();
                } else {
                    adapter.showfoot();
                    getApageData(lastgetpara);
                    adapter.hidefoot();
                }

            }
        }, 1000);

    }

    public void connectError(Map<String, Object> resResult) {
        String errorMsg = resResult.get("body").toString();
        Toast toast = Toast.makeText(MainActivity.this,
                errorMsg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
