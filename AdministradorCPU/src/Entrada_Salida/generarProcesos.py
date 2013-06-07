import random
import sys

a = -1
b = -1

while(a == -1):
  print("Introduzca la cantidad de procesos de tiempo real")
  a = int(raw_input())

while(b == -1):
  print("Introduza la cantidad de procesos normales")
  b = int(raw_input())

f = file("procesos13.xml", "w")

f.write("<procesos>\n")
'''Ciclo para los procesos de tiempo real, se crean los procesos de tiempo real
con numeros randon para la prioridad estatica, la cantidad de veces que va a CPU
o Entrada/Salida y la cantidad de ticks que estara en CPU o Entrda/Salida'''
for i in range(1, a+1): 
    f.write("\t<proceso>\n")
    f.write("\t\t<pid>"+ str(i)  +"</pid>\n")
    f.write("\t\t<esTiempoReal>true</esTiempoReal>\n")
    f.write("\t\t<prioridadEstatica>"+ str(random.randint(1, 99)) +"</prioridadEstatica>\n")
    for j in range(0, random.randint(1, 5+1)):
      if j % 2 == 0:
        f.write("\t\t<tiempoCPU>"+ str(random.randint(100,2000))  +"</tiempoCPU>\n") 
      else:
        f.write("\t\t<tiempoIO>"+ str(random.randint(100,2000))  +"</tiempoIO>\n") 
    f.write("\t\t<tiempoEntrada>"+ str(random.randint(0,100))  +"</tiempoEntrada>\n")
    f.write("\t</proceso>\n")
    
'''Ciclo para los procesos normales, se crean los procesos normales
con numeros randon para la prioridad estatica, la cantidad de veces que va a CPU
o Entrada/Salida y la cantidad de ticks que estara en CPU o Entrda/Salida'''
for i in range(1, b+1):
    f.write("\t<proceso>\n")
    f.write("\t\t<pid>"+ str(i+a)  +"</pid>\n")
    f.write("\t\t<esTiempoReal>false</esTiempoReal>\n")
    f.write("\t\t<prioridadEstatica>"+ str(random.randint(100, 139)) +"</prioridadEstatica>\n")
    for j in range(0,random.randint(1,5)):
      if j % 2 == 0:
	f.write("\t\t<tiempoCPU>"+ str(random.randint(100,2000))  +"</tiempoCPU>\n") 
      else:
	f.write("\t\t<tiempoIO>"+ str(random.randint(100,2000))  +"</tiempoIO>\n") 
    f.write("\t\t<tiempoEntrada>"+ str(random.randint(0,100))  +"</tiempoEntrada>\n")
    f.write("\t</proceso>\n")

f.write("</procesos>\n")
