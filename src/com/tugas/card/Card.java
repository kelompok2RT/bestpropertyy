package com.tugas.card;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javaswingdev.GoogleMaterialDesignIcon;
import javaswingdev.GoogleMaterialIcon;
import javaswingdev.GradientType;

public class Card extends javax.swing.JPanel {
    private GoogleMaterialDesignIcon icon;

    public Card() {
        initComponents();
        init();
    }

    private void init() {
        setOpaque(false);
        setBackground(Color.WHITE);
        setIcon(GoogleMaterialDesignIcon.AUTORENEW);
    }

    public void setIcon(GoogleMaterialDesignIcon icon) {
        this.icon = icon;
        lbIcon.setIcon(new GoogleMaterialIcon(icon, GradientType.DIAGONAL_1, new Color(191, 191, 191), Color.WHITE, 32).toIcon());
    }

    public GoogleMaterialDesignIcon getIcon() {
        return icon;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Area area = new Area(new RoundRectangle2D.Double(0, 20, getWidth(), getHeight() - 20, 10, 10));
        g2.setColor(getBackground());
        g2.fill(area);
        area.subtract(new Area(new Rectangle2D.Double(0, 20, getWidth(), getHeight() - 23)));
        g2.setPaint(new GradientPaint(0, 0, lbIcon.getColor1(), getWidth(), 0, lbIcon.getColor2()));
        g2.fill(area);
        g2.dispose();
        super.paintComponent(g);
    }

    public Color getColor1() {
        return lbIcon.getColor1();
    }

    public void setColor1(Color color1) {
        lbIcon.setColor1(color1);
    }

    public Color getColor2() {
        return lbIcon.getColor2();
    }

    public void setColor2(Color color2) {
        lbIcon.setColor2(color2);
    }

    public void setDescription(String description) {
        lbDescription.setText(description);
    }

    public String getDescription() {
        return lbDescription.getText();
    }

    public void setValues(String values) {
        lbValues.setText(values);
    }

    public String getValues() {
        return lbValues.getText();
    }

    public void setData(ModelCard data) {
        lbValues.setText(data.getValues());
        lbDescription.setText(data.getDescription());
        if (data.getColor1() != null) {
            setColor1(data.getColor1());
        }
        if (data.getColor2() != null) {
            setColor2(data.getColor2());
        }
        if (data.getIcon() != null) {
            setIcon(data.getIcon());
        }
        repaint();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbIcon = new com.tugas.card.LabelIcon();
        lbValues = new javax.swing.JLabel();
        lbDescription = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        lbIcon.setText(" ");

        lbValues.setBackground(new java.awt.Color(0, 0, 0));
        lbValues.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        lbValues.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbValues.setText("$");

        lbDescription.setBackground(new java.awt.Color(0, 0, 0));
        lbDescription.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lbDescription.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbDescription.setText("Report Income Monthly");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lbDescription))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addComponent(lbValues, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 27, Short.MAX_VALUE)
                        .addComponent(lbValues, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbDescription;
    private com.tugas.card.LabelIcon lbIcon;
    private javax.swing.JLabel lbValues;
    // End of variables declaration//GEN-END:variables
}
