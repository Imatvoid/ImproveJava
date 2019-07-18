###  Object

>1. public final native Class<?> getClass()
>2. public native int hashCode()
>3. public boolean equals(Object obj)
>4. protected native Object clone() throws CloneNotSupportedException
>5. public String toString()
>6. public final native void notify()
>7. public final native void notifyAll()
>8. public final native void wait(long timeout) throws InterruptedException
>9. public final void wait(long timeout, int nanos) throws InterruptedException
>10. public final void wait() throws InterruptedException
>11. protected void finalize() throws Throwable { }



##### 1.public final native Class<?> getClass()

返回当前运行时对象的Class对象，注意这里是运行时

##### 2.public native int hashCode()

hashCode方法也是一个native方法。

该方法返回对象的哈希码，主要使用在哈希表中，比如JDK中的HashMap。

哈希码的通用约定如下：

1. 在java程序执行过程中，在一个对象没有被改变的前提下，无论这个对象被调用多少次，hashCode方法都会返回相同的整数值。对象的哈希码没有必要在不同的程序中保持相同的值。
2. 如果2个对象使用equals方法进行比较并且相同的话，那么这2个对象的hashCode方法的值也必须相等。
3. 如果根据equals方法，得到两个对象不相等，那么这2个对象的hashCode值不需要必须不相同。但是，不相等的对象的hashCode值不同的话可以提高哈希表的性能。

通常情况下，不同的对象产生的哈希码是不同的。默认情况下，对象的哈希码是通过将该对象的内部地址转换成一个整数来实现的。

String的hashCode方法实现如下， 计算方法是 s[0]*31^(n-1) + s[1]*31^(n-2) + … + s[n-1]，其中s[0]表示字符串的第一个字符，n表示字符串长度：

```
public int hashCode() {
    int h = hash;
    if (h == 0 && value.length > 0) {
        char val[] = value;

        for (int i = 0; i < value.length; i++) {
            h = 31 * h + val[i];
        }
        hash = h;
    }
    return h;
}
```

比如”fo”的hashCode = 102 *31^1 + 111 = 3273， “foo”的hashCode = 102* 31^2 + 111 * 31^1 + 111 = 101574 (‘f’的ascii码为102, ‘o’的ascii码为111)

hashCode在哈希表HashMap中的应用：

```java
// Student类，只重写了hashCode方法
public static class Student {

    private String name;
    private int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

Map<Student, String> map = new HashMap<Student, String>();
Student stu1 = new Student("fo", 11);
Student stu2 = new Student("fo", 22);
map.put(stu1, "fo");
map.put(stu2, "fo");
```

上面这段代码中，map中有2个元素stu1和stu2。但是这2个元素是在哈希表中的同一个数组项中的位置，也就是在同一串链表中。 但是为什么stu1和stu2的hashCode相同，但是两条元素都插到map里了，这是因为map判断重复数据的条件是 **两个对象的哈希码相同并且(两个对象是同一个对象或者两个对象相等[equals为true])**。 所以再给Student重写equals方法，并且只比较name的话，这样map就只有1个元素了。

```
@Override
public boolean equals(Object o) {
    if (this == o) return true;  // 自反性
    if (o == null || getClass() != o.getClass()) return false; //null
    Student student = (Student) o;
    return this.name.equals(student.name);
}
```

这个例子直接说明了hashCode中通用约定的第三点：

第三点：如果根据equals方法，得到两个对象不相等，那么这2个对象的hashCode值不需要必须不相同。但是，不相等的对象的hashCode值不同的话可以提高哈希表的性能。 –> 上面例子一开始没有重写equals方法，导致两个对象不相等，但是这两个对象的hashCode值一样，所以导致这两个对象在同一串链表中，影响性能。



##### 3.public boolean equals(Object obj)

比较两个对象是否相等。Object类的默认实现，即比较2个对象的内存地址是否相等：

```java
public boolean equals(Object obj) {
    return (this == obj);
}
```

- 自反性
- 对称性
- 传递性
- 幂等性(一致性)
- x.equals(null) =返回false

注意点：如果重写了equals方法，通常有必要重写hashCode方法，这点已经在hashCode方法中说明了。



#####  4.protected native Object clone() throws CloneNotSupportedException
创建并返回当前对象的一份拷贝。一般情况下，对于任何对象 x，表达式 x.clone() != x 为true，x.clone().getClass() == x.getClass() 也为true。

Object类的clone方法是一个protected的native方法。

由于Object本身没有实现Cloneable接口，所以不重写clone方法并且进行调用的话会发生CloneNotSupportedException异常。



##### 5.public String toString()

Object对象的默认实现，即输出类的名字@实例的哈希码的16进制：

```
public String toString() {
    return getClass().getName() + "@" + Integer.toHexString(hashCode());
}
```

toString方法的结果应该是一个简明但易于读懂的字符串。建议Object所有的子类都重写这个方法。





##### protected void finalize() throws Throwable { }

finalize方法是一个protected方法，Object类的默认实现是不进行任何操作。

该方法的作用是实例被垃圾回收器回收的时候触发的操作，就好比 “死前的最后一波挣扎”。

不应依赖finalize，因为JVM不会保证它的执行，或许可以在其中做一些资源清理的检查报警。

[finalize的问题](https://www.cnblogs.com/Smina/p/7189427.html)

[方法签名](https://juejin.im/post/5a389954f265da432d28379e)

待补充，finalize与引用











https://ihenu.iteye.com/blog/2233249

https://fangjian0423.github.io/2016/03/12/java-Object-method/