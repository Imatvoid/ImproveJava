> 二叉小顶堆实现，可以用一棵完全二叉树表示



## Queue接口

```java
public interface Queue<E> extends Collection<E> {
    boolean add(E var1);

    boolean offer(E var1); //false

    E remove();

    E poll(); // null

    E element();

    E peek(); // null
}
```



## Priority 性能

- 初始化O(N)
- 插入/删除 log(N)



## 优先级队列

https://github.com/CarpenterLee/JCFInternals/blob/master/markdown/8-PriorityQueue.md

写的挺好的.



## 参考

https://github.com/CarpenterLee/JCFInternals/blob/master/markdown/8-PriorityQueue.md