call g++ -c -I"C:\Program Files\Java\jdk1.8\include" -I"C:\Program Files\Java\jdk1.8\include\win32" NativeInput.cpp
call g++ -Wl,--add-stdcall-alias -shared -o NativeInput.dll NativeInput.o
copy /y NativeInput.dll ..\src\main\resources
del NativeInput.o
del NativeInput.dll