package com.bonc.service.sql.mybatis.interceptor;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * debug时打印mybatis执行SQL，参数已拼接。
 * @author Administrator
 *
 */
@Intercepts({
		@Signature(type = Executor.class, method = "update", args = {
				MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = {
				MappedStatement.class, Object.class, RowBounds.class,
			ResultHandler.class }) })
@Component
public class ExecuteSqlLogInterceptor implements Interceptor {
	private static final Logger logger = LoggerFactory.getLogger(ExecuteSqlLogInterceptor.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if(logger.isDebugEnabled() || true) {
			MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
			Object parameter = null;
			if (invocation.getArgs().length > 1) {
				parameter = invocation.getArgs()[1];
			}
			BoundSql boundSql = mappedStatement.getBoundSql(parameter);
			Configuration configuration = mappedStatement.getConfiguration();
			
			String exexcSql = getRuntimeExeSql(configuration, boundSql);
			logger.info("[Execute sql]: "+exexcSql);
		}
		//保存修改和删除操作日志
//		saveDmlSql(exexcSql);
		return invocation.proceed();
	}

	private void saveDmlSql(String exexcSql) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(exexcSql.substring(0, 6).equals("UPDATE") || exexcSql.substring(0, 6).equals("DELETE")){
			Map<String, Object> p = new HashMap<String, Object>();
			String yhbh = "";
			String xm = "";
			String jh = "";
			String ip = "";
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			if(requestAttributes != null){ //登录后不为空,没登录执行定时任务时为空
				HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
				yhbh = (String) request.getSession().getAttribute("yhbh");
				jh = (String) request.getSession().getAttribute("jh");
				xm = (String) request.getSession().getAttribute("xm");
				ip = (String) request.getSession().getAttribute("ip");	
			}
			p.put("yhbh", yhbh);
			p.put("jh", jh);
			p.put("xm", xm);
			p.put("ip", ip);
			p.put("jlbh", UUID.randomUUID().toString().replaceAll("-", ""));
			p.put("cznr", exexcSql.length()>2000?exexcSql.substring(0, 2000):exexcSql);
			p.put("czsj", format.format(new Date()));
		}
	}
 
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
	public ExecuteSqlLogInterceptor() {
		System.out.println("[ExecuteSqlLogInterceptor]");
	}
	@Override
	public void setProperties(Properties props) {
	}
 
	private static String getRuntimeExeSql(Configuration configuration,BoundSql boundSql) {
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
		if (parameterMappings.size() > 0 && parameterObject != null) {
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
				sql = sql.replaceFirst("\\?",getParameterValue(parameterObject));
			} else {
				MetaObject metaObject = configuration.newMetaObject(parameterObject);
				for (ParameterMapping parameterMapping : parameterMappings) {
					String propertyName = parameterMapping.getProperty();
					if (metaObject.hasGetter(propertyName)) {
						Object obj = metaObject.getValue(propertyName);
						sql = sql.replaceFirst("\\?", getParameterValue(obj));
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						Object obj = boundSql.getAdditionalParameter(propertyName);
						sql = sql.replaceFirst("\\?", getParameterValue(obj));
					}
				}
			}
		}
		return sql;
	}
 
	private static String getParameterValue(Object obj) {
		String value = null;
		if (obj instanceof String) {
			value = "'" + obj.toString() + "'";
		} else if (obj instanceof Date) {
			DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			value = "'" + formatter.format(new Date()) + "'";
		} else {
			if (obj != null) {
				value = obj.toString();
			} else {
				value = "";
			}
 
		}
		return value;
	}
}