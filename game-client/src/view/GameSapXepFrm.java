/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author ADMIN
 */
public class GameSapXepFrm extends javax.swing.JFrame {
    private JPanel gameZoneJPanel;
    private JLabel timeLabel;
    private JLabel orderLabel;
    private JButton resetButton;
    private JButton sendButton;
    private Timer gameTimer;
    private int elapsedTime = 0;
    
    private List<GameSapXepFrm.NumberBox> numberBoxes;
    private List<GameSapXepFrm.NumberBox> bottomBoxes; // Hàng dưới
    private boolean ascendingOrder = true;
    
    private static final int BOX_WIDTH = 80;
    private static final int BOX_HEIGHT = 60;
    private static final int MARGIN = 10;
    private static final int MAX_WIDTH = 500; // Thêm biến maxWidth để dễ chỉnh sửa
    private static final int TOP_ROW_Y = 30;        // Vị trí Y bắt đầu của hàng trên
    private static final int TOP_ROW_X = 45;        // Vị trí X bắt đầu của hàng trên
    private static final int BOTTOM_ROW_Y = 450;    // Vị trí Y bắt đầu của hàng dưới
    private static final int BOTTOM_ROW_X = 50;     // Vị trí X bắt đầu của hàng dưới
    private static final int LINE_SPACING = 10;     // Khoảng cách giữa các dòng khi xuống hàng
    private static final int ROW_SPACING = 10;      // Khoảng cách giữa hàng trên và hàng dưới

  
    public GameSapXepFrm() throws IOException {
        initComponents();
        setTitle("Sorting Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        
        initializeComponents();
        setupLayout();
        startGame();
        
    }
     private void initializeComponents() {
      // ZoneJPanel.enable(true);
        numberBoxes = new ArrayList<>();
        bottomBoxes = new ArrayList<>();
        // Game zone panel
        gameZoneJPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Tính vị trí đường phân cách dựa trên hàng dưới
                int dividerY = calculateDividerY();

                // Draw top row numbers
                for (GameSapXepFrm.NumberBox box : numberBoxes) {
                    box.draw(g2d);
                }

                // Draw bottom row numbers
                for (GameSapXepFrm.NumberBox box : bottomBoxes) {
                    box.draw(g2d);
                }

                // Vẽ đường phân cách
                g2d.setColor(Color.GRAY);
                g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
                g2d.drawLine(0, dividerY, getWidth(), dividerY);
            }

            private int calculateDividerY() {
                int maxTopY = BOTTOM_ROW_Y;
//                for (NumberBox box : numberBoxes) {
//                    maxTopY = Math.max(maxTopY, box.y + BOX_HEIGHT);
//                }
                return maxTopY - ROW_SPACING; // Cách hàng trên 10px
            }
        };
        
        gameZoneJPanel.setPreferredSize(new Dimension(600, 250));
        gameZoneJPanel.setBackground(new Color(240, 240, 240));
        
        // Add mouse listener to game zone
        gameZoneJPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleBoxClick(e.getX(), e.getY());
            }

//            private void handleBoxClick(int x, int y) {
//                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//            }
        });
        
        // Info labels
        timeLabel = new JLabel("Time: 0s", JLabel.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        orderLabel = new JLabel("Order: Ascending", JLabel.CENTER);
        orderLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Buttons
        resetButton = new JButton("Reset");
        sendButton = new JButton("Send");
        
        // Style buttons
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setBackground(new Color(255, 100, 100));
        sendButton.setBackground(new Color(100, 255, 100));
        resetButton.setForeground(Color.WHITE);
        sendButton.setForeground(Color.WHITE);
        
        // Timer
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                timeLabel.setText("Time: " + elapsedTime + "s");
            }
        });
        
        // Button listeners
        resetButton.addActionListener(e -> resetGame());
        sendButton.addActionListener(e -> checkSolution());
        
//        // Initialize number boxes list
//        numberBoxes = new ArrayList<>();
    }
     private void setupLayout() throws IOException {
        ZoneJPanel.setLayout(new BorderLayout());
        ZoneJPanel.setOpaque(false); 
        String imagePath = "assets/drawable/boardteacher.jpg";
        ImageIcon icon = new ImageIcon(imagePath);
        // Check xem ảnh có load được không
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            System.out.println("✅ Ảnh loaded thành công!");
            System.out.println("Kích thước: " + icon.getIconWidth() + "x" + icon.getIconHeight());
        } else {
            System.out.println("❌ Không load được ảnh");
            System.out.println("Status: " + icon.getImageLoadStatus());
        }
        JLabel backgroundLabel = new JLabel(icon);

        backgroundLabel.setLayout(new BorderLayout());

        // Top panel for game info
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(timeLabel);
        topPanel.add(orderLabel);
        //topPanel.setOpaque(false); 
        topPanel.setBackground(new Color(255, 255, 51));
        
        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(resetButton);
        bottomPanel.add(sendButton);
        bottomPanel.setBackground(new Color(255, 255, 51));
        //bottomPanel.setOpaque(false); 
        gameZoneJPanel.setOpaque(false); 
        
        backgroundLabel.add(topPanel, BorderLayout.NORTH);
        backgroundLabel.add(gameZoneJPanel, BorderLayout.CENTER);
        backgroundLabel.add(bottomPanel, BorderLayout.SOUTH);
          // Thêm backgroundLabel vào ZoneJPanel
        ZoneJPanel.add(backgroundLabel, BorderLayout.CENTER);
    }
    
    private void startGame() {
        elapsedTime = 0;
        gameTimer.start();
        generateNumbers();
    }
    
    private void generateNumbers() {
        numberBoxes.clear();
        bottomBoxes.clear(); // QUAN TRỌNG: Xóa cả hàng dưới
        
        // Generate 6 random numbers (giống trong ảnh: 7, 2, 1, 9, 5, 12)
        int[] initialNumbers = {7, 2, 1, 9, 5, 12, 11, 3, 4, 22, 34, 54};
        //int[] initialNumbers = {86, 73, 59, 41, 28, 19, 7, 64, 92, 35, 53, 10};

        
        for (int i = 0; i < initialNumbers.length; i++) {
            int x = MARGIN + i * (BOX_WIDTH + MARGIN);
//            int y = 70; // Vị trí Y trong panel
//            numberBoxes.add(new NumberBox(initialNumbers[i], x, y));
            numberBoxes.add(new GameSapXepFrm.NumberBox(initialNumbers[i], x, TOP_ROW_Y, true));
        }
        rearrangeTopRow();
        rearrangeBottomRow();
        gameZoneJPanel.repaint();
    }
    
    private void handleBoxClick(int x, int y) {
         // Kiểm tra click trên hàng trên
        for (GameSapXepFrm.NumberBox box : numberBoxes) {
            if (box.contains(x, y)) {
                moveBoxToBottom(box);
                gameZoneJPanel.repaint();
                return;
            }
        }
         // Kiểm tra click trên hàng dưới
        for (GameSapXepFrm.NumberBox box : bottomBoxes) {
            if (box.contains(x, y)) {
                moveBoxToTop(box);
                gameZoneJPanel.repaint();
                return;
            }
        }
        
    }
      private void moveBoxToBottom(NumberBox box) {
        numberBoxes.remove(box);
         box.isTopRow = false;
         bottomBoxes.add(box);

         // Sắp xếp lại cả hai hàng
         rearrangeTopRow();
         rearrangeBottomRow();  
    }
    private void moveBoxToTop(NumberBox box) {

          bottomBoxes.remove(box);
          box.isTopRow = true;
          numberBoxes.add(box);

          // Sắp xếp lại cả hai hàng
          rearrangeTopRow();
          rearrangeBottomRow();
    }
    private void rearrangeTopRow() {
        int currentX = TOP_ROW_X;
        int currentY = TOP_ROW_Y;

        for (int i = 0; i < numberBoxes.size(); i++) {
            NumberBox box = numberBoxes.get(i);

            // Kiểm tra nếu vượt quá maxWidth
            if (currentX + BOX_WIDTH > MAX_WIDTH) {
                // Xuống dòng mới
                currentX = TOP_ROW_X;
                currentY += BOX_HEIGHT +LINE_SPACING;
            }

            box.x = currentX;
            box.y = currentY;
            currentX += BOX_WIDTH + MARGIN;
        }
    }

    private void rearrangeBottomRow() {
        int currentX = MARGIN;
        int currentY = BOTTOM_ROW_Y;

        for (int i = 0; i < bottomBoxes.size(); i++) {
            NumberBox box = bottomBoxes.get(i);

            // Kiểm tra nếu vượt quá maxWidth
            if (currentX + BOX_WIDTH > MAX_WIDTH+280) {
                // Xuống dòng mới
                currentX = MARGIN;
                currentY += BOX_HEIGHT + LINE_SPACING;
            }

            box.x = currentX;
            box.y = currentY;
            currentX += BOX_WIDTH + MARGIN;
        }
    }

    private void swapBoxes(NumberBox box1, NumberBox box2) {
        int tempX = box1.x;
        int tempY = box1.y;
        
        box1.x = box2.x;
        box1.y = box2.y;
        
        box2.x = tempX;
        box2.y = tempY;
    }
    
    private void resetGame() {
        elapsedTime = 0;
        timeLabel.setText("Time: 0s");
        generateNumbers();
    }
    
    private void checkSolution() {
        if (isSortedCorrectly()) {
            gameTimer.stop();
            JOptionPane.showMessageDialog(this, 
                "Congratulations! You won!\nTime: " + elapsedTime + "s", 
                "Victory", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Not sorted correctly. Try again!", 
                "Try Again", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private boolean isSortedCorrectly() {
        if(!numberBoxes.isEmpty()){
            return false;
        }
        // Chỉ kiểm tra hàng trên (hàng chơi chính)
        for (int i = 0; i < bottomBoxes.size() - 1; i++) {
            int current = bottomBoxes.get(i).value;
            int next = bottomBoxes.get(i + 1).value;
            
            if (ascendingOrder && current > next) {
                return false;
            } else if (!ascendingOrder && current < next) {
                return false;
            }
        }
        return true;
    }
    private boolean isSortedByDigitSumDescending(){
        if (!numberBoxes.isEmpty()) {
            return false;
        }

        for (int i = 0; i < bottomBoxes.size() - 1; i++) {
            int currentSum = digitSum(bottomBoxes.get(i).value);
            int nextSum = digitSum(bottomBoxes.get(i + 1).value);

            if (currentSum < nextSum) {
                return false; // vì yêu cầu giảm dần
            }
        }
        return true;
    }
    private int digitSum(int n) {
    int sum = 0;
    while (n > 0) {
        sum += n % 10;
        n /= 10;
    }
    return sum;
}


    
    private int getNumberAtPosition(int index) {
        // Tìm số tại vị trí X cụ thể (vì các box có thể đã bị hoán đổi vị trí)
        int positionX = MARGIN + index * (BOX_WIDTH + MARGIN);
        for (NumberBox box : numberBoxes) {
            if (Math.abs(box.x - positionX) < 5) { // Dung sai nhỏ
                return box.value;
            }
        }
        return -1; // Không tìm thấy
    }
    
    // Inner class for number boxes
    private class NumberBox {
        int value;
        int x, y;
        boolean  isTopRow;
        
        NumberBox(int value, int x, int y, boolean isTopRow) {
            this.value = value;
            this.x = x;
            this.y = y;
            this.isTopRow = isTopRow;
        }
        
        boolean contains(int pointX, int pointY) {
            return pointX >= x && pointX <= x + BOX_WIDTH &&
                   pointY >= y && pointY <= y + BOX_HEIGHT;
        }
        
      
        void draw(Graphics2D g2d) {
        // Draw box với gradient đẹp như ban đầu
        GradientPaint gradient;
        if (isTopRow) {
            // Hàng trên - màu xanh đẹp
            gradient = new GradientPaint(
                x, y, new Color(180, 220, 255), 
                x, y + BOX_HEIGHT, new Color(140, 180, 255)
            );
        } else {
         // Hàng dưới - màu vàng
                gradient = new GradientPaint(
                    x, y, new Color(255, 240, 180), 
                    x, y + BOX_HEIGHT, new Color(255, 220, 140)
        );
        }
        g2d.setPaint(gradient);
        g2d.fillRoundRect(x, y, BOX_WIDTH, BOX_HEIGHT, 15, 15);
        
        // Draw border - viền đẹp như ban đầu
        g2d.setColor(isTopRow ? new Color(80, 130, 200) : new Color(120, 160, 220));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, BOX_WIDTH, BOX_HEIGHT, 15, 15);
        
        // Draw number - giữ nguyên như cũ
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        String text = String.valueOf(value);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        g2d.drawString(text, x + (BOX_WIDTH - textWidth) / 2, 
                      y + (BOX_HEIGHT + textHeight) / 2 - 4);
    }
    
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ZoneJPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ZoneJPanel.setBackground(new java.awt.Color(255, 255, 51));
        ZoneJPanel.setForeground(new java.awt.Color(255, 255, 255));
        ZoneJPanel.setDoubleBuffered(false);
        ZoneJPanel.setEnabled(false);

        javax.swing.GroupLayout ZoneJPanelLayout = new javax.swing.GroupLayout(ZoneJPanel);
        ZoneJPanel.setLayout(ZoneJPanelLayout);
        ZoneJPanelLayout.setHorizontalGroup(
            ZoneJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 744, Short.MAX_VALUE)
        );
        ZoneJPanelLayout.setVerticalGroup(
            ZoneJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 717, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ZoneJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 249, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ZoneJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GameSapXepFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameSapXepFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameSapXepFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameSapXepFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new GameSapXepFrm().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(GameSapXepFrm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ZoneJPanel;
    // End of variables declaration//GEN-END:variables
}
