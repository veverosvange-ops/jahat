
import ghidra.app.script.GhidraScript;
import ghidra.app.decompiler.DecompInterface;
import ghidra.app.decompiler.DecompileResults;
import ghidra.program.model.listing.Function;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

public class ExportFunctions extends GhidraScript {

    private String safeName(String name) {
        return name.replaceAll("[^a-zA-Z0-9_]", "_");
    }

    @Override
    public void run() throws Exception {

        String[] args = getScriptArgs();
        String outputPath = args.length > 0
            ? args[0] : "output";

        File root = new File(outputPath);
        File functionsDir = new File(root, "functions");

        functionsDir.mkdirs();

        DecompInterface decompiler = new DecompInterface();
        decompiler.openProgram(currentProgram);

        File allFile = new File(root, "all-functions.c");
        FileWriter all = new FileWriter(allFile);

        File listFile = new File(root, "function-list.txt");
        FileWriter list = new FileWriter(listFile);

        Set<String> usedNames = new HashSet<>();
        int success = 0;
        int failed = 0;

        for (Function function :
             currentProgram.getFunctionManager().getFunctions(true)) {

            monitor.checkCancelled();

            String name = safeName(function.getName());
            String address = function.getEntryPoint().toString();

            String filename = name + "_" +
                safeName(address) + ".c";

            if (!usedNames.add(filename)) {
                filename = success + "_" + filename;
            }

            list.write(function.getName() + " @ " +
                address + "\n");

            DecompileResults result =
                decompiler.decompileFunction(
                    function, 120, monitor
                );

            if (result.decompileCompleted()) {

                String code =
                    result.getDecompiledFunction().getC();

                FileWriter individual = new FileWriter(
                    new File(functionsDir, filename)
                );

                individual.write(code);
                individual.close();

                all.write("\n/* " + function.getName() +
                    " @ " + address + " */\n");

                all.write(code);
                all.write("\n");

                success++;

            } else {
                failed++;
            }
        }

        all.close();
        list.close();
        decompiler.dispose();

        println("Successful: " + success);
        println("Failed: " + failed);
    }
}
