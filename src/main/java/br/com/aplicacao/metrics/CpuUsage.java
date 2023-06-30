package br.com.aplicacao.metrics;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class CpuUsage {

    private static OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

    public static double getCPULoad() {
        double cpuLoad = 0.0;

        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;
            cpuLoad = sunOsBean.getProcessCpuLoad() * 100.0;
        }

        return cpuLoad;
    }

    public static void main(String[] args) {
        double cpuUsage = getCPULoad();
        System.out.println("CPU Usage: " + cpuUsage + "%");
    }
}
