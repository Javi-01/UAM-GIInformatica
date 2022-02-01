--------------------------------------------------------------------------------
-- Bloque de control para la ALU. Arq0 2021-2022.
--
-- (INCLUIR AQUI LA INFORMACION SOBRE LOS AUTORES, Quitar este mensaje)
--
-- Javier Fraile Iglesias
--------------------------------------------------------------------------------

library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;

entity alu_control is
   port (
      -- Entradas:
      ALUOp  : in std_logic_vector (2 downto 0); -- Codigo de control desde la unidad de control
      Funct  : in std_logic_vector (5 downto 0); -- Campo "funct" de la instruccion
      -- Salida de control para la ALU:
      ALUControl : out std_logic_vector (3 downto 0) -- Define operacion a ejecutar por la ALU
   );
end alu_control;

architecture rtl of alu_control is
  -- Tipo para los codigos de control de la ALU:
  subtype t_aluControl is std_logic_vector (3 downto 0);
  -- Codigos de control:
  constant ALU_OR   : t_aluControl := "0111";
  constant ALU_NOT  : t_aluControl := "0101";
  constant ALU_XOR  : t_aluControl := "0110";
  constant ALU_AND  : t_aluControl := "0100";
  constant ALU_SUB  : t_aluControl := "0001";
  constant ALU_ADD  : t_aluControl := "0000";
  constant ALU_SLT  : t_aluControl := "1010";
  constant ALU_S16  : t_aluControl := "1101";

begin

  AluControl <= ALU_ADD when AluOp = "000" else -- lw o sw, que hace una suma
                ALU_S16 when AluOp = "011" else -- lui,
                ALU_ADD when (AluOp = "010" and Funct = "100000") or AluOp = "100" else -- add y addi
                ALU_SUB when (AluOp = "010" and Funct = "100010") or AluOp = "001" else -- sub y beq
                ALU_AND when (AluOp = "010" and Funct = "100100") or AluOp = "101" else -- and y andi
                ALU_OR  when AluOp = "010" and Funct = "100101" else -- or
				    ALU_XOR when ALUOp = "010" and Funct = "100110" else -- xor
                ALU_SLT when (AluOp = "010" and Funct = "101010") or ALUOp = "110" else -- slt y slti
                ALU_NOT; -- when AluOp_IDEX = "10" and Inm_ext_IDEX(5 downto 0) = "100110"; -- xor

end architecture;
