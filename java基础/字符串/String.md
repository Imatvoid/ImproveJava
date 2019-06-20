# String

String类是不可变的，java设计者认为共享的价值大于提取、拼接。



## Java字符串的编码

Unicode编码，UTF-16  存储

讲的很好:http://www.cnblogs.com/fnlingnzb-learner/p/6163205.html

https://www.jianshu.com/p/50107efaac30

 java不同编码之间进行转换，都需要使用unicode作为中转。

http://www.lianhekj.com/question/496912925852330444.html

## StringBuilder  和 StringBuffer

- API相同

- 由于 StringBuilder 相较于 StringBuffer 有速度优势，所以多数情况下建议使用 StringBuilder 类。然而在应用程序要求线程安全的情况下，则必须使用 StringBuffer 类。

## 面试问题

### 理解Java的字符串，String、StringBuffer、StringBuilder有什么区别？

- String是Java语言非常基础和重要的类，提供了构造和管理字符串的各种基本逻辑。它是典型的Immutable类，被声明成为final class，所有属性也都是final的。也由于它的不可变性，类似拼接、裁剪字符串等动作，都会产生新的String对象。由于字符串操作的普遍性，所以相关操作的效率往往对应用性能有明显影响。

- StringBuffer是为解决上面提到拼接产生太多中间对象的问题而提供的一个类，我们可以用append或者add方法，把字符串添加到已有序列的末尾或者指定位置。StringBuffer本质是一个线程安全的可修改字符序列，它保证了线程安全，也随之带来了额外的性能开销，所以除非有线程安全的需要，不然还是推荐使用它的后继者，也就是StringBuilder。

- StringBuilder是Java 1.5中新增的，在能力上和StringBuffer没有本质区别，但是它去掉了线程安全的部分，有效减小了开销，是绝大部分情况下进行字符串拼接的首选。