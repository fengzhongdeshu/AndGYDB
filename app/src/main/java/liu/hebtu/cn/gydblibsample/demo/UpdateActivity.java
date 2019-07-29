package liu.hebtu.cn.gydblibsample.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import cn.dagebo.andlib.GYDBOprator;
import liu.hebtu.cn.gydblibsample.R;

public class UpdateActivity extends AppCompatActivity {

    private Button btn_add ;

    private EditText edit_id , edit_name , edit_hobbpy , edit_age , edit_score ;

    private GYDBOprator oprator ;

    /**   */
    public static final String INTENT_EXTRA_USER = "_edit_user";

    private User user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        getSupportActionBar().setTitle("更新");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        oprator = GYDBOprator.getInstance().initModel(User.class);

        btn_add = (Button)findViewById(R.id.btn_add) ;
        edit_id = (EditText) findViewById(R.id.edit_id) ;
        edit_name = (EditText) findViewById(R.id.edit_name) ;
        edit_hobbpy = (EditText) findViewById(R.id.edit_hobbpy) ;
        edit_age = (EditText) findViewById(R.id.edit_age) ;
        edit_score = (EditText) findViewById(R.id.edit_score) ;

        edit_id.setEnabled(false);
        edit_name.setEnabled(false);

        if (getIntent().hasExtra(INTENT_EXTRA_USER)){
            user = (User)getIntent().getSerializableExtra(INTENT_EXTRA_USER) ;
            edit_id.setText(user.get_id()+"");
            edit_name.setText(user.getName());
            edit_hobbpy.setText(user.getHoppy());
            edit_age.setText(user.getAge()+"");
            edit_score.setText(user.getScore()+"");
        }

        btn_add.setText("更新");
        btn_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                User u = new User() ;
                u.set_id(Integer.valueOf(edit_id.getText().toString()));
                u.setName(edit_name.getText().toString());
                u.setHoppy(edit_hobbpy.getText().toString());
                u.setAge(Integer.valueOf(edit_age.getText().toString()));
                u.setScore(Integer.valueOf(edit_score.getText().toString()));

                oprator.where("_id","=",user.get_id()+"");
                oprator.updateColume("hoppy" , u.getHoppy()) ;
                oprator.updateColume("age",String.valueOf(u.getAge())) ;
                oprator.updateColume("score" , String.valueOf(u.getScore())) ;

                boolean bol = oprator.update() ;
                if (bol){
                    btn_add.setText("更新成功");
                }else {
                    btn_add.setText("更新失败");
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
