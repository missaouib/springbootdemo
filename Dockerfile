FROM adoptopenjdk/openjdk11:jdk-11.0.8_10-alpine
VOLUME /tmp
ADD target/*.jar app.jar

#设置alpine系统时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/${TZ} /etc/localtime && echo ${TZ} > /etc/timezone

EXPOSE 8088
EXPOSE 9005
ENTRYPOINT [ "java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9005", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar" ]
HEALTHCHECK CMD --interval=30s --timeout=3s curl -f http://localhost:8088/health || exit 1

#transport 用于在调试程序和VM使用的进程之间通讯
#dt_socket 套接字传输
#server=y/n VM是否需要作为调试服务器执行
#address=7899 调试服务器监听的端口号
#suspend=y/n 是否在调试客户端建立连接之后启动 VM