package org.example;
import java.io.PrintWriter;
import java.io.FileWriter;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.Scanner;

/**
 * Clase que implementa el algoritmo para resolver el problema específico.
 *
 * @version 2.5
 * @author Juan Camilo Velez
 * @author Daniela Orrego
 */
public class Algoritmo {
    /**
     * Constructor predeterminado de la clase Algoritmo.
     * Este constructor no realiza ninguna acción específica.
     */
    public Algoritmo() {
        // Puedes agregar algún comentario aquí si es necesario.
    }

    /**
     * Método principal que inicia la ejecución del algoritmo.
     *
     * @param args Argumentos de la línea de comandos (no se utilizan en este caso).
     * @throws IOException Si hay un error de entrada/salida durante la ejecución.
     */
    public static void main(String[] args) throws IOException {
        // Especifica la ubicación inicial para el selector de archivos
        String initialDirectory = "C:\\Users\\velez\\IdeaProjects\\ProyectoADA\\untitled\\Matrices";

        // Crea un selector de archivos
        JFileChooser fileChooser = new JFileChooser(initialDirectory);
        fileChooser.setDialogTitle("SELECCIONADOR DE ARCHIVOS");

        // Filtra para mostrar solo archivos .txt
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Solo Permite Archivos (*.txt)", "txt");
        fileChooser.setFileFilter(filter);

        // Muestra el diálogo de selección de archivos
        int result = fileChooser.showOpenDialog(null);

        if (result != JFileChooser.APPROVE_OPTION) {
            System.out.println("Operación Cancelada por el Usuario.");
            return;
        }

        // Obtiene el archivo seleccionado
        File selectedFile = fileChooser.getSelectedFile();
        String fileName = selectedFile.getAbsolutePath();
        String outputFileName = GuardarCopia(fileName);

        BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
        int n = Integer.parseInt(reader.readLine());
        reader.close();

        long HoraInicio = System.currentTimeMillis();

        Scanner readFile = new Scanner(selectedFile);

        // Leer el máximo consecutivo de juegos en casa/fuera
        int min = readFile.nextInt();
        int ha = readFile.nextInt();

        while (readFile.hasNextInt()) {
            readFile.nextInt();
            n++;
        }
        n = (int) Math.sqrt(n);
        readFile.close();

        int[][] DistCiudades = new int[n][n];
        int i;
        int j;

        readFile = new Scanner(selectedFile);

        readFile.nextLine();

        // la distancia entre las ciudades
        for (j = 0; j < n; j++) {
            for (i = 0; i < n; i++) {
                DistCiudades[i][j] = readFile.nextInt();
            }
        }
        readFile.close();



        int [][]PesoNodo= new int [n][1];
        int PesoIndice = 0;

        // Obtener los pesos de los nodos para cada ciudad.
        for(j=0;j<n;j++){
            for(i=0;i<n;i++){
                PesoNodo[j][0] += DistCiudades[i][j];
            }
        }

        // Inicializar peso del nodo mínimo
        int PesoTiemp = 10000000;

        // Identificar el peso del nodo mínimo
        for(i=0;i<n;i++){
            if(PesoNodo[i][0]<PesoTiemp){
                PesoTiemp = PesoNodo[i][0];
                PesoIndice = i;
            }
        }

        System.out.println(PesoNodo[PesoIndice][0]+" es el peso mínimo del nodo que pertenece al índice " + PesoIndice);
        System.out.println();

        // Solucion PTorneo
        int[]CaminoEleg = new int [n+1];
        int distancia = 0;

        // Llenar la ruta inicial
        for(i=1;i<n;i++){
            if(i!=PesoIndice){
                CaminoEleg[i] = i;
            }
        }

        // Establecer el peso mínimo del  nodo inicial/final
        CaminoEleg [0] = PesoIndice;
        CaminoEleg [n] = PesoIndice;

        // Calcular la distancia total
        for(i=0;i<n;i++){
            distancia += DistCiudades[CaminoEleg[i]][CaminoEleg[i+1]];
        }

        int TiempDistancia=0;
        int [] TiempCamino = new int[n+1];
        int Tiempte;

        // Copiar ruta de inicio
        for(i=0;i<n+1;i++){
            TiempCamino[i] = CaminoEleg[i];
        }

        // Iniciar intercambios
        int contar = 0;

        // Comenzar primero aceptar algoritmo
        while(contar!=100){

            for(j=1;j<n-1;j++){
                for(i=1;i<n-1;i++){
                    // Comience a intercambiar nodos, pero deje solo el nodo con peso mínimo de estrella
                    Tiempte=TiempCamino[j];
                    TiempCamino[j]=TiempCamino[i];
                    TiempCamino[i]=Tiempte;
                    TiempDistancia = 0;

                    // Calcular la distancia del nuevo camino propuesto.
                    for(int z=0;z<n;z++){
                        TiempDistancia += DistCiudades[TiempCamino[z]][TiempCamino[z+1]];
                    }
                    // Compruebe si la ruta propuesta es mejor y, de ser así, reemplácela
                    if(TiempDistancia<distancia){
                        for(int b=0;b<n+1;b++){
                            TiempCamino[b]=TiempCamino[b];
                        }
                        distancia = TiempDistancia;

                        System.out.println("Ruta PTorneo más corta encontrada hasta ahora: " + distancia);//a medida que se encuentran mejores caminos...
                    }

                    // Revertir el cambio a Tiempte
                    else{
                        Tiempte=TiempCamino[i];
                        TiempCamino[i]=TiempCamino[j];
                        TiempCamino[j]=Tiempte;
                    }
                }
            }
            // Para el bucle while
            contar++;
        }

        System.out.println();
        System.out.println("La mejor ruta de PTorneo encontrada es (nodos): " );

        // Imprimir la mejor ruta encontrada
        for(int a=0;a<n+1;a++){
            if(a!=n){
                System.out.print(CaminoEleg[a]+ "->");
            }
            else {
                System.out.print(CaminoEleg[a]);
            }
        }
        System.out.println();
        System.out.println("Con una distancia de:" + distancia);

        // El siguiente algoritmo requiere un formato específico de la solución del algoritmo.
        int CaminoForm[] = new int [n];

        // Solo el último nodo tiene el peso mínimo de pasos.
        for(i=0;i<CaminoForm.length;i++){
            CaminoForm[i]=CaminoEleg[i+1];
        }

        System.out.println();
        System.out.println("Reacomodando la ruta para el siguiente algoritmo:");
        System.out.println();

        // Imprime la ruta reformateada
        for(int a=0;a<n;a++){
            if(a!=(n-1))
                System.out.print(CaminoForm[a]+ "->");
            else System.out.print(CaminoForm[a]);
        }
        System.out.println();
        System.out.println();

        int [][]Cronograma = PTorneo.PTorneo(ha,min,CaminoForm,n);

        // Pasar al siguiente algoritmo

        // Obtener distancias
        int []DistanEquipo= new int [n];
        int DistanTotal = 0;

        for(j=0;j<n;j++){
            for(i=0;i<(2*n-3);i++){

                if(i==0 && Cronograma[j][i]<0){
                    // Primer partido fuera de casa
                    // Nota: (programa[j][i])-1) debido a que los números de las ciudades se incrementan en +1 para evitar el problema con -0 = 0
                    DistanEquipo[j] += DistCiudades[j][Math.abs(Cronograma[j][i])-1];
                }

                // 2x partidos en casa o (1 en casa y 1 fuera)
                if(Cronograma[j][i]>0){

                    // no ir a ninguna parte 2x a casa
                    if(Cronograma[j][i+1]>0){
                        DistanEquipo[j] += DistCiudades[j][j];
                    }
                    // 1 en casa, 1 fuera
                    else{
                        DistanEquipo[j] += DistCiudades[j][Math.abs(Cronograma[j][i+1])-1];
                    }
                }
                // 1 lejos 1 en casa
                else if(Cronograma[j][i+1]>0){
                    DistanEquipo[j] += DistCiudades[Math.abs(Cronograma[j][i])-1][j];
                }
                // 2 de distancia
                else{
                    DistanEquipo[j] += DistCiudades[Math.abs(Cronograma[j][i])-1][Math.abs(Cronograma[j][i+1])-1];
                }

                // Si el último partido fue fuera de casa, regrese a casa.
                if(i==(2*n-4) && Cronograma [j][i+1]<0){
                    DistanEquipo[j] += DistCiudades[j][Math.abs(Cronograma[j][i+1])-1];
                }
            }

            // Imprime la distancia recorrida por cada equipo.
            System.out.println("Equipo "+(j+1)+ " tiene una distancia total recorrida de: " + DistanEquipo[j]);
            DistanTotal += DistanEquipo[j];
        }
        // Distancia total recorrida por todos los equipos.
        System.out.println();
        System.out.println("Distancia total de viaje del equipo: " + DistanTotal);
        System.out.println();
        // Tiempo total de cálculo
        long TiempFinal = System.currentTimeMillis();
        System.out.println("Tiempo total: " + (TiempFinal-HoraInicio) + "ms");

        // Guardar la salida en un archivo de texto
        guardarSalidaEnArchivo(outputFileName, CaminoEleg, distancia, DistanEquipo, DistanTotal, TiempFinal - HoraInicio);
    }

    /**
     * Método para guardar una copia del archivo original.
     *
     * @param fileName Nombre del archivo original.
     * @return Nombre del archivo de copia generado.
     */
    private static String GuardarCopia(String fileName) {
        File inputFile = new File(fileName);
        String parentDirectory = inputFile.getParent();
        String inputFileName = inputFile.getName();

        int dotIndex = inputFileName.lastIndexOf('.');
        String nameWithoutExtension = dotIndex == -1 ? inputFileName : inputFileName.substring(0, dotIndex);
        String extension = dotIndex == -1 ? "" : inputFileName.substring(dotIndex);

        return parentDirectory + File.separator + nameWithoutExtension + "C" + extension;
    }

    /**
     * Método para guardar los resultados en un archivo de texto.
     *
     * @param outputFileName Nombre del archivo de salida.
     * @param CaminoEleg Ruta elegida por el algoritmo PTorneo.
     * @param distancia Distancia total de la ruta elegida.
     * @param DistanEquipo Distancias recorridas por cada equipo.
     * @param DistanTotal Distancia total recorrida por todos los equipos.
     * @param tiempoTotal Tiempo total de ejecución.
     */
    private static void guardarSalidaEnArchivo(String outputFileName, int[] CaminoEleg, int distancia, int[] DistanEquipo, int DistanTotal, long tiempoTotal) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFileName))) {
            writer.println("La mejor ruta de PTorneo encontrada es (nodos): ");

            // Imprimir la mejor ruta encontrada
            for (int a = 0; a < CaminoEleg.length; a++) {
                if (a != CaminoEleg.length - 1) {
                    writer.print(CaminoEleg[a] + "->");
                } else {
                    writer.print(CaminoEleg[a]);
                }
            }
            writer.println();
            writer.println("Con una distancia de:" + distancia);
            writer.println();

            // Imprimir la distancia recorrida por cada equipo
            for (int j = 0; j < DistanEquipo.length; j++) {
                writer.println("Equipo " + (j + 1) + " tiene una distancia total recorrida de: " + DistanEquipo[j]);
            }

            // Imprimir la distancia total recorrida por todos los equipos
            writer.println();
            writer.println("Distancia total de viaje del equipo: " + DistanTotal);

            // Imprimir el tiempo total de ejecución
            writer.println();
            writer.println("Tiempo total: " + tiempoTotal + "ms");

            System.out.println("Resultados guardados en el archivo '" + outputFileName + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}