package chunxpd.company.app.kutcse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import chunxpd.company.app.kutcse.R;

public class LoginActivity extends AppCompatActivity {

    private LoginActivity.SessionCallback sessionCallback;
    private WebView mWebView;
    private Handler mHandler = new Handler();
    private boolean mFlag = false;
    private boolean bool_isBackbuttonTap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        sessionCallback = new LoginActivity.SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();


        findViewById(R.id.btn_nologin).setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){
            return ;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            request();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.d("error", "Session Fail Error is " + exception.getMessage().toString());
        }
    }

    public void request(){
        UserManagement.requestMe(new MeResponseCallback() {

            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    //redirectMainActivity();
                }
            }


            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("error", "Session Closed Error is " + errorResult.toString());
            }

            @Override
            public void onNotSignedUp() {
                Toast.makeText(LoginActivity.this, " 로그인 안됨 ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(UserProfile result) {
                //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.
                //Toast.makeText(MainActivity.this, "사용자 이름은 " + result.getNickname(), Toast.LENGTH_SHORT).show();
                String kakaoID = String.valueOf(result.getId()); // userProfile에서 ID값을 가져옴
                String kakaoNickname = result.getNickname();     // Nickname 값을 가져옴
                Logger.d("UserProfile : " + result);
                Toast.makeText(LoginActivity.this, "로그인 완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();


            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!mFlag) {
                Toast.makeText(this, "'뒤로'버튼을 한번 더 누르시면 종료 됩니다!", Toast.LENGTH_SHORT).show();    // 종료안내 toast 를 출력
                mFlag = true;
                mHandler.sendEmptyMessageDelayed(0, 2000);    // 2000ms 만큼 딜레이
                return false;
            }else {
                // 앱 종료 code
                moveTaskToBack(true);
                finish();
                android.os.Process.killProcess(Process.myPid());
            }
        } else {
            // 뒤로 가기 실행
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
        }
        return true;
    }

}
