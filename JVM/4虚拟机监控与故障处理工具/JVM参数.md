### JVM参数类别



Java启动参数共分为三类；

- **标准参数（-）**，所有的JVM实现都必须实现这些参数的功能，而且向后兼容；

- **非标准参数（-X）**，默认jvm实现这些参数的功能，但是并不保证所有jvm实现都满足，且不保证向后兼容；

- **非Stable参数（-XX）**，此类参数各个jvm实现会有所不同，将来可能会随时取消，需要慎重使用；



#### 标准参数（Standard Option）

标准参数是被所有JVM实现都要支持的参数。用于做一些常规的通用的动作，比如检查版本、设置classpath等。

```shell
java -version
java -help 
```

-server

指定JVM的启动模式是client模式还是server模式，具体就是 Java HotSpot Client(Server) VM 版本。目前64位的JDK启动，一定是server模式，会忽略这个参数。

-Dproperty=value

这个参数是最常见的了，就是设置系统属性，设置后的属性可以在代码中System.getProperty方法获取到。

-ea

断言机制

**-esa** 

 激活系统类的断言。

-dsa

关闭系统类的断言。

#### -X  非标准参数（Non-Standard Options）

这里的非标准参数主要是针对官方JVM也就是HotSpot的，当然各家自研的JVM也有自己的非标准参数。

```shell
-Xint  #在 interpreted-only模式运行程序。编译为native的模式不再生效，所有的字节码都在解释器环境下解释执行。
-Xcomp # 禁止编译时的解释执行
-Xmixed  # 用解释器执行所有的字节码，除了被编译为native code的hot method。
```


-Xms 等价于-XX:InitialHeapSize  其实是-XX参数

-Xmx 等价于-XX:MaxHeapSize  其实是-XX参数

-Xss   等价于-XX:ThreadStackSize  其实是-XX参数



#### -XX 高级运行时参数（Advanced Runtime Options）

> 相对不稳定可能改变，主要用于JVM调优和debug

-XX:[+-]<name> 表示启用或者禁用name属性

```
-XX:+UseConcMarkSweepGc  #启用CMS垃圾收集器
-XX:+UseG1GC   #启用G1垃圾收集器
```

-XX:<key>=<value> 表示name属性的值是value

```
-XX:MaxGCPauseMillis=500
 XX:GCTimeRatio=19
```

注意

-Xms 等价于-XX:InitialHeapSize  其实是-XX参数

-Xmx 等价于-XX:MaxHeapSize  其实是-XX参数

-Xss   等价于-XX:ThreadStackSize  其实是-XX参数



-XX:MetaspaceSize 元数据区大小

-XX:MaxMetaspaceSize  元数据区最大值





## GC日志相关参数

- `-XX:+PrintGC` 输出GC日志
- `-XX:+PrintGCDetails` 输出GC的详细日志
- `-XX:+PrintGCTimeStamps` 输出GC的时间戳（以基准时间的形式）
- `-XX:+PrintGCDateStamps` 输出GC的时间戳（以日期的形式，如 2017-09-04T21:53:59.234+0800）
- `-XX:+PrintHeapAtGC` 在进行GC的前后打印出堆的信息
- `-Xloggc:../logs/gc.log` 日志文件的输出路径



## 一个典型的参数配置



## 参考

https://www.jianshu.com/p/4ae227a86d68

https://blog.csdn.net/ymaini/article/details/81952547















