package container;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;


public class CollectionOp {
	/**
	 * 测试Stream 排序操作；
	 * Stream中间操作暂不执行，执行终端操作时执行。
	 * Stream是对原数据源的一种查询，不修改源数据。
	 */
	@Test
	public void sortList() {
		Stream<Integer> stream = Arrays.asList(1, 3, 2, 0)
//				.stream()
				.parallelStream()
				.filter(i->{ 
					System.out.println(i);
					return i>0;
					})
				.sorted();
//		System.out.println(stream.isParallel());
		System.out.println("nana");
		System.out.println(stream.collect(Collectors.toList()));
	}
	
	@Test
	public void testListToArray() {
		System.out.println(Arrays.toString(
				Arrays.asList("a","b").toArray(new String[0])
				));
	}
}
