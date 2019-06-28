## 线程

### 线程状态

![image-20190628160925269](/Users/yangxu/Library/Application%20Support/typora-user-images/image-20190628160925269.png)

### 新建线程

```java
public class ThreadA  extends  Thread{
    @Override
    public void run() {
        //super.run();
        System.out.println("new thread");
    }
}

Thread thread = new Thread(() -> System.out.println("new thread runable")
        );
```

### 终止线程

stop()

暴力停止,可能导致数据不一致.

```java
            while (true) {
                
//                if(stopme){
//                    System.out.println("exit by stop me");
//                    break;
//                }
                 do something
              
            }
```

### 线程中断

> 线程中断并不会使线程立即退出,而是给线程发送一个通知,告知希望其退出.如何处理是线程自己的事情

- public void Thread.interrupt()   // 中断线程
- public boolean Thread.isInterrupted() //判断线程是否被中断
- public static boolean Thread.interruped() //判断线程是否被中断,并清除中断状态

Thread.interrupt() 实例方法,设置中断位

Thread.isInterrupted() 实例方法,检验中断标志位,返回是否被中断

Thread.interruped() 静态方法 检验中断标志位,返回是否被中断,并且清除

**只是设置中断标记位,不做处理,那什么都不会发生** 

**sleep方法由于中断被抛出异常,此时,它会清除中断标记位置.**

### 等待wait()和通知notify

public final void wait() throws InterruptedException 

public final native void notify() 随机唤醒

对象的等待池(因为是随机选择,所以用池而不是队列来描述).

public final native void notifyAll() 全部唤醒

> wait 只能在synchronized中使用?
>
> notify并不是立即释放锁

### wait 和 sleep 区别

他们都会让线程等待一段时间,但是wait会释放对象的锁,但是sleep不会

### 挂起suspend()和继续执行resume()

