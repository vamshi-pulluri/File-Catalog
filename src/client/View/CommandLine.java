package client.View;

public class CommandLine {
    String MSG_DELIMITER = " ";
    private Command cmd;
    private String[] args;

    CommandLine(String inputString){
        retrieveCmd(inputString);
        retrieveParameters(inputString);
    }

    private void retrieveCmd(String input){
        try{
            String[] arguments = input.split(MSG_DELIMITER);
            cmd = Command.valueOf(arguments[0].toUpperCase());
        }
        catch(Exception e){
            cmd = Command.ILLEGAL;
        }
    }

    private void retrieveParameters(String input){
        args = removeCommand(input).split(MSG_DELIMITER);
        if(args.length == 1 && args[0].equals("")){
            args = new String[0];
        }
    }

    public Command getCmd() {
        return cmd;
    }

    public String[] getArgs() {
        return args;
    }

    private String removeCommand(String input){
        if(cmd == Command.ILLEGAL){
            return input;
        }
        int len = cmd.toString().length();
        String withoutCMD = input.substring(len);
        return withoutCMD.trim();
    }
}