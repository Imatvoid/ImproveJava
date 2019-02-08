### IOC
> Inverse Of Control 控制反转

**普通的IOC方式**

主要分为三种类型，spring主要支持构造函数住如何属性注入

-  构造函数注入

  通过构造函数设置具体实例

- 属性注入

  通过getter setter方法设置具体实例(并不总是需要，构造函数不好)

- 接口注入

  

**容器完成依赖关系的注入**

spring就是一个这样的容器，它通过配置配置文件或注解描述类和类之间的依赖关系，自动完成类的初始化。(反射机制)



**Java反射机制**

```java
@SuppressWarnings("unchecked")
    public static void main(String[] args)  throws  Throwable{
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        Class clazz = loader.loadClass("com.example.reflect.Car");
        Constructor constructor = clazz.getDeclaredConstructor();
        Car car =(Car) constructor.newInstance();


        Method setColor = clazz.getMethod("setColor",String.class);
        setColor.invoke(car,"red");

        System.out.print(1);

    }
```

上述代码中的配置项可以抽取到xml配置文件，从而让容器通过反射的方式实现注入。

