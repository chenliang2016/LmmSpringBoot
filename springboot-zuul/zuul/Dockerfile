FROM java:8-jdk-alpine

COPY ./target/zuul-0.0.1.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch zuul-0.0.1.jar'

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENTRYPOINT ["java", "-jar", "-Dcsp.sentinel.app.type=1", "zuul-0.0.1.jar"]