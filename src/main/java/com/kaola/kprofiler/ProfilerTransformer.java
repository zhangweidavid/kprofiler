package com.kaola.kprofiler;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

import com.kaola.kprofiler.config.ProfFilter;
import com.kaola.kprofiler.javassist.ClassPool;
import com.kaola.kprofiler.javassist.CtClass;
import com.kaola.kprofiler.javassist.CtField;
import com.kaola.kprofiler.javassist.CtMethod;
import com.kaola.kprofiler.javassist.LoaderClassPath;
import com.kaola.kprofiler.javassist.Modifier;
import com.kaola.kprofiler.utils.LogFactory;

/**
 * Created by wei.zw on 2017/7/6.
 */
public class ProfilerTransformer implements ClassFileTransformer {

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer)
			throws IllegalClassFormatException {
		try {
			if (loader == null) {
				return classfileBuffer;
			}
			if (ProfFilter.isNotNeedInject(className)) {
				return classfileBuffer;
			}
			if (ProfFilter.modifiedClass.contains(className)) {
				return classfileBuffer;
			}
			
			ClassPool mPool = new ClassPool(true);
			mPool.appendClassPath(new LoaderClassPath(loader));
			mPool.importPackage("com.kaola.kprofiler");
			CtClass mCtc = mPool.makeClass(new ByteArrayInputStream(classfileBuffer));
			// 不对接口进行增强
			if (mCtc.isInterface() || Modifier.isNative(mCtc.getModifiers()) || mCtc.isAnnotation() || mCtc.isArray()
					|| mCtc.isEnum() || mCtc.isFrozen()) {
				return classfileBuffer;
			}

			// 只监控白名单包
			boolean isStarted = ProfFilter.isNeedProfiler(className);
			if (isStarted) {
				LogFactory.getRunLog().info("[===========开启性能监控统计=========]:" + className);
			}
			Set<String> getterAndSetterMethods = new HashSet<>();
			CtField[] fields = mCtc.getDeclaredFields();
			if (fields != null && fields.length > 0) {
				for (CtField field : fields) {
					String fName = field.getName();

					String t = fName;
					if (fName.length() > 1) {
						t = fName.substring(0, 1).toUpperCase() + fName.substring(1);
					} else {
						t = fName.toUpperCase();
					}
					getterAndSetterMethods.add("set" + t);
					getterAndSetterMethods.add("get" + t);
					if (field.getType() == CtClass.booleanType) {
						getterAndSetterMethods.add("is" + t);
					}
				}
			}
			// 获取类下除了构造方法意外的所有方法
			CtMethod[] methods = mCtc.getDeclaredMethods();
			if (methods != null) {
				// TODO 该处需要读取配置文件对指定对方法进行增强
				for (CtMethod method : methods) {
					if (method.isEmpty()) {
						continue;
					}
					String methodName = method.getName();
					if (methodName.endsWith("main") && method.getModifiers() == 9) {
						continue;
					}
					if (getterAndSetterMethods.contains(methodName)) {
						continue;
					}
					if (isStarted) {
						method.insertBefore("ProfilerUtil.start(\"" + method.getLongName() + "\");");
					} else {
						method.insertBefore("ProfilerUtil.entery(\"" + method.getLongName() + "\");");
					}
					method.insertAfter("ProfilerUtil.release();", true);

				}
			}
			ProfFilter.modifiedClass.add(className);
			byte[] clazz = mCtc.toBytecode();
			mCtc.defrost();
			return clazz;
		} catch (Exception e) {
			LogFactory.getRunLog().error(e);
			return classfileBuffer;

		}
	}

}
