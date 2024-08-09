package andrelsf.com.github.msaccounts.configs;

import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MDCInterceptor implements HandlerInterceptor {

  private static final Logger log = LoggerFactory.getLogger(MDCInterceptor.class);
  private final ThreadLocal<StopWatch> stopWatch = ThreadLocal.withInitial(StopWatch::create);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object body) {
    log.info("Received request: {} {}", request.getMethod(), request.getRequestURI());
    startWatch();
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    var watch = stopWatch();
    long endTime = watch.getTime(TimeUnit.MILLISECONDS);
    log.info("Finished request: {} {}. Time spent: {}ms", request.getMethod(), request.getRequestURI(), endTime);
    watch.reset();
  }

  private void startWatch() {
    StopWatch watch = stopWatch.get();
    if (watch.isStopped()) {
      watch.start();
    }
  }

  @PreDestroy
  void onDestroy() {
    stopWatch.remove();
  }

  private StopWatch stopWatch() {
    StopWatch watch = stopWatch.get();
    if (watch.isStarted()) {
      watch.stop();
    }
    return watch;
  }
}
