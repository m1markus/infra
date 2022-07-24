// gcc -Wall -o led23 led23.c -lwiringPi
#include <wiringPi.h>
int main (void)
{
  int led1 = 4;
  wiringPiSetup () ;
  pinMode (led1, OUTPUT) ;
  for (;;)
  {
    digitalWrite (led1, HIGH) ; delay (500) ;
    digitalWrite (led1,  LOW) ; delay (500) ;
  }
  return 0 ;
}
