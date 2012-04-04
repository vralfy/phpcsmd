/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.ui.reports;

import de.foopara.phpcsmd.exec.pdepend.PdependTypes;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.HashSet;
import javax.swing.JPanel;

/**
 *
 * @author n.specht
 */
public class JdependGraph extends JPanel {

    final private int offsetLeft   = 20;
    final private int offsetTop    = 20;

    private int offsetRight  = 20;
    private int offsetBottom = 20;

    private int stepSizeX;
    private int stepSizeY;

    private HashSet<PdependTypes.PdependPackage> packages = new HashSet<PdependTypes.PdependPackage>();

    public JdependGraph() {
        super();
        int height = 300;
        int width = 400;
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
        if (g == null) return;

//        g.setColor(Color.white);
//        g.clearRect(0, 0, this.getWidth(), this.getHeight());

        g.setColor(Color.darkGray);

        this.stepSizeX = (this.getWidth() -  (this.offsetLeft + this.offsetRight)) / 10;
        this.stepSizeY = (this.getHeight() - (this.offsetTop + this.offsetBottom)) / 10;
        this.offsetRight = this.getWidth() - (this.offsetLeft + 10 * this.stepSizeX);
        this.offsetBottom = this.getHeight() - (this.offsetTop + 10 * this.stepSizeY);

        for (int i=0; i<11; i++) {
            g.drawLine(this.offsetLeft + i * this.stepSizeX, this.offsetTop, this.offsetLeft + i * this.stepSizeX, this.getHeight() - this.offsetBottom);
        }

        for (int i=0; i<11; i++) {
            g.drawLine(this.offsetLeft, this.offsetTop + i * this.stepSizeY, this.getWidth() - this.offsetRight, this.offsetTop + i * this.stepSizeY);
        }

        g.setColor(Color.green);
        g.drawLine(this.offsetLeft, this.offsetTop, this.getWidth() - this.offsetRight, this.getHeight() - this.offsetBottom);

        g.setColor(Color.black);
        g.drawLine(this.offsetLeft,     this.offsetTop, this.offsetLeft,     this.getHeight() - this.offsetBottom + 1);
        g.drawLine(this.offsetLeft - 1, this.offsetTop, this.offsetLeft - 1, this.getHeight() - this.offsetBottom + 1);
        g.drawLine(this.offsetLeft - 1, this.getHeight() - this.offsetBottom,     this.getWidth() - this.offsetRight, this.getHeight() - this.offsetBottom);
        g.drawLine(this.offsetLeft - 1, this.getHeight() - this.offsetBottom + 1, this.getWidth() - this.offsetRight, this.getHeight() - this.offsetBottom + 1);

        for(PdependTypes.PdependPackage p : this.packages) {
            if (!(p.A == p.I && p.I == p.D && p.D == 0.f)) {
                int x = (int)(p.A * 10 * this.stepSizeX + this.offsetLeft);
                int y = (int)((1.0 - p.I) * 10 * this.stepSizeY + this.offsetTop);
                for (int diameter=20;diameter>0; diameter--) {
                    if (p.D < 0.1) {
                        g.setColor(new Color(0.f, 1.f/255 * (100 + diameter*7), 0.f));
                    } else {
                        g.setColor(new Color(1.f, 1.f/255 * (50 + diameter*5), 0.f));
                    }
                    g.fillOval(x - (diameter / 2), y - (diameter / 2), diameter, diameter);
                }
                g.setColor(Color.black);
                g.drawString(p.name, x + 10, y);
            }
        }
    }
}
