在JDK1.7以后，新增了一个命令行工具 jcmd。他是一个多功能的工具，可以用它来导出堆、查看Java进程、导出线程信息、执行GC、还可以进行采样分析（jmc 工具的飞行记录器）.

适用于脚本.



## 格式

```shell
jcmd <process id/main class> <command> [options]
```

  jcmd  =  jcmd -l



| 命令                        | 说明                                                         |              |
| --------------------------- | ------------------------------------------------------------ | ------------ |
| help                        | jcmd help可以列出所有的命令。jcmd help<command>可以给出特定命令的语法。 |              |
| ManagementAgent.stop        | 停止JMX Agent                                                |              |
| ManagementAgent.start_local | 开启本地JMX Agent                                            |              |
| ManagementAgent.start       | 开启JMX Agent                                                |              |
| Thread.print                | 参数-l打印java.util.concurrent锁信息，相当于：jstack         |              |
| PerfCounter.print           | 相当于：jstat -J-Djstat.showUnsupported=true -snap           |              |
| GC.class_histogram          | 相当于：jmap -histo                                          |              |
| GC.heap_dump                | 相当于：jmap -dump:format=b,file=xxx.bin                     |              |
| GC.run_finalization         | 相当于：System.runFinalization()                             |              |
| GC.run                      | 相当于：System.gc()                                          |              |
| VM.uptime                   | 参数-date打印当前时间，VM启动到现在的时候，以秒为单位显示    |              |
| VM.flags                    | 参数-all输出全部，相当于：jinfo -flags , jinfo -flag         |              |
| VM.system_properties        | 相当于：jinfo  -sysprops                                     |              |
| VM.command_line             | 相当于：jinfo -sysprops                                      | grep command |
| VM.version                  | 相当于：jinfo -sysprops                                      | grep version |





## 示例

#### GC.heap_dump

命令：jcmd PID GC.heap_dump FILE_NAME
描述：查看 JVM 的Heap Dump

```shell
$ jcmd 10576 GC.heap_dump ~/dump.hprof
10576:
Heap dump file created
```





### VM.flags

获取调优标志

```shell
$ jcmd 31339 VM.flags | tr ' ' '\n' #坚果云的GUI进程
31339:
-XX:CICompilerCount=4
-XX:InitialHeapSize=262144000
-XX:MaxHeapFreeRatio=40
-XX:MaxHeapSize=2147483648
-XX:MaxNewSize=715653120
-XX:MinHeapDeltaBytes=524288
-XX:MinHeapFreeRatio=20
-XX:NewSize=87031808
-XX:OldSize=175112192
-XX:+UseCompressedClassPointers
-XX:+UseCompressedOops
-XX:+UseFastUnorderedTimeStamps
-XX:+UseParallelGC

```



### VM.command_line

获取启动参数JVM启动参数,

```shell
$ jcmd 31339 VM.command_line
31339:
VM Arguments:
jvm_args: -ea -Dfile.encoding=UTF-8 -Xmx2048M -XX:MinHeapFreeRatio=20 -XX:MaxHeapFreeRatio=40 -Dlog4j.defaultInitOverride=true -Dswt.autoScale=exact -Djava.util.logging.config.file=/home/atvoid/.nutstore/dist/conf/java.logging.properties -Dnutstore.config.dir=/home/atvoid/.nutstore/dist/conf -Dnutstore.x64=True -Djava.library.path=/home/atvoid/.nutstore/dist/lib/native 
java_command: nutstore.client.gui.NutstoreGUI --restart 1 --use-python-tray
java_class_path (initial): /home/atvoid/.nutstore/dist/lib/jackson-annotations-2.9.7.jar:/home/atvoid/.nutstore/dist/lib/juds-0.95-osx.jar:/home/atvoid/.nutstore/dist/lib/log4j-1.2.17.jar:/home/atvoid/.nutstore/dist/lib/sentry-log4j-1.7.10.jar:/home/atvoid/.nutstore/dist/lib/commons-collections4-4.1.jar:/home/atvoid/.nutstore/dist/lib/sqlite4java.jar:/home/atvoid/.nutstore/dist/lib/slf4j-api-1.7.25.jar:/home/atvoid/.nutstore/dist/lib/swt.jar:/home/atvoid/.nutstore/dist/lib/jackson-core-2.9.7.jar:/home/atvoid/.nutstore/dist/lib/commons-cli-1.2.jar:/home/atvoid/.nutstore/dist/lib/commons-codec-1.4.jar:/home/atvoid/.nutstore/dist/lib/jsr305-3.0.1.jar:/home/atvoid/.nutstore/dist/lib/guava-r07.jar:/home/atvoid/.nutstore/dist/lib/jackson-databind-2.9.7.jar:/home/atvoid/.nutstore/dist/lib/nutstore_client-4.2.2.jar:/home/atvoid/.nutstore/dist/lib/sentry-1.7.10.jar:/home/atvoid/.nutstore/dist/lib/inotify-java-2.1.jar:/home/atvoid/.nutstore/dist/lib/junit-4.12.jar:/home/atvoid/.nutstore/dist/lib/slf4j-log4j12-1.7.25.jar:/home/atvoid/.nutstore/dist/lib/rdiff-java-0.1.0.jar
Launcher Type: SUN_STANDARD

```



#### VM.version

jvm版本信息

```shell
$ jcmd 31339 VM.version #坚果云的GUI进程
31339:
Java HotSpot(TM) 64-Bit Server VM version 25.191-b12
JDK 8.0_191

```





### VM.uptime

jvm启动时间

```shell
31339:
14248.212 s
```



### GC.class_histogram

命令：jcmd PID GC.class_histogram
描述：查看系统中类统计信息

```shell
$ jcmd 20913 GC.class_histogram # 一个普通的测试类,只有Thread.sleep的逻辑
20913:

 num     #instances         #bytes  class name
----------------------------------------------
   1:          1438         151352  [C
   2:           197         149688  [B
   3:           519          59480  java.lang.Class
   4:          1425          34200  java.lang.String
   5:           597          31664  [Ljava.lang.Object;
   6:           127           8128  java.net.URL
   7:           185           7400  java.lang.ref.Finalizer
   8:            75           5400  java.lang.reflect.Field
   9:           137           4384  java.util.concurrent.ConcurrentHashMap$Node
  10:            11           4136  java.lang.Thread
  11:           256           4096  java.lang.Integer

```

