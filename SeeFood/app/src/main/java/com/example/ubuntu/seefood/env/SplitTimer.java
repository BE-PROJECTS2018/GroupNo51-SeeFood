/**
 * Created by ubuntu on 5/2/18.
 */

package com.example.ubuntu.seefood.env;

import android.os.SystemClock;

/**
 * A simple utility timer for measuring CPU time and wall-clock splits.
 */
public class SplitTimer {
  private final Logger logger;

  private long lastWallTime;
  private long lastCpuTime;

  public SplitTimer(final String name) {
    logger = new Logger(name);
    newSplit();
  }

  public void newSplit() {
    lastWallTime = SystemClock.uptimeMillis();
    lastCpuTime = SystemClock.currentThreadTimeMillis();
  }

  public void endSplit(final String splitName) {
    final long currWallTime = SystemClock.uptimeMillis();
    final long currCpuTime = SystemClock.currentThreadTimeMillis();

    logger.i(
        "%s: cpu=%dms wall=%dms",
        splitName, currCpuTime - lastCpuTime, currWallTime - lastWallTime);

    lastWallTime = currWallTime;
    lastCpuTime = currCpuTime;
  }
}
