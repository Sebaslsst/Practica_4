package controller;

import controller.datastruct.graphs.UndirectedLabelGraph;
import controller.datastruct.graphs.Adjacency;
import controller.datastruct.list.LinkedList;
import java.util.Scanner;
import javax.swing.SwingUtilities;

public class Dijkstra {

    public static <E> LinkedList<E> dijkstra(UndirectedLabelGraph<E> graph, E start, E end) throws Exception {
        int n = graph.nro_vertex();
        LinkedList<E> visitados = new LinkedList<>();
        LinkedList<E> pendientes = new LinkedList<>();
        LinkedList<E> camino = new LinkedList<>();
        Float[] distancias = new Float[n + 1];
        E[] anteriores = (E[]) new Object[n + 1];
        
        for (int i = 1; i <= n; i++) {
            distancias[i] = Float.MAX_VALUE;
            anteriores[i] = null;
        }
        distancias[graph.getVertex(start)] = 0f;
        pendientes.add(start);

        while (pendientes.getLength() > 0) {
            E actual = pendientes.get(0);
            float minDist = distancias[graph.getVertex(actual)];
            for (int i = 1; i < pendientes.getLength(); i++) {
                E candidato = pendientes.get(i);
                float dist = distancias[graph.getVertex(candidato)];
                if (dist < minDist) {
                    actual = candidato;
                    minDist = dist;
                }
            }
            if (pendientes.contains(actual)) {
                pendientes.remove(actual);
            }
            visitados.add(actual);

            if (actual.equals(end))
                break;

            LinkedList<Adjacency> adyacentes = graph.adjacencies_label(actual);
            Adjacency[] adyArr = adyacentes.toArray();
            for (Adjacency ady : adyArr) {
                E vecino = graph.getLabel(ady.getDestiny());
                int idxVecino = ady.getDestiny();
                float peso = ady.getWeight() == null ? 1 : ady.getWeight();

                if (!visitados.contains(vecino)) {
                    float nuevaDist = distancias[graph.getVertex(actual)] + peso;
                    if (nuevaDist < distancias[idxVecino]) {
                        distancias[idxVecino] = nuevaDist;
                        anteriores[idxVecino] = actual;
                        if (!pendientes.contains(vecino)) {
                            pendientes.add(vecino);
                        }
                    }
                }
            }
        }

        E actual = end;
        while (actual != null) {
            camino.add(actual, 0);
            actual = anteriores[graph.getVertex(actual)];
        }
        if (camino.getLength() == 0 || !camino.get(0).equals(start)) {
            camino = new LinkedList<>();
        }
        return camino;
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int rows, cols;

        do {
            System.out.print("Ingrese el número de filas (30-100): ");
            rows = scanner.nextInt();
        } while (rows < 30 || rows > 100);

        do {
            System.out.print("Ingrese el número de columnas (30-100): ");
            cols = scanner.nextInt();
        } while (cols < 30 || cols > 100);

        Prim2 prim = new Prim2();
        String maze = prim.generar(rows, cols);
        System.out.println("Laberinto generado:");
        System.out.println(maze);

        String[] lines = maze.split("\n");
        char[][] matriz = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            String[] celdas = lines[i].split(",");
            for (int j = 0; j < cols; j++) {
                matriz[i][j] = celdas[j].charAt(0);
            }
        }
        
        int totalVertices = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matriz[i][j] != '0')
                    totalVertices++;
            }
        }

        UndirectedLabelGraph<String> grafo = new UndirectedLabelGraph<>(totalVertices, String.class);
        String[][] etiquetas = new String[rows][cols];
        int idx = 1;
        String inicio = "", fin = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matriz[i][j] != '0') {
                    String etiqueta = i + "," + j;
                    etiquetas[i][j] = etiqueta;
                    grafo.label_vertex(idx, etiqueta);
                    if (matriz[i][j] == 'S')
                        inicio = etiqueta;
                    if (matriz[i][j] == 'E')
                        fin = etiqueta;
                    idx++;
                }
            }
        }

        if (inicio.equals("") || fin.equals("")) {
            System.out.println("No se encontró el inicio o el fin en el laberinto.");
            scanner.close();
            return;
        }

        int[] dx = { 0, 1, 0, -1 };
        int[] dy = { 1, 0, -1, 0 };
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matriz[i][j] != '0') {
                    String actual = etiquetas[i][j];
                    for (int d = 0; d < 4; d++) {
                        int ni = i + dx[d], nj = j + dy[d];
                        if (ni >= 0 && ni < rows && nj >= 0 && nj < cols && matriz[ni][nj] != '0') {
                            String vecino = etiquetas[ni][nj];
                            grafo.insert_label(actual, vecino, 1f);
                        }
                    }
                }
            }
        }

        LinkedList<String> camino = dijkstra(grafo, inicio, fin);
        if (camino.getLength() == 0 || !camino.get(0).equals(inicio)) {
            System.out.println("No existe un camino entre inicio y fin.");
        } else {
            System.out.println("Camino más corto:");
            for (int i = 0; i < camino.getLength(); i++) {
                System.out.print(camino.get(i));
                if (i < camino.getLength() - 1)
                    System.out.print(" -> ");
            }
            System.out.println();
            SwingUtilities.invokeLater(() -> {
                new Interfaz(matriz, camino).setVisible(true);
            });
        }
        scanner.close();
    }
}