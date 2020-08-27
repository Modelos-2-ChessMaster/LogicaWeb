/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.logica.controlador;

import com.mycompany.logica.fichas.Alfil;
import com.mycompany.logica.fichas.Caballo;
import com.mycompany.logica.fichas.Ficha;
import com.mycompany.logica.fichas.Peon;
import com.mycompany.logica.fichas.Reina;
import com.mycompany.logica.fichas.Rey;
import com.mycompany.logica.fichas.Tablero;
import com.mycompany.logica.fichas.Torre;

/**
 *
 * @author AndresFWilT
 */
public class Controlador {

    static boolean[][] FJ, FE, aux, FichasJCHECK;
    static String[][] FichasJ, FichasE, auxs;
    char posX, posY;
    static int xAct, yAct;
    private String FichaMovida, FichaCambiar;
    private static boolean[] PeonesJ1, PeonesJ2;
    Ficha f;
    Tablero T;
    Caballo c;
    Alfil a;
    Torre t;
    Rey k;
    Reina q;
    Peon p;
    int xA, yA, ctdBoprimido;

    public Controlador() {
    }

    public void crearTablero() {
        //Objetos con las fichas
        //Se settea el tamaño de la matriz de botones
        FichasJ = new String[8][8];
        FichasE = new String[8][8];
        //Metodo que llena de Strings una matriz de 8*8 tanto de fichas del jugador como del enemigo para poder identificar y asi mover
        LlenarStringFichas();
        FJ = new boolean[8][8];
        FE = new boolean[8][8];
        PeonesJ1 = new boolean[8];
        PeonesJ2 = new boolean[8];
        //Metodo que llena de Boolean una matriz de 8*8 de fichas del enemigo
        LlenarBooleanFichas();
    }

    private void LlenarStringFichas() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 0) {
                    if (j == 0 || j == 7) {
                        FichasE[i][j] = "T";
                    }
                    if (j == 1 || j == 6) {
                        FichasE[i][j] = "C";
                    }
                    if (j == 2 || j == 5) {
                        FichasE[i][j] = "A";
                    }
                    if (j == 3) {
                        FichasE[i][j] = "Q";
                    }
                    if (j == 4) {
                        FichasE[i][j] = "K";
                    }
                } else if (i == 1) {
                    FichasE[i][j] = "P";
                } else if (i == 7) {
                    if (j == 0 || j == 7) {
                        FichasJ[i][j] = "T";
                    }
                    if (j == 1 || j == 6) {
                        FichasJ[i][j] = "C";
                    }
                    if (j == 2 || j == 5) {
                        FichasJ[i][j] = "A";
                    }
                    if (j == 3) {
                        FichasJ[i][j] = "K";
                    }
                    if (j == 4) {
                        FichasJ[i][j] = "Q";
                    }
                } else if (i == 6) {
                    FichasJ[i][j] = "P";
                } else {
                    FichasJ[i][j] = null;
                    FichasE[i][j] = null;
                }
            }

        }
    }

    private void LlenarBooleanFichas() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 0 || i == 1) {
                    FE[i][j] = true;
                } else {
                    FE[i][j] = false;
                }
                if (i == 6 || i == 7) {
                    FJ[i][j] = true;
                } else {
                    FJ[i][j] = false;
                }
                PeonesJ1[i] = true;
                PeonesJ2[i] = true;
            }
        }
    }

    public void AccionEjecutada(String Coordenadas) {

        ctdBoprimido++;

        //ImpresionArreglos();
        Tablero T = new Tablero(Coordenadas, FJ, FE, FichasJ, FichasE, PeonesJ1, PeonesJ2, ctdBoprimido);

        if (ctdBoprimido % 2 == 0) { //Boton escogido por el jugador a mover

            ImpresionArreglos();
            ActualizarMatricez(Coordenadas);
            this.FJ = T.getFJ();
            this.FE = T.getFE();
            this.FichasJ = T.getFichasJ();
            this.FichasE = T.getFichasE();
            this.FichaMovida = T.getFichaMovida();
            this.xA = T.getxA();
            this.yA = T.getyA();
            this.xAct = T.getXact();
            this.yAct = T.getYact();
            System.out.println("Coordenadas antiguas: " + xA + " " + yA + " , coordenadas actuales: " + xAct + " " + yAct);

            AnalasisMovimiento(Coordenadas);
            ImpresionArreglos();
            T.ResetearPosibleMov();
            if (T.TerceraRevision(xAct, yAct, FE, FJ, FichasE, FichasJ, ctdBoprimido)) { //CHECK
                if (T.retornarMate()) {
                    // v.Avisos(5);
                } else {
                    //v.Avisos(4);
                    ImpresionArreglos();
                }
            }

        } else {    //Ficha escogida
            if (T.PrimeraRevision() == true) {      //Booleano que verifica si el usuario escogio una ficha suya
                ImpresionArreglos();
                Movimiento(Coordenadas);                 //Metodo que analiza que ficha fue escogida y en que posicion ponerla
                if (T.SegundaRevision() == true) {
                    aux = T.RetornarPosiblesMovimientos();
                    PeonesJ1 = T.retornarBooleanP1();
                    PeonesJ2 = T.retornarBooleanP2();

                } else {
                    //v.Avisos(2);
                    ctdBoprimido--;
                    T.ResetBoolP();
                }

            } else {
                //v.Avisos(1);
                ctdBoprimido--;
            }

        }

    }

    private void AnalasisMovimiento(String N) {//Controlador botones movimientos

        ActualizacionJugador();
    }

    private void Movimiento(String Coordenada) {
        Tablero T = new Tablero(Coordenada, FJ, FE, FichasJ, FichasE, PeonesJ1, PeonesJ2, ctdBoprimido);
        T.AnalisisMovimiento();
    }

    private void ActualizacionJugador() { //Metodo que cambia los boolean y las fichas del jugador actual al otro jugador
        aux = FJ;
        FJ = FE;
        FE = aux;
        auxs = FichasJ;
        FichasJ = FichasE;
        FichasE = auxs;
    }

    private void ImpresionArreglos() {
        System.out.println(" Boolean FJ ");
        //Boolean FJ
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(FJ[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println(" Boolean FE ");
        //Boolean FE
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(FE[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println(" Boolean FichasJ ");
        //String FJ
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(FichasJ[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println(" Boolean FichasE ");
        //String  FE
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(FichasE[i][j] + " ");
            }
            System.out.println(" ");
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.println(" ");
            }
        }
    }

    private void ActualizarMatricez(String Nombre) {
        Tablero T = new Tablero(Nombre, FJ, FE, FichasJ, FichasE, PeonesJ1, PeonesJ2, ctdBoprimido);
        T.ActualizarMatricez(Nombre);
    }
}
