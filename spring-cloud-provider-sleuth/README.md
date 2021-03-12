sleuth + zipkin

zipkin的jar包下载地址：https://search.maven.org/remote_content?g=io.zipkin&a=zipkin-server&v=LATEST&c=exec
启动命令：java -jar zipkin-server-2.23.2.jar
访问地址：http://localhost:9411

zipkin + elasticsearch使用步骤：
1、启动elasticsearch
2、启动zipkin：java -jar zipkin-server-2.23.2.jar --STORAGE_TYPE=elasticsearch --ES_HOSTS=localhost:9200


1、默认情况下，Zipkin Server 都会将跟踪信息存储在内存中，每次重启 Zipkin Server 都会使之前收集的跟踪信息丢失
2、zipkin也支持将跟踪信息存储到Mysql、ElasticSearch等
3、也支持sleuth + logback + ELK(即在logback中配置logstash，logstash将收集到的日志信息推送给elasticsearch，最终由Kibana展示)