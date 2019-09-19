# android ioc+apt+javapoet demo

**IOC**
[IOC百度百科](https://baike.baidu.com/item/控制反转/1158025?fromtitle=ioc&fromid=4853&fr=aladdin)
控制反转（Inversion of Control，缩写为IoC），是面向对象编程中的一种设计原则，可以用来减低计算机代码之间的耦合度。其中最常见的方式叫做依赖注入（Dependency Injection，简称DI），还有一种方式叫“依赖查找”（Dependency Lookup）。通过控制反转，对象在被创建的时候，由一个调控系统内所有对象的外界实体将其所依赖的对象的引用传递给它。也可以说，依赖被注入到对象中。

**Android APT**
[Android APT简书介绍](https://www.jianshu.com/p/7af58e8e3e18)
APT(Annotation Processing Tool)即注解处理器，是一种处理注解的工具，确切的说它是javac的一个工具，它用来在编译时扫描和处理注解。注解处理器以Java代码(或者编译过的字节码)作为输入，生成.java文件作为输出。
简单来说就是在编译期，通过注解生成.java文件。

**JavaPoet**
[JavaPoet简书介绍](https://www.jianshu.com/p/fba2eec47976)
JavaPoet是square推出的开源java代码生成框架，提供Java Api生成.java源文件。这个框架功能非常有用，我们可以很方便的使用它根据注解、数据库模式、协议格式等来对应生成代码。通过这种自动化生成代码的方式，可以让我们用更加简洁优雅的方式要替代繁琐冗杂的重复工作。


###### 实现功能

```
1.Activity中的Widget成员变量加入BindView注解，在apt生成的 Activity_ViewBinding中即可实现对widget成员变量值的注入
2.Activity中的Method中加入OnClick/OnLongClick/OnTouch注解即可实现widget对应事件的注入
```

该demo供android开发者初学IOC+APT+JavaPoet参考

附：apt代码调试方法
1.配置remote configuration。
    Android Studio中配置configuration。添加Remote类型Configuration。其中

```
Name:APT
Command line arguments for remote jvm:
-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
```

2.配置项目gradle.properties文件。添加如下两行

```
org.gradle.daemon=true
org.gradle.jvmargs= -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
```

3.运行调试
    a.选择选择第一步创建的 configuration APT，然后点击debug按钮。
        如果一切正常的话，在Console窗口会输出如下内容，表示远程进程已经启动了

```
Connected to the target VM, address: 'localhost:5005', transport: 'socket'
```

​    b.在processor代码中要调试的地方打上断点，然后切换到configuration app ，然后点击debug按钮即可
​    c.注意，只有被注解标记的类内容有改变时注解处理器才会执行，所以如果发现断点没有中断，记得改一下源代码。
​    d.如果还未进入断点，可删除build目录重新执行b



注：该方式会在编译时生成中间类(Activity_ViewBinding)，避免了IOC的反射带来的性能开销。当然辅助类仍然会用反射，不过只使用一次对性能的影响可忽略。重点是提高了生产力。    



执行流程分析：

![生成中间类流程](/Users/wangzhenzhou/demo/ioc_apt_javapoet_demo/image/生成中间类流程.jpg)





JavaPoet一张图告诉你用法

![javapoet](/Users/wangzhenzhou/demo/ioc_apt_javapoet_demo/image/javapoet.png)