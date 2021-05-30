springCloudDemo

练习 spring cloud 写的一个例子
#### 安装 vue 和element UI
npm install -g @vue/cli
vue create my-app
cd my-app
vue add element-plus

### 过程调用组件 open feign
### 服务注册和发现组件 Eureka
### 熔断组件Hystrix
### 客户端负载均衡组件 Ribbon
### 增加 zuul 
### 打包，需要先加 mvn 的 spring-boot 插件
打 jar 
~~~ 
mvn clean package
~~~
打 fat jar 
~~~
mvn clean package spring-boot:repackage  
~~~