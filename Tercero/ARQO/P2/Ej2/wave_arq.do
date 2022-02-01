onerror {resume}
quietly WaveActivateNextPane {} 0
add wave -noupdate /processor_tb/i_processor/Clk
add wave -noupdate /processor_tb/i_processor/Reset
add wave -noupdate /processor_tb/i_processor/PC_reg
TreeUpdate [SetDefaultTree]
quietly WaveActivateNextPane
add wave -noupdate /processor_tb/i_processor/Alu_MIPS/OpA
add wave -noupdate /processor_tb/i_processor/Alu_MIPS/OpB
add wave -noupdate /processor_tb/i_processor/Alu_MIPS/Result
TreeUpdate [SetDefaultTree]
quietly WaveActivateNextPane
add wave -noupdate /processor_tb/i_processor/ForwardA
add wave -noupdate /processor_tb/i_processor/ForwardB
add wave -noupdate /processor_tb/i_processor/RegsMIPS/ForwardRegA
add wave -noupdate /processor_tb/i_processor/RegsMIPS/ForwardRegB
add wave -noupdate /processor_tb/i_processor/Hazard_Detection
add wave -noupdate /processor_tb/i_processor/Hazard_Detection_beq
add wave -noupdate /processor_tb/i_processor/enable_PCWrite
add wave -noupdate /processor_tb/i_processor/enable_IF_ID
add wave -noupdate /processor_tb/i_processor/Instruction_IF
add wave -noupdate /processor_tb/i_processor/Instruction_ID
add wave -noupdate /processor_tb/i_processor/Instruction_EX
TreeUpdate [SetDefaultTree]
quietly WaveActivateNextPane
add wave -noupdate /processor_tb/i_processor/Instruction_ID
add wave -noupdate /processor_tb/i_processor/Ctrl_Branch_ID
add wave -noupdate /processor_tb/i_processor/Ctrl_Jump_ID
add wave -noupdate /processor_tb/i_processor/Ctrl_MemToReg_ID
add wave -noupdate /processor_tb/i_processor/Ctrl_MemWrite_ID
add wave -noupdate /processor_tb/i_processor/Ctrl_MemRead_ID
add wave -noupdate /processor_tb/i_processor/Ctrl_ALUSrc_ID
add wave -noupdate /processor_tb/i_processor/Ctrl_RegWrite_ID
add wave -noupdate /processor_tb/i_processor/Ctrl_RegDest_ID
TreeUpdate [SetDefaultTree]
quietly WaveActivateNextPane
add wave -noupdate /processor_tb/i_processor/Instruction_EX
add wave -noupdate /processor_tb/i_processor/Ctrl_Branch_EX
add wave -noupdate /processor_tb/i_processor/Ctrl_Jump_EX
add wave -noupdate /processor_tb/i_processor/Ctrl_MemToReg_EX
add wave -noupdate /processor_tb/i_processor/Ctrl_MemWrite_EX
add wave -noupdate /processor_tb/i_processor/Ctrl_MemRead_EX
add wave -noupdate /processor_tb/i_processor/Ctrl_ALUSrc_EX
add wave -noupdate /processor_tb/i_processor/Ctrl_RegWrite_EX
add wave -noupdate /processor_tb/i_processor/Ctrl_RegDest_EX
TreeUpdate [SetDefaultTree]
quietly WaveActivateNextPane
add wave -noupdate -radix binary /processor_tb/i_processor/UnidadControl/ALUOp
add wave -noupdate -radix binary -childformat {{/processor_tb/i_processor/UnidadControl/OpCode(5) -radix binary} {/processor_tb/i_processor/UnidadControl/OpCode(4) -radix binary} {/processor_tb/i_processor/UnidadControl/OpCode(3) -radix binary} {/processor_tb/i_processor/UnidadControl/OpCode(2) -radix binary} {/processor_tb/i_processor/UnidadControl/OpCode(1) -radix binary} {/processor_tb/i_processor/UnidadControl/OpCode(0) -radix binary}} -subitemconfig {/processor_tb/i_processor/UnidadControl/OpCode(5) {-height 17 -radix binary} /processor_tb/i_processor/UnidadControl/OpCode(4) {-height 17 -radix binary} /processor_tb/i_processor/UnidadControl/OpCode(3) {-height 17 -radix binary} /processor_tb/i_processor/UnidadControl/OpCode(2) {-height 17 -radix binary} /processor_tb/i_processor/UnidadControl/OpCode(1) {-height 17 -radix binary} /processor_tb/i_processor/UnidadControl/OpCode(0) {-height 17 -radix binary}} /processor_tb/i_processor/UnidadControl/OpCode
add wave -noupdate -radix binary /processor_tb/i_processor/Alu_control_i/Funct
add wave -noupdate /processor_tb/i_processor/Alu_control_i/ALUControl
TreeUpdate [SetDefaultTree]
quietly WaveActivateNextPane
add wave -noupdate -expand /processor_tb/i_processor/RegsMIPS/regs
TreeUpdate [SetDefaultTree]
quietly WaveActivateNextPane
WaveRestoreCursors {{Cursor 1} {142 ns} 0}
quietly wave cursor active 1
configure wave -namecolwidth 351
configure wave -valuecolwidth 101
configure wave -justifyvalue left
configure wave -signalnamewidth 0
configure wave -snapdistance 10
configure wave -datasetprefix 0
configure wave -rowmargin 4
configure wave -childrowmargin 2
configure wave -gridoffset 0
configure wave -gridperiod 1
configure wave -griddelta 40
configure wave -timeline 0
configure wave -timelineunits ns
update
WaveRestoreZoom {86 ns} {264 ns}
