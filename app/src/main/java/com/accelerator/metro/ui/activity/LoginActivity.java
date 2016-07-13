package com.accelerator.metro.ui.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.accelerator.metro.R;
import com.accelerator.metro.bean.UserInfo;
import com.accelerator.metro.bean.UserLogin;
import com.accelerator.metro.contract.LoginContract;
import com.accelerator.metro.presenter.LoginPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private static final String TAG = "LoginActivity";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.login_account_inputLayout)
    TextInputLayout accountLayout;
    @Bind(R.id.login_password_inputLayout)
    TextInputLayout passwordLayout;
    @Bind(R.id.login_btn)
    Button btnLogin;
    @Bind(R.id.login_tv_register)
    TextView tvRegister;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.login);
        setSupportActionBar(toolbar);
        presenter=new LoginPresenter(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @OnClick(R.id.login_tv_register)
    public void tvRegister(View view) {
        //没有账号？创建一个

    }


    @OnClick(R.id.login_btn)
    public void loginClick(View view) {
        //登录按钮监听

        EditText account = accountLayout.getEditText();
        EditText password = passwordLayout.getEditText();

        String userName = account != null ? account.getText().toString().trim() : null;
        String userPassword = password != null ? password.getText().toString() : null;

        if (checkNotNull(userName)) {
            if (checkNotNull(userPassword)) {
                UserLogin login = new UserLogin();
                login.setUserName(userName);
                login.setUserPassword(userPassword);
                //与服务器通信
                //  presenter.login(login);
                btnLogin.setClickable(false);
            } else {
                Snackbar.make(view, R.string.login_not_empty_password, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.OK, null)
                        .show();
            }
        } else {
            Snackbar.make(view, R.string.login_not_empty_account, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.OK, null)
                    .show();
        }

        //Toast.makeText(this,account.getText().toString()+password.getText().toString(),Toast.LENGTH_SHORT).show();
    }

    private boolean checkNotNull(String str) {
        return !TextUtils.isEmpty(str);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.menu_login_forgot_password:
                Toast.makeText(this, "Forget", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSucceed(UserInfo values) {

    }

    @Override
    public void onFailure(String err) {

    }

    @Override
    public void onCompleted() {

    }
}
