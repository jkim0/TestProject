print.o : print.c
	gcc -fPIC -c print.c

libprint.so : print.o
	gcc -shared -o libprint.so print.o

myPrint : myPrint.c
	gcc -o myPrint myPrint.c -L. -lprint

install :
	sudo cp libprint.so /usr/local/lib
	sudo sh -c "echo '/usr/local/lib' >> /etc/ld.so.conf"
	sudo ldconfig

all: print.o libprint.so install myPrint

clean :
	rm -rf print.o libprint.so myPrint
	sudo rm -rf /usr/local/lib/libprint.so
