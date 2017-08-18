package com.cgwx.yyfwptz.lixiang.AQBP.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.cgwx.yyfwptz.lixiang.AQBP.R;
import com.cgwx.yyfwptz.lixiang.AQBP.di.components.DaggerVCodeComponent;
import com.cgwx.yyfwptz.lixiang.AQBP.di.modules.VCodeModule;
import com.cgwx.yyfwptz.lixiang.AQBP.presenter.VCodeContract;
import com.cgwx.yyfwptz.lixiang.AQBP.presenter.VCodePresenter;
import com.cgwx.yyfwptz.lixiang.AQBP.util.Constants;
import com.githang.statusbar.StatusBarCompat;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VCodeActivity extends AppCompatActivity implements VCodeContract.View{

    @BindView(R.id.back) Button back;
    @BindView(R.id.tel) TextView tel;
    @BindView(R.id.login) Button login;
    @BindView(R.id.vcodeedit) EditText vcodeedit;
    @BindView(R.id.countback) TextView countback;

    @Inject
    VCodePresenter presenter;

    public static VCodeActivity va;
    String pTel;
    String vcode;
    int count = 30;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            count--;
            countback.setText("" + count + "秒后 重新发送验证码");
            countback.setTextColor(Color.parseColor("#b5b5b5"));
            handler.postDelayed(this, 1000);

            if (count == 0) {
                countback.setText("重新发送验证码");
                countback.setTextColor(Color.parseColor("#ff9801"));
                handler.removeCallbacks(runnable);
                countback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Constants.isFastDoubleClick()){
                        }else{
                            presenter.resendMessage(pTel);
                        }
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vcode);
        ButterKnife.bind(this);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FAFAFA"));

        DaggerVCodeComponent.builder().vCodeModule(new VCodeModule(this))
                .build()
                .inject(this);
        Intent intent = getIntent();

        va = this;
        if (intent != null) {
            pTel = intent.getStringExtra("pTel");
            tel.setText(pTel);
        }

        handler.postDelayed(runnable, 1000);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VCodeActivity.this.finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vcode = vcodeedit.getText().toString();
                presenter.checkMessage(pTel, vcode);

            }
        });

        vcodeedit.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        vcodeedit.addTextChangedListener(mTextWatcher);



    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
//          mTextView.setText(s);//将输入的内容实时显示
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            login.setBackgroundResource(R.drawable.orabtn);
            if (vcodeedit.getText().length() == 0) {
                login.setBackgroundResource(R.drawable.gray);

            }

        }
    };


    @Override
    public void recount() {
        handler.postDelayed(runnable, 1000);
        count = 30;
    }

    @Override
    public void checkVcodeSuccess(String id, String name, String tel) {

        Intent intent = new Intent(VCodeActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArray("pinfos", new String[]{id, name, tel});
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
            finish();
        }
    }

}
