package org.example.task2;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {
    static class FileCharacterCounterTask implements Callable<FileResult> {
        private final Path file;

        public FileCharacterCounterTask(Path file) {
            this.file = file;
        }

        @Override
        public FileResult call() throws Exception {
            long characterCount = Files.lines(file).mapToLong(String::length).sum();
            return new FileResult(file.toString(), characterCount);
        }
    }

    static class FileFinderTask extends RecursiveTask<List<Path>> {
        private final Path dir;

        public FileFinderTask(Path dir) {
            this.dir = dir;
        }

        @Override
        protected List<Path> compute() {
            List<Path> textFiles = new ArrayList<>();
            List<FileFinderTask> subtasks = new ArrayList<>();

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path entry : stream) {
                    if (Files.isDirectory(entry)) {
                        // Create a subtask for each subdirectory
                        FileFinderTask subtask = new FileFinderTask(entry);
                        subtasks.add(subtask);
                        subtask.fork();
                    } else if (entry.toString().endsWith(".txt")) {
                        textFiles.add(entry);
                    }
                }

                for (FileFinderTask subtask : subtasks) {
                    textFiles.addAll(subtask.join());
                }
            } catch (IOException e) {
                System.err.println("Error reading directory: " + e.getMessage());
            }

            return textFiles;
        }
    }

    static class FileResult {
        private final String fileName;
        private final long characterCount;

        public FileResult(String fileName, long characterCount) {
            this.fileName = fileName;
            this.characterCount = characterCount;
        }

        public String getFileName() {
            return fileName;
        }

        public long getCharacterCount() {
            return characterCount;
        }
    }

    public static void main(String[] args) {
        String directoryPath = "src/main/resources/dir";

        Path directory = Paths.get(directoryPath);
        if (!Files.isDirectory(directory)) {
            System.out.println("Invalid directory path.");
            return;
        }

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<FileResult>> results = new ArrayList<>();

        try {
            FileFinderTask fileFinderTask = new FileFinderTask(directory);
            List<Path> textFiles = forkJoinPool.invoke(fileFinderTask);

            System.out.println("Found " + textFiles.size() + " text files.");

            for (Path textFile : textFiles) {
                results.add(executorService.submit(new FileCharacterCounterTask(textFile)));
            }

            for (Future<FileResult> future : results) {
                try {
                    FileResult result = future.get();
                    System.out.println("File: " + result.getFileName() + " - Characters: " + result.getCharacterCount());
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println("Error processing file: " + e.getMessage());
                }
            }
        } finally {
            executorService.shutdown();
            forkJoinPool.shutdown();
        }
    }
}
