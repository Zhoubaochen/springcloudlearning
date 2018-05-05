package hello.endpoints.metrics;

import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
/**
 * 参考SystemPublicMetrics
 * @author Administrator
 *
 */
public class MySystemPublicMetrics implements PublicMetrics {
	private long timestamp;

	public MySystemPublicMetrics() {
		this.timestamp = System.currentTimeMillis();
	}
	@Override
	public Collection<Metric<?>> metrics() {
		Collection<Metric<?>> result = new LinkedHashSet<Metric<?>>();
		Runtime runtime = Runtime.getRuntime();
		result.add(newMemoryMetric("mem",
				runtime.totalMemory() + getTotalNonHeapMemoryIfPossible()));
		result.add(newMemoryMetric("mem.free", runtime.freeMemory()));
		result.add(new Metric<Integer>("processors", runtime.availableProcessors()));
		result.add(new Metric<Long>("instance.uptime",
				System.currentTimeMillis() - this.timestamp));
		return result;
	}
	private Metric<Long> newMemoryMetric(String name, long bytes) {
		return new Metric<Long>(name, bytes / 1024);
	}
	private long getTotalNonHeapMemoryIfPossible() {
		try {
			return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();
		}
		catch (Throwable ex) {
			return 0;
		}
	}
}
