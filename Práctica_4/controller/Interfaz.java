package controller;

import javax.swing.*;
import java.awt.*;
import controller.datastruct.list.LinkedList;

public class Interfaz extends JFrame {
    private char[][] matriz;
    private LinkedList<String> camino;

    public Interfaz(char[][] matriz, LinkedList<String> camino) {
        this.matriz = matriz;
        this.camino = camino;
        setTitle("Laberinto Resuelto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        LaberintoPanel panel = new LaberintoPanel();
        // Ajusta el tamaño preferido del panel según el tamaño del laberinto
        int cellSize = 20;
        panel.setPreferredSize(new Dimension(matriz[0].length * cellSize, matriz.length * cellSize));
        JScrollPane scrollPane = new JScrollPane(panel);
        setContentPane(scrollPane);
        pack();
    }

    class LaberintoPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int cellSize = 20;
            // Pintar el laberinto
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[0].length; j++) {
                    char c = matriz[i][j];
                    if (c == '0') g.setColor(Color.BLACK); // Pared
                    else if (c == 'S') g.setColor(Color.GREEN); // Entrada
                    else if (c == 'E') g.setColor(Color.RED); // Salida
                    else g.setColor(Color.WHITE); // Camino
                    g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    g.setColor(Color.GRAY);
                    g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
            }
            // Pintar el camino resuelto
            g.setColor(Color.BLUE);
            for (int k = 0; k < camino.getLength(); k++) {
                String[] pos = camino.get(k).split(",");
                int i = Integer.parseInt(pos[0]);
                int j = Integer.parseInt(pos[1]);
                g.fillRect(j * cellSize + 5, i * cellSize + 5, cellSize - 10, cellSize - 10);
            }
        }
    }
}