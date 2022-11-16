package kimble.automation.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.TreeMap;

import kimble.automation.domain.mobile.general.Functions;
import kimble.automation.Config;

public class Cache {
	
	static Cache cache;
	static File cacheFile;
	
	static {
		cacheFile = new File("configCache.json");
		if(cacheFile.exists()) {
			try {
				cache = Functions.deserialize(new FileInputStream(cacheFile), Cache.class);
			} catch (Exception e) { throw new RuntimeException("failed to deserialise the cache file", e); }
		}
		else {
			cache = new Cache();
			cache.cacheMap = new TreeMap();
			save();
		}
	}
	
	public static void save() {
		try {
			Functions.serialize(cache, new FileOutputStream(cacheFile));
		} catch (Exception e) { throw new RuntimeException("failed to serialise the cache file", e); }
	}

	public static Object get(Object key) { return cache.cacheMap.get(key); }
	public static void set(Object key, Object value) { cache.cacheMap.put(key, value); save(); }
	
	public static Config getConfig() { return cache.config; }
	public static void setConfig(Config aConfig) { cache.config = aConfig; save(); }
	
	public TreeMap cacheMap;
	public Config config;
	
}
