## JDK静态代理

### 静态代理基本实现

创建一个接口，然后创建被代理的类实现该接口并且实现该接口中的抽象方法。之后再创建一个代理类，同时使其也实现这个接口。在代理类中持有一个被代理对象的引用，而后在代理类方法中调用该对象的方法。

其实就是代理类为被代理类预处理消息、过滤消息并在此之后将消息转发给被代理类，之后还能进行消息的后置处理.

```java
public class Client {

    public static void main(String[] args) {
        HelloProxy helloProxy = new HelloProxy();
        helloProxy.sayHello();
    }

    static class HelloProxy implements HelloWorldInterface {
        private HelloWorldInterface helloWorldInterface = new HelloWorldImpl();

        @Override
        public void sayHello() {
            System.out.println("before invoke static ");
            helloWorldInterface.sayHello();
            System.out.println("after invoke static ");
        }
    }
}
```



### 静态代理缺点

使用JDK静态代理很容易就完成了对一个类的代理操作。但是JDK静态代理的缺点也暴露了出来：由于代理只能为一个类服务，如果需要代理的类很多，那么就需要编写大量的代理类，比较繁琐。我们可以使用动态代理解决.

JDK静态代理是通过直接编码创建的，而JDK动态代理是利用反射机制在运行时创建代理类的。

## JDK动态代理

### 动态代理简介

其实在动态代理中，核心是InvocationHandler。每一个代理的实例都会有一个关联的调用处理程序(InvocationHandler).

InvocationHandler会保存实际的target对象.

对待代理实例(Proxy)进行调用时，将对方法的调用进行编码并指派到它的调用处理器(InvocationHandler)的invoke方法。所以对代理对象实例方法的调用都是通过InvocationHandler中的invoke方法来完成的，而invoke方法会根据传入的代理对象、方法名称以及参数决定调用代理的哪个方法。





### 步骤

使用JDK动态代理的五大步骤：

1）通过实现InvocationHandler接口来自定义自己的InvocationHandler；

2）通过Proxy.getProxyClass获得动态代理类；

3）通过反射机制获得代理类的构造方法，方法签名为getConstructor(InvocationHandler.class)；

4）通过构造函数获得代理对象并将自定义的InvocationHandler实例对象传为参数传入；

5）通过代理对象调用目标方法；



### 使用

HelloWorldInterface接口

```java
public interface HelloWorldInterface {
    void sayHello();
}
```

HelloWorldInvocationHandler

target保存具体实现的引用,通过 method.invoke(target, args);调用

```java
public class HelloWorldInvocationHandler implements InvocationHandler {
    private Object target;

    public HelloWorldInvocationHandler(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("method :" + method.getName() + " is invoked!");
        return method.invoke(target, args);
    }
}
```



方式1

对应5步走

```java
        Class<?> proxyClass = Proxy.getProxyClass(DynamicProxyLearn.class.getClassLoader(), HelloWorldInterface.class);
        final Constructor<?> cons = proxyClass.getConstructor(InvocationHandler.class);
        final InvocationHandler ih = new HelloWorldInvocationHandler(new HelloWorldImpl());
        HelloWorldInterface helloWorld = (HelloWorldInterface) cons.newInstance(ih);
        helloWorld.sayHello();
```

方式2

进行了封装,更常用,原理和上边是一样的.

```java
HelloWorldInterface helloWorld =  (HelloWorldInterface) Proxy.newProxyInstance(DynamicProxyLearn.class.getClassLoader()
                ,new Class<?>[]{HelloWorldInterface.class}
                , new HelloWorldInvocationHandler(new HelloWorldImpl())
                );
        helloWorld.sayHello();
```



### Proxy.newProxyInstance()

这个方法基本包含了jdk动态代理的逻辑.

```java
    @CallerSensitive
    public static Object newProxyInstance(ClassLoader loader,
                                          Class<?>[] interfaces,
                                          InvocationHandler h)
        throws IllegalArgumentException
    {
        Objects.requireNonNull(h);// 如果InvocationHandler为空直接抛出空指针异常，之后所有的单纯的判断null并抛异常，都是此方法
        // 拷贝类实现的所有接口
        final Class<?>[] intfs = interfaces.clone();
        // 获取当前系统安全接口
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
         // Reflection.getCallerClass返回调用该方法的方法的调用类;
         // loader：接口的类加载器
	    // 进行包访问权限、类加载器权限等检查
            checkProxyAccess(Reflection.getCallerClass(), loader, intfs);
        }

        /*
         * Look up or generate the designated proxy class.
	     * 译: 查找或生成指定的代理类
         */
        Class<?> cl = getProxyClass0(loader, intfs);

        /*
         * Invoke its constructor with the designated invocation handler.
         */
        try {
            if (sm != null) {
                checkNewProxyPermission(Reflection.getCallerClass(), cl);
            }

            final Constructor<?> cons = cl.getConstructor(constructorParams);
            final InvocationHandler ih = h;
            if (!Modifier.isPublic(cl.getModifiers())) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() {
                    public Void run() {
                        cons.setAccessible(true);
                        return null;
                    }
                });
            }
            // 向代理类构造器传入invocationHandler
            return cons.newInstance(new Object[]{h});
        } catch (IllegalAccessException|InstantiationException e) {
            throw new InternalError(e.toString(), e);
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new InternalError(t.toString(), t);
            }
        } catch (NoSuchMethodException e) {
            throw new InternalError(e.toString(), e);
        }
    }
```

newProxyInstance()方法帮我们执行了生成代理类----获取构造器----注入invocationHandler生成代理对象这三步.

生成代理类: Class<?> cl = getProxyClass0(loader, intfs);

获取构造器: final Constructor<?> cons = cl.getConstructor(constructorParams);

生成代理对象: cons.newInstance(new Object[]{h});



### **Proxy.getProxyClass0()如何生成代理类？**

这部分可以暂时跳过,直接看生成的代理类.

Proxy.newProxyInstance()中,`Class<?> cl = getProxyClass0(loader, intfs);`

传入了类加载器和需要实现的接口数组.

```java
private static Class<?> getProxyClass0(ClassLoader loader,
                                           Class<?>... interfaces) {
	      //接口数不得超过65535个
        if (interfaces.length > 65535) {
            throw new IllegalArgumentException("interface limit exceeded");
        }
 
        // If the proxy class defined by the given loader implementing
        // the given interfaces exists, this will simply return the cached copy;
        // otherwise, it will create the proxy class via the ProxyClassFactory
	     // 译: 如果缓存中有代理类了直接返回，否则将由代理类工厂ProxyClassFactory创建代理类
        return proxyClassCache.get(loader, interfaces);
    }
```

我们继续追踪创建方法.发现在ProxyClassFactory.apply()方法中调用代理类创建。

```java
byte[] proxyClassFile = ProxyGenerator.generateProxyClass(proxyName, interfaces, accessFlags);
```

 **代理类创建真正在ProxyGenerator.generateProxyClass（）**

```java

public static byte[] generateProxyClass(final String name, Class<?>[] interfaces, int accessFlags) {
        ProxyGenerator gen = new ProxyGenerator(name, interfaces, accessFlags);
        // 真正生成字节码的方法
        final byte[] classFile = gen.generateClassFile();
        // 如果saveGeneratedFiles为true 则生成字节码文件，所以在开始我们要设置这个参数
        .......
        return classFile;

}
```

**代理类生成的最终方法是ProxyGenerator.generateClassFile()**

```java
private byte[] generateClassFile() {
        /* ============================================================
         * Step 1: Assemble ProxyMethod objects for all methods to generate proxy dispatching code for.
         * 步骤1：为所有方法生成代理调度代码，将代理方法对象集合起来。
         */
        //增加 hashcode、equals、toString方法
        addProxyMethod(hashCodeMethod, Object.class);
        addProxyMethod(equalsMethod, Object.class);
        addProxyMethod(toStringMethod, Object.class);
        // 获得所有接口中的所有方法，并将方法添加到代理方法中
        for (Class<?> intf : interfaces) {
            for (Method m : intf.getMethods()) {
                addProxyMethod(m, intf);
            }
        }
 
        /*
         * 验证方法签名相同的一组方法，返回值类型是否相同；意思就是重写方法要方法签名和返回值一样
         */
        for (List<ProxyMethod> sigmethods : proxyMethods.values()) {
            checkReturnTypes(sigmethods);
        }
 
        /* ============================================================
         * Step 2: Assemble FieldInfo and MethodInfo structs for all of fields and methods in the class we are generating.
         * 为类中的方法生成字段信息和方法信息
         */
        try {
            // 生成代理类的构造函数
            methods.add(generateConstructor());
            for (List<ProxyMethod> sigmethods : proxyMethods.values()) {
                for (ProxyMethod pm : sigmethods) {
                    // add static field for method's Method object
                    fields.add(new FieldInfo(pm.methodFieldName,
                            "Ljava/lang/reflect/Method;",
                            ACC_PRIVATE | ACC_STATIC));
                    // generate code for proxy method and add it
					// 生成代理类的代理方法
                    methods.add(pm.generateMethod());
                }
            }
            // 为代理类生成静态代码块，对一些字段进行初始化
            methods.add(generateStaticInitializer());
        } catch (IOException e) {
            throw new InternalError("unexpected I/O Exception", e);
        }
 
        if (methods.size() > 65535) {
            throw new IllegalArgumentException("method limit exceeded");
        }
        if (fields.size() > 65535) {
            throw new IllegalArgumentException("field limit exceeded");
        }
 
        /* ============================================================
         * Step 3: Write the final class file.
         * 步骤3：编写最终类文件
         */
        /*
         * Make sure that constant pool indexes are reserved for the following items before starting to write the final class file.
         * 在开始编写最终类文件之前，确保为下面的项目保留常量池索引。
         */
        cp.getClass(dotToSlash(className));
        cp.getClass(superclassName);
        for (Class<?> intf: interfaces) {
            cp.getClass(dotToSlash(intf.getName()));
        }
 
        /*
         * Disallow new constant pool additions beyond this point, since we are about to write the final constant pool table.
         * 设置只读，在这之前不允许在常量池中增加信息，因为要写常量池表
         */
        cp.setReadOnly();
 
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);
 
        try {
            // u4 magic;
            dout.writeInt(0xCAFEBABE);
            // u2 次要版本;
            dout.writeShort(CLASSFILE_MINOR_VERSION);
            // u2 主版本
            dout.writeShort(CLASSFILE_MAJOR_VERSION);
 
            cp.write(dout);             // (write constant pool)
 
            // u2 访问标识;
            dout.writeShort(accessFlags);
            // u2 本类名;
            dout.writeShort(cp.getClass(dotToSlash(className)));
            // u2 父类名;
            dout.writeShort(cp.getClass(superclassName));
            // u2 接口;
            dout.writeShort(interfaces.length);
            // u2 interfaces[interfaces_count];
            for (Class<?> intf : interfaces) {
                dout.writeShort(cp.getClass(
                        dotToSlash(intf.getName())));
            }
            // u2 字段;
            dout.writeShort(fields.size());
            // field_info fields[fields_count];
            for (FieldInfo f : fields) {
                f.write(dout);
            }
            // u2 方法;
            dout.writeShort(methods.size());
            // method_info methods[methods_count];
            for (MethodInfo m : methods) {
                m.write(dout);
            }
            // u2 类文件属性：对于代理类来说没有类文件属性;
            dout.writeShort(0); // (no ClassFile attributes for proxy classes)
 
        } catch (IOException e) {
            throw new InternalError("unexpected I/O Exception", e);
        }
 
        return bout.toByteArray();
    }
```

**通过addProxyMethod（）添加hashcode、equals、toString方法。**

```java
private void addProxyMethod(Method var1, Class var2) {
        String var3 = var1.getName();  //方法名
        Class[] var4 = var1.getParameterTypes();   //方法参数类型数组
        Class var5 = var1.getReturnType();    //返回值类型
        Class[] var6 = var1.getExceptionTypes();   //异常类型
        String var7 = var3 + getParameterDescriptors(var4);   //方法签名
        Object var8 = (List)this.proxyMethods.get(var7);   //根据方法签名却获得proxyMethods的Value
        if(var8 != null) {    //处理多个代理接口中重复的方法的情况
            Iterator var9 = ((List)var8).iterator();
            while(var9.hasNext()) {
                ProxyGenerator.ProxyMethod var10 = (ProxyGenerator.ProxyMethod)var9.next();
                if(var5 == var10.returnType) {
                    /*归约异常类型以至于让重写的方法抛出合适的异常类型，我认为这里可能是多个接口中有相同的方法，而这些相同的方法抛出的异常类                      型又不同，所以对这些相同方法抛出的异常进行了归约*/
                    ArrayList var11 = new ArrayList();
                    collectCompatibleTypes(var6, var10.exceptionTypes, var11);
                    collectCompatibleTypes(var10.exceptionTypes, var6, var11);
                    var10.exceptionTypes = new Class[var11.size()];
                    //将ArrayList转换为Class对象数组
                    var10.exceptionTypes = (Class[])var11.toArray(var10.exceptionTypes);
                    return;
                }
            }
        } else {
            var8 = new ArrayList(3);
            this.proxyMethods.put(var7, var8);
        }    
        ((List)var8).add(new ProxyGenerator.ProxyMethod(var3, var4, var5, var6, var2, null));
       /*如果var8为空，就创建一个数组，并以方法签名为key,proxymethod对象数组为value添加到proxyMethods*/
    }

```



### 生成的动态代理类$ProxyXXX.class字节码反编译：

```java
public final class $Proxy0 extends Proxy implements HelloWorldInterface {
    private static Method m1;
    private static Method m3;
    private static Method m2;
    private static Method m0;

    public $Proxy0(InvocationHandler var1) throws  {
        super(var1);
    }

    public final boolean equals(Object var1) throws  {
        try {
            return (Boolean)super.h.invoke(this, m1, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }
    //HelloWorldInterface方法实现,实际调用invocationHandler
    public final void sayHello() throws  {
        try {
            super.h.invoke(this, m3, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final String toString() throws  {
        try {
            return (String)super.h.invoke(this, m2, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final int hashCode() throws  {
        try {
            return (Integer)super.h.invoke(this, m0, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    static {
        try {
            m1 = Class.forName("java.lang.Object").getMethod("equals", Class.forName("java.lang.Object"));
            m3 = Class.forName("com.learn.java.proxy.dynamic.hello.HelloWorldInterface").getMethod("sayHello");
            m2 = Class.forName("java.lang.Object").getMethod("toString");
            m0 = Class.forName("java.lang.Object").getMethod("hashCode");
        } catch (NoSuchMethodException var2) {
            throw new NoSuchMethodError(var2.getMessage());
        } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
        }
    }
}
```








### Jdk动态代理的缺点

只能代理接口,代理类必须实现接口



Proxy  <-  Invocationhandler(可以写代理逻辑) <- Impl实际实现

### 参考

https://blog.csdn.net/yhl_jxy/article/details/80586785




















