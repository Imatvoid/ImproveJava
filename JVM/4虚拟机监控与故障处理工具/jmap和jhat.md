

## jmap 

> 堆转储快照
>
> jmap(JVM Memory Map)命令用于生成heap dump文件，如果不使用这个命令，还阔以使用-XX:+HeapDumpOnOutOfMemoryError参数来让虚拟机出现OOM的时候·自动生成dump文件。
> jmap不仅能生成dump文件，还可以查询finalize执行队列、Java堆和永久代的详细信息，如当前使用率、当前使用的是哪种收集器等。



**jmap [option] <LVMID> (连接正在执行的进程)**
**jmap [option] <executable <core> (连接一个core文件)**
**jmap [option] [server_id@]<remote server IP or hostname> (链接远程服务器)**



option参数

- -dump:<dump-options> 生成堆转储快照

  　dump-options:

  　　　　　　live 只转储存活的对象，如果没有指定则转储所有对象

  　　　　　　format=b 二进制格式

  　　　　　　file=<file>   转储文件到 <file>

- -heap : 显示Java堆详细信息

- -histo : 显示堆中对象的统计信息

  -histo[:live] 打印堆中存活的java对象统计信息

- -clstats 打印类加载器统计信息

- -finalizerinfo : 显示在F-Queue队列等待Finalizer线程执行finalizer方法的对象

- -permstat : to print permanent generation statistics

- -F : 当-dump没有响应时，强制生成dump快照

**导出堆转储文件**

```
$ jmp -dump:format=b,file=heap.hprof 18384
```



**输出堆的详细信息**

```shell
$ jmap -heap 11666
```

```java
Attaching to process ID 11666, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.25-b02

using thread-local object allocation.
Parallel GC with 4 thread(s)

Heap Configuration: //堆内存初始化配置
   MinHeapFreeRatio         = 0 //对应jvm启动参数-XX:MinHeapFreeRatio设置JVM堆最小空闲比率(default 40)
   MaxHeapFreeRatio         = 100 //对应jvm启动参数 -XX:MaxHeapFreeRatio设置JVM堆最大空闲比率(default 70)
   MaxHeapSize              = 1073741824 (1024.0MB) //对应jvm启动参数-XX:MaxHeapSize=设置JVM堆的最大大小
   NewSize                  = 22020096 (21.0MB) //对应jvm启动参数-XX:NewSize=设置JVM堆的新生代的默认大小
   MaxNewSize               = 357564416 (341.0MB) //对应jvm启动参数-XX:MaxNewSize=设置JVM堆的新生代的最大大小
   OldSize                  = 45088768 (43.0MB) //对应jvm启动参数-XX:OldSize=<value>:设置JVM堆的老年代的大小
   NewRatio                 = 2 //对应jvm启动参数-XX:NewRatio=:新生代和老生代的大小比率
   SurvivorRatio            = 8 //对应jvm启动参数-XX:SurvivorRatio=设置新生代中Eden区与Survivor区的大小比值
   MetaspaceSize            = 21807104 (20.796875MB) // 元数据区大小
   CompressedClassSpaceSize = 1073741824 (1024.0MB) //类压缩空间大小
   MaxMetaspaceSize         = 17592186044415 MB //元数据区最大大小
   G1HeapRegionSize         = 0 (0.0MB) //G1垃圾收集器每个Region大小

Heap Usage: //堆内存使用情况
PS Young Generation 
Eden Space: //Eden区内存分布
   capacity = 17825792 (17.0MB) //Eden区总容量
   used     = 12704088 (12.115562438964844MB) //Eden区已使用
   free     = 5121704 (4.884437561035156MB) //Eden区剩余容量
   71.26801434685203% used //Eden区使用比率
From Space: //其中一个Survivor区的内存分布
   capacity = 2097152 (2.0MB)
   used     = 1703936 (1.625MB)
   free     = 393216 (0.375MB)
   81.25% used
To Space: //另一个Survivor区的内存分布
   capacity = 2097152 (2.0MB)
   used     = 0 (0.0MB)
   free     = 2097152 (2.0MB)
   0.0% used
PS Old Generation
   capacity = 52428800 (50.0MB) //老年代容量
   used     = 28325712 (27.013504028320312MB) //老年代已使用
   free     = 24103088 (22.986495971679688MB) //老年代空闲
   54.027008056640625% used //老年代使用比率

15884 interned Strings occupying 2075304 bytes.
```



**输出存活对象统计信息**

```shell
$ jmap -histo:live 11666 | less
num     #instances         #bytes  class name
----------------------------------------------
1:         46608        1111232  java.lang.String
2:          6919         734516  java.lang.Class
3:          4787         536164  java.net.SocksSocketImpl
4:         15935         497100  java.util.concurrent.ConcurrentHashMap$Node
5:         28561         436016  java.lang.Object
```




## jhat

> jhat(JVM Heap Analysis Tool)命令是与jmap搭配使用，用来分析jmap生成的dump，jhat内置了一个微型的HTTP/HTML服务器，生成dump的分析结果后，可以在浏览器中查看。在此要注意，一般不会直接在服务器上进行分析，因为jhat是一个耗时并且耗费硬件资源的过程，一般把服务器生成的dump文件复制到本地或其他机器上进行分析。

## Memory Analyzer Tool(MAT)

内存分析工具

