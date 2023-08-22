# Java Maven Demo
Simple Java Maven Demo Project to demonstrate some Maven options and possibilities.

## 本地运行

确保本地环境安装了Java和Maven 

```bash
$ git clone https://github.com/goodrain/java-maven-demo.git
$ cd java-maven-demo
$ mvn install
$ java $JAVA_OPTS -jar target/dependency/webapp-runner.jar --port $PORT target/*.jar
```
