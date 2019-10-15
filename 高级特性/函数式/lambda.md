# Lambda

## 原理分析

### Lambdas 表达式和 invokedynamic

为了解决前面提到的担心，Java 语言和 JVM 工程师决定将翻译策略推迟到运行时。利用 Java 7 引入的 invokedynamic 字节码指令我们可以高效地完成这一实现。将 Lambda 表达式转化成字节码只需要如下两步：

1. 生成一个 invokedynamic 调用点，也叫做 Lambda 工厂。当调用时返回一个 Lambda 表达式转化成的[函数式接口](http://docs.oracle.com/javase/8/docs/api/java/lang/FunctionalInterface.html)实例。

2. 将 Lambda 表达式的方法体转换成方法供 invokedynamic 指令调用。

为了阐明上述的第一步，我们这里举一个包含 Lambda 表达式的简单类：

```java
import java.util.function.Function;
public class Lambda {
    Function<String, Integer> f = s -> Integer.parseInt(s);
}
```

查看上面的类经过编译之后生成的字节码：

```java
0: aload_0
1: invokespecial #1 
4: aload_0
5: invokedynamic #2, 0 
                  #0:apply:()Ljava/util/function/Function;
10: putfield #3 
13: return
```



需要注意的是，方法引用的编译稍微有点不同，因为 javac 不需要创建一个合成的方法，javac 可以直接访问该方法。

Lambda 表达式转化成字节码的第二步取决于 Lambda 表达式是否为对变量捕获。Lambda 表达式方法体需要访问外部的变量则为对变量捕获，反之则为对变量不捕获。

对于不进行变量捕获的 Lambda 表达式，其方法体实现会被提取到一个与之具有相同签名的静态方法中，这个静态方法和 Lambda 表达式位于同一个类中。比如上面的那段 Lambda 表达式会被提取成类似这样的方法：

```java
static Integer lambda$1(String s) {
    return Integer.parseInt(s);
}
```



参考:

https://www.infoq.cn/article/Java-8-Lambdas-A-Peek-Under-the-Hood





### 去糖

#### 无状态“lambda”

Lambda表达式的一种最简单的形式，就是从外部作用域里捕获不到状态(无状态lambda，stateless lambda)：

```java
class A {
    public void foo() {
        List<String> list = ...;
        list.forEach( s -> { System.out.println(s); } );
    }
}
```

此lambda的自然签名是`(String)V`；`forEach`方法有一个参数`Block<String>`，其lambda描述符为`(Ojbect)V`。编译器将lambda体去糖为一个签名为其自然签名的静态方法，同时为去糖后的函数体生成一个方法名。

```java
class A {
    public void foo() {
        List<String> list = ...;
        list.forEach( [lambda for lambda$1 as Block] );
    }

    static void lambda$1(String s) {
        System.out.println(s);
    }
}
```



#### 去糖例子 -- 带不可变值的lambda

Lambda表达式的其他形式需要捕获外部`final`或者`effectively fianl`[5](https://lowzj.com/notes/java/translation-of-lambda-expressions.html#fn_5)的局部变量、外部实例中的字段(相当于捕获`final`型的外部`this`引用)。

```java
class B {
    public void foo() {
        List<Person> list = ...;
        final int bottom = ..., top = ...;
        list.removeIf( p -> (p.size >= bottom && p.size <= top) );
    }
}
```

上例中的lambda从外部作用域中捕获了*final*的局部变量`bottom`和`top`。



待补充.

https://lowzj.com/notes/java/translation-of-lambda-expressions.html