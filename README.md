# Java Maven Demo
Simple Java Maven Project to demonstrate Maven options and possibilities.

## 本地运行

确保本地环境安装了Java Maven 

```bash
$ git clone https://github.com/goodrain/java-maven-demo.git
$ cd java-maven-demo
$ mvn install
$ java $JAVA_OPTS -jar target/dependency/webapp-runner.jar --port $PORT target/*.jar
```
