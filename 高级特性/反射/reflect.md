# 反射

> Java 反射机制在程序**运行时**，对于任意一个类，都能够知道这个类的所有属性和方法；对于任意一个对象，都能够调用它的任意一个方法和属性。这种 **动态的获取信息** 以及 **动态调用对象的方法** 的功能称为 **java 的反射机制**。
>
> 反射机制很重要的一点就是“运行时”，其使得我们可以在程序运行时加载、探索以及使用编译期间完全未知的 `.class` 文件。换句话说，Java 程序可以加载一个运行时才得知名称的 `.class` 文件，然后获悉其完整构造，并生成其对象实体、或对其 fields（变量）设值、或调用其 methods（方法）。

## 反射基础

### Class类

在Java中用来表示运行时类型信息的对应类就是Class类.

我们有下面几种方式可以拿到Class对象

```java

        Random random = new Random();
        // 方式1
        Class clr1 = random.getClass();
        // 方式2
        Class clr2 = Class.forName("java.util.Random");
        // 方式3
        Class clr3 = Random.class; // if you import java util
        if(clr1 == clr2 && clr2 ==clr3){
            System.out.println("获取Random对象class的方法拿到了同一个对象");
        }
        String name = clr1.getName(); // name is set to "java util Random "
        System.out.println(name);
```

### 获取类的信息

```java
public class FatherClass {
    public String mFatherName;
    public int mFatherAge;

    public void printFatherMsg(){}
}
```



```java
public class SonClass extends FatherClass{

    private String mSonName;
    protected int mSonAge;
    public String mSonBirthday;

    public void printSonMsg(){
        System.out.println("Son Msg - name : "
                + mSonName + "; age : " + mSonAge);
    }

    private void setSonName(String name){
        mSonName = name;
    }

    private void setSonAge(int age){
        mSonAge = age;
    }

    private int getSonAge(){
        return mSonAge;
    }

    private String getSonName(){
        return mSonName;
    }
}
```

#### Filed类

 通过反射获取类的所有变量

```java
/**
 * 通过反射获取类的所有变量
 */
private static void printFields(){
    //1.获取并输出类的名称
    Class mClass = SonClass.class;
    System.out.println("类的名称：" + mClass.getName());

    //2.1 获取所有 public 访问权限的变量
    // 包括本类声明的和从父类继承的
    Field[] fields = mClass.getFields();

    //2.2 获取所有本类声明的变量（不问访问权限）
    //Field[] fields = mClass.getDeclaredFields();

    //3. 遍历变量并输出变量信息
    for (Field field :
            fields) {
        //获取访问权限并输出
        int modifiers = field.getModifiers();
        System.out.print(Modifier.toString(modifiers) + " ");
        //输出变量的类型及变量名
        System.out.println(field.getType().getName()
                 + " " + field.getName());
    }
}
```

- `getFields()` 方法，输出 `SonClass` 类以及其所继承的父类( 包括 `FatherClass` 和 `Object` ) 的 `public`变量
- `getDeclaredFields()` ， 输出 `SonClass` 类的所有成员变量，不问访问权限。

#### Method类

通过反射获取类的所有方法

```java
/**
 * 通过反射获取类的所有方法
 */
private static void printMethods(){
    //1.获取并输出类的名称
    Class mClass = SonClass.class;
    System.out.println("类的名称：" + mClass.getName());

    //2.1 获取所有 public 访问权限的方法
    //包括自己声明和从父类继承的
    Method[] mMethods = mClass.getMethods();

    //2.2 获取所有本类的的方法（不问访问权限）
    //Method[] mMethods = mClass.getDeclaredMethods();

    //3.遍历所有方法
    for (Method method :
            mMethods) {
        //获取并输出方法的访问权限（Modifiers：修饰符）
        int modifiers = method.getModifiers();
        System.out.print(Modifier.toString(modifiers) + " ");
        //获取并输出方法的返回值类型
        Class returnType = method.getReturnType();
        System.out.print(returnType.getName() + " "
                + method.getName() + "( ");
        //获取并输出方法的所有参数
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter:
             parameters) {
            System.out.print(parameter.getType().getName()
                    + " " + parameter.getName() + ",");
        }
        //获取并输出方法抛出的异常
        Class[] exceptionTypes = method.getExceptionTypes();
        if (exceptionTypes.length == 0){
            System.out.println(" )");
        }
        else {
            for (Class c : exceptionTypes) {
                System.out.println(" ) throws "
                        + c.getName());
            }
        }
    }
}
```

- 调用 `getMethods()` 方法

  获取 `SonClass` 类所有 `public` 访问权限的方法，包括从父类继承的

- 调用 `getDeclaredMethods()` 方法

  打印信息中，输出的都是 `SonClass` 类的方法，不问访问权限。

#### Constructor类

## 访问或操作类的私有变量和方法



https://juejin.im/post/598ea9116fb9a03c335a99a4#heading-6

## Class<T>类解析





## 应用举例

- jdk动态代理

​     运行期通过获得class的构造器,生成一个代理对象.

- beanutils

  ```java
  Object value = readMethod.invoke(source);
  writeMethod.invoke(target, value);
  ```

  

- ORM框架

  利用反射获取filed的类名,属性名,和属性值,更新到sql语句

  ```sql
  insert into {0}({1}) values({2})   //0是类名,也就是表名  1是属性命 2是属性值
  ```

  当然,对tablename可以指定TableName的注解.对filed指定FieldName注解.

  

## 反射机制的优缺点

**一句话，反射机制的优点就是可以实现动态创建对象和编译，体现出很大的灵活性**，特别是在 J2EE 的开发中它的灵活性就表现的十分明显。比如，一个大型的软件，不可能一次就把把它设计的很完美，当这个程序编译后，发布了，当发现需要更新某些功能时，我们不可能要用户把以前的卸载，再重新安装新的版本，假如这样的话，这个软件肯定是没有多少人用的。采用静态的话，需要把整个程序重新编译一次才可以实现功能的更新，而采用反射机制的话，它就可以不用卸载，只需要在运行时才动态的创建和绵译，就可以实现该功能.

**它的缺点是对性能有影响。使用反射基本上是一种解释操作，我们可以告诉 JVM，我们希望做什么井且它满足我们的要求。这类操作总是慢于直接执行相同的操作。**



参考:

https://juejin.im/post/5afaeb456fb9a07aa43c5e3b