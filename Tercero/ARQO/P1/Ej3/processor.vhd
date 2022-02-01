--------------------------------------------------------------------------------
-- Procesador MIPS con pipeline curso Arquitectura 2021-2022
--
-- Javier Fraile Iglesias
--
--------------------------------------------------------------------------------

library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;

entity processor is
   port(
      Clk         : in  std_logic; -- Reloj activo en flanco subida
      Reset       : in  std_logic; -- Reset asincrono activo nivel alto
      -- Instruction memory
      IAddr      : out std_logic_vector(31 downto 0); -- Direccion Instr
      IDataIn    : in  std_logic_vector(31 downto 0); -- Instruccion leida
      -- Data memory
      DAddr      : out std_logic_vector(31 downto 0); -- Direccion
      DRdEn      : out std_logic;                     -- Habilitacion lectura
      DWrEn      : out std_logic;                     -- Habilitacion escritura
      DDataOut   : out std_logic_vector(31 downto 0); -- Dato escrito
      DDataIn    : in  std_logic_vector(31 downto 0)  -- Dato leido
   );
end processor;

architecture rtl of processor is

  component alu
    port(
      OpA      : in std_logic_vector (31 downto 0);
      OpB      : in std_logic_vector (31 downto 0);
      Control  : in std_logic_vector (3 downto 0);
      Result   : out std_logic_vector (31 downto 0);
      Signflag : out std_logic;
      Zflag    : out std_logic
    );
  end component;

  component reg_bank
     port (
        Clk   : in std_logic; -- Reloj activo en flanco de subida
        Reset : in std_logic; -- Reset as�ncrono a nivel alto
        A1    : in std_logic_vector(4 downto 0);   -- Direcci�n para el puerto Rd1
        Rd1   : out std_logic_vector(31 downto 0); -- Dato del puerto Rd1
        A2    : in std_logic_vector(4 downto 0);   -- Direcci�n para el puerto Rd2
        Rd2   : out std_logic_vector(31 downto 0); -- Dato del puerto Rd2
        A3    : in std_logic_vector(4 downto 0);   -- Direcci�n para el puerto Wd3
        Wd3   : in std_logic_vector(31 downto 0);  -- Dato de entrada Wd3
        We3   : in std_logic -- Habilitaci�n de la escritura de Wd3
     );
  end component reg_bank;

  component control_unit
     port (
        -- Entrada = codigo de operacion en la instruccion:
        OpCode   : in  std_logic_vector (5 downto 0);
        -- Seniales para el PC
        Jump     : out std_logic; -- Intruccion de jump
        Branch   : out  std_logic; -- 1 = Ejecutandose instruccion branch
        -- Seniales relativas a la memoria
        MemToReg : out  std_logic; -- 1 = Escribir en registro la salida de la mem.
        MemWrite : out  std_logic; -- Escribir la memoria
        MemRead  : out  std_logic; -- Leer la memoria
        -- Seniales para la ALU
        ALUSrc   : out  std_logic;                     -- 0 = oper.B es registro, 1 = es valor inm.
        ALUOp    : out  std_logic_vector (2 downto 0); -- Tipo operacion para control de la ALU
        -- Seniales para el GPR
        RegWrite : out  std_logic; -- 1 = Escribir registro
        RegDst   : out  std_logic  -- 0 = Reg. destino es rt, 1=rd
     );
  end component;

  component alu_control is
   port (
      -- Entradas:
      ALUOp  : in std_logic_vector (2 downto 0); -- Codigo de control desde la unidad de control
      Funct  : in std_logic_vector (5 downto 0); -- Campo "funct" de la instruccion
      -- Salida de control para la ALU:
      ALUControl : out std_logic_vector (3 downto 0) -- Define operacion a ejecutar por la ALU
   );
 end component alu_control;

  signal Alu_Op2      : std_logic_vector(31 downto 0);
  signal ALU_Igual    : std_logic;
  signal Zflag_MEM    : std_logic;
  signal AluControl   : std_logic_vector(3 downto 0);
  signal reg_RD_data  : std_logic_vector(31 downto 0);
  signal reg_RD, reg_RD_EX, reg_RD_MEM, reg_RD_WB       : std_logic_vector(4 downto 0); -- Señales auxiliares del reg. destino para las diferentes etapas en las que son usadas

  signal Regs_eq_branch : std_logic;
  signal PC_next        : std_logic_vector(31 downto 0);
  signal PC_reg         : std_logic_vector(31 downto 0);
  signal PC_plus4_IF, PC_plus4_ID, PC_plus4_EX : std_logic_vector(31 downto 0); -- PC+4 en las diferentes etapas 

  signal Instruction_IF, Instruction_ID, Instruction_EX : std_logic_vector(31 downto 0); -- La instrucción desde lamem de instr en las diferentes etapas
  signal Inm_ext_ID, Inm_ext_EX : std_logic_vector(31 downto 0); -- La parte baja de la instrucción extendida de signo en las diferentes etapas
  signal reg_RS_ID, reg_RS_EX : std_logic_vector(31 downto 0); -- Señales auxiliares del reg. RS para las diferentes etapas en las que son usadas
  signal reg_RT_ID, reg_RT_EX, reg_RT_MEM : std_logic_vector(31 downto 0); -- Señales auxiliares del reg. RT para las diferentes etapas en las que son usadas

  signal dataIn_Mem_MEM, dataIn_Mem_WB : std_logic_vector(31 downto 0); --From Data Memory
  signal Addr_Branch, Addr_Branch_MEM : std_logic_vector(31 downto 0); -- Señales auxiliares de la dirección de salto de la instruccion branch para las diferentes etapas en las que son usadas

  signal Addr_Jump, Addr_Jump_dest, Addr_Jump_MEM : std_logic_vector(31 downto 0); -- Señales auxiliares de la dirección de salto de la instruccion jump para las diferentes etapas en las que son usadas
  signal PC_Src, PC_Src_WB : std_logic; -- Señales auxiliares del PC_Src para las diferentes etapas en las que son usadas
  signal Alu_Res, Alu_Res_MEM, Alu_Res_WB : std_logic_vector(31 downto 0); -- Señales auxiliares del resultado para las diferentes etapas en las que son usadas
  
  -- Señales auxiliares de la UC para las diferentes etapas en las que son usadas
  signal OpCode_EX : std_logic_vector (5 downto 0);
  signal Ctrl_Branch_ID, Ctrl_Branch_EX, Ctrl_Branch_MEM  : std_logic; 
  signal Ctrl_Jump_ID, Ctrl_Jump_EX, Ctrl_Jump_MEM, Ctrl_Jump_WB : std_logic; 
  signal Ctrl_MemToReg_ID, Ctrl_MemToReg_EX, Ctrl_MemToReg_MEM, Ctrl_MemToReg_WB : std_logic; 
  signal Ctrl_MemWrite_ID, Ctrl_MemWrite_EX, Ctrl_MemWrite_MEM : std_logic; 
  signal Ctrl_MemRead_ID, Ctrl_MemRead_EX, Ctrl_MemRead_MEM  : std_logic;
  signal Ctrl_ALUSrc_ID, Ctrl_ALUSrc_EX : std_logic;                    
  signal Ctrl_ALUOP_ID, Ctrl_ALUOP_EX : std_logic_vector (2 downto 0); 
  signal Ctrl_RegWrite, Ctrl_RegWrite_ID, Ctrl_RegWrite_EX, Ctrl_RegWrite_MEM, Ctrl_RegWrite_WB : std_logic;  
  signal Ctrl_RegDest_ID, Ctrl_RegDest_EX : std_logic;  

  -- Señales auxiliares enables para los diferentes stages
  signal enable_IF_ID : std_logic;
  signal enable_ID_EX : std_logic;
  signal enable_EX_MEM : std_logic;
  signal enable_MEM_WB : std_logic;

begin

  -- Etapa IF

  PC_next <= Addr_Jump_dest when PC_Src = '1' else PC_plus4_IF;

  PC_reg_proc: process(Clk, Reset)
  begin
    if Reset = '1' then
      PC_reg <= (others => '0');
    elsif rising_edge(Clk) then
      PC_reg <= PC_next;
    end if;
  end process;

  PC_plus4_IF    <= PC_reg + 4;
  IAddr       <= PC_reg;
  Instruction_IF <= IDataIn;

  -- Etapa IF/ID

  IF_ID_Regs: process(Clk, Reset)
  begin
    if Reset = '1' then
      PC_plus4_ID <= (others => '0');
      Instruction_ID <= (others => '0');
    elsif rising_edge(Clk) then -- and enable_IF_ID = '1';
      PC_plus4_ID <= PC_plus4_IF;
      Instruction_ID <= Instruction_IF;
    end if;
  end process;

  enable_IF_ID <= '1';

  -- Etapa ID 

  RegsMIPS : reg_bank
  port map (
    Clk   => Clk,
    Reset => Reset,
    A1    => Instruction_ID(25 downto 21),
    Rd1   => reg_RS_ID,
    A2    => Instruction_ID(20 downto 16),
    Rd2   => reg_RT_ID,
    A3    => reg_RD,
    Wd3   => reg_RD_data,
    We3   => Ctrl_RegWrite
  );

  UnidadControl : control_unit
  port map( -- Cambiar nombres y añadir mas señales
    OpCode   => Instruction_ID(31 downto 26),
    -- Señales para el PC
    Jump   => Ctrl_Jump_ID,
    Branch   => Ctrl_Branch_ID,
    -- Señales para la memoria
    MemToReg => Ctrl_MemToReg_ID,
    MemWrite => Ctrl_MemWrite_ID,
    MemRead  => Ctrl_MemRead_ID,
    -- Señales para la ALU
    ALUSrc   => Ctrl_ALUSrc_ID,
    ALUOP    => Ctrl_ALUOP_ID,
    -- Señales para el GPR
    RegWrite => Ctrl_RegWrite_ID,
    RegDst   => Ctrl_RegDest_ID
  );

  Inm_ext_ID <= x"FFFF" & Instruction_ID(15 downto 0) when Instruction_ID(15)='1' else
                x"0000" & Instruction_ID(15 downto 0);


  --Ctrl_Jump_ID <= '0'; --nunca salto incondicional
  --Regs_eq_branch <= '1' when (reg_RS_ID = reg_RT_ID) else '0';

  -- Etapa ID/EX

  ID_EX_Regs: process(Clk, Reset)
  begin
    if Reset = '1' then
      -- Recoger toda la UC
      OpCode_EX <= (others => '0');
      Ctrl_Jump_EX <= '0';
      Ctrl_Branch_EX <= '0';
      Ctrl_MemToReg_EX <= '0';
      Ctrl_MemWrite_EX <= '0';
      Ctrl_MemRead_EX <= '0';
      Ctrl_ALUSrc_EX <= '0';
      Ctrl_ALUOP_EX <= (others => '0');
      Ctrl_RegWrite_EX <= '0';
      Ctrl_RegDest_EX <= '0';
      -- Resto de datos y registros
      PC_plus4_EX <= (others => '0');
      reg_RS_EX <= (others => '0');
      reg_RT_EX <= (others => '0');
      Inm_ext_EX <= (others => '0');
      Instruction_EX <= (others => '0');
    elsif rising_edge(Clk) then -- and enable_ID_EX = '1';
      -- Recoger toda la UC
      OpCode_EX <= Instruction_ID(31 downto 26);
      Ctrl_Jump_EX <= Ctrl_Jump_ID;
      Ctrl_Branch_EX <= Ctrl_Branch_ID;
      Ctrl_MemToReg_EX <= Ctrl_MemToReg_ID;
      Ctrl_MemWrite_EX <= Ctrl_MemWrite_ID;
      Ctrl_MemRead_EX <= Ctrl_MemRead_ID;
      Ctrl_ALUSrc_EX <= Ctrl_ALUSrc_ID;
      Ctrl_ALUOP_EX <= Ctrl_ALUOP_ID;
      Ctrl_RegWrite_EX <= Ctrl_RegWrite_ID;
      Ctrl_RegDest_EX <= Ctrl_RegDest_ID;
      -- Resto de datos y registros
      PC_plus4_EX <= PC_plus4_ID;
      reg_RS_EX <= reg_RS_ID;
      reg_RT_EX <= reg_RT_ID;
      Inm_ext_EX <= Inm_ext_ID;
      Instruction_EX <= Instruction_ID;
    end if;
  end process;

  enable_ID_EX <= '1';

  -- Etapa EX

  Alu_control_i: alu_control
  port map(
    -- Entradas:
    ALUOp  => Ctrl_ALUOP_EX, -- Codigo de control desde la unidad de control
    Funct  => Instruction_EX (5 downto 0), -- Campo "funct" de la instruccion
    -- Salida de control para la ALU:
    ALUControl => AluControl -- Define operacion a ejecutar por la ALU
  );

  Alu_MIPS : alu
  port map (
    OpA      => reg_RS_EX,
    OpB      => Alu_Op2,
    Control  => AluControl,
    Result   => Alu_Res,
    Signflag => open,
    Zflag    => ALU_IGUAL
  );

  Addr_Jump      <= PC_plus4_EX(31 downto 28) & Instruction_EX(25 downto 0) & "00";
  Addr_Branch    <= PC_plus4_EX + (Inm_ext_EX(29 downto 0) & "00");

  reg_RD_EX     <= Instruction_EX(20 downto 16) when Ctrl_RegDest_EX = '0' else Instruction_EX(15 downto 11);
  Alu_Op2       <= reg_RT_EX when Ctrl_ALUSrc_EX = '0' else Inm_ext_EX;

  -- Etapa EX/MEM

  EX_MEM_Regs: process(Clk, Reset)
  begin
    if Reset = '1' then
      -- Recoger toda la UC menos AluSrc, ALUOp, RegDst
      Ctrl_Jump_MEM <= '0';
      Ctrl_Branch_MEM <= '0';
      Ctrl_MemToReg_MEM <= '0';
      Ctrl_MemWrite_MEM <= '0';
      Ctrl_MemRead_MEM <= '0';
      Ctrl_RegWrite_MEM <= '0';
      -- Resto de datos y registros
      Addr_Jump_MEM <= (others => '0');
      Addr_Branch_MEM <= (others => '0');
      Zflag_MEM <= '0';
      Alu_Res_MEM <= (others => '0');
      reg_RT_MEM <= (others => '0');
      reg_RD_MEM <= (others => '0');
    elsif rising_edge(Clk) then -- and enable_EX_MEM = '1';
      -- Recoger toda la UC menos AluSrc, ALUOp, RegDst
      Ctrl_Jump_MEM <= Ctrl_Jump_EX;
      Ctrl_Branch_MEM <= Ctrl_Branch_EX;
      Ctrl_MemToReg_MEM <= Ctrl_MemToReg_EX;
      Ctrl_MemWrite_MEM <= Ctrl_MemWrite_EX;
      Ctrl_MemRead_MEM <= Ctrl_MemRead_EX;
      Ctrl_RegWrite_MEM <= Ctrl_RegWrite_EX;
	    -- Resto de datos y registros 
      Addr_Jump_MEM <= Addr_Jump;
      Addr_Branch_MEM <= Addr_Branch;
      Zflag_MEM <= ALU_IGUAL;
      Alu_Res_MEM <= Alu_Res;
      reg_RT_MEM <= reg_RT_EX;
      reg_RD_MEM <= reg_RD_EX;
    end if;
  end process;

  enable_EX_MEM <= '1';

  -- Etapa MEM

  PC_Src  <= Ctrl_Jump_MEM or (Ctrl_Branch_MEM and Zflag_MEM);
  Addr_Jump_dest <= Addr_Jump_MEM   when Ctrl_Jump_MEM='1' else
                    Addr_Branch_MEM when Ctrl_Branch_MEM='1' else
                    (others =>'0');

  DAddr      <= Alu_Res_MEM;
  DDataOut   <= reg_RT_MEM;
  DWrEn      <= Ctrl_MemWrite_MEM;
  dRdEn      <= Ctrl_MemRead_MEM;
  dataIn_Mem_MEM <= DDataIn;

  -- Etapa MEM/WB

  MEM_WB_Regs: process(Clk, Reset)
  begin
    if Reset = '1' then
      -- Recoger toda la UC menos AluSrc, ALUOp, RegDst, Branch, MemWrite, MemRead
      Ctrl_Jump_WB <= '0';
      Ctrl_MemToReg_WB <= '0';
      Ctrl_RegWrite_WB <= '0';
	  -- Resto de datos y registros
      dataIn_Mem_WB <= (others => '0');
      Alu_Res_WB <= (others => '0');
      reg_RD_WB <= (others => '0');
    elsif rising_edge(Clk) then -- and enable_MEM_WB = '1';
      -- Recoger toda la UC menos AluSrc, ALUOp, RegDst, Branch, MemWrite, MemRead 
      Ctrl_Jump_WB <= Ctrl_Jump_MEM;
      Ctrl_MemToReg_WB <= Ctrl_MemToReg_MEM;
      Ctrl_RegWrite_WB <= Ctrl_RegWrite_MEM;
      -- Resto de datos y registros
      dataIn_Mem_WB <= dataIn_Mem_MEM;
      Alu_Res_WB <= Alu_Res_MEM;
      reg_RD_WB <= reg_RD_MEM;
    end if;
  end process;

  enable_MEM_WB <= '1';

  -- Etapa WB

  Ctrl_RegWrite <= Ctrl_RegWrite_WB;
  reg_RD_data <= dataIn_Mem_WB when Ctrl_MemToReg_WB = '1' else Alu_Res_WB;
  reg_RD <= reg_RD_WB;

end architecture;
