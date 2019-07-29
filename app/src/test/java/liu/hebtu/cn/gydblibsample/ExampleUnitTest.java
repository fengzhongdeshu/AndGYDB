package liu.hebtu.cn.gydblibsample;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        System.out.println(String.format("%s",123));
        //System.out.print(String.format("\\%%s\\%","a"));

        List<String> names = getAllPropertiesWithClass(Person.class) ;
        for (int i = 0 ; i<names.size() ; i++){
            System.out.println(names.get(i));
        }

    }

    public static List<String> getAllPropertiesWithClass(Class clazz){
        List<String> filesList = new ArrayList<>() ;

        Field[] fields = clazz.getDeclaredFields() ;
        for (int i =0 ; i<fields.length ; i++){
            filesList.add(fields[i].getName()) ;
        }

        return filesList ;
    }
    static class Person{
        private String name ;
        private int age ;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}