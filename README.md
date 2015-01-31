#Example of application timers in a scalable CloudFoundry app. 

Cloud Foundry has no external scheduler (e.g. cron). This means you have to rely on an external service, or write it yourself and, if you want to run more than one instance figure out how to do it in a way that scales horizontally. This project is an experiment in just that. I used the Redis service provided by PWS and Spring Scheduling to kick off my tasks.

#Getting started

```
   git clone https://github.com/krujos/distsched
   cd distsched
```

This project uses the [Cloud Foundry Maven Plugin](https://github.com/cloudfoundry/cf-java-client/tree/master/cloudfoundry-maven-plugin) in favor of a manifest. You'll need to change it to match your org and space if you want to use it, as you probably don't have permission to mine. 

```   
   mvn cf:push   
```