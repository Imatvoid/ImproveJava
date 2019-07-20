

##  Java内存模型(JMM)

### 原子性(Atomicity)

不可中断

32位系统下,long(64位)的读取不是原子性的

### 可见性(Visibility)

一个线程修改了共享变量的值,其他线程能够立即知道

![1561796662802](./assets/concurrent/1561796662802.png)



可见性常被编译器优化(指令重排)/缓存优化/硬件优化(有些读内存不会立即触发,而是进入硬件队列等待) 影响

### 有序性(Ordering)



#### Happen Before原则



## 线程

### 线程状态


![image-20190629141808810](./assets/concurrent/image-20190629141808810.png)

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

### 终止线程stop() @Deprecated 

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

对象的等待池(因为是随机选择,所以用池而不是队列来描述)和锁池.

public final native void notifyAll() 全部唤醒

> wait notify notifyAll 只能在synchronized中使用,否则会抛出java.lang.IllegalMonitorStateException
>
> notify并不是立即释放锁
>
> 多线程内使用while做判断条件{wait()}



### wait 和 sleep 区别

他们都会让线程等待一段时间,但是wait会释放对象的锁,但是sleep不会

### 挂起suspend()和继续执行resume()  @Deprecated 

不建议使用,已废弃,suspend后不会释放锁,还是runnable状态,容易误判.

同时如果resume意外先于suspend执行,则可能会导致其他线程无法拿到锁资源,从而死锁.

**可以使用wait和notify实现suspend和resume**



### 等待线程结束join() 和 谦让 yield()

#### Thread.join()

public final void join() throws InterruptedException 
public final **synchronized** void join(long millis)throws InterruptedException 

join会阻塞当前线程无限等待,第二个方法则给个一个最大等待时间

本质是调用线程wait方法,然后执行完会notifyAll

```java
 while (isAlive()) {
                wait(0);
 }
```

> 不要在应用程序中,在Thread实例对象上使用wait/notify/notify等方法,这可能会影响系统api的工作,或者被系统影响

#### **Thread.yield()**

public static native void yield()

让出cpu使用权后继续争夺

### 线程组

可以将线程绑定到线程组.

- activeCount() 获取活动线程数
- list() 打印此线程组所有线程信息



### 守护线程(Daemon)

比如垃圾回收线程,JIT线程

在start之前设置setDaemon(true)

### 线程优先级

注意这依赖底层操作系统,无法预测确切结果,只是倾向.





## 人手一只笔: ThreadLocal 







## 锁

### 提高锁性能的几个建议

#### 尽可能减少持有锁的时间

锁的范围尽量小

#### 减小锁的粒度

ConcurrentHashMap的分段锁

#### 读写分离锁替换独占锁



#### 锁分离





#### 锁粗化







### java虚拟机对锁优化做的努力

#### 偏向锁

在几乎没有竞争的场景表现很好,在竞争激烈的场合不如不用

#### 轻量级锁

#### 自旋锁

### 无锁





### 死锁



## synchronized同步

> 加锁,同时保证可见性.有序性(限制线程串行执行).可以替代volatile

#### 用法

- 指定加锁对象(类,实例)
- 作用于方法(静态方法,实例方法)



## volatile关键字

英文语意,易变的,不稳定的.

这等于告诉java虚拟机,这个变量可能被多个线程修改.

> volatile 对保证操作原子性有很大帮助,但它本身无法保证原子性

#### volatile 保证可见性

```java
public class NoVisibility {
    private static boolean ready;
    private static int number;

    private static class ReaderThread extends Thread {
        public void run() {
            while (!ready)
                Thread.yield();
            System.out.println(number);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ReaderThread().start();
        Thread.sleep(1000);
        number = 42;
        ready = true;
        Thread.sleep(10000);
    }
}
```



#### volatile 保证有序性





## 一个隐蔽的问题



```java
public class BadLockOnInteger implements Runnable {

    public static Integer i = 0;
    static BadLockOnInteger instance = new BadLockOnInteger();
    
    @Override
    public void run() {
        for (int j = 0; j < 1000000; j++) {
            synchronized (i) {//这里同步的并不是同一个对象,因为i是以Integer关键字创建的
                //正确做法应该是 synchronized (instance)
                i++;
            }
        }
    }

    /**
     * 得到的结果并不是2000000,在多线程的操作中出现了错误
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String args[]) throws InterruptedException {
        Thread thread1 = new Thread(instance);
        Thread thread2 = new Thread(instance);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(i);
    }
}
```



Integer是不可变对象,i++ 实际是Integer.valueOf(),锁住的不是一个对象



## JDK并发包

### 倒计数器CountDownLatch

> 火箭发射前,做好所有检查工作后,再发射火箭

![image-20190718154244309](assets/concurrent/image-20190718154244309.png)


```java
// 新建
CountDownLatch end = new CountDownLatch(10);

// 递减
{
 end.countDown();
}


// 等待直到条件全部达成
{
 end.await();
 System.out.println("Fire!");
}
```



### 循环栅栏CyclicBarrier

和CountDownLatch 很类似,但功能更加强大

CountDownLatch的计数器只能使用一次,而CyclicBarrier的计数器可以使用reset()方法重
置。所以CyclicBarrier能处理更为复杂的业务场景。例如,如果计算发生错误,可以重置计数
器,并让线程重新执行一次。

![image-20190718155336625](assets/concurrent/image-20190718155336625.png)

 Cyclic是循环,代表可以反复使用,barrierAction可以指定一次计数完成后应该采取的动作

```java
//parties 计数总数,也就是参与的线程数量.当await线程的数量到达parties的时候
//barrierAction  一次计数完成后应该采取的动作
public CyclicBarrier(int parties, Runnable barrierAction) {
  
}
```



```java


{
  cyclicBarrier.await();
}
// 新建cyclicBarrier
CyclicBarrier cyclicBarrier = new CyclicBarrier(N, new BarrierRun());
```



可能抛出InterruptedException, BrokenBarrierException 

其中BrokenBarrierException计数器已经破损.再也无法达成.此时应该让线程退出.

比如,一共10个线程要在cyclicBarrier上等待后执行,如果一个线程调用了interrupt.那么很可能在被中断线程上得到一个InterruptedException ,在其他 9个等待的线程上得到BrokenBarrierException.

BrokenBarrierException可以避免其他线程无谓的等待.



### 信号量Semphore

Semaphore可以控制某个资源可被同时访问的个数，通过 acquire() 获取一个许可，如果没有就等待，而 release() 释放一个许可。

可以通过构造方法设置是否公平.

```java
public Semaphore(int permits) {}

public Semaphore(int permits, boolean fair){}
```

一个典型的例子是.

```java
public class SemapDemo implements Runnable {
    // 最大的并发是5.
    final Semaphore semp = new Semaphore(5);

    @Override
    public void run() {
        try {
            
            semp.acquire();
            //----------------临界区--------------
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getId() + ":done!");
            //----------------临界区--------------
            semp.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 总共20个线程,系统会以5个线程一组为单位,依次执行并输出
     *
     * @param args
     */
    public static void main(String args[]) throws  InterruptedException{
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        final SemapDemo demo = new SemapDemo();
        for (int i = 0; i < 20; i++) {
            executorService.submit(demo);
        }
        // 不然线程池没退出
        Thread.sleep(10000);
        executorService.shutdown();
    }
}
```

#### 主要方法

##### 获取许可证

```java
//获取一个许可证(响应中断)，在没有可用的许可证时当前线程被阻塞。
public void acquire() throws InterruptedException {
   sync.acquireSharedInterruptibly(1);
}

//获取一个许可证(不响应中断)
public void acquireUninterruptibly() {
   sync.acquireShared(1);
}

//尝试获取许可证(非公平获取)，立即返回结果（非阻塞）。
public boolean tryAcquire() {
   return sync.nonfairTryAcquireShared(1) >= 0;
}

//尝试获取许可证(定时获取)
public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException {
   return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
}

```

##### 释放许可证

```java
public void release() {
    sync.releaseShared(1);
}
// 释放几个许可
public void release(int permits) {
    if (permits < 0) throw new IllegalArgumentException();
    sync.releaseShared(permits);
}

```







### 重入锁ReentrantLock

> 重入锁可以完全代替synchronized,在java5之前,其性能远远优于synchronized,java6对synchronized做了大量优化,使得两者差距并不大,但ReentrantLock无疑更灵活.

#### 一个简单的demo

```java
public class ReenterLock implements Runnable {
    public static ReentrantLock lock = new ReentrantLock();
    public static int i = 0;

    @Override
    public void run() {
        for (int j = 0; j < 1000000; j++) {
            lock.lock();
            try {
                i++;
            } finally {
                lock.unlock();
            }

        }
    }

    public static void main(String args[]) throws InterruptedException {
        ReenterLock reenterLock = new ReenterLock();
        Thread thread1 = new Thread(reenterLock);
        Thread thread2 = new Thread(reenterLock);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(i);
    }
}

```



#### 重入

支持重入,可以多次lock,但是也必须释放相同的次数,释放少了锁依然持有,释放多了会有ILLegalMonitorStateException

#### 中断响应

等待锁的过程中,可以取消申请.这对解决死锁有帮助

lock.lockInterruptibly()

#### 锁申请等待延时

只等待一段时间,这对解决死锁有帮助

lock.tryLock(5, TimeUnit.SECONDS)

tryLock也可以不带参数运行,那样直接返回true/false



#### 公平锁

> 先申请先获得,不会产生饥饿现象

synchronized是非公平的,在创建reentrantlock的时候可以指定公平还是非公平

但是实现公平锁要求系统维护一个有序队列,因此公平锁的实现成本较高,性能相对低下.





### 重入锁的好搭档 Condition 

synchronized -- wait notify

Condition和ReentrantLock组合可以实现类似上面组合的效果



### 读写锁ReadWriteLock

读写锁是JDK1.5提供的读写分离锁,可以有效减少所竞争.

|      | 读     | 写   |
| ---- | ------ | ---- |
| 读   | 非阻塞 | 阻塞 |
| 写   | 阻塞   | 阻塞 |

这时多个线程同时读,可以真正并行.当系统读的需求很大的时候,对性能提升很大.

```java
    private static Lock readLock = reentrantReadWriteLock.readLock();
    private static Lock writeLock = reentrantReadWriteLock.writeLock();
```





### 线程阻塞工具类LockSupport

LockSupport是一个非常方便的线程阻塞工具,可以在任意位置让线程阻塞,

`park`和`unpark`方法提供了阻塞和解除阻塞线程的有效方法，并且不会遇到过时方法Thread.suspend和Thread.resume 导致线程变得不可用的问题.

LockSupport对park和unpark的调用顺序并没有要求，先调用unpark，再调用park，依旧可以获得许可，让线程继续运行。这一点与Thread.suspend和Thread.resume以及Object.wait() Object.notify 要求固定的顺序不同。



和Object.wait()相比,无需先获得锁,也不会抛出InterruptedException异常.

LockSupport的park能够能响应interrupt事件，且不会抛出InterruptedException异常,只是继续执行而已。

#### 方法

##### 暂停执行

```java
// park方法阻塞的是当前的线程，也就是说在哪个线程中调用，那么哪个线程就被阻塞（在没有获得许可的情况下）。
public static void park() {
        UNSAFE.park(false, 0L);
    } 
public static void park(Object blocker) {
        Thread t = Thread.currentThread();
        setBlocker(t, blocker);
        UNSAFE.park(false, 0L);
        setBlocker(t, null);
    }
```

支持一个`blocker`对象参数。此对象在线程受阻塞时被记录，以允许监视工具和诊断工具确定线程受阻塞的原因。（这样的工具可以使用方法 `getBlocker(java.lang.Thread)`访问blocker。）建议最好使用这些形式，而不是不带此参数的原始形式。在锁实现中提供的作为`blocker`的普通参数是`this`。

##### 恢复执行

```java
// 传入一个线程对象.
public static void unpark(Thread thread) {
        if (thread != null)
            UNSAFE.unpark(thread);
    }
```

<https://juejin.im/post/5b018f70f265da0b7d0ba86a>



## Future模式

Future模式有点类似在网上买东西。如果我们在网上下单买了一部手机，当我们支付完成后，手机并没有办法立即送到家里，但是在电脑上会立即产生一个订单。这个订单就是将来发货或者领取手机的重要凭证，这个凭证也就是Future模式中会给出的一个契约。在支付活动结束后，大家不会傻傻地等着手机到来，而是各忙各的。而这张订单就成了商家的动力.

future模式无法立即给你数据,但允许你拿到一个契约,你可以将来通过契约拿到你想要的数据.

### JDK的Future

#### 接口Future<V>

提供了如下功能:

　　1）判断任务是否完成；

　　2）能够中断任务；

　　3）能够获取任务执行结果。

```java
// 一个立即返回的契约
public interface Future<V> {

    // 取消任务 mayInterruptIfRunning决定已经开始的任务时中断还是可以继续完成
    // 如果任务已经完成,则无论mayInterruptIfRunning为true还是false，此方法肯定返回false
    // 如果任务还没开始,返回true
    boolean cancel(boolean mayInterruptIfRunning);

    boolean isCancelled();

    // 正常地结束,异常结束,或者被取消,返回true
    boolean isDone();

    // 阻塞住,还可能抛出运行时异常CancellationException
    V get() throws InterruptedException, ExecutionException;

  
    V get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException;
}
```

#### 接口Callable<V>

这是一个类似**Runnable**的接口.但有返回值,可以抛出异常.

Callable里面的call方法是可以抛出异常的，我们可以捕获异常进行处理；但是Runnable里面的run方法是不可以抛出异常的，异常要在run方法内部必须得到处理，不能向外界抛出；

```java
Runnable r = new Runnable() {
            @Override
            public void run() throws  Exception{
                // 会报错,只能抛出运行时异常.
                throw new Exception();
                //throw new RuntimeException();
               
            }
        };
```

```java
@FunctionalInterface
public interface Callable<V> {
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    V call() throws Exception;
}
```



#### 具体实现FutureTask<V>

```java
public class FutureTask<V> implements RunnableFuture<V>
```

FutureTask类实现了RunnableFuture接口，我们看一下RunnableFuture接口的实现：

```java
public interface RunnableFuture<V> extends Runnable, Future<V> {
    void run();
}
```

可以看出RunnableFuture继承了Runnable接口和Future接口，而FutureTask实现了RunnableFuture接口。**所以它既可以作为Runnable被线程执行，又可以作为Future得到Callable的返回值**。

FutureTask提供了2个构造器：

```java
`public` `FutureTask(Callable<V> callable) {``}``public` `FutureTask(Runnable runnable, V result) {``}`
```

事实上，FutureTask是Future接口的一个唯一实现类。

#### 参考

<https://www.cnblogs.com/dolphin0520/p/3949310.html>

https://www.cnblogs.com/cz123/p/7693064.html

### Guava对Future的支持

在JDK自带的简单 Future 模式中，虽然我们可以使用 Future.get()方法得到 Future 的处理结果，但是这个方法是阻塞的，因此并不利于我们开发高并发应用。但在 Guava 中，增强了 Future 模式，增加了对 Future 模式完成时的回调接口，使得 Future 完成时可以自动通知应用程序进行后续处理。

```java
ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

        ListenableFuture<String> future = listeningExecutorService.submit(() -> "hello");

        Futures.addCallback(future, new FutureCallback<String>() {
            @Override
            public void onSuccess(@Nullable String s) {
                System.out.println("处理成功");
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("处理失败" + throwable);
            }
        }, MoreExecutors.newDirectExecutorService());
```



## 线程池

线程复用,不必总是频繁的创建和销毁.

![image-20190718180035357](assets/concurrent/image-20190718180035357.png)

### 前置

#### Executor接口

```java
public interface Executor {

    /**
     * @param command the runnable task
     * @throws RejectedExecutionException if this task cannot be
     * accepted for execution
     * @throws NullPointerException if command is null
     */
    void execute(Runnable command);
}
```

#### ExecutorService 接口

```java
public interface ExecutorService extends Executor {
 
    void shutdown();
    boolean isShutdown();
    boolean isTerminated();
    boolean awaitTermination(long timeout, TimeUnit unit)
        throws InterruptedException;
    // 如果为了可取消性而使用 Future 但又不提供可用的结果，则可以声明 Future<?> 形式类型、并返回 null 作为底层任务的结果。
    Future<?> submit(Runnable task);
    <T> Future<T> submit(Callable<T> task);
    ......
}
```



Executor是一个顶层接口，在它里面只声明了一个方法execute(Runnable)，返回值为void，参数为Runnable类型，从字面意思可以理解，就是用来执行传进去的任务的；

然后ExecutorService接口继承了Executor接口，并声明了一些方法：submit、invokeAll、invokeAny以及shutDown等；

抽象类AbstractExecutorService实现了ExecutorService接口，基本实现了ExecutorService中声明的所有方法；

```java
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) { };
    public Future<?> submit(Runnable task) {};
```

然后ThreadPoolExecutor继承了类AbstractExecutorService。



### ThreadPoolExcutor



```java
public class ThreadPoolExecutor extends AbstractExecutorService {
 public ThreadPoolExecutor(int corePoolSize,
                           int maximumPoolSize,
                           long keepAliveTime,
                           TimeUnit unit,
                           BlockingQueue<Runnable> workQueue,
                           ThreadFactory threadFactory,
                           RejectedExecutionHandler handler);
}
```

#### 参数

**corePoolSize：** 线程池核心线程数最大值

**maximumPoolSize：** 线程池最大线程数大小

**keepAliveTime：** 线程池中非核心线程空闲的存活时间大小

**unit：** 线程空闲存活时间单位

**workQueue：** 存放任务的阻塞队列

**threadFactory：** 用于设置创建线程的工厂，可以给创建的线程设置有意义的名字，可方便排查问题。

**handler：**  线城池的饱和策略事件，主要有四种类型。

#### 线程使用策略

![image-20190719153527434](assets/concurrent/image-20190719153527434.png)

#### 拒绝策略

- AbortPolicy(抛出一个异常，默认的)

- DiscardPolicy(直接丢弃任务)

- DiscardOldestPolicy（丢弃队列里最老的任务，将当前这个任务继续提交给线程池）

- CallerRunsPolicy（交给线程池调用所在的线程进行处理)





#### 异常处理

默认是无法感知异常的.

```java
ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            threadPool.submit(() -> {
                System.out.println("current thread name" + Thread.currentThread().getName());
                Object object = null;
                System.out.print("result## " + object.toString());
            });
        }
```

##### 1.使用try catch

```java
ExecutorService threadPool = Executors.newFixedThreadPool(5);
for (int i = 0; i < 5; i++) {
            threadPool.submit(() -> {
                System.out.println("current thread name" + Thread.currentThread().getName());
                try{
                    Object object = null;
                    System.out.print("result## " + object.toString());
                }catch (Exception e){
                    // 可以感知.
                    System.out.println(Thread.currentThread().getName()+"异常");
                    // 线程可以继续复用.
                    throw  new RuntimeException(Thread.currentThread().getName()+"异常");
                }
            });
        }
```

##### 2. 使用Future.get方法.

```java
 ExecutorService threadPool = Executors.newFixedThreadPool(5);
 for (int i = 0; i < 5; i++) {
            Future future = threadPool.submit(() -> {
                System.out.println("current thread name" + Thread.currentThread().getName());
                Object object = null;
                System.out.print("result## " + object.toString());
            });
            try {
                future.get();
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + "异常");
                //终止主线程
                throw new RuntimeException(Thread.currentThread().getName() + "异常");
            }
        }
```

##### 3.为工作者线程设置UncaughtExceptionHandler，在uncaughtException方法中处理异常

自定义ThreadFactory.

```java
 ExecutorService threadPool = Executors.newFixedThreadPool(1, r -> {
            Thread t = new Thread(r);
            t.setUncaughtExceptionHandler(
                    (t1, e) -> {
                        System.out.println(t1.getName() + "线程抛出的异常"+e);
                        //throw new RuntimeException();
                    });
            return t;
        });
        threadPool.execute(()->{
            Object object = null;
            System.out.print("result## " + object.toString());
        });
```

##### 4.重写ThreadPoolExecutor的afterExecute方法，处理传递的异常引用

```java
class ExtendedExecutor extends ThreadPoolExecutor {

        public ExtendedExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        // jdk文档里面给的例子。。
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            if (t == null && r instanceof Future<?>) {
                try {
                    Object result = ((Future<?>) r).get();
                } catch (CancellationException ce) {
                    t = ce;
                } catch (ExecutionException ee) {
                    t = ee.getCause();
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // ignore/reset
                }
            }
            if (t != null)
                System.out.println(t);
        }
    }
```

#### 工作队列

- ArrayBlockingQueue
- LinkedBlockingQueue
- DelayQueue
- PriorityBlockingQueue
- SynchronousQueue

##### ArrayBlockingQueue

ArrayBlockingQueue（有界队列）是一个用数组实现的有界阻塞队列，按FIFO排序量。

##### LinkedBlockingQueue

LinkedBlockingQueue（可设置容量队列）基于链表结构的阻塞队列，按FIFO排序任务，容量可以选择进行设置，不设置的话，将是一个无边界的阻塞队列，最大长度为Integer.MAX_VALUE，吞吐量通常要高于ArrayBlockingQuene；newFixedThreadPool线程池使用了这个队列

##### DelayQueue

DelayQueue（延迟队列）是一个任务定时周期的延迟执行的队列。根据指定的执行时间从小到大排序，否则根据插入到队列的先后排序。newScheduledThreadPool线程池使用了这个队列。



##### PriorityBlockingQueue

PriorityBlockingQueue（优先级队列）是具有优先级的无界阻塞队列；



##### SynchronousQueue

SynchronousQueue（同步队列）一个不存储元素的阻塞队列，每个插入操作必须等到另一个线程调用移除操作，否则插入操作一直处于阻塞状态，吞吐量通常要高于LinkedBlockingQuene，newCachedThreadPool线程池使用了这个队列。



#### 常用的线程池

newFixedThreadPool (固定数目线程的线程池)

newSingleThreadExecutor(单线程的线程池)

newCachedThreadPool(可缓存线程的线程池)

newScheduledThreadPool(定时及周期执行的线程池)





##### newFixedThreadPool

```java
  public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>(),
                                      threadFactory);
    }
```

线程池特点：

- 核心线程数和最大线程数大小一样
- 没有所谓的非空闲时间，即keepAliveTime为0
- 阻塞队列为无界队列LinkedBlockingQueue



工作机制:

![image-20190719173649356](assets/concurrent/image-20190719173649356.png)

**使用无界队列的线程池会导致内存飙升吗？**

答案 **：会的，newFixedThreadPool使用了无界的阻塞队列LinkedBlockingQueue，如果线程获取一个任务后，任务的执行时间比较长(比如，上面demo设置了10秒)，会导致队列的任务越积越多，导致机器内存使用不停飙升，** 最终导致OOM。

使用场景

FixedThreadPool 适用于处理CPU密集型的任务，确保CPU在长期被工作线程使用的情况下，尽可能的少的分配线程，即适用执行长期的任务。



##### newCachedThreadPool

```java
public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>(),
                                      threadFactory);
    }
```

**线程池特点**：

- 核心线程数为0
- 最大线程数为Integer.MAX_VALUE
- 阻塞队列是SynchronousQueue
- 非核心线程空闲存活时间为60秒

当提交任务的速度大于处理任务的速度时，每次提交一个任务，就必然会创建一个线程。极端情况下会创建过多的线程，耗尽 CPU 和内存资源。由于空闲 60 秒的线程会被终止，长时间保持空闲的 CachedThreadPool 不会占用任何资源。

**工作机制**

![image-20190719173755826](assets/concurrent/image-20190719173755826.png)

提交任务

因为没有核心线程，所以任务直接加到SynchronousQueue队列。

判断是否有空闲线程，如果有，就去取出任务执行。

如果没有空闲线程，就新建一个线程执行。

执行完任务的线程，还可以存活60秒，如果在这期间，接到任务，可以继续活下去；否则，被销毁。

**使用场景**

用于并发执行大量短期的小任务。



##### newSingleThreadExecutor

```java
public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
        return new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>(),
                                    threadFactory));
    }

```

**线程池特点**

- 核心线程数为1
- 最大线程数也为1
- 阻塞队列是LinkedBlockingQueue
- keepAliveTime为0



**工作机制**

![image-20190719174117371](assets/concurrent/image-20190719174117371.png)



- 提交任务
- 线程池是否有一条线程在，如果没有，新建线程执行任务
- 如果有，讲任务加到阻塞队列
- 当前的唯一线程，从队列取任务，执行完一个，再继续取，一个人（一条线程）夜以继日地干活。

使用场景

适用于串行执行任务的场景，一个任务一个任务地执行。

##### newScheduledThreadPool



```java
 public ScheduledThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS,
              new DelayedWorkQueue());
    }

```



**线程池特点**

- 最大线程数为Integer.MAX_VALUE
- 阻塞队列是DelayedWorkQueue
- keepAliveTime为0
- scheduleAtFixedRate() ：按某种速率周期执行
- scheduleWithFixedDelay()：在某个延迟后执行



**工作机制**

- 添加一个任务
- 线程池中的线程从 DelayQueue 中取任务
- 线程从 DelayQueue 中获取 time 大于等于当前时间的task
- 执行完后修改这个 task 的 time 为下次被执行的时间
- 这个 task 放回DelayQueue队列中

```java
/**
    创建一个给定初始延迟的间隔性的任务，之后的下次执行时间是上一次任务从执行到结束所需要的时间+给定的间隔时间
    */
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleWithFixedDelay(()->{
            System.out.println("current Time" + System.currentTimeMillis());
            System.out.println(Thread.currentThread().getName()+"正在执行");
        }, 1, 3, TimeUnit.SECONDS);

  /**
    创建一个给定初始延迟的间隔性的任务，之后的每次任务执行时间为 初始延迟 + N * delay(间隔) 
    */
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
            scheduledExecutorService.scheduleAtFixedRate(()->{
            System.out.println("current Time" + System.currentTimeMillis());
            System.out.println(Thread.currentThread().getName()+"正在执行");
        }, 1, 3, TimeUnit.SECONDS);;

```

#### 使用场景

周期性执行任务的场景，需要限制线程数量的场景

回到面试题：**说说几种常见的线程池及使用场景？**

回答这四种经典线程池 **：newFixedThreadPool，newSingleThreadExecutor，newCachedThreadPool，newScheduledThreadPool，分线程池特点，工作机制，使用场景分开描述，再分析可能存在的问题，比如newFixedThreadPool内存飙升问题** 即可




#### execute方法和submit方法



execute()方法实际上是Executor接口中声明的方法，在ThreadPoolExecutor进行了具体的实现，这个方法是ThreadPoolExecutor的核心方法，通过这个方法可以向线程池提交一个任务，交由线程池去执行。

submit()方法是在ExecutorService中声明的方法，在AbstractExecutorService就已经有了具体的实现，在ThreadPoolExecutor中并没有对其进行重写，这个方法也是用来向线程池提交任务的，但是它和execute()方法不同，它能够返回任务执行的结果，去看submit()方法的实现，会发现它实际上还是调用的execute()方法，只不过它利用了Future来获取任务执行结果（Future相关内容将在下一篇讲述）。









UncaughtExceptionHandler只对execute提交有作用.

#### 参考

<https://juejin.im/post/5d1882b1f265da1ba84aa676>  比喻很好

<https://www.cnblogs.com/dolphin0520/p/3932921.html>

<https://segmentfault.com/a/1190000015808897>














### Excuters

不建议使用,会屏蔽细节.













