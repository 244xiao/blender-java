/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blender.ghost;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;

//import com.sun.opengl.util.Animator;
import com.jogamp.opengl.util.AnimatorBase;
//import com.sun.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.FPSAnimator;

/**
 *
 * @author jladere
 */
public class GhostSystem implements AWTEventListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener, WindowListener {
    
    private GhostCallbackEventConsumer consumer;
    //private Animator anim;
    private AnimatorBase anim;
    
    public GhostSystem() {
    	
    }
    
    boolean addEventConsumer(GhostCallbackEventConsumer eventConsumer) {
    	consumer = eventConsumer;
    	return true;
    }
    
    void getMainDisplayDimensions(int[] width, int[] height) {
    	Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	    width[0] = d.width;
	    height[0] = d.height;
    }
    
    JFrame createWindow(String title, int left, int top, int width, int height,
    		Object state, Object type, boolean bstereoVisual, int numOfAASamples) {
    	JFrame ghostwin;

	    Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);

	    GLJPanel glSurface = new GLJPanel();
	    glSurface.setAutoSwapBufferMode(false);
	    glSurface.addGLEventListener((GLEventListener)type);
	    glSurface.addMouseListener(this);
	    glSurface.addMouseMotionListener(this);
	    glSurface.addMouseWheelListener(this);
	    anim = new FPSAnimator(glSurface, 80, true);
	    
	    ghostwin = new JFrame(title);
	    ghostwin.addWindowListener(this);
	    ghostwin.getContentPane().add(glSurface, BorderLayout.CENTER);
	    ghostwin.getContentPane().setPreferredSize(new Dimension(width, height));
	    
//	    // TMP
//	    ghostwin.pack();
//	    ghostwin.setVisible(true);
//	    anim.start();
		
		return ghostwin;
    }
    
    // TMP
    void showWindow(JFrame window) {
    	window.pack();
    	window.setVisible(true);
	    anim.start();
    }
    
    boolean disposeWindow(JFrame window) {
    	window.dispose();
		return true;
    }
    
    boolean validWindow(JFrame window) {
//    	return window.isValid();
    	return window.isShowing() && GhostAPI.userData.get(window)!=null;
    }

    public void keyPressed(KeyEvent e) {
        consumer.eventCallback.run(e, consumer.userData);
    }

    public void keyReleased(KeyEvent e) {
        consumer.eventCallback.run(e, consumer.userData);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        consumer.eventCallback.run(e, consumer.userData);
    }

    public void mouseReleased(MouseEvent e) {
        consumer.eventCallback.run(e, consumer.userData);
    }

    public void mouseMoved(MouseEvent e) {
        consumer.eventCallback.run(e, consumer.userData);
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        consumer.eventCallback.run(e, consumer.userData);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        consumer.eventCallback.run(e, consumer.userData);
    }

    public void windowClosing(WindowEvent e) {
        consumer.eventCallback.run(e, consumer.userData);
    }

    public void windowActivated(WindowEvent e) {
        consumer.eventCallback.run(e, consumer.userData);
    }

    public void windowDeactivated(WindowEvent e) {
        consumer.eventCallback.run(e, consumer.userData);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void eventDispatched(AWTEvent event) {

        KeyEvent kevt = (KeyEvent) event;
        if (kevt.getID() == KeyEvent.KEY_PRESSED) {
            keyPressed(kevt);
        } else if (kevt.getID() == KeyEvent.KEY_RELEASED) {
            keyReleased(kevt);
        } else if (kevt.getID() == KeyEvent.KEY_TYPED) {
            keyTyped(kevt);
        }

    }
}
