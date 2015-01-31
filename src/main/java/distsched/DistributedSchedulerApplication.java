package distsched;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
public class DistributedSchedulerApplication {

	@Value("#{environment.CF_INSTANCE_INDEX}")
	private String cfInstanceIndex;

	private Jedis jedis = null;

	@Autowired
	private JedisPool jedisPool;

	/**
	 * This function implements a redis distributed lock. It uses the default
	 * timeouts with setnx. It's detailed in the section
	 * "Correct implementation with a single instance" on the page below.
	 * 
	 * This implementation dosen't get  concerned with clock skew, which can be an 
	 * issue for tasks that have very short intervals (i.e. the interval is shorter 
	 * than the possible skew) that can be addressed with a vector clock and / or 
	 * a key naming stragey that is more sophisticated than what I have here. 
	 * 
	 * @see <a
	 *      href="http://redis.io/topics/distlock">http://redis.io/topics/distlock</a>
	 * 
	 * @param key
	 *            to lock
	 * @param message
	 *            to print
	 */
	private void lockAndPrint(String key, String message) {
		if (null == jedis) {
			jedis = jedisPool.getResource();
		}
		if (1 == jedis.setnx(key, cfInstanceIndex)) {
			System.err.println(message);
			try {
				Thread.sleep(750); // introduce some lock contention
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (cfInstanceIndex.equals(jedis.get(key))) {
				jedis.del(key);
			}
		} else {
			System.err.println(cfInstanceIndex + " did not aquire " + key);
		}
	}

	@Scheduled(fixedDelay = 2000)
	private void tick() {
		lockAndPrint("tick_key", "tick");
	}

	@Scheduled(cron = "* * * * * *")
	private void tock() {
		lockAndPrint("tock_key", "tock");
	}

	public static void main(String[] args) {
		SpringApplication.run(DistributedSchedulerApplication.class, args);
	}
}
