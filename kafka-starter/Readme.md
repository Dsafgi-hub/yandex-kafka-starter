Проект для автоконфигурации подключения к Kafka

Для работы с этим проектом, необходимо добавить в свой проект как зависимость
```xml
<dependency>
    <groupId>ru.dev.bachinin</groupId>
    <artifactId>kafka-starter</artifactId>
    <version>${kafka-starter.version}</version>
</dependency>
```

Общие настройки для работы проекта, которые необходимо добавить в файл *`application.yml`
```yaml
kafka:
  consumer:
    topic: example-topic
    group-id: group
    bootstrap-servers: localhost:29092
  producer:
    topic: example-topic
    bootstrap-servers: localhost:29092
```

Блок *`kafka` включает в себя блоки для работы как с потребителями (блок *`consumer`), 
так и продьюсерами (блок *`producer`). 
Каждый блок имеет следующие настройки:
*`bootstrap-servers` - список серверов брокера для подключения
*`topic` - топик на сервере брокера для подключения
*`group-id` - группа на сервере брокера для подключения
