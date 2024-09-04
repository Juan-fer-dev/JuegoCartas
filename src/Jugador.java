import java.util.Random;

import javax.swing.JPanel;

public class Jugador {

    private final int TOTAL_CARTAS = 10;
    private final int MARGEN = 10;
    private final int DISTANCIA = 50;

    private Carta[] carta = new Carta[TOTAL_CARTAS];

    private Random r = new Random();

    public void Repartir() {
        /*
         * for (int i = 0; i < TOTAL_CARTAS; i++) {
         * carta[i] = new Carta(r);
         * }
         */
        int i = 0;
        for (Carta c : carta) {
            carta[i++] = new Carta(r);
        }
    }

    public void mostrar(JPanel pnl) {
        int i = 0;
        for (Carta c : carta) {
            c.mostrar(pnl, MARGEN + i++ * DISTANCIA, MARGEN);
        }
        pnl.repaint();
    }

    public String GetGrupos() {
        String mensaje = "No se encontraron grupos";
        int[] contadores = new int[NombreCarta.values().length];

        for (Carta c : carta) {
            contadores[c.getNombre().ordinal()]++;
        }

        boolean hayGrupos = false;
        for (int i = 0; i < contadores.length; i++) {
            if (contadores[i] >= 2) {
                if (!hayGrupos) {
                    hayGrupos=true;
                    mensaje ="Los grupos que se encontraron fueron:\n";
                }
                mensaje += Grupo.values()[contadores[i]]+ " de " + NombreCarta.values()[i] + "\n";
            }
        }

        return mensaje;
    }

}
