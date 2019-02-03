### Thread类的方法



#### start()

多次调用会抛出java.lang.IllegalThreadStateException异常。start声明的顺序并不等于启动的顺序。



#### currentThread()

返回当前正在运行的线程



#### isAlive()

this.isaAlive()

Thread.currentThread().isAlive()



#### sleep()

让当前执行的线程，Thread.currentThead()返回的线程暂停执行xx毫秒



#### getId()

返回线程唯一ID





#### 线程的停止

----------------------------

#### interrupt

打标



#### interrupted()

静态方法，**当前线程**是否中断，当前线程指的是运行this.interrupted的线程,注意会清除标志

```java
 public static boolean interrupted() {
        return currentThread().isInterrupted(true);
    }
```





#### isInterrupted()

测试线程是否已经中断

```java
public boolean isInterrupted() {
        return isInterrupted(false);
    }
```

