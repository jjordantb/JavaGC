package ui;

import gc.GCHeapImpl;
import gc.GCObject;
import util.Exec;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Frame {

    private final JFrame demoFrame = new JFrame("Garbage Collection Demo");

    public Frame() {
        this.demoFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.demoFrame.setLayout(new BorderLayout());
        this.demoFrame.getContentPane().setPreferredSize(new Dimension(1200, 800));
        this.demoFrame.add(new DrawingPanel(), BorderLayout.CENTER);
        this.demoFrame.pack();
        this.demoFrame.setLocationRelativeTo(null);
        this.demoFrame.setVisible(true);
    }

    private class DrawingPanel extends JPanel {

        public DrawingPanel() {
            super.setBackground(new Color(36, 41, 49));
        }

        @Override
        protected void paintComponent(Graphics gg) {
            super.paintComponent(gg);
            Graphics2D g = (Graphics2D) gg;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            List<HeapObject> heapObjects = new ArrayList<>();

            // Drawing the Heap
            int startX = 10, startY = 35, width = 100, height = 20, cols = 1;
            g.setFont(new Font("default", Font.BOLD, 16));
            g.setColor(Color.WHITE);
            g.drawString("Heap Size: " + GCHeapImpl.getMemory().getCarat(), 10,25);
            g.setFont(new Font("default", Font.BOLD, 13));
            for (GCObject o : GCHeapImpl.getMemory().getHeap()) {
                if (startY + height > super.getHeight() - 10) {
                    startY = 35;
                    startX = 10 + (cols * 40) + (cols * width);
                    cols++;
                }
                g.setColor(new Color(34, 226, 237));
                g.drawRect(startX - 1, startY - 1, width + 2, height + 2);
                if (o != null) {
                    g.setColor(new Color(64, 72, 85));
                    HeapObject ho = new HeapObject(o, new Rectangle(startX, startY, width, height));
                    heapObjects.add(ho);
                    g.fill(ho.getBounds());
                    g.setColor(Color.WHITE);
                    String s = o.get().toString();
                    g.drawString(s.substring(0, Math.min(s.length(), 12)), startX + 2, startY + (height / 2) + 5);
                }
                startY += height + 4;
            }

            // Draw heap references
            for (HeapObject heapObject : heapObjects) {
                if (heapObject.getObject() != null) {
                    g.setColor(Color.MAGENTA);
                    int offset = 10;
                    List<GCObject> children = heapObject.getObject().getChildren();
                    for (GCObject child : children) {
                        HeapObject hoChild = this.find(heapObjects, child);
                        if (hoChild != null) {
                            Point p = hoChild.getPointer(true);
                            g.drawLine(p.x, p.y, p.x + offset, p.y);
                            Point p2 = heapObject.getPointer(true);
                            g.drawLine(p2.x, p2.y, p2.x + offset, p2.y);
                            g.drawLine(p.x + offset, p.y, p2.x + offset, p2.y);
                        }
                    }
                }
            }

            // Drawing the roots and their references
            startX = super.getWidth() - width - 50; startY = 35; width = 100; height = 20; cols = 1;
            for (Map.Entry<Long, List<GCObject>> roots : GCHeapImpl.getMemory().getRoots().entrySet()) {
                g.setFont(new Font("default", Font.BOLD, 16));
                g.setColor(Color.WHITE);
                g.drawString("GC roots threadID: " + roots.getKey(), startX - 30, startY);
                startY += 20;
                g.setFont(new Font("default", Font.BOLD, 13));
                for (GCObject root : roots.getValue()) {
                    g.setColor(new Color(34, 226, 237));
                    g.drawRect(startX - 1, startY - 1, width + 2, height + 2);
                    if (root != null) {
                        g.setColor(new Color(64, 72, 85));
                        g.fillRect(startX, startY, width, height);
                        g.setColor(Color.WHITE);
                        String s = root.get().toString();
                        g.drawString(s.substring(0, Math.min(s.length(), 12)), startX + 2, startY + (height / 2) + 5);

                        // draw pointers
                        for (Object child : root.getChildren()) {
                            HeapObject obj;
                            if ((obj = this.find(heapObjects, (GCObject) child)) != null) {
                                Rectangle bounds = obj.getBounds();
                                g.drawLine(startX, startY + (height / 2), bounds.x + bounds.width, bounds.y + (bounds.height / 2));
                            }
                        }
                    }
                    startY += height + 4;
                }
                startY += 20;
            }
        }

        private HeapObject find(List<HeapObject> heapObjects, GCObject o) {
            for (HeapObject ho : heapObjects) {
                if (ho.getObject().equals(o)) {
                    return ho;
                }
            }
            return null;
        }

    }

    public void repaint() {
        this.demoFrame.repaint();
    }

}
