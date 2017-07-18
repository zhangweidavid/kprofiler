# kprofiler
对方法调用栈中各个阶段耗时进行统计

## 配置文件
 默认配置文件为 ${user.home}/.kprofiler/profile.properties
 
 profiler.logName 用于指定性能日志名称 默认值为kprofiler.log
 
 profiler.logHome 用户指定性能日志目录 默认值为${user.home}/kprofiler/
 
 profiler.profilerPackage 用于指定性能日志统计的开始入口包，只有访问该包下的方法才会输出性能日志
 
 profiler.excludePackage  用于指定排出性能日志统计的包，对该包下的方法不进行增强
 
 ## 日志格式说明
