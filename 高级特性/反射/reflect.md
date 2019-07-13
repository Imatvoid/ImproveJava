## 假如你学会了反射

•在运行时分析类的能力 。
• 在运行时查看对象 , 例如, 编写一个 toString 方法供所有类使用 。
• 实现通用的数组操作代码 。
• 利用 Method 对象, 这个对象很像中的函数指针 。

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



### Filed类

### Method类

### Constructor类



### 参考

https://juejin.im/post/598ea9116fb9a03c335a99a4#heading-6





## Class<T>类解析