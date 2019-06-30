## 并发下的ArrayList



```java

public class ArrayListMultiThread {
    static ArrayList<Integer> arrayList = new ArrayList<Integer>(10);

    public static class AddThread implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                arrayList.add(i);
            }
        }
    }

    /**
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String args[]) throws InterruptedException {
        Thread thread1 = new Thread(new AddThread());
        Thread thread2 = new Thread(new AddThread());

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(arrayList.size());
    }

}
```



你可能会得到三种结果

- ArrayList大小确实200万,运气非常好

   * ArrayList是一个线程不安全的容器,多线程操作时会出现冲突,报错信息如下:
     * Exception in thread "Thread-1" java.lang.ArrayIndexOutOfBoundsException: 22
     * at java.util.ArrayList.add(ArrayList.java:441)
     * at chapter2.ArrayListMultiThread$AddThread.run(ArrayListMultiThread.java:27)
     * at java.lang.Thread.run(Thread.java:745)

     在扩容过程中,没有锁,导致了不一致的内部状态

- 大小<200万,但没报错,写入覆盖了,但没报错,如果在生产环境这非常难以排查



Vector是一个线程安全的容器,可以代替ArrayList

















