# trino-fluentd
This is the Plugin for Trino to send queries into fluentd.

# build
```
mvn clean package
```

# deploy
create event-listener.properties

for example, /pato/to/trino-server/etc/event-listener.properties
```
event-listener.name=trino-fluentd
event-listener.fluentd-host=localhost
event-listener.fluentd-port=24224
event-listener.fluentd-tag=trino.query
```

copy jar (trino-fluentd-0.0.7.jar includes all dependencies)
```
# ls -1 /pato/to/trino-server/plugin/copy-fluentd/
trino-fluentd-0.0.7.jar
```

# reference
- https://trino.io/docs/current/develop/event-listener.html
- https://github.com/wyukawa/presto-fluentd
