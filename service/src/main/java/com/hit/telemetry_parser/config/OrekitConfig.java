package com.hit.telemetry_parser.config;

import lombok.extern.slf4j.Slf4j;
import org.orekit.data.DataContext;
import org.orekit.data.DirectoryCrawler;

import java.io.File;
import java.util.Locale;

/**
 * Orekit模块初始化配置
 *
 * @author Yang_Haiyue
 * @author 卫星技术研究所_哈尔滨工业大学
 * @since 2023/3/27 18:39
 */
@Slf4j
public class OrekitConfig {

    public static void init() {
        log.info("Aerodynamics component - starting...");
        // configure Orekit
        File home = new File("src/main/resources/lib/");
        File orekitData = new File(home, "orekit-data-plus");
        if (!orekitData.exists()) {
            System.err.format(Locale.US, "Failed to find %s folder%n",
                    orekitData.getAbsolutePath());
            System.err.format(Locale.US, "You need to download %s from the %s page and unzip it in %s for this tutorial to work%n",
                    "orekit-data.zip", "https://www.orekit.org/forge/projects/orekit/files",
                    home.getAbsolutePath());
            System.exit(1);
        }
        DataContext.getDefault().getDataProvidersManager().addProvider(new DirectoryCrawler(orekitData));
        log.info("Aerodynamics component - start completed.");
    }
}
