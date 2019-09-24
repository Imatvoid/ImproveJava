
## 介绍

>Shutdown Hooks are a special construct that allow developers to plug in a piece of code to be executed when the JVM is shutting down.

JDK提供了Java.Runtime.addShutdownHook(Thread hook)方法，可以注册一个JVM关闭的钩子，这个钩子可以在一下几种场景中被调用：

1. 程序正常退出
2. 使用System.exit()
3. 终端使用Ctrl+C触发的中断
4. 系统关闭
5. OutOfMemory宕机
6. 使用Kill pid命令干掉进程（注：在使用kill -9 pid时，是不会被调用的）



## 情况测试

### 程序正常退出

```java
public class HookTest {
    public void start()
    {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run()
            {
                System.out.println("Execute Hook.....");
            }
        }));
    }

    public static void main(String[] args)
    {
        new HookTest().start();
        System.out.println("The Application is doing something");

        try
        {
            TimeUnit.MILLISECONDS.sleep(5000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

}
```

The Application is doing something
Execute Hook.....



### OutOfMemory宕机

```java
package com.learn.java.jvm.shutdownhook;

import java.util.concurrent.TimeUnit;

/**
 * -Xmx20M  这样可以保证会有OutOfMemoryError的发生。
 */
public class HookOutOfMemory {

    public void start()
    {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run()
            {
                System.out.println("Execute Hook.....");
            }
        }));
    }

    public static void main(String[] args)
    {
        new HookTest().start();
        System.out.println("The Application is doing something");
        byte[] b = new byte[500*1024*1024];
        try
        {
            TimeUnit.MILLISECONDS.sleep(5000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }


}
```

```
The Application is doing something
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at com.hook.HookOutOfMemory.main(HookTest2.java:22)
Execute Hook.....
```



