CFLAG = -L. -lme

one : 
	gcc -fPIC -c one.c


two : 
	gcc -shared -WI,-soname,libme.so.0 -o libme.so.0.0.0 one.o


three :
	ln -s libme.so.0.0.0 libme.so
	ln -s libme.so.0.0.0 libme.so.0


link : 
	sudo cp libme.so ../../../../../../../lib
	sudo ldconfig


comfile :
	gcc -o two two.c $(CFLAG) 

all : one two three link comfile

clean:
	rm -r one.o
	rm -r two
	rm -r libme.so
