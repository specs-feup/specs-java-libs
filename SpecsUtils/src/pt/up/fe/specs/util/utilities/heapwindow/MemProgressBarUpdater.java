/*
 * Copyright 2010 SPeCS Research Group.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util.utilities.heapwindow;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 *
 * @author Joao Bispo
 */
class MemProgressBarUpdater extends SwingWorker<Object, Object> {

    public MemProgressBarUpdater(JProgressBar jProgressBar) {
        this.jProgressBar = jProgressBar;
        this.jProgressBar.setStringPainted(true);
    }

    @Override
    protected Object doInBackground() throws Exception {
        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long usedMemory = heapSize - heapFreeSize;

        long mbFactor = (long) Math.pow(1024, 2);
        // long kbFactor = (long) Math.pow(1024, 1);

        heapSizeMb = (int) (heapSize / mbFactor);
        currentSizeMb = (int) (usedMemory / mbFactor);

        java.awt.EventQueue.invokeLater(() -> {
            String barString = MemProgressBarUpdater.this.currentSizeMb + "MiB / "
                    + MemProgressBarUpdater.this.heapSizeMb
                    + "MiB";

            MemProgressBarUpdater.this.jProgressBar.setMinimum(0);
            MemProgressBarUpdater.this.jProgressBar.setMaximum(MemProgressBarUpdater.this.heapSizeMb);
            MemProgressBarUpdater.this.jProgressBar.setValue(MemProgressBarUpdater.this.currentSizeMb);
            MemProgressBarUpdater.this.jProgressBar.setString(barString);
            // System.err.println("Heap Size:"+heapSizeMb);
            // System.err.println("Current Size:"+currentSizeMb);
        });

        return null;
    }

    @Override
    protected void done() {
        /*
        jProgressBar.setMinimum(0);
        jProgressBar.setMaximum(heapSizeMb);
        jProgressBar.setValue(currentSizeMb);
        System.err.println("Heap Size:"+heapSizeMb);
        System.err.println("Current Size:"+currentSizeMb);
         *
         */
    }

    /**
     * INSTANCE VARIABLES
     */
    private final JProgressBar jProgressBar;
    int heapSizeMb;
    int currentSizeMb;

}
