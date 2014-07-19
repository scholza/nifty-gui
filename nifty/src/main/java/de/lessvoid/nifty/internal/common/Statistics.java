package de.lessvoid.nifty.internal.common;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import de.lessvoid.nifty.api.NiftyStatistics.FrameInfo;

public class Statistics {
  private static final int TIME_HISTORY = 10;

  private int frameCounter = 0;
  private final Queue<FrameInfo> frameHistory = new LinkedBlockingQueue<FrameInfo>(TIME_HISTORY);
  private final long[] times = new long[Type.values().length];

  private enum Type {
    /**
     * Time spent synchronizing the Nifty scene graph with the render node scene graph.
     */
    Synchronize,
    Update,
    Render,
    RenderBatchCount
  }

  public void startFrame() {
    for (Type type : Type.values()) {
      times[type.ordinal()] = -1;
    }
    times[Type.RenderBatchCount.ordinal()] = 0;
  }

  /**
   * Should be called when processing for a frame ends. This will put all the data we've recorded into a new
   * FrameInfo instance and will store it for later retrieval.
   */
  public void endFrame() {
    addSample(new FrameInfo(
        frameCounter++,
        times[Type.Render.ordinal()],
        times[Type.Update.ordinal()],
        times[Type.Synchronize.ordinal()],
        times[Type.RenderBatchCount.ordinal()]));
  }

  /**
   * Get all samples collected so far.
   * @return FrameInfo array
   */
  public FrameInfo[] getFrameInfos() {
    return frameHistory.toArray(new FrameInfo[0]);
  }

  private void addSample(final FrameInfo frameInfo) {
    if (frameHistory.offer(frameInfo)) {
      return;
    }
    frameHistory.poll();
    addSample(frameInfo);
  }

  public void startUpdate() {
    start(Type.Update);
  }

  public void stopUpdate() {
    stop(Type.Update);
  }

  public void startRender() {
    start(Type.Render);
  }

  public void stopRender() {
    stop(Type.Render);
  }

  public void startSynchronize() {
    start(Type.Synchronize);
  }

  public void stopSynchronize() {
    stop(Type.Synchronize);
  }

  private void start(final Type type) {
    times[type.ordinal()] = System.nanoTime();
  }

  private void stop(final Type type) {
    times[type.ordinal()] = System.nanoTime() - times[type.ordinal()];
  }
}