package distsched.config;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.JedisPool;

@Configuration
@Profile("cloud")
public class RedisCloudConfig extends AbstractCloudConfig {

	@Bean(destroyMethod = "destroy")
	public JedisPool newJedisPool() {
		JedisConnectionFactory factory = (JedisConnectionFactory) this
				.connectionFactory().redisConnectionFactory();
		JedisPool pool = new JedisPool(factory.getPoolConfig(),
				factory.getHostName(), factory.getPort(), factory.getTimeout(),
				factory.getPassword(), factory.getDatabase());
		return pool;
	}
}