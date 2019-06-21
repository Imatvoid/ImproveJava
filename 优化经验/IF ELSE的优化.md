# 如何优化IF ELSE



## 为什么要优化

太复杂地嵌套if else 会导致难以阅读,以及扩展性极差,违反了单一职责原则和开闭原则.



## IF ELSE 嵌套过深



### 适当地抽取方法

https://refactoring.com/catalog/extractFunction.html

### 卫语句 -  漏斗式的代码

优化前

```JAVA
double getPayAmount(){
    double result;
    if(_isDead) {
        result = deadAmount();
    }else{
        if(_isSeparated){
            result = separatedAmount();
        }
        else{
            if(_isRetired){
                result = retiredAmount();
            else{
                result = normalPayAmount();
            }
        }
    }
    return result;
 }
```

优化后

```java
double getPayAmount(){
    if(_isDead) 
        return deadAmount();

    if(_isSeparated)
        return separatedAmount();

    if(_isRetired)
        return retiredAmount();

    return normalPayAmount();
}
```



###  尝试Optional

举一个简单的例子

```JAVA
String str = "Hello World!";
if (str != null) {
   System.out.println(str);
} else {
   System.out.println("Null");
}
```

优化后

```java
Optional<String> strOptional = Optional.of("Hello World!");
strOptional.ifPresentOrElse(System.out::println, () -> System.out.println("Null"));
```





## IF ELSE 数量过多

### 表驱动法(IF 判断条件单一)

>  表驱动法是一种编程模式，它的本质是，从表里查询信息来代替逻辑语句(if,case)。



```java
Map<?, Function<?> action> actionsMap = new HashMap<>();

// 初试配置对应动作
actionsMap.put(value1, (someParams) -> { doAction1(someParams)});
actionsMap.put(value2, (someParams) -> { doAction2(someParams)});
actionsMap.put(value3, (someParams) -> { doAction3(someParams)});
 
// 省略 null 判断
actionsMap.get(param).apply(someParams);
```



### 责任链



### 注解驱动

通过 Java 注解（或其它语言的类似机制）定义执行某个方法/类的条件。在程序执行时，通过对比入参与注解中定义的条件是否匹配，再决定是否调用此方法。具体实现时，可以采用表驱动或职责链的方式实现。

### 更复杂: 状态机  / 规则引擎



## 参考

http://cmsblogs.com/?p=2691

https://blog.csdn.net/qq_35440678/article/details/77939999

策略加工厂

https://blog.csdn.net/u011507568/article/details/70238491