package fr.lifl.magique.gui.file;
import java.util.*;
import java.io.*;
public class NotFormatedException extends IOException {
	public NotFormatedException (int num,String s) {super("Error line "+num+":"+s);}
}
