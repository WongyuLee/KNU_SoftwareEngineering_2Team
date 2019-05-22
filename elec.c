#include <stdio.h>
#include <wiringPi.h>
 
#define LAMP 20
 
int main(void)
{
    wiringPiSetupGpio();
    pinMode(LAMP, OUTPUT);
 
    printf("********** LAMP Control **********\n");
    printf("*** STOP: [Ctrl] + [c]");
 
    while(1)
    {
        digitalWrite(LAMP, 0);
        delay(3000);
        digitalWrite(LAMP, 1);
        delay(3000);
    }
 
    return 0;
}
