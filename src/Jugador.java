
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

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
                    hayGrupos = true;
                    mensaje = "Los grupos que se encontraron fueron:\n";
                }
                mensaje += Grupo.values()[contadores[i]] + " de " + NombreCarta.values()[i] + "\n";
            }
        }

        return mensaje;
    }

    public String GetEscaleras() {
        Map<Pinta, List<Carta>> cartasPorPinta = new HashMap<>();
        for (Carta c : carta) {
            cartasPorPinta.computeIfAbsent(c.getpinta(), k -> new ArrayList<>()).add(c);
        }

        StringBuilder resultado = new StringBuilder();
        for (Map.Entry<Pinta, List<Carta>> entry : cartasPorPinta.entrySet()) {
            List<Carta> cartas = entry.getValue();
            cartas.sort((c1, c2) -> Integer.compare(c1.getValor(), c2.getValor()));

            int longitudEscalera = 1;
            for (int i = 1; i < cartas.size(); i++) {
                if (cartas.get(i).getValor() == cartas.get(i - 1).getValor() + 1) {
                    longitudEscalera++;
                } else {
                    if (longitudEscalera >= 2) {
                        resultado.append("Escalera de ").append(entry.getKey()).append(": ");
                        for (int j = i - longitudEscalera; j < i; j++) {
                            resultado.append(cartas.get(j).getNombre()).append(" ");
                        }
                        resultado.append("\n");
                    }
                    longitudEscalera = 1;
                }
            }
            if (longitudEscalera >= 3) {
                resultado.append("Escalera de ").append(entry.getKey()).append(": ");
                for (int j = cartas.size() - longitudEscalera; j < cartas.size(); j++) {
                    resultado.append(cartas.get(j).getNombre()).append(" ");
                }
                resultado.append("\n");
            }
        }

        if (resultado.length() == 0) {
            return "No se encontraron escaleras.";
        } else {
            return resultado.toString();
        }
    }

    public String calcularPuntaje() {
        // Organizar cartas por pinta
        Map<Pinta, List<Carta>> cartasPorPinta = new HashMap<>();
        for (Carta c : carta) {
            cartasPorPinta.computeIfAbsent(c.getpinta(), k -> new ArrayList<>()).add(c);
        }

        // Identificar cartas en figuras
        Set<Carta> cartasEnFiguras = new HashSet<>();

        // Buscar grupos
        for (NombreCarta nombre : NombreCarta.values()) {
            long count = Arrays.stream(carta)
                    .filter(c -> c.getNombre() == nombre)
                    .count();
            if (count >= 2) {
                cartasEnFiguras.addAll(Arrays.stream(carta)
                        .filter(c -> c.getNombre() == nombre)
                        .collect(Collectors.toList()));
            }
        }

        for (List<Carta> cartas : cartasPorPinta.values()) {
            cartas.sort((c1, c2) -> Integer.compare(c1.getValor(), c2.getValor())); 
            int longitudEscalera = 1;
            for (int i = 1; i < cartas.size(); i++) {
                if (cartas.get(i).getValor() == cartas.get(i - 1).getValor() + 1) {
                    longitudEscalera++;
                } else {
                    if (longitudEscalera >= 2) {
                        cartasEnFiguras.addAll(cartas.subList(i - longitudEscalera, i));
                    }
                    longitudEscalera = 1;
                }
            }
            if (longitudEscalera >= 3) {
                cartasEnFiguras.addAll(cartas.subList(cartas.size() - longitudEscalera, cartas.size()));
            }
        }

        int puntaje = 0;
        for (Carta c : carta) {
            if (!cartasEnFiguras.contains(c)) {
                puntaje += valorCarta(c);
            }
        }

        return "su puntaje es: " + puntaje;
    }

    private int valorCarta(Carta c) {
        NombreCarta nombre = c.getNombre();
        switch (nombre) {
            case AS:
            case JACK:
            case QUEEN:
            case KING:
                return 10;
            default:
                return c.getValor();
        }
    }
}
