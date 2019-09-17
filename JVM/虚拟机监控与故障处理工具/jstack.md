##### jstack

> jstack用于生成java虚拟机当前时刻的线程快照

jstack [option] LVMID
option参数

- -F : 当正常输出请求不被响应时，强制输出线程堆栈
- -l : 除堆栈外，显示关于锁的附加信息
- -m : 如果调用到本地方法的话，可以显示C/C++的堆栈



![Protocol state machine example - Thread States and Life Cycle in Java 6.](https://www.uml-diagrams.org/examples/state-machine-example-java-6-thread-states.png)





```shell
$ jstack 16661
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.201-b09 mixed mode):

"Attach Listener" #63 daemon prio=9 os_prio=0 tid=0x00007f30b0003800 nid=0x281c waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"ProcessThread(sid:0 cport:2181):" #13 prio=5 os_prio=0 tid=0x00007f30d82c1800 nid=0x4125 waiting on condition [0x00007f30dc2e9000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000eca4b8c8> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.zookeeper.server.PrepRequestProcessor.run(PrepRequestProcessor.java:123)


```



（1）"Thread-1"表示线程名称

（2）"prio=6"表示线程优先级

（3）"tid=00000000497cec00"表示线程Id

（4）nid=0x219c

线程对应的本地线程Id，这个重点说明下。因为Java线程是依附于Java虚拟机中的本地线程来运行的，实际上是本地线程在执行Java线程代码，只有本地线程才是真正的线程实体。Java代码中创建一个thread，虚拟机在运行期就会创建一个对应的本地线程，而这个本地线程才是真正的线程实体。Linux环境下可以使用"top -H -p JVM进程Id"来查看JVM进程下的本地线程（也被称作LWP）信息，注意这个本地线程是用十进制表示的，nid是用16进制表示的，转换一下就好了，0x219c对应的本地线程Id应该是8604。

（5）"[0x000000004a3bf000..0x000000004a3bf790]"表示线程占用的内存地址

（6）"java.lang.Thread.State：BLOCKED"表示线程的状态

