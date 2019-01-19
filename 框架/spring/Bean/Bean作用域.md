### Bean作用域



| 作用域                     | 描述                                                         |
| -------------------------- | ------------------------------------------------------------ |
| 单例(singleton)            | （默认）每一个Spring IoC容器仅仅存在唯一的一个实例对象       |
| 原型（prototype）          | 每次从容器调用bean时，都返回一个新的实例，每次调用getBean（） 等于 newXxxBean（） |
| 请求（request）            | 每次HTTP请求会产生一个Bean对象，也就是说，每一个HTTP请求都有自己的Bean实例。只适用于WebApplicationContext环境 |
| 会话（session）            | 同一个HTTP Session共享一个Bean，不同的HttpSession使用不同的Bean。同样，只适用于WebApplicationContext环境 |
| 全局会话（global session） | 同一个全局session共享一个Bean。通常用于Portlet  web场景，在其他环境下等同于session作用域，同样，只适用于WebApplicationContext环境 |

```xml
 <bean id="boss2" class="com.smart.scope.Boss" p:car-ref="car" scope="prototype"/>
```

##### 自定义作用域（暂不探讨）



#### 作用域依赖问题

**Example1**

非Web作用域引用Web作用域

```xml
<bean name = "car"  class="xxxx" scope="request">
     <aop:scoped-proxy />
</bean>

<bean id = "boss" class="xxxxx">
    <property name = "car" ref="car"/>
</bean>
```

如果不使用aop只能得到失望的结果。

使用aop后，在boss中得到的是动态的代理对象（Car的子类，如果car是接口，则是car 的实现类），spring会在动态代理中实现一段逻辑：判断当前boss的线程，从而找到对应的HttpRequest，再从HttpRequest域拿到对应的 car。

(java只能对接口实现动态代理，Spring使用CGLib为类生成动态代理的子类)




