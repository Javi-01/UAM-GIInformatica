# Prog de prueba para Pr�ctica 2. Ej 2

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
  nop
  nop
  nop
  nop

  # COMPROBACIÓN DEL PRIMERO RIESGO INDICADO EN EL ENUNCIADO (INDICAR EL TIPO DE RIESGO)
  add $s0, $zero, $t2 # en r16 un 2 = 0 + 2
  beq $s0, $t2, R_type_salto_efecivo # Debería saltar puesto que $s0 = 2 = $t2
  add $s0, $t1, $t2 # en r16 un 3 = 1 + 2 # No debe de ocurrir puesto que debería de saltar previamente
  add $s0, $t3, $t2 # en r16 un 6 = 4 + 2 # No debe de ocurrir puesto que debería de saltar previamente
  add $s0, $t4, $t2 # en r16 un 16 = 8 + 2 # No debe de ocurrir puesto que debería de saltar previamente
  nop
  nop

  R_type_salto_efecivo: 
    add $s0, $zero, $t3 # en r16 un 4 = 0 + 4
    beq $s0, $t2, R_type_salto_efecivo # No debería saltar puesto que $s0 = 4 != $t2 = 2
    add $s0, $t1, $t2 # en r16 un 3 = 1 + 2 # Debe de ocurrir puesto que no debería de saltar previamente
    add $s0, $t3, $t2 # en r16 un 6 = 4 + 2 # Debe de ocurrir puesto que no debería de saltar previamente
    add $s0, $t4, $t2 # en r16 un 10 = 8 + 2 # Debe de ocurrir puesto que no debería de saltar previamente
    nop
    nop

  # COMPROBACIÓN DEL SEGUNDO RIESGO INDICADO EN EL ENUNCIADO (INDICAR EL TIPO DE RIESGO)
  lw $s2, 4($zero) # en r18 un 2
  beq $t2, $t2, lw_salto_efecivo # Debería saltar puesto que $t2 = 2 = $t2
  add $s1, $t1, $t2 # en r17 un 3 = 1 + 2 # No debe de ocurrir puesto que debería de saltar previamente
  add $s1, $t3, $t2 # en r17 un 6 = 4 + 2 # No debe de ocurrir puesto que debería de saltar previamente
  add $s1, $t4, $t2 # en r17 un 10 = 8 + 2 # No debe de ocurrir puesto que debería de saltar previamente
  nop
  nop

  lw_salto_efecivo:
    lw $s0, 8($zero) # en r18 un 4
    beq $t3, $t2, lw_salto_efecivo # No debería saltar puesto que $t3 = 4 != $t2 = 2
    add $s0, $t1, $t2 # en r16 un 3 = 1 + 2 # Debe de ocurrir puesto que no debería de saltar previamente
    add $s0, $t3, $t2 # en r16 un 6 = 4 + 2 # Debe de ocurrir puesto que no debería de saltar previamente
    add $s0, $t4, $t2 # en r16 un 10 = 8 + 2 # Debe de ocurrir puesto que no debería de saltar previamente
    nop
    nop



