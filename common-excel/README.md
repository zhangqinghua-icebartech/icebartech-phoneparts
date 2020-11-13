# Excel4J v2.0.0
   
## 一. v2.0.0新特性
1. Excel读取支持部分类型转换了(如转为Integer,Long,Date(部分)等) v2.0.0之前只能全部内容转为String
2. Excel支持非注解读取Excel内容了,内容存于`List<List<String>>`对象内
3. 现在支持`List<List<String>>`导出Excel了(可以不基于模板)
4. Excel新增了Map数据样式映射功能(模板可以为每个key设置一个样式,具体定义为:&key, 导出Map数据的样式将与key值映射)

## 二. 基于注解
```
   @ExcelField(title = "学号", order = 1)
   private Long id;

   @ExcelField(title = "姓名", order = 2)
   private String name;

   @ExcelField(title = "入学日期", order = 3)
   private Date date;

   @ExcelField(title = "班级", order = 4)
   private Integer classes;

   @ExcelField(title = "是否开除", order = 5)
   private String expel;
```

## 三. 读取Excel快速实现

### 1.待读取Excel(截图)
![待读取Excel截图](https://raw.githubusercontent.com/Crab2died/Excel4J/master/src/test/resource/image/v2.0.0/students_02.png)

### 2. 转换函数(/src/test/java/base/Excel2Module.java#excel2Object2)
```
   @Test
   public void excel2Object2() throws Exception {
       
       String path = "D:\\IdeaSpace\\Excel4J\\src\\test\\resource\\students_02.xlsx";

       // 不基于注解,将Excel内容读至List<List<String>>对象内
       List<List<String>> lists = ExcelUtils.getInstance().readExcel2List(path, 1, 3, 0);
       System.out.println("读取Excel至String数组：");
       for (List<String> list : lists) {
           System.out.println(list);
       }
       // 基于注解,将Excel内容读至List<Student2>对象内
       List<Student2> students = ExcelUtils.getInstance().readExcel2Objects(path, Student2.class, 0);
       System.out.println("读取Excel至对象数组(支持类型转换)：");
       for (Student2 st : students) {
           System.out.println(st);
       }
   }
```

### 3. 转换结果
```
读取Excel至String数组：
[10000000000001, 张三, 2016/01/19, 101, 是]
[10000000000002, 李四, 2017-11-17 10:19:10, 201, 否]
[10000000000004, 王二, 2017/11/17, 301, 否]
读取Excel至对象数组(支持类型转换)：
Student2{id=10000000000001, name='张三', date=Tue Jan 19 00:00:00 CST 2016, classes=101, expel='是'}
Student2{id=10000000000002, name='李四', date=Fri Nov 17 10:19:10 CST 2017, classes=201, expel='否'}
Student2{id=10000000000004, name='王二', date=Fri Nov 17 00:00:00 CST 2017, classes=301, expel='否'}
```

## 四. 导出Excel

### 1. 不基于模板快速导出

#### 1) 导出函数(/src/test/java/base/Module2Excel.java#testList2Excel)
```
    @Test
    public void testList2Excel() throws Exception {
        
        List<List<String>> list2 = new ArrayList<>();
        List<String> header = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<String> _list = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                _list.add(i + " -- " + j);
            }
            list2.add(_list);
            header.add(i + "---");
        }
        ExcelUtils.getInstance().exportObjects2Excel(list2, header, "D:/D.xlsx");
    }
```
#### 2) 导出效果(截图)
![无模板导出截图](https://raw.githubusercontent.com/Crab2died/Excel4J/master/src/test/resource/image/v2.0.0/list_export.png)

### 2. 基于模板`List<Oject>`导出

#### 1) 导出函数(/src/test/java/base/Module2Excel.java#testObject2Excel)
```
    @Test
    public void testObject2Excel() throws Exception {

        String tempPath = "D:\\IdeaSpace\\Excel4J\\src\\test\\resource\\normal_template.xlsx";
        List<Student1> list = new ArrayList<>();
        list.add(new Student1("1010001", "盖伦", "六年级三班"));
        list.add(new Student1("1010002", "古尔丹", "一年级三班"));
        list.add(new Student1("1010003", "蒙多(被开除了)", "六年级一班"));
        list.add(new Student1("1010004", "萝卜特", "三年级二班"));
        list.add(new Student1("1010005", "奥拉基", "三年级二班"));
        list.add(new Student1("1010006", "得嘞", "四年级二班"));
        list.add(new Student1("1010007", "瓜娃子", "五年级一班"));
        list.add(new Student1("1010008", "战三", "二年级一班"));
        list.add(new Student1("1010009", "李四", "一年级一班"));
        Map<String, String> data = new HashMap<>();
        data.put("title", "战争学院花名册");
        data.put("info", "学校统一花名册");
        // 基于模板导出Excel
        ExcelUtils.getInstance().exportObjects2Excel(tempPath, 0, list, data, Student1.class, false, "D:/A.xlsx");
        // 不基于模板导出Excel
        ExcelUtils.getInstance().exportObjects2Excel(list, Student1.class, true, null, true, "D:/B.xlsx");

    }
```

#### 2) 导出模板(截图)
![导出模板截图](https://raw.githubusercontent.com/Crab2died/Excel4J/master/src/test/resource/image/v2.0.0/normal_template.png)

#### 3) 基于模板导出结果(截图)
![基于模板导出结果图](https://raw.githubusercontent.com/Crab2died/Excel4J/master/src/test/resource/image/v2.0.0/normal_export.png)

#### 4) 不基于模板导出结果(截图)
![不基于模板导出结果图](https://raw.githubusercontent.com/Crab2died/Excel4J/master/src/test/resource/image/v2.0.0/object_export.png)

### 3. 基于模板`Map<String, Collection<Object.toString>>`导出

#### 1) 导出函数(/src/test/java/base/Module2Excel.java#testMap2Excel)
```
    @Test
    public void testMap2Excel() throws Exception {

        Map<String, List> classes = new HashMap<>();

        Map<String, String> data = new HashMap<>();
        data.put("title", "战争学院花名册");
        data.put("info", "学校统一花名册");

        classes.put("class_one", new ArrayList<Student1>() {{
            add(new Student1("1010009", "李四", "一年级一班"));
            add(new Student1("1010002", "古尔丹", "一年级三班"));
        }});
        classes.put("class_two", new ArrayList<Student1>() {{
            add(new Student1("1010008", "战三", "二年级一班"));
        }});
        classes.put("class_three", new ArrayList<Student1>() {{
            add(new Student1("1010004", "萝卜特", "三年级二班"));
            add(new Student1("1010005", "奥拉基", "三年级二班"));
        }});
        classes.put("class_four", new ArrayList<Student1>() {{
            add(new Student1("1010006", "得嘞", "四年级二班"));
        }});
        classes.put("class_six", new ArrayList<Student1>() {{
            add(new Student1("1010001", "盖伦", "六年级三班"));
            add(new Student1("1010003", "蒙多", "六年级一班"));
        }});

        ExcelUtils.getInstance().exportObject2Excel("D:\\IdeaSpace\\Excel4J\\src\\test\\resource\\map_template.xlsx",
                0, classes, data, Student1.class, false, "D:/C.xlsx");
    }
```

#### 2) 导出模板(截图)
![导出模板截图](https://raw.githubusercontent.com/Crab2died/Excel4J/master/src/test/resource/image/v2.0.0/map_export_template.png)

#### 3) 导出结果(截图)
![导出结果图](https://raw.githubusercontent.com/Crab2died/Excel4J/master/src/test/resource/image/v2.0.0/map_export.png)

## 五. 使用(JDK1.7及以上)
#### 1) git clone https://github.com/Crab2died/Excel4J.git Excel4J
#### 2) cd Excel4J
#### 3) mvn install -DskipTests
#### 4) maven 引用：
```
<dependency>
    <groupId>com.github</groupId>
    <artifactId>Excel4J</artifactId>
    <version>${Excel4J-version}</version>
</dependency>
```

## 六. 链接
#### github -> [github地址](https://github.com/Crab2died/Excel4J)
#### 码云   -> [码云](https://git.oschina.net/Crab2Died/Excel4J)
