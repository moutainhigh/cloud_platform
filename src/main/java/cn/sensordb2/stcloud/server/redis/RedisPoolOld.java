package cn.sensordb2.stcloud.server.redis;

import org.apache.log4j.Logger;

import cn.sensordb2.stcloud.util.Tools;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisPoolOld {
	public static Logger logger = Logger.getLogger(RedisPoolOld.class);

	private static RedisPoolOld instance;
	// Redis������IP
	private static String SERVER = RedisIniUtil.getInstance().getServer();

	// Redis�Ķ˿ں�
	private static int PORT = RedisIniUtil.getInstance().getPort();

	// ��������
	private static String PASSWORD = RedisIniUtil.getInstance().getPassword();

	// ��������ʵ���������Ŀ��Ĭ��ֵΪ8��
	// �����ֵΪ-1�����ʾ�����ƣ����pool�Ѿ�������maxActive��jedisʵ�������ʱpool��״̬Ϊexhausted(�ľ�)��
	private static int MAX_TOTAL = 20;

	// ����һ��pool����ж��ٸ�״̬Ϊidle(���е�)��jedisʵ����Ĭ��ֵҲ��8��
	private static int MAX_IDLE = 8;

	// �ȴ��������ӵ����ʱ�䣬��λ���룬Ĭ��ֵΪ-1����ʾ������ʱ����������ȴ�ʱ�䣬��ֱ���׳�JedisConnectionException��
	private static int MAX_WAIT = 10000;

	private static int TIMEOUT = 10000;

	// ��borrowһ��jedisʵ��ʱ���Ƿ���ǰ����validate���������Ϊtrue����õ���jedisʵ�����ǿ��õģ�
	private static boolean TEST_ON_BORROW = true;

	private static JedisPool jedisPool = null;
	
	private RedisPoolOld() {
		
	}
	
	public static void initInstance() {
		if(!RedisIniUtil.getInstance().isRedisTurnOn()) return;
		if(instance==null) {
			instance =  new RedisPoolOld();
			instance.init();
		}
	}

	public static RedisPoolOld getInstance() {
		if(!RedisIniUtil.getInstance().isRedisTurnOn()) return null;

		if(instance==null) {
			instance =  new RedisPoolOld();
			instance.init();
		}
		return RedisPoolOld.instance;
	}

	/**
	 * ��ʼ��Redis���ӳ�
	 */
	public void init() {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(MAX_TOTAL);
			config.setMaxIdle(MAX_IDLE);
			config.setMaxWaitMillis(MAX_WAIT);
			config.setTestOnBorrow(TEST_ON_BORROW);
			jedisPool = new JedisPool(config, SERVER, PORT, TIMEOUT, PASSWORD);
			logger.info("Redis pool is inited successfully");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Tools.getTrace(e));
		}
	}

	/**
	 * ��ȡJedisʵ��
	 * 
	 * @return
	 */
	public synchronized Jedis getJedis() {
		try {
			if (jedisPool != null) {
				Jedis resource = jedisPool.getResource();
				return resource;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Tools.getTrace(e));
			return null;
		}
	}

	/**
	 * �ͷ�jedis��Դ
	 * 
	 * @param jedis
	 */
	public void returnResource(final Jedis jedis) {
		if (jedis != null) {
			jedisPool.returnResource(jedis);
		}
	}
}
