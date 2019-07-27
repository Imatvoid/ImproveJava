## Demo

```java
        ArrayList<String> list = new ArrayList<String>();
        list.add(new String("Monday"));
        list.add(new String("Tuesday"));
        list.add(new String("Wensday"));
        Iterator<String> it = list.iterator();//得到迭代器
        while(it.hasNext()){
            String weekday = it.next();//访问元素
            System.out.println(weekday.toUpperCase());
        }
```





## 源码分析

```java
  public Iterator<E> iterator() {
        return new ArrayList.Itr();
  }
```

私有内部类

```java
private class Itr implements Iterator<E> {
        int cursor; // 索引index
        int lastRet = -1;
        int expectedModCount;

        Itr() {
            // 保存当时的修改次数
            this.expectedModCount = ArrayList.this.modCount;
        }

        public boolean hasNext() {
            // 索引 0 - size-1
            return this.cursor != ArrayList.this.size;
        }
        final void checkForComodification() {
            // 不允许并发修改
            if (ArrayList.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
        
        public E next() {
            this.checkForComodification();
          
            int var1 = this.cursor;
            if (var1 >= ArrayList.this.size) {
                throw new NoSuchElementException();
            } else {
                Object[] var2 = ArrayList.this.elementData;
                if (var1 >= var2.length) {
                    throw new ConcurrentModificationException();
                } else {
                    this.cursor = var1 + 1;
                    return var2[this.lastRet = var1];
                }
            }
        }

        public void remove() {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            } else {
                this.checkForComodification();

                try {
                    ArrayList.this.remove(this.lastRet);
                    this.cursor = this.lastRet;
                    this.lastRet = -1;
                    this.expectedModCount = ArrayList.this.modCount;
                } catch (IndexOutOfBoundsException var2) {
                    throw new ConcurrentModificationException();
                }
            }
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            final int size = ArrayList.this.size;
            int i = cursor;
            if (i >= size) {
                return;
            }
            final Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length) {
                throw new ConcurrentModificationException();
            }
            while (i != size && modCount == expectedModCount) {
                consumer.accept((E) elementData[i++]);
            }
            // update once at end of iteration to reduce heap write traffic
            cursor = i;
            lastRet = i - 1;
            checkForComodification();
        }

        
    }
```

