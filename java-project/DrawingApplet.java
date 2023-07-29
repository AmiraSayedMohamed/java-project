import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class DrawingApplet extends JApplet implements ActionListener, MouseListener, MouseMotionListener {
    private int startX, startY, endX, endY;
    private String selectedShape = "Line";
    private Color selectedColor = Color.BLACK;
    private boolean fillShape = false;
    private boolean eraserMode = false;
    private int lineWidth = 1;

    private ArrayList<ShapeData> shapes = new ArrayList<>();

    private JButton lineButton;
    private JButton rectangleButton;
    private JButton ovalButton;
    private JButton colorRedButton;
    private JButton colorBlueButton;
    private JButton colorGreenButton;
    private JButton eraserButton;
    private JButton fillButton;
    private JButton borderButton;

    private class ShapeData {
        String shape;
        Color color;
        boolean fill;
        int x1, y1, x2, y2;

        public ShapeData(String shape, Color color, boolean fill, int x1, int y1, int x2, int y2) {
            this.shape = shape;
            this.color = color;
            this.fill = fill;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

    @Override
    public void init() {
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        lineButton = new JButton("Line");
        rectangleButton = new JButton("Rectangle");
        ovalButton = new JButton("Oval");
        colorRedButton = new JButton("Red");
        colorBlueButton = new JButton("Blue");
        colorGreenButton = new JButton("Green");
        eraserButton = new JButton("Eraser");
        fillButton = new JButton("Fill");
        borderButton = new JButton("Border");

        lineButton.addActionListener(this);
        rectangleButton.addActionListener(this);
        ovalButton.addActionListener(this);
        colorRedButton.addActionListener(this);
        colorBlueButton.addActionListener(this);
        colorGreenButton.addActionListener(this);
        eraserButton.addActionListener(this);
        fillButton.addActionListener(this);
        borderButton.addActionListener(this);

        buttonPanel.add(lineButton);
        buttonPanel.add(rectangleButton);
        buttonPanel.add(ovalButton);
        buttonPanel.add(colorRedButton);
        buttonPanel.add(colorBlueButton);
        buttonPanel.add(colorGreenButton);
        buttonPanel.add(eraserButton);
        buttonPanel.add(fillButton);
        buttonPanel.add(borderButton);

        add(buttonPanel, BorderLayout.NORTH);

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("Line")) {
            selectedShape = "Line";
            eraserMode = false;
        } else if (command.equals("Rectangle")) {
            selectedShape = "Rectangle";
            eraserMode = false;
        } else if (command.equals("Oval")) {
            selectedShape = "Oval";
            eraserMode = false;
        } else if (command.equals("Red")) {
            selectedColor = Color.RED;
            eraserMode = false;
        } else if (command.equals("Blue")) {
            selectedColor = Color.BLUE;
            eraserMode = false;
        } else if (command.equals("Green")) {
            selectedColor = Color.GREEN;
            eraserMode = false;
        } else if (command.equals("Eraser")) {
            eraserMode = true;
        } else if (command.equals("Fill")) {
            fillShape = true;
        } else if (command.equals("Border")) {
            fillShape = false;
        }

        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw all the stored shapes
        for (ShapeData shapeData : shapes) {
            g.setColor(shapeData.color);
            int x1 = shapeData.x1;
            int y1 = shapeData.y1;
            int x2 = shapeData.x2;
            int y2 = shapeData.y2;

            if (shapeData.shape.equals("Line")) {
                g.drawLine(x1, y1, x2, y2);
            } else if (shapeData.shape.equals("Rectangle")) {
                int width = Math.abs(x2 - x1);
                int height = Math.abs(y2 - y1);
                if (shapeData.fill) {
                    g.fillRect(x1, y1, width, height);
                } else {
                    g.drawRect(x1, y1, width, height);
                }
            } else if (shapeData.shape.equals("Oval")) {
                int width = Math.abs(x2 - x1);
                int height = Math.abs(y2 - y1);
                if (shapeData.fill) {
                    g.fillOval(x1, y1, width, height);
                } else {
                    g.drawOval(x1, y1, width, height);
                }
            }
            // Add handling for other shapes here
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();

        if (!eraserMode) {
            shapes.add(new ShapeData(selectedShape, selectedColor, fillShape, startX, startY, endX, endY));
        } else {
            // Eraser mode: Remove shapes that intersect with the current selection
            ArrayList<ShapeData> shapesToRemove = new ArrayList<>();
            for (ShapeData shapeData : shapes) {
                if (shapeIntersectsSelection(shapeData)) {
                    shapesToRemove.add(shapeData);
                }
            }
            shapes.removeAll(shapesToRemove);
        }

        repaint();
    }

    // Helper method to check if a shape intersects with the current selection
    private boolean shapeIntersectsSelection(ShapeData shapeData) {
        int x1 = Math.min(startX, endX);
        int y1 = Math.min(startY, endY);
        int x2 = Math.max(startX, endX);
        int y2 = Math.max(startY, endY);

        int shapeX1 = Math.min(shapeData.x1, shapeData.x2);
        int shapeY1 = Math.min(shapeData.y1, shapeData.y2);
        int shapeX2 = Math.max(shapeData.x1, shapeData.x2);
        int shapeY2 = Math.max(shapeData.y1, shapeData.y2);

        return !(x2 < shapeX1 || x1 > shapeX2 || y2 < shapeY1 || y1 > shapeY2);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Not used
    }

    // Main method (only for running the applet locally)
    public static void main(String[] args) {
        JFrame frame = new JFrame("Drawing Applet");
        DrawingApplet applet = new DrawingApplet();
        frame.add(applet);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
