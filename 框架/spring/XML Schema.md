###  XML Schema

-------
[w3cschool](http://www.w3school.com.cn/schema/schema_intro.asp)

**XML Schema 是基于 XML 的 DTD 替代者。**

**XML Schema 可描述 XML 文档的结构。**

**XML Schema 语言也可作为 XSD（XML Schema Definition）来引用。**

##### 作用：
- 定义可出现在文档中的元素
- 定义可出现在文档中的属性
- 定义哪个元素是子元素
- 定义子元素的次序
- 定义子元素的数目
- 定义元素是否为空，或者是否可包含文本
- 定义元素和属性的数据类型
- 定义元素和属性的默认值以及固定值

##### 一个简单的 XML 文档：
请看这个名为 "note.xml" 的 XML 文档：
```xml
<?xml version="1.0"?>
<note>
<to>George</to>
<from>John</from>
<heading>Reminder</heading>
<body>Don't forget the meeting!</body>
</note>
```



##### XML Schema

下面这个例子是一个名为 "note.xsd" 的 XML Schema 文件，它定义了上面那个 XML 文档的元素：

```xml
<?xml version="1.0"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
targetNamespace="http://www.w3school.com.cn"
xmlns="http://www.w3school.com.cn"
elementFormDefault="qualified">

<xs:element name="note">
    <xs:complexType>
      <xs:sequence>
	<xs:element name="to" type="xs:string"/>
	<xs:element name="from" type="xs:string"/>
	<xs:element name="heading" type="xs:string"/>
	<xs:element name="body" type="xs:string"/>
      </xs:sequence>
    </xs:complexType>
</xs:element>

</xs:schema>
```

note 元素是一个复合类型，因为它包含其他的子元素。其他元素 (to, from, heading, body) 是简易类型，因为它们没有包含其他元素。



##### 对 XML Schema 的引用

此文件包含对 XML Schema 的引用：

```xml
<?xml version="1.0"?>
<note
xmlns="http://www.w3school.com.cn"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://www.w3school.com.cn note.xsd">

<to>George</to>
<from>John</from>
<heading>Reminder</heading>
<body>Don't forget the meeting!</body>
</note>
```

















.








