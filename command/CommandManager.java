package Notorious.command;

import java.util.ArrayList;
import java.util.List;

import Notorious.command.impl.Bind;
import Notorious.command.impl.Config;
import Notorious.command.impl.Friend;
import Notorious.command.impl.Help;
import Notorious.command.impl.Name;
import Notorious.command.impl.Toggle;
import Notorious.command.impl.rx;

public class CommandManager {

    private static final List<Command> commands = new ArrayList<>();

    public static final String prefix = ".";

    public static void loadCommands() {
        commands.add(new Command("Help", "list of all commands.", new Help()));
        commands.add(new Command("Name", "Copies your username to the clipboard.", new Name()));
        commands.add(new Command("Bind", "Binds the selected module to the selected key.", new Bind()));
        commands.add(new Command("Toggle", "Toggles the selected module.", new Toggle()));
        commands.add(new Command("Config", "Load and create configs. ", new Config()));
        commands.add(new Command("Friend", "Add and delete friends.", new Friend()));
        commands.add(new Command("rx", "Reloads xray blocks.", new rx()));

        System.out.println("[" + "Notorious" + "] " + commands.size() + " commands loaded!");

    }

    public static List<String> getArgs(String text) {
        if (!isCommand(text))
            return new ArrayList<>();

        final List<String> args = new ArrayList<>();

        final String[] split = seperatePrefix(text).split(" ");

        int beginIndex = 1;

        for (int i = beginIndex; i < split.length; i++){
            final String arg = split[i];

            if (arg == null)
                continue;

            args.add(arg);
        }

        return args;
    }

    public static Command findCommand(String text) {
        final String[] split = seperatePrefix(text).split(" ");

        if (split.length <= 0)
            return null;


        return commands.stream()
                .filter(cmd -> cmd.getName().equalsIgnoreCase(split[0]))
                .findFirst()
                .orElse(null);
    }

    public static String seperatePrefix(String text) {
        if (!text.startsWith(prefix))
            return prefix + text;

        return text.substring(1);
    }

    public static boolean isCommand(String text){
        return findCommand(text) != null;
    }

    public static List<Command> getCommands() {
        return commands;
    }
}
