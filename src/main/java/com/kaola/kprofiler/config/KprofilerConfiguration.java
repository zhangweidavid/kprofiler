package com.kaola.kprofiler.config;

import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import com.kaola.kprofiler.utils.StringUtils;

/**
 * Kprofiler 配置对象
 */
public class KprofilerConfiguration {

	/**
	 * 是否开启debug模式
	 */
	private boolean debugModel = true;

	/**
	 * 配置文件名
	 */
	private static String CONFIG_FILE_NAME = "profile.properties";

	/**
	 * 默认的配置文件路径，~/.kprofiler/profile.properties
	 */
	private static File DEFAULT_CONFIG_FILE = new File(
			System.getProperty("user.home") + File.separator + ".kprofiler" + File.separator + CONFIG_FILE_NAME);

	private static String user_home = StringUtils.defaultIfBlank(System.getProperty("user.home"), "/tmp");

	private String logHome;

	private static int ignoreThreshold = 2;

	private boolean ignoreGetterAndSetter = true;

	private Set<String> profilerPackage = new HashSet<>();

	private static Set<String> excludePackage = new HashSet<>();

	private static Set<String> excludeMethod = new HashSet<>();
	static {
		// 默认不注入的Package
		excludePackage.add("java");
		excludePackage.add("sun");// 包含sunw
		excludePackage.add("com/sun/");
		excludePackage.add("org/");//
		excludePackage.add("javassist/");
		// 不注入profile本身
		excludePackage.add("com/kaola/kprofiler/");
		excludePackage.add("com/taobao/");
		excludePackage.add("com/alibaba/");
		excludePackage.add("io/");
		excludePackage.add("ch/qos/logback/");
		excludePackage.add("junit/");
		excludePackage.add("oracle/");
		excludePackage.add("com/google/");
		excludePackage.add("net/sf/");
		excludePackage.add("com/caucho/hessian/");
		excludePackage.add("com/netease/kaola/kschedule");
		excludePackage.add("com/netease/sentry/");
		excludePackage.add("com/netease/soa/trace");
		excludePackage.add("com/baidu/disconf/");
		excludePackage.add("com/netease/napm/");
		excludePackage.add("com/netease/haitao/framework/");
		excludePackage.add("redis/clients/");
		excludePackage.add("rx/");
	}

	/**
	 * 日志目录
	 */
	private String logName = "kprofiler.log";

	private static KprofilerConfiguration instance = new KprofilerConfiguration();

	public static KprofilerConfiguration getInstance() {
		return instance;
	}

	/**
	 * 
	 */
	private KprofilerConfiguration() {
		File file = findConfigFile();
		if (file.exists() && file.isFile()) {
			Properties properties = loadProperties(file);
			initConfig(properties);
		}

	}

	/**
	 * 查找配置文件
	 * 
	 * @return
	 * @author wei.zw
	 */
	private File findConfigFile() {
		String specified = System.getProperty(CONFIG_FILE_NAME);
		if (specified != null) {
			return new File(specified);
		}
		return DEFAULT_CONFIG_FILE;
	}

	public Properties loadProperties(File file) {
		DelegatingProperties properties = new DelegatingProperties(System.getProperties());
		try {
			properties.load(new FileReader(file)); // 配置文件原始内容，未进行变量替换

		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}

	public boolean getDebugModel() {
		return debugModel;
	}

	public static int getIgnoreThreshold() {
		return ignoreThreshold;
	}

	private void initConfig(Properties properties) {
		String debugModel = properties.getProperty("profiler.debug");
		String logName = properties.getProperty("profiler.logName");
		String logHome = properties.getProperty("profiler.logHome");
		String ignoreThreshold = properties.getProperty("profiler.ignoreThreshold");
		String profilePackageStr = properties.getProperty("profiler.profilerPackage");
		String ignoreSetterAndGetter = properties.getProperty("profiler.ignore.getterAndSetter");
		if (!StringUtils.isBlank(profilePackageStr)) {
			String[] packages = profilePackageStr.split(";");
			for (String pkg : packages) {
				profilerPackage.add(pkg);
			}
		}

		String excludePackageStr = properties.getProperty("profiler.excludePackage");
		if (!StringUtils.isBlank(excludePackageStr)) {
			String[] packages = excludePackageStr.split(";");
			for (String pkg : packages) {
				excludePackage.add(pkg);
			}
		}
		setIgnoreThreshold(Integer.valueOf(StringUtils.defaultIfBlank(ignoreThreshold, "2")));
		setIgnoreGetterAndSetter("true".equals(ignoreSetterAndGetter));
		setDebugModel("true".equals(debugModel));
		setLogName(StringUtils.defaultIfBlank(logName, "kprofiler.log"));
		setLogHome(StringUtils.defaultIfBlank(logHome, user_home + "/kprofiler/"));
	}

	public String getLogHome() {
		return logHome;
	}

	public void setLogHome(String logHome) {
		this.logHome = logHome;
	}

	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public void setDebugModel(boolean debugModel) {
		this.debugModel = debugModel;
	}

	public static String getUser_home() {
		return user_home;
	}

	public boolean isIgnoreGetterAndSetter() {
		return ignoreGetterAndSetter;
	}

	public void setIgnoreGetterAndSetter(boolean ignoreGetterAndSetter) {
		this.ignoreGetterAndSetter = ignoreGetterAndSetter;
	}

	public Set<String> getProfilerPackage() {
		return profilerPackage;
	}

	public void setProfilerPackage(Set<String> profilerPackage) {
		this.profilerPackage = profilerPackage;
	}

	public static Set<String> getExcludePackage() {
		return excludePackage;
	}

	public void setExcludePackage(Set<String> excludePackage) {
		KprofilerConfiguration.excludePackage = excludePackage;
	}

	public Set<String> getExcludeMethod() {
		return excludeMethod;
	}

	public static void setExcludeMethod(Set<String> excludeMethod) {
		KprofilerConfiguration.excludeMethod = excludeMethod;
	}

	public static void setIgnoreThreshold(int ignoreThreshold) {
		KprofilerConfiguration.ignoreThreshold = ignoreThreshold;
	}

}
