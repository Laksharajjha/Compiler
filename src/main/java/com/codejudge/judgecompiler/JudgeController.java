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

        try {
            // 1. Write code to file
            FileWriter writer = new FileWriter("Main.java");
            writer.write(code);
            writer.close();

            // 2. Compile
            Process compile = Runtime.getRuntime().exec("javac Main.java");
            compile.waitFor();

            // Check compilation errors
            if (compile.exitValue() != 0) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(compile.getErrorStream()));
                StringBuilder error = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    error.append(line).append("\n");
                }
                return "❌ Compilation Error:\n" + error;
            }

            // 3. Run with input
            Process run = Runtime.getRuntime().exec("java Main");

            // Feed input
            if (input != null && !input.isEmpty()) {
                BufferedWriter inputWriter = new BufferedWriter(new OutputStreamWriter(run.getOutputStream()));
                inputWriter.write(input);
                inputWriter.newLine();
                inputWriter.flush();
                inputWriter.close();
            }

            // Get output
            BufferedReader reader = new BufferedReader(new InputStreamReader(run.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            return output.toString();
        } catch (Exception e) {
            return "❌ Runtime Error:\n" + e.getMessage();
        }
    }
}
