package cn.dagebo.andlib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Author: liuguoyan
 * DateTime: 2019/7/20  下午10:12
 * Company: http://www.everjiankang.com.cn
 * Illustration:
 */
public class GYDBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = GYDBConfig.dbName ;
    private final static int DB_VERSION = 1 ;

    public GYDBHelper(Context c){
        this(c , DB_NAME , null , DB_VERSION) ;
    }

    public GYDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE CONTENTTABLE ( m_id integer PRIMARY KEY AUTOINCREMENT , model TEXT, colume TEXT, bus_id integer, value TEXT );" ;
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
