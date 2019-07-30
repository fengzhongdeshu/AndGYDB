AndGYDB
=========================

![Image text](https://github.com/fengzhongdeshu/GYDB/blob/master/Image/gydb_03.png)
## Example

To run the example project, clone the repo, and run `pod install` from the Example directory first.

## Document
**框架特点:**  
1:使用sqlite3为数据存储媒介，以键值形式存储数据。  
2:app升级或者数据字段升级，无需关心数据迁移问题，不需要维护版本，数据存储字段可以任意添加或者删除。  
3:完全面向对象操作，且不需要继承特定的类，只需要把Model存储即可。  
4:操作简单，简单调用即可实现CRUD操作。  
 

##Usage##  
**初始化**  
在Application或者在使用lib前：  
```java
GYDBBaseManager.get().init(this.getApplicationContext());  
```

**配置**  
```java
(1)调试模式:GYDBConfig.debug = true ;  
(2)数据库名称:GYDBConfig.dbName = "demo.db" ;  
```

**保存**  
```java
User u = new User() ;  
GYDBOprator oprator = GYDBOprator.getInstance().initModel(User.class);  
oprator.addSaveModel(u) ;  
List<Object> list = oprator.save() ;  
if (list.size()==0){  
      btn_add.setText("保存成功");  
    }else {  
      btn_add.setText("保存失败");  
  }  
```
**查询**    
```java
GYDBOprator oprator = GYDBOprator.getInstance().initModel(User.class) ;  
oprator.where("name" , "=" , "张三") ;  
oprator.andWhere("age" , ">" , "12") ;  
List<Object> arr = oprator.query() ;  
```
**删除**  
```java
GYDBOprator oprator = GYDBOprator.getInstance().initModel(User.class) ;  
oprator.where("name" , "=" , "张三") ;  
oprator.andWhere("age" , ">" , "12") ;  
boolean bol = oprator.remove() ;  
```
**更新**  
```java
GYDBOprator oprator = GYDBOprator.getInstance().initModel(User.class) ;  
//设置要更新的字段和值  
oprator.updateColume("age","12") ;  
//添加过滤条件  
oprator.where("name" , "=" , "张三") ;  
oprator.andWhere("age" , ">" , "12") ;  
boolean bol = oprator.update();  
```


## Installation

GYDB is available through gradle . To install
it, simply add the following line to your build.gradle:

```
Add it in your root build.gradle at the end of repositories:
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Add the dependency
dependencies {
	        implementation 'com.github.fengzhongdeshu:AndGYDB:0.3.0'
	}
```

## Author

liuguoyan, liuguoyan21@126.com

## License

GYDB is available under the MIT license. See the LICENSE file for more info.
