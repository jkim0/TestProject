#include<stdio.h>
#include"err.h"

void main()
{

	char a = 'c';
	char *b= &a;
	print_errno(b);
}
