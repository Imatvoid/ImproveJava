##### jinfo

> Configuration Info for Java

实时的查看和更改虚拟机的各项参数

```
jinfo [option] pid
```









##### jmap + MAT

>堆转储快照
>
>jmap(JVM Memory Map)命令用于生成heap dump文件，如果不使用这个命令，还阔以使用-XX:+HeapDumpOnOutOfMemoryError参数来让虚拟机出现OOM的时候·自动生成dump文件。
>jmap不仅能生成dump文件，还阔以查询finalize执行队列、Java堆和永久代的详细信息，如当前使用率、当前使用的是哪种收集器等。

jmap [option] LVMID

option参数

- -dump : 生成堆转储快照
- -finalizerinfo : 显示在F-Queue队列等待Finalizer线程执行finalizer方法的对象
- -heap : 显示Java堆详细信息
- -histo : 显示堆中对象的统计信息
- -permstat : to print permanent generation statistics
- -F : 当-dump没有响应时，强制生成dump快照

```
jmp -dump:format=b,file=heap.hprof 18384
```



##### jhat

>jhat(JVM Heap Analysis Tool)命令是与jmap搭配使用，用来分析jmap生成的dump，jhat内置了一个微型的HTTP/HTML服务器，生成dump的分析结果后，可以在浏览器中查看。在此要注意，一般不会直接在服务器上进行分析，因为jhat是一个耗时并且耗费硬件资源的过程，一般把服务器生成的dump文件复制到本地或其他机器上进行分析。





##### jstack

> jstack用于生成java虚拟机当前时刻的线程快照

jstack [option] LVMID
option参数

- -F : 当正常输出请求不被响应时，强制输出线程堆栈
- -l : 除堆栈外，显示关于锁的附加信息
- -m : 如果调用到本地方法的话，可以显示C/C++的堆栈



![Protocol state machine example - Thread States and Life Cycle in Java 6.](https://www.uml-diagrams.org/examples/state-machine-example-java-6-thread-states.png)


