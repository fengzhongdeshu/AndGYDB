package cn.dagebo.andlib;

/**
 * Author: liuguoyan
 * DateTime: 2019/7/20  下午9:33
 * Company: http://www.everjiankang.com.cn
 * Illustration:
 */
public class GYDBModel {

    private int m_id  ;

    private String model ;

    private String colume ;

    private int bus_id ;

    private String value ;
    /**
     搜索时的条件，如 < ,> ,= 等，默认是 =
     */
    private String condition ;
    /**
     逻辑运算符   or , and
     */
    private String logic ;

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColume() {
        return colume;
    }

    public void setColume(String colume) {
        this.colume = colume;
    }

    public int getBus_id() {
        return bus_id;
    }

    public void setBus_id(int bus_id) {
        this.bus_id = bus_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }
}
