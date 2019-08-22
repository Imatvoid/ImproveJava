Producer Extends Consumer Super.
生产者Extends,消费者Super.

<!-- more -->

## 测试
类继承结构:  
Executive(经理) extends Manager(管理者) extends Employee(职工)  extends Object

还有一个测试的范性类.
```java 
    static class Employee {

    }
    static class Manager extends Employee {

    }

    static class Executive extends Manager {

    }
    static class Test<T> {
        T value;

        public void setValue(T value) {
            this.value = value;
        }
        public T getValue() {
            return value;
        }
    }
```

## 你能理解下面的代码吗
? extends T 初始化
```java
//      Test<? extends Manager> t4= new Test<Employee>(); // 报错
//      Test<? extends Manager> t5 = new Test<Object>(); // 报错
        Test<? extends Manager> t6 = new Test<Manager>();
        Test<? extends Manager> t7 = new Test<Executive>();
```

? super T 初始化
```java
//      Test<? super Manager> t1 = new Test<Executive>(); // 报错
        Test<? super Manager> t2 = new Test<Manager>();
        Test<? super Manager> t3 = new Test<Object>();
        Test<? super Manager> t4 = new Test<Employee>();
				
```

可以看出,? extend T 和 ? super T 限制了可以声明类型的允许范围.以Manager为界限,? extend T 可以声明Manager以及Manager以下, ? super T可以声明Manager以及Manager以上.

## ? extends T 
描述一个这样的对象/容器对象,它/它内部的对象起码是个T,你对T的所有期望都可以满足.可以做到T可以做的所有事.
因此我们可以get(),无论容器中是T的哪个子类或者T本身,我们都可以当作T来操作.
这也就是生产者Extends,它可以对外提供对象.
但你不知道T实际是什么,是哪个子类.所以你的任何set操作,或者容器的add操作都不被java允许.否则就可能出现一个装香蕉的水果篮子里出现了苹果的情况.

```java
           Test<? extends Manager> t7 = new Test<Executive>();
//        t7.setValue(new Employee());  // 报错
//        t7.setValue(new Manager()); // 报错
//        t7.setValue(new Executive()); // 报错
//        t7.setValue(new Object()); // 报错
```

## 令人迷惑的 ? super T
或许你以为,按照对称性, ? super T描述的应该是一个什么都可以放的对象/容器,只要是T的父类.包括Object.
以下语句也确实不会报错.
```java
        Test<? super Manager> t2 = new Test<Manager>();
        Test<? super Manager> t3 = new Test<Object>();
        Test<? super Manager> t4 = new Test<Employee>();
```
但是你会失望的发现.在执行set操作/容器的add操作的时候.
```java
        Test<? super Manager> t4 = new Test<Employee>();
        t4.setValue(new Manager());
        t4.setValue(new Executive());
//        t4.setValue(new Object());    //报错
//        t4.setValue(new Employee());  //报错
```
你只能set/add  Manager和Manager的子类.
这是因为:
? super T 确实描述了一个T的父类对象/父类容器,但不知道是哪个父类,所以你只能set T/T的子类. 因为,所有T的子类都可以转化为任何一个T的父类.
set/add 操作是绝对安全的. 这也就是消费者 Super. 可以接受对象.

但由于你不清楚实际是那个父类,所以你只能get到一个Object.    
`Object o = t4.getValue();`
这是合法的,但是没有什么意义.Object对象除了一个引用,无法进行什么操作.
## 容器操作
这方面的例子可以看<http://awesomehuan.com/2016/03/05/Java%E4%B8%AD%E7%9A%84PECS/>加深印象.
## 巧妙的运用

```java
public class Collections { 
  public static <T> void copy  ( List<? super T> dest, List<? extends T> src)   {  
      for (int i=0; i<src.size(); i++) 
        dest.set(i,src.get(i)); 
  } 
}
```
上面代码中copy方法的功能是将src中的数据复制到dest中，这里src就是生产者，dest就是消费者。
设计这样的方法，好处就是，可以复制任意类型的List，通用性特别强。
## 参考
<https://stackoverflow.com/questions/2723397/what-is-pecs-producer-extends-consumer-super>

<http://awesomehuan.com/2016/03/05/Java%E4%B8%AD%E7%9A%84PECS/>