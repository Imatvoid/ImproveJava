## 同步屏障CyclicBarrier

















#### CyclicBarrier和CountDownLatch的区别

CountDownLatch的计数器只能使用一次,而CyclicBarrier的计数器可以使用reset()方法重
置。所以CyclicBarrier能处理更为复杂的业务场景。例如,如果计算发生错误,可以重置计数
器,并让线程重新执行一次。