package br.com.gennex.socket.util;

public class FppsUtil extends Util {
	public static final String DelimitadorParametros = ";";
	public static final String ParamsVazio = "";
	public static final String Param1 = "param1".toUpperCase();
	public static final String Param2 = "param1;param2".toUpperCase();
	public static final String Param3 = "param1;param2;param3".toUpperCase();
	public static final String Param3UltimoVazio = "param1;param2;param3;"
			.toUpperCase();
	public static final String Param5Vazio = "param1;param2;param3;;param4"
			.toUpperCase();

	public static final String Command = "Command".toUpperCase();
	public static final String Command2 = "Command2".toUpperCase();

	public static final String ReqSemParams = Command + "(" + ParamsVazio + ")";
	public static final String ReqSem1Param = Command + "(" + Param1 + ")";
	public static final String ReqSem2Param = Command + "(" + Param2 + ")";
	public static final String ReqSem3Param = Command + "(" + Param3 + ")";
	public static final String ReqSem3UltimoVazio = Command + "("
			+ Param3UltimoVazio + ")";
	public static final String ReqSem5Vazio = Command + "(" + Param5Vazio + ")";
}
