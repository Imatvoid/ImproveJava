> 虚拟机进程状况工具

## jps (JVM Process Status Tool)

除了名字像UNIX的ps命令之外,它的功能也和ps命令类似:可以列出正在运行的虚拟机进程,并显示虚拟机执行主类(Main Class,main()函数所在的类)名称以及这些进程的**本地虚拟机唯一ID(Local Virtual Machine Identifier,LVMID)**。虽然功能比较单一,但它是使用频率最高的JDK命令行工具,因为其他的JDK工具大多需要输入它查询到的LVMID来确定要监控的是哪一个虚拟机进程。



 jps可以列出jvm进程lvmid，主类类名，main函数参数, jvm参数，jar名称等信息。

```shell
jps [options] [hostid]
```

- -l   输出应用程序主类完整package名称或jar完整名称
- -p  只输出id
- -m JVM启动时候输给main函数的参数
- -v: 列出jvm参数, -Xms20m -Xmx50m是启动程序指定的jvm参数



```shell
$ jps
12133 Main
31339 NutstoreGUI
16892 Jps
20911 Launcher

$ jps -l
12133 com.intellij.idea.Main
31339 nutstore.client.gui.NutstoreGUI
20911 org.jetbrains.jps.cmdline.Launcher
16943 sun.tools.jps.Jps

```

可以使用jcmd替代

```
17040 sun.tools.jcmd.JCmd
12133 com.intellij.idea.Main
31339 nutstore.client.gui.NutstoreGUI --restart 1 --use-python-tray
20911 org.jetbrains.jps.cmdline.Launcher /home/atvoid/SoftWare/idea-IU-183.5429.30/lib/oro-2.0.8.jar:/home/atvoid/SoftWare/idea-IU-183.5429.30/lib/jna.jar:.......................

```

