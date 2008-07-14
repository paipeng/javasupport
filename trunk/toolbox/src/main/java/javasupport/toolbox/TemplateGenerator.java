package javasupport.toolbox;

import javasupport.jdk.lang.CliApplication;

public class TemplateGenerator extends CliApplication {
	
	public static void main(String[] argv) {
		String[] args = parseOptions(argv);
		if(hasOpt("h") || hasOpt("help")){
			printExitUsage();
		}
		System.out.println(java.util.Arrays.asList(args));
	}

	private static void printExitUsage() {
		System.out.println("template [options] <subCommand> [subCommandArguments]");
		System.exit(0);
	}
}
