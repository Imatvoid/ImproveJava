

### Bean之间的关系

##### 父子Bean(继承)

```xml
  <!-- 父子<bean> -->
	<bean id="abstractCar" class="com.smart.tagdepend.Car"
	      p:brand="红旗CA72" p:price="2000.00" p:color="黑色"
	      abstract="true"/>
	      
	<bean id="car3" parent="abstractCar">
		<property name="color" value="红色"/>
	</bean>
	<bean id="car4" parent="abstractCar" >
		<property name="color" value="白色"/>
	</bean>		
```



##### 依赖（ref  depends-on）

1. 显式依赖

   <ref> 建立对其他元素的依赖关系，spring负责管理这些关系，保证当实例化这个bean时，所以来的bean已经初始化。 

2. 隐式依赖

   程序功能、逻辑上的先后顺序（前置依赖）

   ```xml
   <!-- <bean>的信赖 -->
   	<bean id="cacheManager" class="com.smart.tagdepend.CacheManager" depends-on="sysInit" />
   	<bean id="sysInit" class="com.smart.tagdepend.SysInit" />
   ```

   depends-ons属性可以指定前置依赖的bean，多个bean可以空格或者逗号分割。

##### 引用（idref）

引用id属性值

```xml
  <!-- <bean>引用 -->
    <bean id="car" class="com.smart.tagdepend.Car"/>
    <bean id="boss" class="com.smart.tagdepend.Boss" >
       <property name="carId"  >
          <!--   <value>car</value> -->
           <idref bean="car"/>
       </property>
    </bean>
```



























































.