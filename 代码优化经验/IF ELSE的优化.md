# 如何优化IF ELSE

<!-- more -->

## 为什么要优化

太复杂地嵌套if else 会导致难以阅读,以及扩展性极差,违反了单一职责原则和开闭原则.


## IF ELSE 嵌套过深的问题



### 适当地抽取方法

比如下边这个[例子](https://refactoring.com/catalog/extractFunction.html)


一个方法不该做太多事情,它应当专注于自己.极端情况下就像是函数式编程的纯函数.

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

整个方法就像是漏斗一样,只有真正满足条件的请求,能够走下去.逻辑清晰了很多

###  尝试JDK8的Optional

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

这里的例子比较简单,你可能看不出Optional的威力.

再来个复杂的.

```java
public class Test {


    public static void main(String[] args) {

        // creepy initialization step, dont worry
        Employee employee = new Employee();

        // 优化前
        if(employee != null){
            Salary salary = employee.getSalary();
            if(salary !=null){
                Unit unit = salary.getUnit();
                if(unit != null){
                    System.out.println("I discovered the variable finally " + unit);
                }
            }
        }
        // 优化后
        Optional.ofNullable(employee)
                .map(Employee::getSalary)
                .map(Salary::getUnit)
                .map(Unit::getPrecision)
                .ifPresent(e -> System.out.println("I discovered the variable finally " + e));


    }


    static class Employee {

        Salary salary;

        public Salary getSalary() {
            return salary;
        }

        public void setSalary(Salary salary) {
            this.salary = salary;
        }
    }

    class Salary {
        // 单位
        Unit unit;

        public Unit getUnit() {
            return unit;
        }

        public void setUnit(Unit unit) {
            this.unit = unit;
        }
    }

    class Unit {
        // 精度
        Integer precision;

        public Integer getPrecision() {
            return precision;
        }

        public void setPrecision(Integer precision) {
            this.precision = precision;
        }
    }

}
```





## IF ELSE 数量过多的问题

### 表驱动法(IF 判断条件单一)

>  表驱动法是一种编程模式，它的本质是，从表里查询信息来代替逻辑语句(if,case)。

举个简单的例子
```java
/**
 * 表驱动法
 */
public class TableDrivenApproach {
    //private static Map<?, Function<?,?> > actionsMap =new HashMap<>();
    // private static Map<Integer, Function<Integer, Integer>> actionsMap = new HashMap<>();
    private static HashMap<Integer, Function<Integer, Integer>> actionsMap = new HashMap<>();

    static {
        actionsMap.put(1, e -> e + 1);
        actionsMap.put(2, e -> e + 2);
        actionsMap.put(3, e -> e + 3);
    }

    public static void main(String[] args) {
        System.out.println(actionsMap.get(1).apply(0));
        System.out.println(actionsMap.get(2).apply(0));
        System.out.println(actionsMap.get(3).apply(0));

    }
}
```



### 责任链

if else 的本质是分支判断,找到一个符合条件的分支然后执行其内部逻辑.   
那么我们其实可以把一个一个分支拆成handler,使用责任链模式.   

```java
 static abstract class Handler {

        /**
         * 持有后继的责任对象
         */
        protected Handler successor;

        /**
         * 示意处理请求的方法，虽然这个示意方法是没有传入参数的
         * 但实际是可以传入参数的，根据具体需要来选择是否传递参数
         */
        public abstract void handleRequest();

        /**
         * 取值方法
         */
        public Handler getSuccessor() {
            return successor;
        }

        /**
         * 赋值方法，设置后继的责任对象
         */
        public void setSuccessor(Handler successor) {
            this.successor = successor;
        }

    }

    static class ConcreteHandler extends Handler {
        /**
         * 处理方法，调用此方法处理请求
         */
        @Override
        public void handleRequest() {
            /**
             * 判断是否有后继的责任对象
             * 如果有，就转发请求给后继的责任对象
             * 如果没有，则处理请求
             */
            if (getSuccessor() != null) {
                System.out.println("放过请求");
                getSuccessor().handleRequest();
            } else {
                System.out.println("处理请求");
            }
        }

    }
```


### 注解驱动

通过 Java 注解（或其它语言的类似机制）定义执行某个方法/类的条件。       
在程序执行时，通过对比入参与注解中定义的条件是否匹配，再决定是否调用此方法。具体实现时，可以采用表驱动或职责链的方式实现。

### 更复杂: 状态机  / 规则引擎

可以给个轻量级的规则引擎的例子.
[easyrules](https://github.com/j-easy/easy-rules)


## 参考

http://cmsblogs.com/?p=2691

https://blog.csdn.net/qq_35440678/article/details/77939999

策略加工厂  

https://blog.csdn.net/u011507568/article/details/70238491