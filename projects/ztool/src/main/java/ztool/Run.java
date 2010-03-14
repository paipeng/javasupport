package ztool;

/** Run a CliBase subclass a main application. */
public class Run {	
	public static void main(String[] args) throws 
		ClassNotFoundException, 
		InstantiationException, 
		IllegalAccessException {
		
		// Check for at least main class name argument.
		int argsSize = args.length;
		if (argsSize < 1) {
			System.out.println(
					"Run <SimpleClassName> [options] [arguments]\n" +
					"or\n" +
					"Run <FullPackageClassName> [options] [arguments]\n");
			System.exit(-1);
		}
		
		String[] toolArgs = new String[argsSize - 1];
		System.arraycopy(args, 1, toolArgs, 0, argsSize - 1);
		
		// Auto prefix package name or Capitalize first letter if missing.
		String toolName = args[0];
		if (toolName.indexOf(".") < 0) {
			if (Character.isLowerCase(toolName.charAt(0))) {
				toolName = ("" + toolName.charAt(0)).toUpperCase() + 
					toolName.substring(1);
			}
			toolName = "ztool." + toolName;
		}		
		
		// Create a instance of the tool and run it.
		Class<?> appClz = Class.forName(toolName);
		Object appObj = appClz.newInstance();
		if (!(appObj instanceof CliBase)) {
			throw new IllegalArgumentException("Failed to run tool " +
					toolName + ", because it's not sub-class of " + CliBase.class);			
		}		
		CliBase app = (CliBase)appObj;
		app.runMain(toolArgs);
	}
}
