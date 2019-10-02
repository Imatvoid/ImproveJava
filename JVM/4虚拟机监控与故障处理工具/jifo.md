## jinfo

> Configuration Info for Java

实时的查看和更改虚拟机的各项参数

```
jinfo [option] pid
```

options参数解释：

- -flag <name> 打印指定名称的参数

- -flag [+|-]<name> 打开或关闭参数

- -flag <name>=<value> 设置参数

- -flags 打印所有参数

- -sysprops 打印系统配置

- <no option> 打印上面两个选项

  

**查看JVM参数和系统配置**

```shell
$ jinfo 11666
$ jinfo -flags 11666
$ jinfo -sysprops 11666
```



**查看打印GC日志参数**

```shell
$ jinfo -flag PrintGC 11666 
$ jinfo -flag PrintGCDetails 11666
```

**打开GC日志参数**

```shell
$ jinfo -flag +PrintGC 11666
$ jinfo -flag +PrintGCDetails 11666
```

**关闭GC日志参数**

```shell
$ jinfo -flag -PrintGC 11666
$ jinfo -flag -PrintGCDetails 11666
```







