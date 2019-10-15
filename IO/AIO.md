## Asynchronous I/O

### 介绍

AIO 也就是 NIO 2。在 Java 7 中引入了 NIO 的改进版 NIO 2,它是异步非阻塞的IO模型。异步 IO 是基于事件和回调机制实现的，也就是应用操作之后会直接返回，不会堵塞在那里，当后台处理完成，操作系统会通知相应的线程进行后续的操作。

AIO 是异步IO的缩写，虽然 NIO 在网络操作中，提供了非阻塞的方法，但是 NIO 的 IO 行为还是同步的。对于 NIO 来说，加入了轮询机制,但是本质还是同步的,

AIO 的 I/O 操作，有两种方式的 API 可以进行：

- Future 方式；
- Callback 方式。



### Future 方式

Future 方式：即提交一个 I/O 操作请求，返回一个 Future。然后您可以对 Future 进行检查，确定它是否完成，或者阻塞 IO 操作直到操作正常完成或者超时异常。





## 参考

https://segmentfault.com/a/1190000012976683