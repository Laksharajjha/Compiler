package com.codejudge.judgecompiler;

import org.springframework.web.bind.annotation.*;
import java.io.*;

@RestController
@RequestMapping("/judge")
@CrossOrigin("*")
public class JudgeController {

    @PostMapping("/run")
    public String runCode(@RequestBody CodeRequest request) {
        String code = request.getCode();
        String input = request.getInput();
        String lang = request.getLanguage();

        try {
            String filename;
            Process compile = null;

            // Step 1: Write code to file
            switch (lang.toLowerCase()) {
                case "java":
                    filename = "Main.java";
                    writeToFile(filename, code);
                    compile = Runtime.getRuntime().exec("javac " + filename);
                    if (compile.waitFor() != 0) return readError(compile.getErrorStream(), "Compilation Error");
                    break;

                case "cpp":
                    filename = "main.cpp";
                    writeToFile(filename, code);
                    compile = Runtime.getRuntime().exec("/usr/bin/g++ main.cpp -o main");
                    if (compile.waitFor() != 0) return readError(compile.getErrorStream(), "Compilation Error");
                    break;

                case "python":
                    filename = "main.py";
                    writeToFile(filename, code);
                    break;

                default:
                    return "❌ Unsupported language: " + lang;
            }

            // Step 2: Execute
            Process run;
            switch (lang.toLowerCase()) {
                case "java":
                    run = Runtime.getRuntime().exec("java Main");
                    break;
                case "cpp":
                    run = Runtime.getRuntime().exec("./main");
                    break;
                case "python":
                    run = Runtime.getRuntime().exec("python3 main.py");
                    break;
                default:
                    return "❌ Unsupported language: " + lang;
            }

            // Feed input
            if (input != null && !input.isEmpty()) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(run.getOutputStream()));
                writer.write(input);
                writer.newLine();
                writer.flush();
                writer.close();
            }

            // Read output
            BufferedReader outputReader = new BufferedReader(new InputStreamReader(run.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = outputReader.readLine()) != null) {
                output.append(line).append("\n");
            }

            return output.toString();

        } catch (Exception e) {
            return "❌ Runtime Error:\n" + e.getMessage();
        }
    }

    private void writeToFile(String filename, String code) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write(code);
        writer.close();
    }

    private String readError(InputStream errorStream, String prefix) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
        StringBuilder error = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            error.append(line).append("\n");
        }
        return "❌ " + prefix + ":\n" + error;
    }
}
