# 如何优化IF ELSE



## 为什么要优化

太复杂地嵌套if else 会导致难以阅读,以及扩展性极差,违反了单一职责原则和开闭原则.







## 卫语句 -  漏斗式的代码

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



##  尝试Optional

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





## 参考

http://cmsblogs.com/?p=2691

https://juejin.im/post/5cc6a7fc5188250f015b5843

https://blog.csdn.net/qq_35440678/article/details/77939999

策略加工厂

https://blog.csdn.net/u011507568/article/details/70238491