my_lib.o : my_lib.c
	gcc -c my_lib.c 
my_lib.a : my_lib.o
	ar rc libmy_lib.a my_lib.o
main: main.c libmy_lib.a
	gcc main.c -o main -L./ -lmy_lib
clear :
	rm libmy_lib.a main
