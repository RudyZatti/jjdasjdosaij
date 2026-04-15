import java.io.File;
import java.io.FileWriter;

class FilasTandem{
    private static class Atendente{
        double tAtendimento;
        double tMax;
        double tMin;

        public Atendente(double tMax, double tMin) {
            this.tAtendimento = -1.0;
            this.tMax = tMax;
            this.tMin = tMin;
        }
    }

    private static double current_x = 1.0;
    private static int count = 0;

    public static void main(String[] args) {
        final int totalGens = 100000;

        double tempo = 0.0;
        int clientesPerdidos = 0;
        int clientesPerdidosB = 0;
        final int maxFilaA = 3;
        final int maxFilaB = 5;
        double tempoAtendendo = 0.0;
        double tempoOcioso = 0.0;
        double tempoOciosoB = 0.0;
        double tempoAtendendoB = 0.0;
        Atendente a1 = new Atendente(4.0, 3.0);
        Atendente a2 = new Atendente(4.0, 3.0);
        Atendente b1 = new Atendente(3.0, 2.0);
        int emFilaA = 0;
        int emFilaB = 0;
        double nextChegada = 1.5; // Agenda primeira chegada
        try{
            FileWriter writer = new FileWriter("out.txt");
            while (count < totalGens) {
                if (nextChegada == 0.0) { // Quando a chegada acontece
                    if (emFilaA == maxFilaA)
                        clientesPerdidos++; // Cliente Perdido
                    else
                        emFilaA++; // Adiciona o cliente que recém chegou na fila
                    nextChegada = evento(1.0, 4.0, totalGens, writer); // Agenda a próxima
                }

                if (a1.tAtendimento == 0.0) { // Término do atendimento do a1
                    emFilaA--; // Tira da fila
                    emFilaB++;
                    a1.tAtendimento = -1.0; // Bota o atendente como livre
                }
                if (a2.tAtendimento == 0.0) {
                    // Mesma coisa que com o a1
                    emFilaA--;
                    emFilaB++;
                    a2.tAtendimento = -1.0;
                }

                if (b1.tAtendimento == 0.0) {
                    emFilaB--;
                    b1.tAtendimento = -1.0;
                }

                if (emFilaB > maxFilaB) {
                    clientesPerdidosB += emFilaB - maxFilaB;
                    emFilaB = maxFilaB;
                }

                if (b1.tAtendimento == -1.0 && emFilaB > 0)
                    b1.tAtendimento = evento(b1.tMin, (int) b1.tMax, totalGens, writer);

                if (a1.tAtendimento == -1.0 && emFilaA > 0)
                    if (!(a2.tAtendimento > -1.0 && emFilaA == 1))
                        a1.tAtendimento = evento((int) a1.tMin, (int) a1.tMax, totalGens, writer);
                // Tem 2 IFs pra garantir que o a1 não começe a atender um cliente que já esteja com o a2

                if (a2.tAtendimento == -1.0 && emFilaA > 1)
                    a2.tAtendimento = evento((int) a2.tMin, (int) a2.tMax, totalGens, writer);

                double nextTempo;
                if (a1.tAtendimento > 0.0 && a1.tAtendimento <= nextChegada) {
                    if (a2.tAtendimento > 0.0 && a2.tAtendimento < a1.tAtendimento)
                        nextTempo = a2.tAtendimento;
                    else
                        nextTempo = a1.tAtendimento;
                } else if (a2.tAtendimento > 0.0 && a2.tAtendimento <= nextChegada)
                    nextTempo = a2.tAtendimento;
                else
                    nextTempo = nextChegada;

                if (a1.tAtendimento > -1.0)
                    a1.tAtendimento -= nextTempo;

                if (a2.tAtendimento > -1.0)
                    a2.tAtendimento -= nextTempo;

                nextChegada -= nextTempo;
                tempo += nextTempo;

                // Registra se o tempo passado foi ocioso ou não
                if (a1.tAtendimento > -1.0 || a2.tAtendimento > -1.0)
                    tempoAtendendo += nextTempo;
                else
                    tempoOcioso += nextTempo;

                if (b1.tAtendimento > -1.0)
                    tempoAtendendoB += nextTempo;
                else
                    tempoOciosoB += nextTempo;
            }
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("Tempo total: " + tempo);
        System.out.println("Clientes perdidos: " + clientesPerdidos);
        System.out.println("Tempo atendendo: " + tempoAtendendo);
        System.out.println("Tempo ocioso: " + tempoOcioso);
        System.out.println("Tempo atendendo fila 2: " + tempoAtendendoB);
        System.out.println("Tempo ocioso fila 2: " + tempoOciosoB);
        System.out.println("Clientes perdidos fila 2: " + clientesPerdidosB);
    }

    private static double evento(double tMin, double tMax, int totalGens, FileWriter writer) throws Exception {
        current_x = genNext((int) (current_x * 100)) / 100.0;
        count++;
        if (count >= totalGens) {
            return -100.0;
        }
        writer.write(current_x + "\n");
        return (tMin + (current_x * (tMax - tMin)));
    }

    private static int genNext(int num) {
        final int M = 100;
        final int c = 1;
        final int a = 2;
        return ((a * num) + c) % M;
    }
}


