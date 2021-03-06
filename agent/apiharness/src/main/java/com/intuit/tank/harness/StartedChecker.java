package com.intuit.tank.harness;

/*
 * #%L
 * Intuit Tank Agent (apiharness)
 * %%
 * Copyright (C) 2011 - 2015 Intuit Inc.
 * %%
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * #L%
 */

public class StartedChecker implements Runnable {

    private static final long SLEEP_TIME = 1000 * 60 * 5; // 5 minutes

    @Override
    public void run() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!APITestHarness.getInstance().isStarted()) {
            System.exit(1);
        }

    }

}
