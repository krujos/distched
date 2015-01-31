#Example of application timers in a scalable CloudFoundry app. 

Cloud Foundry has no external scheduler (e.g. cron). This means you have to rely on an external service, or write it yourself and, if you want to run more than one instance figure out how to do it in a way that scales horizontally. This project is an experiment in just that. I used the Redis service provided by PWS and Spring Scheduling to kick off my tasks and make sure that only one instance picks up a task at at a time.

I implement the distributed lock pattern outlined on the [Redis](http://redis.io/topics/distlock) page for a single instance of redis. See the "Correct implementation with a single instance" section.  

##Getting started

```
   git clone https://github.com/krujos/distsched
   cd distsched
```

This project uses the [Cloud Foundry Maven Plugin](https://github.com/cloudfoundry/cf-java-client/tree/master/cloudfoundry-maven-plugin) in favor of a manifest. You'll need to change it to match your org and space if you want to use it, as you probably don't have permission to mine. 

```   
   mvn cf:push   
```

##What to expect when you're not expecting
The code is setup to introduce lock contention and force different instances to fail acquiring keys. You should see output that looks something like what's below, which more or less proves that while all instances are scheduling, only one instance actually runts the task. 

```
2015-01-31T11:30:46.00-0600 [App/1]   ERR tock
2015-01-31T11:30:46.00-0600 [App/0]   ERR 0 did not aquire tock_key
2015-01-31T11:30:46.00-0600 [App/3]   ERR 3 did not aquire tock_key
2015-01-31T11:30:46.31-0600 [App/0]   ERR 0 did not aquire tick_key
2015-01-31T11:30:46.51-0600 [App/2]   ERR 2 did not aquire tock_key
2015-01-31T11:30:46.98-0600 [App/3]   ERR tick
2015-01-31T11:30:47.00-0600 [App/0]   ERR 0 did not aquire tock_key
2015-01-31T11:30:47.00-0600 [App/1]   ERR tock
2015-01-31T11:30:47.00-0600 [App/2]   ERR 2 did not aquire tock_key
```
