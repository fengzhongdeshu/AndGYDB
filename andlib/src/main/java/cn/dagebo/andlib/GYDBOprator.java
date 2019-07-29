package cn.dagebo.andlib;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: liuguoyan
 * DateTime: 2019/7/24  下午3:49
 * Company: http://www.everjiankang.com.cn
 * Illustration:
 */
public class GYDBOprator {

    private static GYDBOprator oprator ;

    private GYDBOprator(){
    }

    public static GYDBOprator getInstance(){
        if (oprator ==null){
            oprator = new GYDBOprator() ;
        }
        return oprator ;
    }

    private Class clazz ;

    /**
     条件
     */
    private List<GYDBModel> wheres = new ArrayList<>() ;

    /**
     更新的models
     */
    private List<GYDBModel> uppers = new ArrayList<>() ;

    /**
     要保存的model列表
     */
    private List<Object> saveModels = new ArrayList<>() ;

    /**
     * 初始化Model类型
     * @param clazz
     */
    public GYDBOprator initModel(Class clazz){
        this.clazz = clazz ;
        return this ;
    }

    /**
     恢复运行环境
     */
    private void resetEnviroment(){
        wheres.clear();
        uppers.clear();
        saveModels.clear();
    }

    /**
     条件组装器
     @param colume 字段名
     @param compare 比较符号:< , > = 等
     @param value 比较的值
     @return instance
     */
    public GYDBOprator where(String colume , String compare , String value){
        return commonWhereColume(colume , compare , value ,null) ;
    }

    public GYDBOprator andWhere(String colume , String compare , String value){
        return commonWhereColume(colume , compare , value ,"AND") ;
    }

    public GYDBOprator orWhere(String colume , String compare , String value){
        return commonWhereColume(colume , compare , value ,"OR") ;
    }

    private GYDBOprator commonWhereColume(String colume , String compare , String value ,String logic){
        String name = modelNameWithClass(this.clazz) ;

        GYDBModel model = new GYDBModel() ;
        model.setModel(name);
        model.setColume(colume);
        model.setValue(value);
        model.setCondition(compare);
        model.setLogic(logic);

        this.wheres.add(model) ;

        return this ;
    }

    /**
     更新容器，用于存放更新的字段的值
     @param colume 要更新的字段
     @param value 字段值
     @return return value description
     */
    public GYDBOprator updateColume(String colume , String value){
        String name = modelNameWithClass(this.clazz) ;
        GYDBModel model = new GYDBModel() ;
        model.setModel(name);
        model.setColume(colume);
        model.setValue(value);
        this.uppers.add(model) ;
        return this ;
    }

    /**
     添加要保存的model
     @param model model description
     @return return value description
     */
    public GYDBOprator addSaveModel(Object model){

        this.saveModels.add(model) ;

        return this ;
    }

    /**
     批量添加要保存的model
     @param models models description
     @return return value description
     */
    public GYDBOprator addSaveModelList(List<Object> models){
        this.saveModels.addAll(models) ;
        return this ;
    }

    /**
     保存
     @return return 返回存储失败的model , 返回nil 表示全部存储成功
     */
    public List<Object> save(){

        List<Object> failModel = new ArrayList<>() ;

        if (saveModels!=null && saveModels.size()>0){
            for (int i = 0 ;i<saveModels.size() ; i++){
                Object model = saveModels.get(i) ;
                boolean saveb = saveOneModel(model) ;
                if (!saveb){
                    failModel.add(model) ;
                }
            }
        }
        resetEnviroment();
        return failModel ;
    }

    /**
     查询
     @return result
     */
    public List<Object> query(){

        List<GYDBModel> models = null ;

        if (wheres.size()>0){
            models = GYDBBaseManager.get().getContentWithFiledsEnques(wheres) ;
        } else {
            models = GYDBBaseManager.get().getAllWithModelString(modelNameWithClass(this.clazz)) ;
        }

        List<Object> bizModels = new ArrayList<>() ;
        int bus_id = -1 ;
        Object m = null ;

        try{
            for (int i = 0 ; i<models.size() ; i++){
                GYDBModel mod = models.get(i) ;
                if (bus_id!= mod.getBus_id()){
                    m = this.clazz.newInstance();
                    bizModels.add(m) ;
                    bus_id = mod.getBus_id() ;
                }
                //通过反射设置属性值
                Field f = m.getClass().getDeclaredField(mod.getColume()) ;
                f.setAccessible(true);
                String type=f.getType().getSimpleName();
                if("int".equalsIgnoreCase(type)||"integer".equalsIgnoreCase(type)){
                    f.set(m,Integer.parseInt(mod.getValue()));
                }else if("double".equalsIgnoreCase(type)){
                    f.set(m,Double.parseDouble(mod.getValue()));
                }else if("float".equalsIgnoreCase(type)){
                    f.set(m , Float.parseFloat(mod.getValue()));
                }else if("String".equalsIgnoreCase(type)){
                    f.set(m,mod.getValue());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        resetEnviroment();

        return bizModels ;
    }

    /**
     删除
     @return result
     */
    //-(BOOL)remove ;
    public boolean remove(){

        boolean ret = false ;

        if (wheres.size()>0){
            ret = GYDBBaseManager.get().removeContentWithFiledsEques(wheres);
        }else {
            ret = GYDBBaseManager.get().removeAllWithModelString(modelNameWithClass(this.clazz)) ;
        }
        resetEnviroment();
        return ret ;
    }

    /**
     更新
     @return return value description
     */
    public boolean update(){
        boolean bol  = GYDBBaseManager.get().updateWithFiledsEques(uppers , wheres) ;
        resetEnviroment();
        return bol ;
    }

    public String modelNameWithClass(Class clazz){
        return clazz.getSimpleName() ;
    }



    /********************* 工具方法 **********************/

    private boolean saveOneModel(Object model){

        Field[] array  = getAllPropertiesWithClass(model.getClass());
        List<GYDBModel> models = new ArrayList<>() ;

        try{
            for (int i =0 ;i<array.length ; i++){
                String key = array[i].getName() ;
                Field filed = array[i];
                filed.setAccessible(true);
                String value = String.valueOf(filed.get(model)) ;
                String name = modelNameWithClass(model.getClass()) ;
                GYDBModel m = new GYDBModel() ;
                m.setModel(name);
                m.setColume(key);
                m.setValue(value);
                models.add(m) ;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return GYDBBaseManager.get().insertContent(models) ;
    }


    private Field[] getAllPropertiesWithClass(Class clazz){
        Field[] fields = clazz.getDeclaredFields() ;
        return fields ;
    }

}
