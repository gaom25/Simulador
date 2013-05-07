import random
import sys

numero_proc = sys.argv[1]

f = file("procesos.xml", "w")

f.write("<procesos>\n")

for i in range(1, int(numero_proc) +1):
    f.write("\t<proceso>\n")
    f.write("\t\t<pid>"+ str(i)  +"</pid>\n")

    esTiempoReal = random.choice([True, False])

    f.write("\t\t<esTiempoReal>"+ ("true" if esTiempoReal else "false")  +"</esTiempoReal>\n")
    f.write("\t\t<prioridadEstatica>"+ (str(random.randint(1, 99)) if esTiempoReal else str(random.randint(100, 139)))  +"</prioridadEstatica>\n")
    f.write("\t\t<tiempoCPU>"+ str(random.randint(1,20))  +"</tiempoCPU>\n")
    f.write("\t\t<tiempoEntrada>"+ str(i)  +"</tiempoEntrada>\n")
    f.write("\t</proceso>\n")

f.write("</procesos>\n")