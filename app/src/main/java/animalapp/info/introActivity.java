package animalapp.info;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class introActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        startLoading();
    }

    //인트로 실행하는 함수
    private void startLoading(){
        //2초 후 인트로 액티비티 제거
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();//인트로 액티비티 확실히 종료시키기(뒤로 버튼으로도 돌아오지 못함)
            }
        },2000);//ms단위로 입력해야함
    }

    protected void onPause(){
        super.onPause(); //화면 벗어나면 인트로 취소
        finish();
    }
}