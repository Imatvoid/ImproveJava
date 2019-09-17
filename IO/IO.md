## IO体系介绍



Java IO读写文件的IO流分为两大类，字节流和字符流.

基于字节流的读写基类: InputStream和OutputStream

基于字符流的读写基类: Reader和Writer



![image-20190823160407842](assets/IO/image-20190823160407842.png)



**按操作对象分类结构图**



![image-20190823160516492](assets/IO/image-20190823160516492.png)





## 分类方式

- 按照流的流向分，可以分为输入流和输出流；
- 按照操作单元划分，可以划分为字节流和字符流；
- 按照流的角色划分为节点流和处理流。



## 装饰器模式

java Io流共涉及40多个类，这些类看上去很杂乱，但实际上很有规则，而且彼此之间存在非常紧密的联系， Java Io流的40多个类都是从如下4个抽象类基类中派生出来的。

- **InputStream/Reader**: 所有的输入流的基类，前者是字节输入流，后者是字符输入流。
- **OutputStream/Writer**: 所有输出流的基类，前者是字节输出流，后者是字符输出流。





## 使用





### 参考

https://blog.csdn.net/yhl_jxy/article/details/79272792





