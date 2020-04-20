#include <stdio.h>
#include <stdlib.h>
#include "findPrimeDivisor.c"
#include "gmp_findPrimeDivisor.c"
#include <string.h>
#include "pthread.h"
#include "semaphore.h"
#include "list.h"

#define BUFFERSIZE 8
/************************************************/
/* Read the input in test.txt
/* Write the output in out.txt
/************************************************/


str_node_t *bufferIn[BUFFERSIZE];
str_node_t *bufferOut[BUFFERSIZE];
pthread_mutex_t mutexIn;
pthread_mutex_t mutexOut;
sem_t semInFull;
sem_t semInEmpty;
sem_t semOutFull;
sem_t semOutEmpty;
int threadInFinish = 0;
int calculationThreadsFinish=0;
unsigned int bufferInAdd = 0;
unsigned int bufferInTake = 0;
unsigned int bufferOutAdd = 0;
unsigned int bufferOutTake = 0;

char *takeFromBufferIn() {
    char *numberToEvaluate = bufferIn[bufferInTake % BUFFERSIZE]->value;
    bufferInTake++;
    return numberToEvaluate;
}

void putToBufferIn(str_node_t *add) {
    bufferIn[bufferInAdd % BUFFERSIZE] = add;
    bufferInAdd++;
}

str_node_t *takeFromBufferOut() {
    str_node_t *node = bufferOut[bufferOutTake % BUFFERSIZE];
    bufferOutTake++;
    return node;
}

void putToBufferOut(str_node_t *add) {
    bufferOut[bufferOutAdd % BUFFERSIZE] = add;
    bufferOutAdd++;
}

void *findFactors(void *input) {
    //Check if the thread which fills bufferIn has finished and if bufferIn is empty
    while (!threadInFinish && sem_getvalue(&semInFull,NULL)!=0) {
        //Take the number to factorize from bufferIn

        sem_wait(&semInFull);
        pthread_mutex_lock(&mutexIn);
        char *line = takeFromBufferIn();//Critical
        pthread_mutex_unlock(&mutexIn);
        sem_post(&semInEmpty);

        int *primetab = (int *) input;  //pass primetab in argument of the function
        str_node_t *divList = malloc(sizeof(str_node_t *));
        unsigned int str_len = strlen(line);
        line[str_len - 1] = 0;   // remove trailing newline etc

        if (str_len <= 16) { //Small number with less than 16 digits
            uint64_t tmp = strtoull(line, NULL, 10);
            if (tmp > 0) {
                divList = findPrimeDivisor(tmp, primetab);
            }
        } else if (str_len < 100) { //Big number with more than 16 digits
            mpz_t x;
            mpz_init_set_str(x, line, 10);
            divList = rho_factors(x);
            mpz_clear(x);
        }

        //put the factorization in bufferOut
        addNode_str(line, &divList);
        sem_wait(&semOutEmpty);
        pthread_mutex_lock(&mutexOut);
        putToBufferOut(divList);//Critical
        pthread_mutex_unlock(&mutexOut);
        sem_post(&semOutFull);
    }
}

void *fillInBufferIn(void *input) {
    FILE *fr = (FILE *) input;
    char line[1024];
    while (fgets(line, 128, fr) != NULL) {
        str_node_t *node = malloc(sizeof(str_node_t *));
        strcpy_s(node->value, sizeof(char) * 1024, line);
        sem_wait(&semInEmpty);
        pthread_mutex_lock(&mutexIn);
        putToBufferIn(node);//Critical
        pthread_mutex_unlock(&mutexIn);
        sem_post(&semInFull);
    }
    threadInFinish = 1;
}

void *copyInFileOut(void *input) {
    FILE *fw = (FILE *) input;
    while (!calculationThreadsFinish) {
        str_node_t *divList=malloc(sizeof(str_node_t*));
        sem_wait(&semOutFull);
        pthread_mutex_lock(&mutexOut);
        divList=takeFromBufferOut();//Critical
        pthread_mutex_unlock(&mutexOut);
        sem_post(&semOutEmpty);
        while (divList != NULL) {
            fprintf(fw, " %s", divList->value);
            str_node_t *tmp = divList;
            divList = divList->next;
            free(tmp);
        }
        fprintf(fw, "\n");
    }
}

/************************************************/
/* Read the input in test.txt
/* Write the output in out.txt
/************************************************/
int main(int argc, char *argv[]) {
    FILE *fr, *fw;

    int numberOfThreads = 4;
    if (0 == 1)        //TODO: trouver la condition du if pour vérifier la présence d'un arg '-N'
        numberOfThreads = argv[2];

    //Init threads, mutex and semaphore
    if (phtread_mutex_init(&mutexIn, NULL) != 0)
        return -2;
    if (phtread_mutex_init(&mutexIn, NULL) != 0)
        return -2;

    if (sem_init(&semInFull, 0, 0) == -1)
        return -3;
    if (sem_init(&semInEmpty, 0, 8) == -1)
        return -3;
    if (sem_init(&semOutFull, 0, 0) == -1)
        return -3;
    if (sem_init(&semOutEmpty, 0, 8) == -1)
        return -3;
    pthread_t threads[numberOfThreads];
    pthread_t threadIn;
    pthread_t threadOut;

    fr = fopen("test.txt", "r");
    fw = fopen("out.txt", "w");
    if (fr == NULL || fw == NULL) return -1;

    int *primetab = primeList();
    pthread_create(&threadIn,NULL,&fillInBufferIn,fr);
    for (int i=0;i<numberOfThreads;i++){
        pthread_create(&threads[i],NULL,&findFactors,primetab);
    }
    pthread_create(&threadOut,NULL,&copyInFileOut,fw);
    pthread_join(threadIn,NULL);
    for (int i=0;i<numberOfThreads;i++){
        pthread_join(&threads[i]);
    }
    calculationThreadsFinish=1;
    pthread_join(threadOut,NULL);

    //loop through each line
    /*while(fgets(line,128,fr)!= NULL){
        int str_len = strlen(line);
        line[str_len-1] = 0;   // remove trailing newline etc

        fprintf(fw,"%s",line);

        if(str_len<=16){ //Small number with less than 16 digits
            uint64_t tmp = strtoull(line,NULL,10);
            if(tmp>0){
                divList = findPrimeDivisor(tmp, primetab);
            }
        }else if(str_len<100){ //Big number with more than 16 digits
            mpz_t x;
            mpz_init_set_str(x,line,10);
            divList = rho_factors(x);
            mpz_clear(x);
        }

        //loop through the results and append them to the output file
        while(divList!=NULL){
            fprintf(fw, " %s", divList->value);
            str_node_t* tmp = divList;
            divList=divList->next;
            free(tmp);
        }
        fprintf(fw,"\n");

    }*/

    sem_destroy(&semInFull);
    sem_destroy(&semOutFull);
    sem_destroy(&semInEmpty);
    sem_destroy(&semOutEmpty);
    pthread_mutex_destroy(&mutexIn);
    pthread_mutex_destroy(&mutexOut);
    free(primetab); // free the memory alocated for the prime array
    fclose(fr);
    fclose(fw);

}


