/*
 * Copyright (C) 2026 JHotDraw.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package org.jhotdraw.samples.svg;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class RelativeZoomActionTest {

    @Test
    public void testZoomInIncreasesScaleFactor() {
        assertEquals(1.25d, RelativeZoomAction.computeScaleFactor(1d, 1.25d), 0d);
    }

    @Test
    public void testZoomOutDecreasesScaleFactor() {
        assertEquals(0.8d, RelativeZoomAction.computeScaleFactor(1d, 0.8d), 0d);
    }

    @Test
    public void testZoomInDoesNotExceedMaximumScaleFactor() {
        assertEquals(RelativeZoomAction.MAX_SCALE_FACTOR,
                RelativeZoomAction.computeScaleFactor(50d, 1.25d), 0d);
    }

    @Test
    public void testZoomOutDoesNotGoBelowMinimumScaleFactor() {
        assertEquals(RelativeZoomAction.MIN_SCALE_FACTOR,
                RelativeZoomAction.computeScaleFactor(0.01d, 0.8d), 0d);
    }
}
