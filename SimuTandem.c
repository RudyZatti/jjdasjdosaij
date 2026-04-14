#include <stdio.h>
double evento(double *x, int tMin, int tMax, int *counter);
int genNext(int num);

struct Atendente{
    //Daria pra fazer por array mas acho que isso é mais modular
    double tAtendimento; // Tempo até acabar o atendimento, -1 = livre
    double tMax;
    double tMin;
};

int main(){
    double current_X = 1.0;
    const int totalGens = 100000;

    double tempo = 0.0;
    int clientesPerdidos = 0;
    int maxFilaA = 3;
    int maxFilaB = 5;
    double tempoAtendendo = 0.0;
    double tempoOcioso = 0.0;
    struct Atendente a1 = {-1.0, 3.0, 4.0};
    struct Atendente a2 = {-1.0, 3.0, 4.0};
    struct Atendente b1 = {-1.0, 2.0, 3.0};
    int emFilaA = 0;
    int emFilaB = 0;
    int nextEvent = 3; // 2 = chegada, 3 = saida
    double nextChegada = 1.5; //Agenda primeira chegada
    for(int count = 0; count < totalGens;){
        if (nextChegada == 0.0){ //Quando a chegada acontece
            if(emFilaA == maxFilaA)
                clientesPerdidos++; //Cliente Perdido
            else
                emFilaA++; //Adiciona o cliente que recém chegou na fila
            nextChegada = evento(&current_X, 1, 4, &count); //Agenda a próxima
        }
        
        if(a1.tAtendimento == 0.0){ //Término do atendimento do a1
            emFilaA--; // Tira da fila
            emFilaB++;
            a1.tAtendimento = -1; //Bota o atendente como livre
        }
        if(a2.tAtendimento == 0.0){
            //Mesma coisa que com o a1
            emFilaA--;
            emFilaB++;
            a2.tAtendimento = -1;
        }

        if (b1.tAtendimento == 0.0){
            emFilaB--;
            b1.tAtendimento = -1;
        }
        
        if (emFilaB > maxFilaB){
            clientesPerdidos += emFilaB - maxFilaB;
            emFilaB = maxFilaB;
        }
            
        if(b1.tAtendimento == -1.0 && emFilaB > 0)
            b1.tAtendimento = evento(&current_X, b1.tMin, b1.tMax, &count);

        if ((a1.tAtendimento == -1 && emFilaA > 0))
            if(!(a2.tAtendimento > -1 && emFilaA == 1))
                a1.tAtendimento = evento(&current_X, a1.tMin, a1.tMax, &count);
        //Tem 2 IFs pra garantir que o a1 não começe a atender um cliente que já esteja com o a2
        
        if (a2.tAtendimento == -1.0 && emFilaA > 1)
            a2.tAtendimento = evento(&current_X, a2.tMin, a2.tMax, &count);
        
        //Abaixo acontece a progressão do tempo.
        double nextTempo;
        if(a1.tAtendimento > 0.0 && a1.tAtendimento <= nextChegada){
            if(a2.tAtendimento > 0.0 && 
                a2.tAtendimento < a1.tAtendimento)
                nextTempo = a2.tAtendimento;
            else
                nextTempo = a1.tAtendimento;
        }else
            nextTempo = nextChegada;
        
        if(a1.tAtendimento > -1.0)
            a1.tAtendimento -= nextTempo;
        
        if(a2.tAtendimento > -1.0)
            a2.tAtendimento -= nextTempo;
        
        nextChegada -= nextTempo;
        tempo += nextTempo;
        if(a1.tAtendimento > -1.0 || a2.tAtendimento > -1.0)
            tempoAtendendo += nextTempo;
        else
            tempoOcioso += nextTempo;
    }
    printf("Tempo total: %lf\n", tempo);
    printf("Clientes perdidos: %d\n", clientesPerdidos);
    printf("Tempo atendendo: %lf\n", tempoAtendendo);
    printf("Tempo ocioso: %lf\n", tempoOcioso);
    return 0;
}

double evento(double *x, int tMin, int tMax, int *counter){
    //tMin = 2 -> chegada
    //tMin = 3 -> saida
    *x = genNext(*x*100)/100.0;
    (*counter)++;
    return (tMin + (*x*(tMax - tMin)));
}

int genNext(int num){
    const int M = 100;
    const int c = 1;
    const int a = 7;
    return ((a * num) + c) % M;
}



