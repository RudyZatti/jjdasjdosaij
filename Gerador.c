#include <stdio.h>
const int M = 2;
const int c = 1;
const int a = 1;
int genNext(int num);

int main() {
    int seed = 0;
    int generations[1000];
    generations[0] = genNext(seed);
    seed = generations[0];
    for(int i = 1; i < 1000; i++){
        generations[i] = genNext(seed);
        seed = generations[i];
    }
    FILE *file;
    file = fopen("gerado", "w");
    for(int i = 0; i < 1000; i++){
        fprintf(file, "%d ", generations[i]);
    }
    fclose(file);

}

int genNext(int num){
    return ((a * num) + c) % M;
}