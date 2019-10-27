package org.jdesktop.swingx;

import java.awt.Graphics;

import org.jdesktop.swingx.MultiSplitLayout.Divider;

/**
 * Draws a single Divider.  Typically used to specialize the
 * way the active Divider is painted.  
 * 
 * @see #getDividerPainter
 * @see #setDividerPainter
 */
public abstract class DividerPainter {
/**
 * Paint a single Divider.       
 * 
 * @param g the Graphics object to paint with
 * @param divider the Divider to paint
 */
public abstract void paint(Graphics g, Divider divider);
}