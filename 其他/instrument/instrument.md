## 介绍

使用 Instrumentation，**使得开发者可以构建一个独立于应用程序的代理程序（Agent），用来监测和协助运行在 JVM 上的程序，甚至能够替换和修改某些类的定义**。有了这样的功能，开发者就可以实现更为灵活的运行时虚拟机监控和 Java 类操作了，这样的特性实际上提供了 **一种虚拟机级别支持的 AOP 实现方式**.



## 基本功能

### JVM启动前静态Instrument



### JVM启动后动态Instrument

在 Java SE 6 的 Instrumentation 当中，**有一个跟 premain“并驾齐驱”的“agentmain”方法，可以在 main 函数开始运行之后再运行**。





## 参考

https://juejin.im/post/5ac32eba5188255c313af0dd

[https://ouyblog.com/2019/03/%E5%9F%BA%E4%BA%8EJava-Agent%E4%B8%8EJavassist%E5%AE%9E%E7%8E%B0%E9%9B%B6%E4%BE%B5%E5%85%A5AOP](https://ouyblog.com/2019/03/基于Java-Agent与Javassist实现零侵入AOP)