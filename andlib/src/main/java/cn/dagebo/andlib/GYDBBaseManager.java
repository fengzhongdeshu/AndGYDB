package cn.dagebo.andlib;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: liuguoyan
 * DateTime: 2019/7/20  下午9:37
 * Company: http://www.everjiankang.com.cn
 * Illustration:
 */
public class GYDBBaseManager {

    private GYDBHelper mDBHelper ;

    private void log(String info){
        if (GYDBConfig.debug)
        Log.d("GYDBBaseManager" , info) ;
    }

    private static GYDBBaseManager instance ;

    private GYDBBaseManager(){
    }

    public static GYDBBaseManager get(){
        if (instance==null){
            instance = new GYDBBaseManager() ;
        }
        return instance ;
    }

    /**
     * 初始化app时，必须调用的代码
     * @param c
     */
    public void init(Context c){
        mDBHelper = new GYDBHelper(c) ;
    }

    /**
     记录总数
     @param model_name 即表名
     @return 数量
     */
    public int countForModel(String model_name){

        String sql = String.format("select count(*) as tot from ( select bus_id from CONTENTTABLE WHERE model = '%s' GROUP BY bus_id  ) " ,model_name) ;
        int max = 0 ;
        Cursor cursor =mDBHelper.getReadableDatabase().rawQuery(sql ,null) ;
        while (cursor.moveToNext()){
            max = cursor.getInt(cursor.getColumnIndex("tot")) ;
        }
        cursor.close();
        return max ;
    }

    /**
     添加记录
     @param models 要添加的字段
     @return 是否添加成功
     */
    public boolean insertContent(List<GYDBModel> models){

        boolean success = true ;
        int bus_id = queryMaxBusId(models.get(0).getModel())+1 ;

        SQLiteDatabase db = mDBHelper.getWritableDatabase() ;

        db.beginTransaction();
        try{
            for (int i = 0 ;i <models.size() ; i++){
                String sql = String.format("INSERT INTO CONTENTTABLE (model,colume,bus_id,value) VALUES ('%s','%s',%d,'%s')",models.get(i).getModel() ,models.get(i).getColume() ,bus_id , models.get(i).getValue());
                db.execSQL(sql);
            }
            db.setTransactionSuccessful();
        }catch (SQLException e){
            success = false ;
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
        return success ;
    }

    /**
     查询记录
     @param models 字段列表:字段值等于此列表中的字段值
     @return 实体对象
     */
    public List<GYDBModel> getContentWithFiledsEnques(List<GYDBModel> models){


        if (models.size()==0) {
            log("param models can not be empty!!!");
            return null ;
        }
        String sql = "SELECT * FROM CONTENTTABLE WHERE  model = '%s' AND  bus_id IN ( %s ) ORDER BY bus_id ASC " ;

        String condition = getConditionsIds(models) ;
        sql = String.format(sql , models.get(0).getModel(), condition) ;

        return getModelsByQuerySQL(sql) ;
    }


    /**
     查询某个model的所有记录
     @param modelStr model名称
     @return 所有记录
     */
    public List<GYDBModel> getAllWithModelString(String modelStr){

        String sql = String.format("select * from CONTENTTABLE WHERE model = '%s' ORDER BY bus_id ASC " , modelStr) ;

        return getModelsByQuerySQL(sql) ;
    }


    /**
     删除
     @param models 条件
     @return return value description
     */
    public boolean removeContentWithFiledsEques(List<GYDBModel> models){

        if (models.size() ==0){
            log("param models can not be empty!!!");
            return false ;
        }

        String sql = "DELETE FROM CONTENTTABLE WHERE  model = '%s' AND  bus_id IN ( %s ) " ;
        String condition = getConditionsIds(models) ;
        sql = String.format(sql ,models.get(0).getModel(), condition) ;

        return executeUpdate(sql) ;
    }


    /**
     删除所有
     @param modelStr modelStr description
     @return return value description
     */
    public boolean removeAllWithModelString(String modelStr){

        String sql = String.format("DELETE FROM CONTENTTABLE WHERE model = '%s' " , modelStr) ;

        return executeUpdate(sql) ;
    }


    /**
     更新model
     @param files 要更新的字段
     @param models 条件
     @return return value description
     */
    public boolean updateWithFiledsEques(List<GYDBModel> files , List<GYDBModel> models){

        List<GYDBModel> findModels = getContentWithFiledsEnques(models) ;

        if (findModels ==null || findModels.size()==0){
            return true ;
        }
        boolean success = true ;

        SQLiteDatabase db = mDBHelper.getWritableDatabase() ;
        db.beginTransaction();

        try{
            for (int i = 0 ; i<files.size() ; i++){
                for (int j = 0 ; j<findModels.size() ; j++){
                    if (files.get(i).getColume().equals(findModels.get(j).getColume())){
                        String sql = String.format("UPDATE CONTENTTABLE SET value = '%s' WHERE m_id = %s ",files.get(i).getValue() , findModels.get(j).getM_id()) ;
                        db.execSQL(sql);
                    }
                }
            }
            db.setTransactionSuccessful();
        }catch (SQLException e){
            e.printStackTrace();
            success = false ;
        }finally {
            db.endTransaction();
        }
        return success ;
    }



    //#pragma mark --------------- 查询工具类 ---------------

    //工具类
    /** 查询最大的id号，用于插入记录之前使用
     * @model model名，类似于平表中的表名
     * @return 最大的行号  ，默认0
     */
    public int queryMaxBusId(String model_name){
        int max = -1 ;
        String sql = String.format("SELECT  bus_id  FROM CONTENTTABLE WHERE model = '%s' ORDER BY bus_id DESC LIMIT 1 " , model_name) ;
        Cursor cursor =mDBHelper.getReadableDatabase().rawQuery(sql ,null) ;
        while (cursor.moveToNext()){
            max = cursor.getInt(cursor.getColumnIndex("bus_id")) ;
        }
        cursor.close();
        return max ;
    }

    public boolean executeUpdate(String sql){
        log(sql);

        boolean success = true ;
        SQLiteDatabase db = mDBHelper.getWritableDatabase() ;
        try{
            db.execSQL(sql);
        }catch (SQLException e){
            success = false ;
        }

        return success ;
    }


    /**
     * 查询
     * @param sql
     * @return
     */
    public List<GYDBModel> getModelsByQuerySQL(String sql){

        log(sql);
        Cursor cursor = mDBHelper.getReadableDatabase().rawQuery(sql ,null) ;
        List<GYDBModel> result = new ArrayList<>() ;
        while (cursor.moveToNext()){
            GYDBModel model = new GYDBModel() ;
            model.setM_id(cursor.getInt(cursor.getColumnIndex("m_id")));
            model.setModel(cursor.getString(cursor.getColumnIndex("model")));
            model.setColume(cursor.getString(cursor.getColumnIndex("colume")));
            model.setBus_id(cursor.getInt(cursor.getColumnIndex("bus_id")));
            model.setValue(cursor.getString(cursor.getColumnIndex("value")));
            result.add(model) ;
        }
        cursor.close();
        return result ;
    }

    /**
     通过models 获取符合条件的bus_id
     @param models models description
     @return return value description
     */
    public String getConditionsIds(List<GYDBModel> models){

        StringBuilder condition = new StringBuilder() ;

        for (int i = 0 ; i<models.size() ; i++){
            String com = validString(models.get(i).getCondition()) ? models.get(i).getCondition() : "=" ;
            String valueWrapper = com.equalsIgnoreCase("like") ? "%%%s%%%" : "%s" ;

            if (validString(models.get(i).getLogic())){
                String combine = models.get(i).getLogic().equals("AND") ? " INTERSECT " :
                        (models.get(i).getLogic().equals("OR") ? " UNION " : "");
                condition.append(combine) ;
            }

            //String value = String.format(valueWrapper , models.get(i).getValue()) ;
            String value = com.equalsIgnoreCase("like") ? "%"+models.get(i).getValue()+"%" : models.get(i).getValue() ;

            String con = String.format(" SELECT bus_id FROM CONTENTTABLE WHERE model = '%s' AND colume ='%s' AND value %s '%s' ",
                    models.get(i).getModel() , models.get(i).getColume() , com , value
                    ) ;
            condition.append(con) ;
        }

        return condition.toString();
    }


    private boolean validString(String s){
        if (s!=null && s.length()>0){
            return true ;
        }
        return false ;
    }



}
