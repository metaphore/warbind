#define WINVER 0x0500

#include <jni.h>
#include <stdio.h>	//remove?
#include <windows.h>
#include "NativeInput.h"

using namespace std;

// Implementation of native method sayHello() of HelloJNI class
JNIEXPORT void JNICALL Java_com_metaphore_NativeInput_performClick(JNIEnv *env, jobject thisObj, jint x, jint y) {
//	cout << "performing click " << x << ":" << y << endl;
	
	// fflush(stdout);
	// POINT p;
	// GetCursorPos(&p);	
	// cout << "here i am " << p.x << " " << p.y << endl;
	// fflush(stdout);
	// HWND parentWindow = FindWindow("Warcraft III", "Warcraft III");
	// LPARAM lParam = MAKELPARAM(p.x, p.y);
	// SendMessage(parentWindow, WM_LBUTTONDOWN, MK_LBUTTON, lParam);
	// SendMessage(parentWindow, WM_LBUTTONUP, 0, lParam);

	// POINT p;
	// GetCursorPos(&p);
	// INPUT ip = {};
	// ip.type = INPUT_MOUSE;
	// ip.mi.dx = x - p.x; 
	// ip.mi.dy = y - p.y;
	// ip.mi.dwFlags = MOUSEEVENTF_MOVE;
	// SendInput(1, &ip, sizeof(INPUT));

	BlockInput(TRUE);
	Sleep(35);
	BlockInput(FALSE);
	return;
}