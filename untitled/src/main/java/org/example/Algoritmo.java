package org.example;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.Scanner;

/**
 * Clase que implementa un algoritmo específico.
 */
public class Algoritmo {

    /**
     * Método principal que inicia la ejecución del algoritmo.
     *
     * @param args Argumentos de la línea de comandos (no se utilizan en este caso).
     * @throws IOException Si hay un error de entrada/salida durante la ejecución.
     */
    public static void main(String[] args) throws IOException {

        // Crear un selector de archivos
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccione el archivo D.txt");

        // Filtrar para mostrar solo archivos .txt
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de texto (*.txt)", "txt");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(null);

        if (result != JFileChooser.APPROVE_OPTION) {
            System.out.println("Operación cancelada por el usuario.");
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();
        String fileName = selectedFile.getAbsolutePath();

        long startTime = System.currentTimeMillis();

        Scanner readFile = new Scanner(selectedFile);

        // Leer el máximo consecutivo de juegos en casa/fuera
        int ha = readFile.nextInt();

        int n = 0;
        while (readFile.hasNextInt()) {
            readFile.nextInt();
            n++;
        }
        n = (int) Math.sqrt(n);
        readFile.close();

        // ...

        // Create distance matrix
        int[][] cityDistances = new int[n][n];
        int i = 0;
        int j = 0;

        readFile = new Scanner(selectedFile);

        // Saltear la línea que contiene el máximo consecutivo de juegos en casa/fuera
        readFile.nextLine();

        // Populate distance matrix
        for (j = 0; j < n; j++) {
            for (i = 0; i < n; i++) {
                cityDistances[i][j] = readFile.nextInt();
            }
        }
        readFile.close();

        // Max consecutive home and away games
        while (ha > n - 1 || ha <= 0) {
            try {
                /*
                 * Solicitar al usuario que ingrese el máximo consecutivo de juegos en casa/fuera.
                 * Se seguirá solicitando hasta que se ingrese un valor válido.
                 */
                System.out.println("Ingrese el máximo consecutivo de juegos en casa/fuera:");
                ha = Integer.parseInt(new BufferedReader(new InputStreamReader(System.in)).readLine());

                if (ha > n - 1 || ha <= 0) {
                    System.out.println("Se ha introducido un valor no válido. El valor debe ser mayor que 0 y menor que (# de equipos - 1):");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int[][] starWeight = new int[n][1];
        int indexWeight = 0;

        //Obtener pesos de estrella para cada ciudad
        for (j = 0; j < n; j++) {
            for (i = 0; i < n; i++) {
                starWeight[j][0] += cityDistances[i][j];
            }
        }

        //Inicializar peso de estrella
        int tempWeight = 10000000;

        //Identificar el peso de estrella mínimo
        for (i = 0; i < n; i++) {
            if (starWeight[i][0] < tempWeight) {
                tempWeight = starWeight[i][0];
                indexWeight = i;
            }
        }

        System.out.println(starWeight[indexWeight][0] + " es el peso de estrella mínimo que pertenece al índice " + indexWeight);
        System.out.println();

        //TSP Solver
        int[] chosenPath = new int[n + 1];
        int distance = 0;

        //Poblar la ruta inicial
        for (i = 1; i < n; i++) {
            if (i != indexWeight) {
                chosenPath[i] = i;
            }
        }

        //Establecer el peso de estrella mínimo como nodo de inicio/final
        chosenPath[0] = indexWeight;
        chosenPath[n] = indexWeight;

        //Calcular la distancia total
        for (i = 0; i < n; i++) {
            distance += cityDistances[chosenPath[i]][chosenPath[i + 1]];
        }

        int tempDistance = 0;
        int[] tempPath = new int[n + 1];
        int tempHold = 0;

        //Copiar la ruta de inicio
        for (i = 0; i < n + 1; i++) {
            tempPath[i] = chosenPath[i];
        }

        //Comenzar intercambios
        int count = 0;

        //Comenzar primer algoritmo de aceptación
        while (count != 100) {

            for (j = 1; j < n - 1; j++) {
                for (i = 1; i < n - 1; i++) {
                    //Comenzar a intercambiar nodos, pero dejar el nodo de peso de estrella mínimo solo
                    tempHold = tempPath[j];
                    tempPath[j] = tempPath[i];
                    tempPath[i] = tempHold;
                    tempDistance = 0;

                    //Calcular la distancia de la nueva ruta propuesta
                    for (int z = 0; z < n; z++) {
                        tempDistance += cityDistances[tempPath[z]][tempPath[z + 1]];
                    }
                    //Comprobar si la ruta propuesta es mejor y, si es así, reemplazar
                    if (tempDistance < distance) {
                        for (int b = 0; b < n + 1; b++) {
                            chosenPath[b] = tempPath[b];

                        }
                        distance = tempDistance;

                        System.out.println("Ruta TSP más corta encontrada hasta ahora: " + distance);//a medida que se encuentran rutas mejores...

                    }

                    //revertir cambio a tempPath
                    else {
                        tempHold = tempPath[i];
                        tempPath[i] = tempPath[j];
                        tempPath[j] = tempHold;

                    }

                }

            }
            //Para el bucle while
            count++;
        }

        System.out.println();
        System.out.println("Mejor ruta TSP encontrada hasta ahora con restricción de peso de estrella: ");

        //Imprimir la mejor ruta encontrada
        for (int a = 0; a < n + 1; a++) {
            if (a != n) {
                System.out.print(chosenPath[a] + "->");
            } else {
                System.out.print(chosenPath[a]);
            }
        }
        System.out.println();
        System.out.println("Con una distancia de: " + distance);

        //El siguiente algoritmo requiere un formato específico de la solución TSP
        int pathFormatted[] = new int[n];

        //Solo el último nodo es el peso de estrella mínimo
        for (i = 0; i < pathFormatted.length; i++) {
            pathFormatted[i] = chosenPath[i + 1];
        }

        System.out.println();
        System.out.println("Reformateando la ruta para usar en la parte 2... ");
        System.out.println();
        //Imprimir la ruta reformateada
        for (int a = 0; a < n; a++) {
            if (a != (n - 1))
                System.out.print(pathFormatted[a] + "->");
            else System.out.print(pathFormatted[a]);
        }
        System.out.println();
        System.out.println();

        //Crear matriz de horario TTP
        int[][] schedule = new int[n][2 * n - 2];

        //Pasar al siguiente algoritmo

        schedule = PTorneo.PTorneo(ha, pathFormatted, n);

        //Obtener distancias
        int[] teamDistance = new int[n];
        int totalDistance = 0;

        for (j = 0; j < n; j++) {
            for (i = 0; i < (2 * n - 3); i++) {

                if (i == 0 && schedule[j][i] < 0) {
                    //Primer juego fuera
                    //Nota: (schedule[j][i])-1) debido a que los números de ciudad se incrementan en +1 para evitar el problema con -0 = 0
                    teamDistance[j] += cityDistances[j][Math.abs(schedule[j][i]) - 1];
                }

                //2x juegos en casa o (1 en casa 1 fuera)
                if (schedule[j][i] > 0) {

                    //no ir a ninguna parte 2x en casa
                    if (schedule[j][i + 1] > 0) {
                        teamDistance[j] += cityDistances[j][j];
                    }
                    //1 en casa, 1 fuera
                    else {
                        teamDistance[j] += cityDistances[j][Math.abs(schedule[j][i + 1]) - 1];
                    }

                }
                //1 fuera 1 en casa
                else if (schedule[j][i + 1] > 0) {
                    teamDistance[j] += cityDistances[Math.abs(schedule[j][i]) - 1][j];
                }

                //2 fuera
                else {
                    teamDistance[j] += cityDistances[Math.abs(schedule[j][i]) - 1][Math.abs(schedule[j][i + 1]) - 1];
                }

                //Si el último juego es fuera, regresar a casa
                if (i == (2 * n - 4) && schedule[j][i + 1] < 0) {
                    teamDistance[j] += cityDistances[j][Math.abs(schedule[j][i + 1]) - 1];
                }
            }

            //Imprimir la distancia de viaje de cada equipo
            System.out.println("El equipo " + (j + 1) + " tiene una distancia total de viaje de: " + teamDistance[j]);
            totalDistance += teamDistance[j];
        }
        //Distancia total recorrida por todos los equipos
        System.out.println();
        System.out.println("Distancia total de viaje del equipo: " + totalDistance);
        System.out.println();
        //Tiempo total de cálculo
        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo total: " + (endTime - startTime) + "ms");

    }

}