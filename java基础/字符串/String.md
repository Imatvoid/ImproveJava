### String

String类是不可变的，java设计者认为共享的价值大于提取、拼接。



##### Java字符串的编码

Unicode编码，UTF-16  存储

讲的很好:http://www.cnblogs.com/fnlingnzb-learner/p/6163205.html



##### StringBuilder  和 StringBuffer

- API相同

- 由于 StringBuilder 相较于 StringBuffer 有速度优势，所以多数情况下建议使用 StringBuilder 类。然而在应用程序要求线程安全的情况下，则必须使用 StringBuffer 类。

