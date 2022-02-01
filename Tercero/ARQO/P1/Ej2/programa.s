.data 0
num0:  .word 1  # posic 0
num1:  .word 2  # posic 4
num2:  .word 4  # posic 8 
num3:  .word 8  # posic 12 
num4:  .word 16 # posic 16 
num5:  .word 32 # posic 20
num6:  .word 0  # posic 24
num7:  .word 0  # posic 28
num8:  .word 0  # posic 32
num9:  .word 0  # posic 36
num10: .word 0  # posic 40
num11: .word 0  # posic 44

.text 0
main:
  # carga num0 a num5 en los registros 9 a 14
  lw $t1, 0($zero)  # lw $r9,  0($r0)  -> r9  = 1
  lw $t2, 4($zero)  # lw $r10, 4($r0)  -> r10 = 2
  lw $t3, 8($zero)  # lw $r11, 8($r0)  -> r11 = 4 
  lw $t4, 12($zero) # lw $r12, 12($r0) -> r12 = 8 
  lw $t5, 16($zero) # lw $r13, 16($r0) -> r13 = 16
  lw $t6, 20($zero) # lw $r14, 20($r0) -> r14 = 32
  
  # Pruebas adicionales tras la implementación de las insrucciones que 
  # faltaban implementar (addi, andi, slti y j)
  
  j salto

  addi $s3, $t5, 5   # addi $r19, $r13, 5 -> r19 = r13 + 5 = 16 + 5 = 21 ; En el registro 19 no se carga el valor 21 (en decimal) porque se hace previamente un jump

  salto:

  	addi $s1, $t5, 5   # addi $r17, $r13, 5 -> r17 = r13 + 5 = 16 + 5 = 21 
  	addi $s2, $s1, 9   # addi $r18, $r17, 9 -> r18 = r17 + 9 = 21 + 9 = 30
  
  	andi $s0, $t6, 5   # andi $r16, $r15, 5 -> r16 = r15 and 5 = 32 and 5 = 0
  	andi $s1, $t3, 20  # andi $r17, $r11, 20 -> r17 = r11 and 20 = 4 and 20 = 4
  
  	slti $t8, $s2, 5   # slti $r24, $r18, 5 -> r24 = 1 si r18 < 5 else 0 = 0
  	slti $t9, $s0, 6   # slti $r25, $r16, 6 -> r25 = 1 si r16 < 6 else 0 = 1

