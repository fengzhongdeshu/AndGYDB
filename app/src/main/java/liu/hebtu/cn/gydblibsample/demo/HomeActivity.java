package liu.hebtu.cn.gydblibsample.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import liu.hebtu.cn.gydblibsample.R;
import liu.hebtu.cn.gydblibsample.gydb.GYDBBaseManager;

public class HomeActivity extends AppCompatActivity {

    private Button btn_add , btn_query ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //在应用启动时初始化lib，也可以放在Application中
        GYDBBaseManager.get().init(this.getApplicationContext());

        getSupportActionBar().setTitle("GYDB");

        btn_add = (Button)findViewById(R.id.btn_add) ;
        btn_query = (Button)findViewById(R.id.btn_query) ;

        btn_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this ,AddActivity.class) ;
                HomeActivity.this.startActivity(intent);
            }
        });
        btn_query.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this ,QueryActivity.class) ;
                HomeActivity.this.startActivity(intent);
            }
        });

    }



}
