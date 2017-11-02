package be.pxl.ievent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import be.pxl.ievent.App;
import be.pxl.ievent.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.pb_login) ProgressBar pbLogin;
    @BindView(R.id.et_login_mail) EditText etLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login_do)
    public void onLoginClicked(){
        boolean correctCredentials = false;

        pbLogin.setVisibility(View.VISIBLE);

        if(checkCredentials()){
            login();
            pbLogin.setVisibility(View.INVISIBLE);
        }
        else{
            failed();
            pbLogin.setVisibility(View.INVISIBLE);
        }
    }

    private void failed() {
        Toast.makeText(this,"Incorrecte logingegevens",Toast.LENGTH_LONG).show();
    }

    private void login() {
        App.setUserMail(etLogin.getText().toString());

        if(App.isStudent()){
            Intent intent = new Intent(this, StudentOverviewActivity.class);
            startActivity(intent);
        }
        else if(App.isTeacher()){
            Intent intent = new Intent(this, TeacherOverviewActivity.class);
            startActivity(intent);
        }
    }


    private boolean checkCredentials() {
        if(etLogin.getText().toString().contains("pxl.be")){
            return true;
        }
        return false;
    }
}
