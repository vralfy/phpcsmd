package de.foopara.phpcsmd.ui.reports;

import de.foopara.phpcsmd.exec.pdepend.PdependTypes;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JPanel;

public class JdependGraph extends JPanel
{

    private int offsetLeft = 40;

    private int offsetTop = 20;

    private int offsetRight = 40;

    private int offsetBottom = 40;

    private int stepSizeX;

    private int stepSizeY;

    private HashSet<PdependTypes.PdependPackage> packages = new HashSet<PdependTypes.PdependPackage>();

    private static class PackageSummary
    {

        public float A;

        public float I;

        public float D;

        public HashSet<String> name = new HashSet<String>();

    }

    public JdependGraph() {
        super();
        int width = 400;
        int height = 400;
        this.setPreferredSize(new Dimension(width, height));
        this.setMinimumSize(new Dimension(width, height));

        this.setBackground(Color.white);
    }

    public void setPackages(HashSet<PdependTypes.PdependPackage> packages) {
        this.packages = packages;
        this.updateUI();
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (g == null) {
            return;
        }

//        this.setPreferredSize(new Dimension(this.getParent().getParent().getWidth(), this.getParent().getParent().getHeight()-200));
//        g.setColor(Color.white);
//        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        this.drawGraph(g);
        this.drawPackages(g);
    }

    private void drawGraph(Graphics g) {
        g.setColor(Color.darkGray);

        this.stepSizeX = (this.getWidth() - (this.offsetLeft + this.offsetRight)) / 10;
        this.stepSizeY = (this.getHeight() - (this.offsetTop + this.offsetBottom)) / 10;
        this.offsetRight = this.getWidth() - (this.offsetLeft + 10 * this.stepSizeX);
        this.offsetBottom = this.getHeight() - (this.offsetTop + 10 * this.stepSizeY);

        for (int i = 0; i < 11; i++) {
            g.drawLine(this.offsetLeft + i * this.stepSizeX, this.offsetTop, this.offsetLeft + i * this.stepSizeX, this.getHeight() - this.offsetBottom);
        }

        for (int i = 0; i < 11; i++) {
            g.drawLine(this.offsetLeft, this.offsetTop + i * this.stepSizeY, this.getWidth() - this.offsetRight, this.offsetTop + i * this.stepSizeY);
        }

        g.setColor(Color.green);
        g.drawLine(this.offsetLeft, this.offsetTop, this.getWidth() - this.offsetRight, this.getHeight() - this.offsetBottom);

        g.setColor(Color.black);
        g.drawLine(this.offsetLeft, this.offsetTop, this.offsetLeft, this.getHeight() - this.offsetBottom + 1);
        g.drawLine(this.offsetLeft - 1, this.offsetTop, this.offsetLeft - 1, this.getHeight() - this.offsetBottom + 1);
        g.drawLine(this.offsetLeft - 1, this.getHeight() - this.offsetBottom, this.getWidth() - this.offsetRight, this.getHeight() - this.offsetBottom);
        g.drawLine(this.offsetLeft - 1, this.getHeight() - this.offsetBottom + 1, this.getWidth() - this.offsetRight, this.getHeight() - this.offsetBottom + 1);

        g.setFont(new Font("Verdana", Font.ITALIC, 14));
        g.drawString("Abstraction", this.getWidth() / 2, this.getHeight() - this.offsetBottom + 15);
        this.rotatedText(g, "Instability", this.offsetLeft - 12, this.getHeight() / 2, -Math.PI / 2.0);
    }

    private void drawPackages(Graphics g) {
        HashMap<String, PackageSummary> summary = new HashMap<String, PackageSummary>();
        for (PdependTypes.PdependPackage p : this.packages) {
            String key = p.A + "|" + p.I + "|" + p.D;
            PackageSummary tmp;
            if (summary.containsKey(key)) {
                tmp = summary.get(key);
            } else {
                tmp = new PackageSummary();
                tmp.A = p.A;
                tmp.I = p.I;
                tmp.D = p.D;
                summary.put(key, tmp);
            }
            tmp.name.add(p.name);
        }

        g.setFont(new Font("Verdana", Font.BOLD, 11));
        for (PackageSummary p : summary.values()) {
            if (!(p.A == p.I && p.I == p.D && p.D == 0.f)) {
                int x = (int)(p.A * 10 * this.stepSizeX + this.offsetLeft);
                int y = (int)((1.0 - p.I) * 10 * this.stepSizeY + this.offsetTop);
                for (int diameter = 20; diameter > 0; diameter--) {
                    if (p.D < 0.1) {
                        g.setColor(new Color(0.f, 1.f / 255 * (100 + diameter * 7), 0.f));
                    } else {
                        g.setColor(new Color(1.f, 1.f / 255 * (50 + diameter * 5), 0.f));
                    }
                    g.fillOval(x - (diameter / 2), y - (diameter / 2), diameter, diameter);
                }
                g.setColor(Color.black);
                int c = 0;
                for (String name : p.name) {
                    this.rotatedText(g, name, x + 10, y + c * 10, 0);
                    c++;
                }
            }
        }
    }

    private void rotatedText(Graphics g, String text, int x, int y, double angle) {
        g.translate(x, y);
        ((Graphics2D)g).rotate(angle);

        g.drawString(text, 0, 0);

        ((Graphics2D)g).rotate(-angle);
        g.translate(-x, -y);
    }

}
