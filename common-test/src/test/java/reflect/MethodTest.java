package reflect;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 * @date 2020年4月28日
 * DESC: 
 */
public class MethodTest {
	private static final Logger LOG = LoggerFactory.getLogger(MethodTest.class);

	@Test
	public void testMethodName() {
		Method method = MethodTest.class.getMethods()[0];
		String methodName = method.getName();
		LOG.info(method.getDeclaringClass().getTypeName());

		LOG.info(methodName);
	}
}
