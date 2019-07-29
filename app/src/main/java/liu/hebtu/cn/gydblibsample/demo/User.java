package liu.hebtu.cn.gydblibsample.demo;

import java.io.Serializable;

/**
 * Author: liuguoyan
 * DateTime: 2019/7/29  上午9:36
 * Company: http://www.everjiankang.com.cn
 * Illustration:
 */
public class User implements Serializable{

    private int _id ;
    private String name ;
    private String hoppy ;
    private int age ;
    private int score ;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHoppy() {
        return hoppy;
    }

    public void setHoppy(String hoppy) {
        this.hoppy = hoppy;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    @Override
    public String toString() {
        return "User{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", hoppy='" + hoppy + '\'' +
                ", age=" + age +
                ", score=" + score +
                '}';
    }
}
