package liu.hebtu.cn.gydblibsample.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.dagebo.andlib.GYDBOprator;
import liu.hebtu.cn.gydblibsample.R;

public class QueryActivity extends AppCompatActivity {

    private Button btn_delete , btn_query ,btn_edit;
    private LinearLayout wheresLayout ;
    private TextView text_result;

    private List<ConditionPacker> conditionPackers = new ArrayList<>();

    private GYDBOprator oprator ;

    private List<Object> results = new ArrayList<>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        oprator = GYDBOprator.getInstance().initModel(User.class) ;

        getSupportActionBar().setTitle("查询");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        btn_delete = (Button)findViewById(R.id.btn_delete) ;
        btn_query = (Button)findViewById(R.id.btn_query) ;
        btn_edit = (Button)findViewById(R.id.btn_edit) ;
        text_result = (TextView) findViewById(R.id.text_result) ;

        wheresLayout = (LinearLayout)findViewById(R.id.layout_wheres) ;

        for (int i = 0 ; i<3 ; i++){
            View view = View.inflate(this , R.layout.qury_wheres_item ,null) ;
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT) ;
            wheresLayout.addView(view , p);

            ConditionPacker packer = new ConditionPacker() ;
            packer.logicFiled = (EditText) view.findViewById(R.id.item_logic) ;
            packer.nameFiled = (EditText) view.findViewById(R.id.item_colume) ;
            packer.valueFiled = (EditText) view.findViewById(R.id.item_value) ;
            packer.compFiled = (EditText) view.findViewById(R.id.item_comp) ;
            packer.compFiled.setHint(">,<,=,like");
            conditionPackers.add(packer) ;

        }

        btn_query.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                query() ;
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                edit();
            }
        });

    }

    private void query(){
        addWheres(oprator);
        List<Object> arr = oprator.query() ;
        results.clear();
        if (arr!=null){
            results.addAll(arr) ;
        }
        if (arr.size()>0){
            StringBuilder shower = new StringBuilder() ;
            for (int i = 0 ; i<arr.size(); i++){
                shower.append(arr.get(i).toString()) ;
                shower.append("\n");
            }

            text_result.setText(shower.toString());
        }else {
            text_result.setText("无内容");
        }
    }
    private void delete(){
        addWheres(oprator);
        boolean bol = oprator.remove() ;
        if (bol){
            text_result.setText("删除成功");
        }else {
            text_result.setText("删除失败");
        }
    }

    private void edit(){

        if (results.size()==0){
            text_result.setText("请先查询要编辑的数据");
        }else {
            Intent intent = new Intent(this, UpdateActivity.class) ;
            intent.putExtra(UpdateActivity.INTENT_EXTRA_USER , (User)results.get(0)) ;
            startActivity(intent);
        }


    }


    private void addWheres(GYDBOprator oprator){
        for (int i = 0 ; i<conditionPackers.size() ; i++){
            String name = conditionPackers.get(i).nameFiled.getText().toString() ;
            String com = conditionPackers.get(i).compFiled.getText().toString() ;
            String value = conditionPackers.get(i).valueFiled.getText().toString() ;
            String logic = conditionPackers.get(i).logicFiled.getText().toString() ;

            if (i==0 && name.length()>0 && com.length()>0 && value.length()>0){
                oprator.where(name , com , value) ;
            }
            if (name.length()>0 && com.length()>0 && value.length()>0 && logic.length()>0){
                if (logic.equals("or")){
                    oprator.orWhere(name,com,value) ;
                }else if(logic.equals("and")){
                    oprator.andWhere(name,com,value) ;
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.query_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                this.finish();
                return true ;
            case R.id.action_clean:
                clearConditions();
                return true ;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearConditions(){
        for (int i = 0 ; i<conditionPackers.size() ; i++){
            conditionPackers.get(i).logicFiled.setText("");
            conditionPackers.get(i).nameFiled.setText("");
            conditionPackers.get(i).compFiled.setText("");
            conditionPackers.get(i).valueFiled.setText("");
        }
    }


    public static class ConditionPacker{
        public EditText nameFiled ;
        public EditText compFiled ;
        public EditText valueFiled ;
        public EditText logicFiled ;
    }

}
