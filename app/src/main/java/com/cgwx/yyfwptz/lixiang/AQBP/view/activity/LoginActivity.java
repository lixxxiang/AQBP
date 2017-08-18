package com.cgwx.yyfwptz.lixiang.AQBP.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.cgwx.yyfwptz.lixiang.AQBP.R;
import com.cgwx.yyfwptz.lixiang.AQBP.di.components.DaggerLoginComponent;
import com.cgwx.yyfwptz.lixiang.AQBP.di.modules.LoginModule;
import com.cgwx.yyfwptz.lixiang.AQBP.presenter.LoginContract;
import com.cgwx.yyfwptz.lixiang.AQBP.presenter.LoginPresenter;
import com.githang.statusbar.StatusBarCompat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    @BindView(R.id.getVcode)
    Button getVcode;
    @BindView(R.id.tel)
    EditText tel;
    String pTel;

    @Inject
    LoginPresenter presenter;


    public static LoginActivity la;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FAFAFA"));

        SharedPreferences sp = getSharedPreferences("Puser", MODE_PRIVATE);

        la = this;

        DaggerLoginComponent.builder().loginModule(new LoginModule(this))
                .build()
                .inject(this);

        if (sp.getString("pTel", null) != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("pTel", sp.getString("pTel", null));
            intent.putExtra("pId", sp.getString("pId", null));
            intent.putExtra("pName", sp.getString("pName", null));
            startActivity(intent);
            finish();
        }


        tel.setInputType(EditorInfo.TYPE_CLASS_NUMBER);

        getVcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pTel = tel.getText().toString();
                presenter.sendMessage(pTel);
            }
        });

        tel.addTextChangedListener(mTextWatcher);


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
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            getVcode.setBackgroundResource(R.drawable.orabtn);
            if (tel.getText().length() == 0) {
                getVcode.setBackgroundResource(R.drawable.gray);

            }

        }
    };

    @Override
    public void getVCodeSuccess(String tel) {
        Intent intent = new Intent(LoginActivity.this, VCodeActivity.class);
        intent.putExtra("pTel", tel);
        startActivity(intent);
    }
}
