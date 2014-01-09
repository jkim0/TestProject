all : my_lib.o libmy_lib.so main

my_lib.o: my_lib.c 
	gcc -fPIC -c my_lib.c
libmy_lib.so: my_lib.o 
	gcc -shared -W1,-soname,libmy_lib.so -o libmy_lib.so.1.0.1 my_lib.o
	sudo cp libmy_lib.so.1.0.1 ../../../../../../lib
	sudo ldconfig
main: main.c 
	gcc -o main main.c -lmy_lib

clean :
	rm libmy_lib.so.1.0.1 main my_lib.o
	
