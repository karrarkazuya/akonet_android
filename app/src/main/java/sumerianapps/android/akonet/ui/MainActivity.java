package sumerianapps.android.akonet.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.Locale;

import sumerianapps.android.akonet.Models.Javascript.Akonet;
import sumerianapps.android.akonet.R;
import sumerianapps.android.akonet.config.Statics;
import sumerianapps.android.akonet.ui.adapters.ListAdapter;

public class MainActivity extends AppCompatActivity {
    private RecyclerView items_list;

    private boolean injected = false;
    private boolean isp_gathered = true;
    private boolean finished = false;


    private WebView wb;
    private TextView lt;
    private TextView myisp;
    private Button about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // set ltr
        Configuration config = getResources().getConfiguration();
        config.setLayoutDirection(Locale.ENGLISH);

        initializeMembers();

        startWebView();
        lt.setText("Connecting to servers..");

    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public void startWebView() {
        wb.getSettings().setJavaScriptEnabled(true);

        wb.getSettings().setLoadWithOverviewMode(true);
        wb.getSettings().setUseWideViewPort(true);

        wb.getSettings().setSupportZoom(true);
        wb.getSettings().setBuiltInZoomControls(true);
        wb.getSettings().setDisplayZoomControls(false);

        wb.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        wb.setScrollbarFadingEnabled(false);
        wb.addJavascriptInterface(new MyJavaScriptInterface(MainActivity.this), "Android");
        wb.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                try{
                    if(!injected) {
                        lt.setText("Loading data..");
                        wb.loadUrl("javascript: "+Akonet.makeJsonMain()+" window.Android.showHTML(api);");
                        injected = true;
                    }else if(!isp_gathered){
                        wb.loadUrl("javascript: "+Akonet.getISP()+" window.Android.showHTML(isp);");
                    }
                }catch(Exception e){

                }

            }
        });

        wb.loadUrl(Statics.api);
    }

    private void initializeMembers()
    {

        lt = findViewById(R.id.loading_label);
        myisp = findViewById(R.id.my_isp);
        items_list = findViewById(R.id.items_list);
        items_list.setNestedScrollingEnabled(false);
        items_list.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));
        items_list.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));

        wb = findViewById(R.id.webview);


        about = findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(findViewById(R.id.about_layout).getVisibility() == View.GONE) {
                    findViewById(R.id.loading).setVisibility(View.GONE);
                    findViewById(R.id.items_list_layout).setVisibility(View.GONE);
                    findViewById(R.id.about_layout).setVisibility(View.VISIBLE);
                }else{
                    if(injected){
                        findViewById(R.id.items_list_layout).setVisibility(View.VISIBLE);
                    }else{
                        findViewById(R.id.loading).setVisibility(View.VISIBLE);
                        findViewById(R.id.items_list_layout).setVisibility(View.GONE);
                    }
                    findViewById(R.id.about_layout).setVisibility(View.GONE);
                }
            }
        });


        findViewById(R.id.about_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(injected){
                    findViewById(R.id.items_list_layout).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.loading).setVisibility(View.VISIBLE);
                    findViewById(R.id.items_list_layout).setVisibility(View.GONE);
                }
                findViewById(R.id.about_layout).setVisibility(View.GONE);
            }
        });
    }


    /**
     * Interface to read from the js console
     */
    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(final String html) {
            Log.e("rec html :", html);

            if (!finished) {
                if (!isp_gathered) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                myisp.setText(html);
                            } catch (Exception e) {
                                lt.setText("Error, unable to connect :(");
                                findViewById(R.id.loading).setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    finished = true;
                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray list = new JSONArray(html);
                                findViewById(R.id.loading).setVisibility(View.GONE);

                                findViewById(R.id.items_list_layout).setVisibility(View.VISIBLE);
                                ListAdapter adapter = new ListAdapter(list, getApplicationContext());
                                items_list.setAdapter(adapter);

                                isp_gathered = false;
                                wb.loadUrl(Statics.isp);

                            } catch (Exception e) {
                                lt.setText("Error, unable to connect :(");
                                findViewById(R.id.loading).setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }

            }
        }

    }

}
