# java optional

Optional是Java8提供的为了解决null安全问题的一个API。善用Optional可以使我们代码中很多繁琐、丑陋的设计变得十分优雅。





## 对比

### Option之前

```java
public static String getChampionName(Competition comp) throws IllegalArgumentException {
    if (comp != null) {
        CompResult result = comp.getResult();
        if (result != null) {
            User champion = result.getChampion();
            if (champion != null) {
                return champion.getName();
            }
        }
    }
    throw new IllegalArgumentException("The value of param comp isn't available.");
}
```





### Optional 优化

```java
public static String getChampionName(Competition comp) throws IllegalArgumentException {
    return Optional.ofNullable(comp)
            .map(c->c.getResult())
            .map(r->r.getChampion())
            .map(u->u.getName())
            .orElseThrow(()->new IllegalArgumentException("The value of param comp isn't available."));
}
```







## Option源码

```java
// 不希望被继承,修改
public final class Optional<T> {
    private static final Optional<?> EMPTY = new Optional<>();
    private Optional(T value) {
        this.value = Objects.requireNonNull(value);
    }
    public static <T> Optional<T> of(T value) {
        return new Optional<>(value);
    }
    public static <T> Optional<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }
  
    // 判断值是否是空
    public boolean isPresent() {
        return value != null;
    }
    // 不是空则执行一个Consumer函数
    public void ifPresent(Consumer<? super T> consumer) {
        if (value != null)
            consumer.accept(value);
    }
    // 优先使用
    public T orElseGet(Supplier<? extends T> other) {
        return value != null ? value : other.get();
    }
    // 当实例已经存在,可以直接返回的时候,考虑使用
    public T orElse(T other) {
        return value != null ? value : other;
    }
  
  
}
```





### orElse 和 orElse

结论:优先使用orElseGet,除非已经存在可以直接返回的默认对象.

```java
Optional<Foo> opt = ...
// 无视 opt.ifPresent,始终调用new Foo()
Foo x = opt.orElse( new Foo() );
// 只有 opt.ifPresent返回不存在才调用,本质是个supplier函数
Foo y = opt.orElseGet( Foo::new );
```

 `opt` doesn't contain a value, the two are indeed equivalent. But if `opt` **does** contain a value, how many `Foo` objects will be created?

*P.s.: of course in this example the difference probably wouldn't be measurable, but if you have to obtain your default value from a remote web service for example, or from a database, it suddenly becomes very important.*

https://stackoverflow.com/questions/33170109/difference-between-optional-orelse-and-optional-orelseget