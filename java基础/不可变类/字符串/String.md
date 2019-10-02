# String

> String类是不可变的，java设计者认为共享的价值大于提取、拼接。

## 测试
### 题目

```java
public class Test {

    public static void main(String[] args) {

        String str1 = "HelloFlyapi";

        String str2 = "HelloFlyapi";

        String str3 = new String("HelloFlyapi");

        String str4 = "Hello";

        String str5 = "Flyapi";

        String str6 = "Hello" + "Flyapi";

        String str7 = str4 + str5;


        System.out.println("str1 == str2 result: " + (str1 == str2));

        System.out.println("str1 == str3 result: " + (str1 == str3));

        System.out.println("str1 == str6 result: " + (str1 == str6));

        System.out.println("str1 == str7 result: " + (str1 == str7));

        System.out.println("str1 == str7.intern() result: " + (str1 == str7.intern()));

        System.out.println("str3 == str3.intern() result: " + (str3 == str3.intern()));

    }

}
```

结果预测

```
true
false
true
false
true
false
```

### 分析

**String 的创建方式**

从上面的题中你会知道，String的创建方式有两种：
**直接赋值**

此方式在方法区中字符串常量池中创建对象

```java
String str = "flyapi";
```
**构造器**

此方式在堆内存创建对象

```java
String str = new String();
```

**str1 == str2**

```java
String str1 = "HelloFlyapi";
String str2 = "HelloFlyapi";
System.out.println(str1 == str2); // true
```

当执行第一句时，JVM会先去常量池中查找是否存在HelloFlyapi，当存在时直接返回常量池里的引用；当不存在时，会在字符创常量池中创建一个对象并返回引用。

当执行第二句时，同样的道理，由于第一句已经在常量池中创建了，所以直接返回上句创建的对象的引用。

**str1 == str3**

```java
String str1 = "HelloFlyapi";
String str3 = new String("HelloFlyapi");
System.out.println(str1 == str3); // false
```

执行第一句，同上第一句。

执行第二句时，会在堆（heap）中创建一个对象，当字符创常量池中没有‘HelloFlyapi’时，会在常量池中也创建一个对象；当常量池中已经存在了，就不会创建新的了。

**str1 == str6**

```java
String str1 = "HelloFlyapi";
String str6 = "Hello" + "Flyapi";
System.out.println(str1 == str6); // true
```

由于”Hello”和”Flyapi”都是常量，编译时，第二句会被自动编译为‘String str6 = “HelloFlyapi”;

**str1 == str7**

```java
String str1 = "HelloFlyapi";
String str4 = "Hello";
String str5 = "Flyapi";
String str7 = str4 + str5;
System.out.println(str1 == str7); // false
```

其中前三句变量存储的是常量池中的引用地址。

第四句执行时，JVM会在堆（heap）中创建一个以str4为基础的一个StringBuilder对象，然后调用StringBuilder的append()方法完成与str5的合并，之后会调用toString()方法在堆（heap）中创建一个String对象，并把这个String对象的引用赋给str7。



**Intern**

```java
/**
     * 在1.6和以前, 会把intern第一次遇到的,复制一份到永久代.
     * 1.7以后只是会在常量池记录首次出现的实例的引用,把引用留在常量池.
     * @param args
     */
    public static void main(String[] args) {
        String str1 = new StringBuilder("计算机").append("软件").toString();
        System.out.println(str1.intern() == str1);// 1.6以前false 1.6以后true
       

        // str2指向新对象.
        String str2 = new StringBuilder("ja").append("va").toString();
        String str3 = "java";
        System.out.println(System.identityHashCode(str2));
        // java字符串不是第一次出现,常量池中已记录引用,intern直接返回那个引用
        System.out.println(System.identityHashCode(str2.intern()));
        System.out.println(System.identityHashCode(str3));
        System.out.println(System.identityHashCode(str3.intern()));
    }
```

true
104739310
1761291320
1761291320
1761291320

## Java字符串的编码

Unicode编码，UTF-16  存储

讲的很好:

http://www.cnblogs.com/fnlingnzb-learner/p/6163205.html

https://www.jianshu.com/p/50107efaac30

 java不同编码之间进行转换，都需要使用unicode作为中转。

http://www.lianhekj.com/question/496912925852330444.html

## StringBuilder  和 StringBuffer

- API相同
- 由于 StringBuilder 相较于 StringBuffer 有速度优势，所以多数情况下建议使用 StringBuilder 类。然而在应用程序要求线程安全的情况下，则必须使用 StringBuffer 类。

理解Java的字符串，String、StringBuffer、StringBuilder区别

- String是Java语言非常基础和重要的类，提供了构造和管理字符串的各种基本逻辑。它是典型的Immutable类，被声明成为final class，所有属性也都是final的。也由于它的不可变性，类似拼接、裁剪字符串等动作，都会产生新的String对象。由于字符串操作的普遍性，所以相关操作的效率往往对应用性能有明显影响。
- StringBuffer是为解决上面提到拼接产生太多中间对象的问题而提供的一个类，我们可以用append或者add方法，把字符串添加到已有序列的末尾或者指定位置。StringBuffer本质是一个线程安全的可修改字符序列，它保证了线程安全，也随之带来了额外的性能开销，所以除非有线程安全的需要，不然还是推荐使用它的后继者，也就是StringBuilder。
- StringBuilder是Java 1.5中新增的，在能力上和StringBuffer没有本质区别，但是它去掉了线程安全的部分，有效减小了开销，是绝大部分情况下进行字符串拼接的首选。