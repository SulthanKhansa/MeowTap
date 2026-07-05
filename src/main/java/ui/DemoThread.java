package ui;

import util.DigitalClockService;
import java.awt.*;
import javax.swing.*;

/**
 * Demo Multithreading - Menampilkan 3 thread jam digital yang berjalan bersamaan.
 * Setiap thread memiliki STOP button untuk menghentikan thread secara individual.
 */
public class DemoThread extends JFrame {

    private Thread clockThread1;
    private Thread clockThread2;
    private Thread clockThread3;

    private JLabel jLabel1, jLabel2, jLabel3;
    private JButton jButton1, jButton2, jButton3;

    public DemoThread() {
        initComponents();
        startThread1();
        startThread2();
        startThread3();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Demo Multithreading - Jam Digital");

        jLabel1 = new JLabel("jLabel1");
        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 24));

        jLabel2 = new JLabel("jLabel2");
        jLabel2.setFont(new Font("Segoe UI", Font.BOLD, 24));

        jLabel3 = new JLabel("jLabel3");
        jLabel3.setFont(new Font("Segoe UI", Font.BOLD, 24));

        jButton1 = new JButton("STOP");
        jButton1.addActionListener(e -> {
            clockThread1.interrupt();
            jButton1.setText("STOPPED");
            jButton1.setEnabled(false);
        });

        jButton2 = new JButton("STOP");
        jButton2.addActionListener(e -> {
            clockThread2.interrupt();
            jButton2.setText("STOPPED");
            jButton2.setEnabled(false);
        });

        jButton3 = new JButton("STOP");
        jButton3.addActionListener(e -> {
            clockThread3.interrupt();
            jButton3.setText("STOPPED");
            jButton3.setEnabled(false);
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE)
                        .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jButton1)
                        .addComponent(jButton2)
                        .addComponent(jButton3))
                    .addContainerGap(104, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(26, 26, 26)
                            .addComponent(jButton1)))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3))
                    .addContainerGap(269, Short.MAX_VALUE))
        );

        pack();
    }

    private void startThread1() {
        DigitalClockService service = new DigitalClockService(jLabel1, "EEEE, d MMMM yyyy, HH:mm:ss");
        clockThread1 = service.getThread();
        clockThread1.setName("Thread-Jam-1");
        clockThread1.setDaemon(true);
        clockThread1.start();
    }

    private void startThread2() {
        DigitalClockService service = new DigitalClockService(jLabel2, "EEEE, d MMMM yyyy, HH:mm:ss");
        clockThread2 = service.getThread();
        clockThread2.setName("Thread-Jam-2");
        clockThread2.setDaemon(true);
        clockThread2.start();
    }

    private void startThread3() {
        DigitalClockService service = new DigitalClockService(jLabel3, "EEEE, d MMMM yyyy, HH:mm:ss");
        clockThread3 = service.getThread();
        clockThread3.setName("Thread-Jam-3");
        clockThread3.setDaemon(true);
        clockThread3.start();
    }
}
