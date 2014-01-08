#include<stdio.h>
#include"one.h"

void main()
{
    char Name[20];

    printf("이름 입력 : ");
    scanf("%s",Name);

    prtmsg(Name);
}
