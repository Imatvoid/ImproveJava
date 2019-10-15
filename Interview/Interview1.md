## 基础

1.JAVA中的几种基本数据类型是什么，各自占用多少字节

见基础数据类型





## 反射

1、什么是反射机制？

简单说，反射机制值得是程序在运行时能够获取自身的信息。在java中，只要给定类的名字，那么就可以通过反射机制来获得类的所有信息。

见反射章节



2、java反射机制提供了什么功能？

在运行时能够判断任意一个对象所属的类

在运行时构造任意一个类的对象

在运行时判断任意一个类所具有的成员变量和方法

在运行时调用任一对象的方法

在运行时创建新类对象

见反射章节

3.应用

- beanutils
- orm框架
- jdk动态代理

详细见反射章节



4.反射机制的优缺点？

**一句话，反射机制的优点就是可以实现动态创建对象和编译，体现出很大的灵活性**，特别是在 J2EE 的开发中它的灵活性就表现的十分明显。比如，一个大型的软件，不可能一次就把把它设计的很完美，当这个程序编译后，发布了，当发现需要更新某些功能时，我们不可能要用户把以前的卸载，再重新安装新的版本，假如这样的话，这个软件肯定是没有多少人用的。采用静态的话，需要把整个程序重新编译一次才可以实现功能的更新，而采用反射机制的话，它就可以不用卸载，只需要在运行时才动态的创建和绵译，就可以实现该功能.

**它的缺点是对性能有影响。使用反射基本上是一种解释操作，我们可以告诉 JVM，我们希望做什么井且它满足我们的要求。这类操作总是慢于直接执行相同的操作。**



## 读代码

1.下面这段代码的输出结果是什么？

```java
public class Test extends Base{ 
  
    static{ 
        System.out.println("test static"); 
    } 
      
    public Test(){ 
        System.out.println("test constructor"); 
    } 
      
    public static void main(String[] args) { 
        new Test(); 
    } 
} 
  
class Base{ 
      
    static{ 
        System.out.println("base static"); 
    } 
      
    public Base(){ 
        System.out.println("base constructor"); 
    } 
}
```

```java
base static
test static
base constructor
test constructor
```



2.这段代码的输出结果是什么？

```java
public class Test { 
    Person person = new Person("Test"); 
    static{ 
        System.out.println("test static"); 
    } 
      
    public Test() { 
        System.out.println("test constructor"); 
    } 
      
    public static void main(String[] args) { 
        new MyClass(); 
    } 
} 
  
class Person{ 
    static{ 
        System.out.println("person static"); 
    } 
    public Person(String str) { 
        System.out.println("person "+str); 
    } 
} 
  
  
class MyClass extends Test { 
    Person person = new Person("MyClass"); 
    static{ 
        System.out.println("myclass static"); 
    } 
      
    public MyClass() { 
        System.out.println("myclass constructor"); 
    } 
}
```

**注意psvm是静态变量,类没有对象也可以执行**

**先初始化成员变量,再调用构造方法.有父类先初始化父类的成员变量.**

**成员变量和普通初始化块的顺序按定义顺序**

```
test static
myclass static
person static
person Test
test constructor
person MyClass
myclass constructor
```

首先加载Test类，因此会执行Test类中的static块。接着执行new MyClass()，而MyClass类还没有被加载，因此需要加载MyClass类。在加载MyClass类的时候，发现MyClass类继承自Test类，但是由于Test类已经被加载了，所以只需要加载MyClass类，那么就会执行MyClass类的中的static块。

在加载完之后，就通过构造器来生成对象。而在生成对象的时候，必须先初始化父类的成员变量，因此会执行Test中的Person person = new Person()，而Person类还没有被加载过，因此会先加载Person类并执行Person类中的static块，接着执行父类的构造器，完成了父类的初始化，然后就来初始化自身了，因此会接着执行MyClass中的Person person = new Person()，最后执行MyClass的构造器。

3.**这段代码的输出结果是什么？**

```java
public class Test {
     
    static{
        System.out.println("test static 1");
    }
    public static void main(String[] args) {
           System.out.println(1);
    }
     
    static{
        System.out.println("test static 2");
    }
}
```



```
test static 1
test static 2
1
```

4.**静态代码块的初始化顺序**

```java
class Parent {
    static String name = "hello";
    {
        System.out.println("parent block");
    }
    static {
        System.out.println("parent static block");
    }

    public Parent() {
        System.out.println("parent constructor");
    }
}

class Child extends Parent {
    static String childName = "hello";
    {
        System.out.println("child block");
    }
    static {
        System.out.println("child static block");
    }

    public Child() {
        System.out.println("child constructor");
    }
}

public class TestStatic {

    public static void main(String[] args) {
        new Child();// 语句(*)
    }
}
```



```
parent static block
child static block
parent block
parent constructor
child block
child constructor
```



## 并发

### concurrentHashmap 1.8为什么放弃了分段锁

### 线程池的构造函数参数到队列的拒绝策略

