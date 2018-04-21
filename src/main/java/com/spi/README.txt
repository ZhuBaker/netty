JavaSPI 实际上是“基于接口的编程＋策略模式＋配置文件”组合实现的动态加载机制。具体而言：
1. 定义一组接口， 假设是 autocomplete.PrefixMatcher；
2. 写出接口的一个或多个实现(autocomplete.EffectiveWordMatcher, autocomplete.SimpleWordMatcher)；
3. 在 src/main/resources/ 下建立 /META-INF/services 目录， 新增一个以接口命名的文件 autocomplete.PrefixMatcher, 内容是要应用的实现类(autocomplete.EffectiveWordMatcher 或 autocomplete.SimpleWordMatcher 或两者)；
4. 使用 ServiceLoader 来加载配置文件中指定的实现。

SPI 的应用之一是可替换的插件机制。比如查看 JDBC 数据库驱动包，mysql-connector-java-5.1.18.jar
就有一个 /META-INF/services/java.sql.Driver 里面内容是 com.mysql.jdbc.Driver 。

我们系统里抽象的各个模块，往往有很多不同的实现方案，比如日志模块的方案，xml解析模块、jdbc模块的方案等。
面向的对象的设计里，我们一般推荐模块之间基于接口编程，模块之间不对实现类进行硬编码。一旦代码里涉及具体的实现类，就违反了可拔插的原则，如果需要替换一种实现，就需要修改代码。

为了实现在模块装配的时候能不在程序里动态指明，这就需要一种服务发现机制。java spi就是提供这样的一个机制：
为某个接口寻找服务实现的机制。有点类似IOC的思想，就是将装配的控制权移到程序之外，在模块化设计中这个机制尤其重要。

java spi的具体约定如下 ：
当服务的提供者，提供了服务接口的一种实现之后，在jar包的META-INF/services/目录里同时创建一个以服务接口命名的文件。
该文件里就是实现该服务接口的具体实现类。而当外部程序装配这个模块的时候，就能通过该jar包META-INF/services/里的配置文件找到具体的实现类名，并装载实例化，完成模块的注入。
基于这样一个约定就能很好的找到服务接口的实现类，而不需要再代码里制定。
jdk提供服务实现查找的一个工具类：java.util.ServiceLoader
