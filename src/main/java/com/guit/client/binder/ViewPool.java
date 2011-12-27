/*
 * Copyright 2010 Gal Dolber.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.guit.client.binder;

import com.google.gwt.user.client.Timer;

import com.guit.client.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Pool used in GuitBinder for view recycling.
 */
public class ViewPool {

  private static class ViewCache {
    final View view;
    final Date date = new Date();
    private final ArrayList<View> pool;

    public ViewCache(View view, ArrayList<View> pool) {
      this.view = view;
      this.pool = pool;
    }

    public void remove() {
      pool.remove(view);
    }
  }

  private static ArrayList<ViewCache> mainPool = new ArrayList<ViewPool.ViewCache>();
  private static boolean isRunning = false;
  private static Timer cleaner = new Timer() {
    @Override
    public void run() {
      long now = new Date().getTime();
      Iterator<ViewCache> iterator = mainPool.iterator();
      while (iterator.hasNext()) {
        ViewCache cache = iterator.next();
        if (now - cache.date.getTime() >= 5000) {
          cache.remove();
          iterator.remove();
        }
      }
    }
  };

  private final ArrayList<View> pool = new ArrayList<View>();

  public boolean isEmpty() {
    return pool.isEmpty();
  }

  public View pop() {
    assert pool.size() > 0 : "Error";
    View view = pool.get(0);
    pool.remove(0);
    mainPool.remove(view);

    if (mainPool.size() == 0) {
      cleaner.cancel();
      isRunning = false;
    }

    return view;
  }

  public void push(View view) {
    pool.add(view);
    mainPool.add(new ViewCache(view, pool));

    if (!isRunning) {
      cleaner.scheduleRepeating(5000);
      isRunning = true;
    }
  }

  public void clear() {
    for (View view : pool) {
      mainPool.remove(view);
    }
    pool.clear();
  }
}
